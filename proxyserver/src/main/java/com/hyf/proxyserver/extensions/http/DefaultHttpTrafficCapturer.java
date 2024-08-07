package com.hyf.proxyserver.extensions.http;

import com.hyf.proxyserver.server.capturer.SimpleTrafficCapturer;
import com.hyf.proxyserver.server.relay.RelayContext;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.*;
import io.netty.util.AttributeKey;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultHttpTrafficCapturer extends SimpleTrafficCapturer<FullHttpMessage> {

    /**
     * 请求数据对象队列，每个capturer不一样的key，防止冲突
     */
    private final AttributeKey<Queue<RequestData>> requestDataQueueAttributeKey = AttributeKey.valueOf("REQUEST_DATA_" + UUID.randomUUID());

    private final HttpTrafficCapturer[] httpTrafficCapturers;

    public DefaultHttpTrafficCapturer(HttpTrafficCapturer... httpTrafficCapturers) {
        this.httpTrafficCapturers = httpTrafficCapturers;
    }

    @Override
    protected void capture0(RelayContext<FullHttpMessage> context) {

        FullHttpMessage msg = context.getRelayMsg();
        Channel inboundChannel = context.getInboundChannel();
        Channel outboundChannel = context.getOutboundChannel();
        boolean fromClient = context.fromClient();

        boolean isRequest = msg instanceof FullHttpRequest;
        boolean isResponse = msg instanceof FullHttpResponse;

        if (isRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            if (fromClient) {
                captureClientRequest((RelayContext) context, request);
            } else {
                captureRemoteRequest((RelayContext) context, request);
            }
            request = (FullHttpRequest) context.getRelayMsg();
            // 方便服务端响应处理时可获取
            pushRequest(inboundChannel, request.copy());
        }
        //
        else if (isResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            RequestData requestData = popRequest(outboundChannel);
            try {
                if (fromClient) {
                    captureClientResponse((RelayContext) context, requestData.request, response);
                } else {
                    captureRemoteResponse((RelayContext) context, requestData.request, response);
                }
            } finally {
                requestData.release();
            }
        }
    }

    private void captureClientRequest(RelayContext<FullHttpRequest> context, FullHttpRequest request) {
        for (HttpTrafficCapturer httpTrafficCapturer : httpTrafficCapturers) {
            httpTrafficCapturer.captureClientRequest(context, request);
        }
    }

    @Deprecated
    private void captureClientResponse(RelayContext<FullHttpResponse> context, FullHttpRequest request, FullHttpResponse response) {
        for (HttpTrafficCapturer httpTrafficCapturer : httpTrafficCapturers) {
            httpTrafficCapturer.captureClientResponse(context, request, response);
        }
    }

    @Deprecated
    private void captureRemoteRequest(RelayContext<FullHttpRequest> context, FullHttpRequest request) {
        for (HttpTrafficCapturer httpTrafficCapturer : httpTrafficCapturers) {
            httpTrafficCapturer.captureRemoteRequest(context, request);
        }
    }

    private void captureRemoteResponse(RelayContext<FullHttpResponse> context, FullHttpRequest request, FullHttpResponse response) {
        for (HttpTrafficCapturer httpTrafficCapturer : httpTrafficCapturers) {
            httpTrafficCapturer.captureRemoteResponse(context, request, response);
        }
    }

    private RequestData popRequest(Channel channel) {
        Queue<RequestData> requestDataQueue = channel.attr(this.requestDataQueueAttributeKey).get();
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
        Queue<RequestData> requestDataQueue = channel.attr(this.requestDataQueueAttributeKey).get();
        if (requestDataQueue == null) {
            Queue<RequestData> queue = new LinkedBlockingQueue<>();
            channel.attr(this.requestDataQueueAttributeKey).setIfAbsent(queue);
            requestDataQueue = channel.attr(this.requestDataQueueAttributeKey).get();
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

