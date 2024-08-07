package com.hyf.proxyserver.extensions.http;

import com.hyf.proxyserver.server.ssl.cert.SelfSignedCertificateManager;
import com.hyf.proxyserver.server.util.ProxyUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;

import java.io.*;
import java.nio.file.Files;

public class CaCertificateDownloadChannelHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final AsciiString APPLICATION_X509 = AsciiString.cached("application/x-x509-ca-cert");

    private static final String CRT_DOWNLOAD_URI = "/proxyserver.crt";

    public CaCertificateDownloadChannelHandler() {
        super(false);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String uri = request.uri();
        if (!(uri.endsWith(CRT_DOWNLOAD_URI))) {
            ctx.fireChannelRead(request);
            return;
        }

        try {
            FullHttpResponse response = createCaCertificateDownloadResponse(request);
            ctx.write(response);
        } finally {
            request.release();
        }
    }

    private static FullHttpResponse createCaCertificateDownloadResponse(FullHttpRequest request) {
        try {
            DefaultHttpHeaders headers = new DefaultHttpHeaders();
            headers.add(HttpHeaderNames.CONTENT_TYPE, APPLICATION_X509);
            File certificate = SelfSignedCertificateManager.getRoot().certificate();
            ByteBuf body = ProxyUtils.toByteBuf(Files.readAllBytes(certificate.toPath()));
            return new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.OK, body, headers, EmptyHttpHeaders.INSTANCE);
        } catch (IOException e) {
            return new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.INTERNAL_SERVER_ERROR, ProxyUtils.toByteBuf(e.getMessage()));
        }
    }
}
