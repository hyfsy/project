package com.hyf.proxyserver.server.capturer;

import com.hyf.proxyserver.server.relay.RelayContext;
import io.netty.util.internal.TypeParameterMatcher;

public abstract class SimpleTrafficListener<I> implements TrafficListener {

    private final TypeParameterMatcher matcher = TypeParameterMatcher.find(this, SimpleTrafficListener.class, "I");

    @Override
    public boolean accept(RelayContext<?> context) {
        @SuppressWarnings("unchecked")
        RelayContext<I> icontext = (RelayContext<I>) context;
        return acceptInboundMessage(icontext.getRelayMsg()) && accept0(icontext);
    }

    @Override
    public void listen(RelayContext<?> context) {
        @SuppressWarnings("unchecked")
        RelayContext<I> icontext = (RelayContext<I>) context;
        accept0(icontext);
    }

    protected boolean accept0(RelayContext<I> context) {
        return true;
    }

    protected abstract void capture0(RelayContext<I> context);

    private boolean acceptInboundMessage(Object msg) {
        return matcher.match(msg);
    }
}
