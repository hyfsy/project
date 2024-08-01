
package com.hyf.proxyserver.extensions.http;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public interface HttpTrafficCapturer {

    default FullHttpRequest captureClientRequest(Channel inboundChannel, Channel outboundChannel, FullHttpRequest request) {
        return request;
    }

    @Deprecated
    default FullHttpResponse captureClientResponse(Channel inboundChannel, Channel outboundChannel, FullHttpRequest request, FullHttpResponse response) {
        return response;
    }

    @Deprecated
    default FullHttpRequest captureRemoteRequest(Channel inboundChannel, Channel outboundChannel, FullHttpRequest request) {
        return request;
    }

    default FullHttpResponse captureRemoteResponse(Channel inboundChannel, Channel outboundChannel, FullHttpRequest request, FullHttpResponse response) {
        return response;
    }

}
