
package com.hyf.proxyserver.extensions.http;

import com.hyf.proxyserver.server.relay.RelayContext;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public interface HttpTrafficCapturer {

    default void captureClientRequest(RelayContext<FullHttpRequest> context, FullHttpRequest request) {
    }

    @Deprecated
    default void captureClientResponse(RelayContext<FullHttpResponse> context, FullHttpRequest request, FullHttpResponse response) {
    }

    @Deprecated
    default void captureRemoteRequest(RelayContext<FullHttpRequest> context, FullHttpRequest request) {
    }

    default void captureRemoteResponse(RelayContext<FullHttpResponse> context, FullHttpRequest request, FullHttpResponse response) {
    }

}
