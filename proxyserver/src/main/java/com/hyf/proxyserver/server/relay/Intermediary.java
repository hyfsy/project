
package com.hyf.proxyserver.server.relay;

import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;

import com.hyf.proxyserver.server.capturer.TrafficCapturer;
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
        for (TrafficCapturer listener : listeners) {
            if (listener.accept(inboundChannel, outboundChannel, msg, fromClient)) {
                msg = listener.capture(inboundChannel, outboundChannel, msg, fromClient);
            }
        }
        return msg;
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
