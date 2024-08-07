
package com.hyf.proxyserver.server.relay;

import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;

import com.hyf.proxyserver.server.capturer.TrafficCapturer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public final class Intermediary {

    private static final InternalLogger LOG = InternalLoggerFactory.getInstance(Intermediary.class);

    private static final List<TrafficCapturer> capturers = new CopyOnWriteArrayList<>();

    static {
        ServiceLoader.load(TrafficCapturer.class).forEach(Intermediary::addListener);
    }

    /* package */
    static void capture(ChannelHandlerContext ctx, Channel outboundChannel, Object msg) {
        DefaultRelayContext context = new DefaultRelayContext(ctx, outboundChannel, msg);
        for (TrafficCapturer capturer : capturers) {
            if (capturer.accept(context)) {
                capturer.capture(context);
            }
            if (context.finished() && !context.isContinueCaptureWhenFinished()) {
                break;
            }
        }
        context.fireRelayFinished();
    }

    public static void addListener(TrafficCapturer listener) {
        capturers.add(listener);
    }

    public static void removeListener(TrafficCapturer listener) {
        capturers.remove(listener);
    }

    public static void clearListener() {
        capturers.clear();
    }
}
