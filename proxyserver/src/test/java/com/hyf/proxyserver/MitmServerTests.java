package com.hyf.proxyserver;

import com.hyf.proxyserver.extensions.http.HttpTrafficListener;
import com.hyf.proxyserver.server.capturer.*;
import com.hyf.proxyserver.server.relay.RelayChannelInitializer;
import com.hyf.proxyserver.server.relay.RelayContext;
import com.hyf.proxyserver.server.ssl.cert.SelfSignedCertificateManager;
import com.hyf.proxyserver.server.util.ProxyUtils;
import com.hyf.proxyserver.server.util.ServerUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MitmServerTests {

    public static void main3(String[] args) {
        MitmServerConfig mitmServerConfig = new MitmServerConfig();
        mitmServerConfig.setPort(8088);
        MitmServer mitmServer = new MitmServer(mitmServerConfig);
        mitmServer.addChannelInitializer(new RelayChannelInitializer() {
            @Override
            public void initChannelBeforeRelay(Channel inboundChannel, Channel outboundChannel) {
                inboundChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        // if (msg instanceof ByteBuf) {
                        //     ByteBuf buffer = (ByteBuf) msg;
                        //     if (HttpUtils.isHttpPacket2(buffer)) {
                        //         System.out.println("================================================================");
                        //         System.out.println(ProxyUtils.getContentString(buffer));
                        //     }
                        //     try {
                        //         File certificate = SelfSignedCertificateManager.getRoot().certificate();
                        //         ByteBuf body = ProxyUtils.toByteBuf(Files.readAllBytes(certificate.toPath()));
                        //         byte[] bytes1 = ProxyUtils.getContent(body);
                        //         String body2 = "HTTP/1.1 200 OK\n" +
                        //                 "Server: openresty\n" +
                        //                 "Date: Tue, 06 Aug 2024 07:48:30 GMT\n" +
                        //                 "Content-Length: " + bytes1.length + "\n" +
                        //                 "Content-Type: application/x-x509-ca-cert\n\n";
                        //         byte[] bytes = body2.getBytes(StandardCharsets.UTF_8);
                        //         ByteBuf byteBuf = Unpooled.copiedBuffer(bytes, bytes1);
                        //         ctx.writeAndFlush(byteBuf);
                        //         return;
                        //     } catch (IOException e) {
                        //         throw new RuntimeException(e);
                        //     }
                        // }
                        ctx.fireChannelRead(msg);
                    }
                });
            }
        });
        mitmServer.addListeners(new TrafficListener() {
            @Override
            public void listen(RelayContext<?> context) {
                Object msg = context.getRelayMsg();
                if (!(msg instanceof ByteBuf)) {
                    return;
                }
                ByteBuf buffer = (ByteBuf) msg;
            }
        });
        // mitmServer.addCapturers(new ByteBufTrafficCapturer() {
        //     @Override
        //     protected void captureByteBuf(RelayContext<ByteBuf> context) {
        //         ByteBuf msg = context.getRelayMsg();
        //         System.out.println(ProxyUtils.getContentString(msg));
        //     }
        // });
        mitmServer.start();
    }


    private static FullHttpResponse createCaCertificateDownloadResponse() {
        try {
            AsciiString APPLICATION_X509 = AsciiString.cached("application/x-x509-ca-cert");
            DefaultHttpHeaders headers = new DefaultHttpHeaders();
            headers.add(HttpHeaderNames.CONTENT_TYPE, APPLICATION_X509);
            File certificate = SelfSignedCertificateManager.getRoot().certificate();
            ByteBuf body = ProxyUtils.toByteBuf(Files.readAllBytes(certificate.toPath()));
            return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body, headers, EmptyHttpHeaders.INSTANCE);
        } catch (IOException e) {
            return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, ProxyUtils.toByteBuf(e.getMessage()));
        }
    }

    public static void main(String[] args) {
        MitmServerConfig mitmServerConfig = new MitmServerConfig();
        mitmServerConfig.setPort(8088);
        MitmServer mitmServer = new MitmServer(mitmServerConfig);
        ServerUtils.initSimpleHttpCapability(mitmServer, new HttpTrafficListener() {

            @Override
            public void listenClientRequest(RelayContext<FullHttpRequest> context, FullHttpRequest request) {
            }

            @Override
            public void listenRemoteResponse(RelayContext<FullHttpResponse> context, FullHttpRequest request, FullHttpResponse response) {
                System.out.println("================================================================");
                System.out.println(request);
                System.out.println(response);
                System.out.println("================================================================");
            }
        });
        mitmServer.start();
    }

}
