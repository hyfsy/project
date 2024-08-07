package com.hyf.proxyserver.extensions.http;

import com.hyf.proxyserver.server.capturer.SimpleTrafficListener;
import com.hyf.proxyserver.server.relay.RelayContext;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.*;
import io.netty.util.AttributeKey;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultHttpTrafficListener extends SimpleTrafficListener<FullHttpMessage> {

    /**
     * 请求数据对象队列，每个capturer不一样的key，防止冲突
     */
    private final AttributeKey<Queue<RequestData>> requestDataQueueAttributeKey = AttributeKey.valueOf("REQUEST_DATA_" + UUID.randomUUID());

    private final HttpTrafficListener[] httpTrafficListeners;

    public DefaultHttpTrafficListener(HttpTrafficListener... httpTrafficListeners) {
        this.httpTrafficListeners = httpTrafficListeners;
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
                listenClientRequest((RelayContext) context, request);
            } else {
                // listenRemoteRequest((RelayContext) context, request);
                throw new IllegalStateException("Can't happen");
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
                    // listenClientResponse((RelayContext) context, requestData.request, response);
                    throw new IllegalStateException("Can't happen");
                } else {
                    listenRemoteResponse((RelayContext) context, requestData.request, response);
                }
            } finally {
                requestData.release();
            }
        }
    }

    private void listenClientRequest(RelayContext<FullHttpRequest> context, FullHttpRequest request) {
        for (HttpTrafficListener httpTrafficListener : httpTrafficListeners) {
            httpTrafficListener.listenClientRequest(context, request);
        }
    }

    // @Deprecated
    // private void listenClientResponse(RelayContext<FullHttpResponse> context, FullHttpRequest request, FullHttpResponse response) {
    //     for (HttpTrafficListener httpTrafficListener : httpTrafficListeners) {
    //         httpTrafficListener.listenClientResponse(context, request, response);
    //     }
    // }
    //
    // @Deprecated
    // private void listenRemoteRequest(RelayContext<FullHttpRequest> context, FullHttpRequest request) {
    //     for (HttpTrafficListener httpTrafficListener : httpTrafficListeners) {
    //         httpTrafficListener.listenRemoteRequest(context, request);
    //     }
    // }

    private void listenRemoteResponse(RelayContext<FullHttpResponse> context, FullHttpRequest request, FullHttpResponse response) {
        for (HttpTrafficListener httpTrafficListener : httpTrafficListeners) {
            httpTrafficListener.listenRemoteResponse(context, request, response);
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

