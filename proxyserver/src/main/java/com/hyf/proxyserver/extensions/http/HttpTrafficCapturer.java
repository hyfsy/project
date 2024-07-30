
package com.hyf.proxyserver.extensions.http;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public interface HttpTrafficCapturer {

    default Object captureClientRequest(Channel inboundChannel, Channel outboundChannel, FullHttpRequest request) {
        return request;
    }

    @Deprecated
    default Object captureClientResponse(Channel inboundChannel, Channel outboundChannel, FullHttpRequest request, FullHttpResponse response) {
        return response;
    }

    @Deprecated
    default Object captureRemoteRequest(Channel inboundChannel, Channel outboundChannel, FullHttpRequest request) {
        return request;
    }

    default Object captureRemoteResponse(Channel inboundChannel, Channel outboundChannel, FullHttpRequest request, FullHttpResponse response) {
        return response;
    }

}
