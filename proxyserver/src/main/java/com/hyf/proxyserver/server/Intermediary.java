
package com.hyf.proxyserver.server;

import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public final class Intermediary {

    private static final InternalLogger LOG = InternalLoggerFactory.getInstance(Intermediary.class);

    private static final List<TrafficCapturer> listeners = new CopyOnWriteArrayList<>();

    static {
        ServiceLoader.load(TrafficCapturer.class).forEach(Intermediary::addListener);
    }

    /* package */
    static Object capture(Channel inboundChannel, Channel outboundChannel, Object msg,
                          boolean fromClient) {
        if (!(msg instanceof ByteBuf)) {
            LOG.warn("ignored message: " + msg.getClass().getName());
            return msg;
        }
        ByteBuf byteBuf = (ByteBuf) msg;

        for (TrafficCapturer listener : listeners) {
            if (listener.filter(inboundChannel, outboundChannel, byteBuf, fromClient)) {
                byteBuf = listener.capture(inboundChannel, outboundChannel, byteBuf, fromClient);
            }
        }
        return byteBuf;
    }

    public static void addListener(TrafficCapturer listener) {
        listeners.add(listener);
    }

    public static void removeListener(TrafficCapturer listener) {
        listeners.remove(listener);
    }

    public static void clearListener() {
        listeners.clear();
    }
}
