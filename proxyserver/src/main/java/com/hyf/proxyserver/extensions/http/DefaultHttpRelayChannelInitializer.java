package com.hyf.proxyserver.extensions.http;


import com.hyf.proxyserver.server.relay.RelayChannelInitializer;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class DefaultHttpRelayChannelInitializer implements RelayChannelInitializer {
    @Override
    public void initChannelBeforeRelay(Channel inboundChannel, Channel outboundChannel) {
        outboundChannel.pipeline().addLast(new HttpClientCodec());
        outboundChannel.pipeline().addLast(new HttpObjectAggregator(Integer.MAX_VALUE));
        inboundChannel.pipeline().addLast(new HttpServerCodec());
        inboundChannel.pipeline().addLast(new HttpObjectAggregator(Integer.MAX_VALUE));
    }
}
