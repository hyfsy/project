package com.hyf.proxyserver.extensions.http;

import com.hyf.proxyserver.server.capturer.SimpleTrafficCapturer;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.*;
import io.netty.util.AttributeKey;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultHttpTrafficCapturer extends SimpleTrafficCapturer<FullHttpMessage> {

    /**
     * 请求数据对象
     */
    private static final AttributeKey<Queue<RequestData>> REQUEST_DATA_QUEUE = AttributeKey.valueOf("REQUEST_DATA");

    private final HttpTrafficCapturer[] httpTrafficCapturers;

    public DefaultHttpTrafficCapturer(HttpTrafficCapturer... httpTrafficCapturers) {
        this.httpTrafficCapturers = httpTrafficCapturers;
    }

    @Override
    protected FullHttpMessage capture0(Channel inboundChannel, Channel outboundChannel, FullHttpMessage msg, boolean fromClient) {

        boolean isRequest = msg instanceof FullHttpRequest;
        boolean isResponse = msg instanceof FullHttpResponse;

        if (isRequest) {
            // 方便服务端响应处理时可获取
            FullHttpRequest request = (FullHttpRequest) msg;
            if (fromClient) {
                request = captureClientRequest(inboundChannel, outboundChannel, request);
            } else {
                request = captureRemoteRequest(inboundChannel, outboundChannel, request);
            }
            pushRequest(inboundChannel, request.copy());
            return request;
        }
        if (isResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            RequestData requestData = popRequest(outboundChannel);
            try {
                if (fromClient) {
                    response = captureClientResponse(inboundChannel, outboundChannel, requestData.request, response);
                } else {
                    response = captureRemoteResponse(inboundChannel, outboundChannel, requestData.request, response);
                }
            } finally {
                requestData.release();
            }
            return response;
        }
        return msg;
    }

    private FullHttpRequest captureClientRequest(Channel inboundChannel, Channel outboundChannel, FullHttpRequest request) {
        for (HttpTrafficCapturer httpTrafficCapturer : httpTrafficCapturers) {
            request = httpTrafficCapturer.captureClientRequest(inboundChannel, outboundChannel, request);
        }
        return request;
    }

    @Deprecated
    private FullHttpResponse captureClientResponse(Channel inboundChannel, Channel outboundChannel, FullHttpRequest request, FullHttpResponse response) {
        for (HttpTrafficCapturer httpTrafficCapturer : httpTrafficCapturers) {
            response = httpTrafficCapturer.captureClientResponse(inboundChannel, outboundChannel, request, response);
        }
        return response;
    }

    @Deprecated
    private FullHttpRequest captureRemoteRequest(Channel inboundChannel, Channel outboundChannel, FullHttpRequest request) {
        for (HttpTrafficCapturer httpTrafficCapturer : httpTrafficCapturers) {
            request = httpTrafficCapturer.captureRemoteRequest(inboundChannel, outboundChannel, request);
        }
        return request;
    }

    private FullHttpResponse captureRemoteResponse(Channel inboundChannel, Channel outboundChannel, FullHttpRequest request, FullHttpResponse response) {
        for (HttpTrafficCapturer httpTrafficCapturer : httpTrafficCapturers) {
            response = httpTrafficCapturer.captureRemoteResponse(inboundChannel, outboundChannel, request, response);
        }
        return response;
    }

    private RequestData popRequest(Channel channel) {
        Queue<RequestData> requestDataQueue = channel.attr(REQUEST_DATA_QUEUE).get();
        if (requestDataQueue == null) {
            throw new IllegalStateException("request is null");
        }
        RequestData requestData = requestDataQueue.poll();
        if (requestData == null) {
            throw new IllegalStateException("request is null");
        }
        return requestData;
    }

    private void pushRequest(Channel channel, FullHttpRequest request) {
        Queue<RequestData> requestDataQueue = channel.attr(REQUEST_DATA_QUEUE).get();
        if (requestDataQueue == null) {
            Queue<RequestData> queue = new LinkedBlockingQueue<>();
            channel.attr(REQUEST_DATA_QUEUE).setIfAbsent(queue);
            requestDataQueue = channel.attr(REQUEST_DATA_QUEUE).get();
        }
        requestDataQueue.add(new RequestData(request));
    }

    private static class RequestData {
        private final FullHttpRequest request;
        private final AtomicBoolean released = new AtomicBoolean(false);

        public RequestData(FullHttpRequest request) {
            this.request = request;
        }

        public void release() {
            if (released.compareAndSet(false, true)) {
                request.release();
            }
        }
    }
}

