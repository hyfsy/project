package com.hyf.proxyserver.server.ssl;

import com.hyf.proxyserver.server.util.ProxyUtils;
import com.hyf.proxyserver.server.ssl.cert.SelfSignedCertificateManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;

public class SslUtils {

    public static boolean useSsl(ChannelHandlerContext ctx) {
        return ctx.pipeline().get(SslHandler.class.getName()) != null;
    }

    public static String getSslHandlerName() {
        return SslHandler.class.getName();
    }

    public static SslContext getServerRootSslContext() {
        try {
            return SslContextBuilder.forServer(SelfSignedCertificateManager.getRoot().key(), SelfSignedCertificateManager.getRoot().cert()).build();
        } catch (SSLException e) {
            throw new RuntimeException(e);
        }
    }

    public static SslContext getServerSslContext(String hostname) {
        try {
            // 证书链注意顺序，根证书最后
            return SslContextBuilder.forServer(SelfSignedCertificateManager.get(hostname).key(), SelfSignedCertificateManager.get(hostname).cert(), SelfSignedCertificateManager.getRoot().cert()).build();
        } catch (SSLException e) {
            throw new RuntimeException(e);
        }
    }

    private static volatile SslContext clientSslCtx;

    public static SslContext getClientSslContext() {
        if (clientSslCtx == null) {
            synchronized (ProxyUtils.class) {
                if (clientSslCtx == null) {
                    try {
                        clientSslCtx =
                                SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return clientSslCtx;
    }
}
