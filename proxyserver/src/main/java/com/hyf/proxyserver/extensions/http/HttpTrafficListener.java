
package com.hyf.proxyserver.extensions.http;

import com.hyf.proxyserver.server.relay.RelayContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public interface HttpTrafficListener {

    default void listenClientRequest(RelayContext<FullHttpRequest> context, FullHttpRequest request) {
    }

    // @Deprecated
    // default void listenClientResponse(RelayContext<FullHttpResponse> context, FullHttpRequest request, FullHttpResponse response) {
    // }
    //
    // @Deprecated
    // default void listenRemoteRequest(RelayContext<FullHttpRequest> context, FullHttpRequest request) {
    // }

    default void listenRemoteResponse(RelayContext<FullHttpResponse> context, FullHttpRequest request, FullHttpResponse response) {
    }

}
