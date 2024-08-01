package com.hyf.proxyserver;

import com.hyf.proxyserver.extensions.http.HttpTrafficCapturer;
import com.hyf.proxyserver.server.capturer.*;
import com.hyf.proxyserver.server.util.ProxyUtils;
import com.hyf.proxyserver.server.util.ServerUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;

public class MitmServerTests {

    public static void main2(String[] args) {
        MitmServerConfig mitmServerConfig = new MitmServerConfig();
        mitmServerConfig.setPort(8088);
        MitmServer mitmServer = new MitmServer(mitmServerConfig);
        mitmServer.addCapturers(new TrafficCapturer() {
            @Override
            public Object capture(Channel inboundChannel, Channel outboundChannel, Object msg, boolean fromClient) {
                if (!(msg instanceof ByteBuf)) {
                    return msg;
                }
                System.out.println(ProxyUtils.getContentString((ByteBuf) msg));
                return msg;
            }
        });
        mitmServer.start();
    }

    public static void main(String[] args) {
        MitmServerConfig mitmServerConfig = new MitmServerConfig();
        mitmServerConfig.setPort(8088);
        MitmServer mitmServer = new MitmServer(mitmServerConfig);
        ServerUtils.initSimpleHttpCapability(mitmServer, new HttpTrafficCapturer() {
            @Override
            public FullHttpResponse captureRemoteResponse(Channel inboundChannel, Channel outboundChannel, FullHttpRequest request, FullHttpResponse response) {
                return response;
            }
        });
        mitmServer.start();
    }

}
