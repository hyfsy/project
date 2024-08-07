
package com.hyf.proxyserver.server.relay;

import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;

import com.hyf.proxyserver.server.capturer.TrafficListener;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public final class Intermediary {

    private static final InternalLogger LOG = InternalLoggerFactory.getInstance(Intermediary.class);

    private static final List<TrafficListener> trafficListeners = new CopyOnWriteArrayList<>();

    static {
        ServiceLoader.load(TrafficListener.class).forEach(Intermediary::addListener);
    }

    /* package */
    static void relay(ChannelHandlerContext ctx, Channel outboundChannel, Object msg) {
        DefaultRelayContext context = new DefaultRelayContext(ctx, outboundChannel, msg);
        for (TrafficListener trafficListener : trafficListeners) {
            if (trafficListener.accept(context)) {
                trafficListener.listen(context);
            }
            if (context.finished() && !context.isContinueCaptureWhenFinished()) {
                break;
            }
        }
        context.fireRelayFinished();
    }

    public static void addListener(TrafficListener listener) {
        trafficListeners.add(listener);
    }

    public static void removeListener(TrafficListener listener) {
        trafficListeners.remove(listener);
    }

    public static void clearListener() {
        trafficListeners.clear();
    }
}
