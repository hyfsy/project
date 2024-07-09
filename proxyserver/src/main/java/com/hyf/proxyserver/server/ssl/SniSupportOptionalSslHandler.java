package com.hyf.proxyserver.server.ssl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ssl.SniHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.Mapping;
import io.netty.util.ReferenceCountUtil;

/**
 * 支持服务端进行证书选择的可选ssl
 */
public class SniSupportOptionalSslHandler extends EnhancedOptionalSslHandler {

    private final Mapping<String, SslContext> mapping;

    public SniSupportOptionalSslHandler() {
        this(new DynamicDomainWildcardMapping<>(SslUtils::getServerSslContext, SslUtils.getServerRootSslContext()));
    }

    public SniSupportOptionalSslHandler(Mapping<String, SslContext> mapping) {
        this.mapping = mapping;
    }

    @Override
    protected void handleSsl(ChannelHandlerContext ctx) {
        ctx.pipeline().replace(this, SniHandler.class.getName(), new SniHandler(mapping) {
            protected void replaceHandler(ChannelHandlerContext ctx, String hostname, SslContext sslContext) throws Exception {
                SslHandler sslHandler = null;
                try {
                    sslHandler = newSslHandlerServer(ctx, sslContext);
                    // 虽然默认名称是一样的，但此处还是重写下
                    ctx.pipeline().replace(this, newSslHandlerName(), sslHandler);
                    sslHandler = null;
                } finally {
                    if (sslHandler != null) {
                        ReferenceCountUtil.safeRelease(sslHandler.engine());
                    }
                }
            }
        });
    }
}
