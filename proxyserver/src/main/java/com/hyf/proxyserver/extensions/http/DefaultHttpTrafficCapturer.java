package com.hyf.proxyserver.extensions.http;

import com.hyf.proxyserver.server.capturer.SimpleTrafficCapturer;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.*;
import io.netty.util.AttributeKey;

public class DefaultHttpTrafficCapturer extends SimpleTrafficCapturer<FullHttpMessage> {

    /**
     * 请求数据对象
     */
    private static final AttributeKey<FullHttpRequest> REQUEST_DATA = AttributeKey.valueOf("REQUEST_DATA");

    private final HttpTrafficCapturer httpTrafficCapturer;

    public DefaultHttpTrafficCapturer(HttpTrafficCapturer httpTrafficCapturer) {
        this.httpTrafficCapturer = httpTrafficCapturer;
    }

    @Override
    protected Object capture0(Channel inboundChannel, Channel outboundChannel, FullHttpMessage msg, boolean fromClient) {

        boolean isRequest = msg instanceof HttpRequest;
        boolean isResponse = msg instanceof HttpResponse;

        if (isRequest) {
            // 方便服务端响应处理时可获取
            FullHttpRequest request = inboundChannel.attr(REQUEST_DATA).get();
            if (request != null) {
                request.release();
            }
            inboundChannel.attr(REQUEST_DATA).set((FullHttpRequest) msg.copy());
            if (fromClient) {
                return captureClientRequest(inboundChannel, outboundChannel, (FullHttpRequest) msg);
            } else {
                return captureRemoteRequest(inboundChannel, outboundChannel, (FullHttpRequest) msg);
            }
        }
        if (isResponse) {
            FullHttpRequest request = outboundChannel.attr(REQUEST_DATA).get();
            if (request == null) {
                throw new IllegalStateException("request is null");
            }
            if (fromClient) {
                return captureClientResponse(inboundChannel, outboundChannel, request, (FullHttpResponse) msg);
            } else {
                return captureRemoteResponse(inboundChannel, outboundChannel, request, (FullHttpResponse) msg);
            }
        }
        return msg;
    }

    private Object captureClientRequest(Channel inboundChannel, Channel outboundChannel, FullHttpRequest request) {
        return httpTrafficCapturer.captureClientRequest(inboundChannel, outboundChannel, request);
    }

    @Deprecated
    private Object captureClientResponse(Channel inboundChannel, Channel outboundChannel, FullHttpRequest request, FullHttpResponse response) {
        return httpTrafficCapturer.captureClientResponse(inboundChannel, outboundChannel, request, response);
    }

    @Deprecated
    private Object captureRemoteRequest(Channel inboundChannel, Channel outboundChannel, FullHttpRequest request) {
        return httpTrafficCapturer.captureRemoteRequest(inboundChannel, outboundChannel, request);
    }

    private Object captureRemoteResponse(Channel inboundChannel, Channel outboundChannel, FullHttpRequest request, FullHttpResponse response) {
        return httpTrafficCapturer.captureRemoteResponse(inboundChannel, outboundChannel, request, response);
    }
}

