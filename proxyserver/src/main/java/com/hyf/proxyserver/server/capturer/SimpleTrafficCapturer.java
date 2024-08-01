package com.hyf.proxyserver.server.capturer;

import io.netty.channel.Channel;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.TypeParameterMatcher;

public abstract class SimpleTrafficCapturer<I> implements TrafficCapturer {

    private final TypeParameterMatcher matcher = TypeParameterMatcher.find(this, SimpleTrafficCapturer.class, "I");

    @Override
    public boolean accept(Channel inboundChannel, Channel outboundChannel, Object msg, boolean fromClient) {
        @SuppressWarnings("unchecked")
        I imsg = (I) msg;
        return acceptInboundMessage(msg) && accept0(inboundChannel, outboundChannel, imsg, fromClient);
    }

    @Override
    public Object capture(Channel inboundChannel, Channel outboundChannel, Object msg, boolean fromClient) {
        @SuppressWarnings("unchecked")
        I imsg = (I) msg;
        return capture0(inboundChannel, outboundChannel, imsg, fromClient);
    }

    protected boolean accept0(Channel inboundChannel, Channel outboundChannel, I msg, boolean fromClient) {
        return true;
    }

    protected abstract I capture0(Channel inboundChannel, Channel outboundChannel, I msg, boolean fromClient);

    private boolean acceptInboundMessage(Object msg) {
        return matcher.match(msg);
    }
}
