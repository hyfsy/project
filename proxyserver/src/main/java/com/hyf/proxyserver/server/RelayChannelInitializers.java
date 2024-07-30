package com.hyf.proxyserver.server;

import io.netty.channel.Channel;

import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;

public class RelayChannelInitializers {

    private static final List<RelayChannelInitializer> initializers = new CopyOnWriteArrayList<>();

    static {
        ServiceLoader.load(RelayChannelInitializer.class).forEach(initializers::add);
    }

    public static void initChannelRelay(Channel inboundChannel, Channel outboundChannel) {
        inboundChannel.pipeline().addLast(RelayHandler.class.getName(), new RelayHandler(outboundChannel));
        outboundChannel.pipeline().addLast(RelayHandler.class.getName(), new RelayHandler(inboundChannel));

        // 此处可添加针对特定协议的编解码的功能
        initializers.forEach(i -> i.initChannel(inboundChannel, outboundChannel));
    }

    public static void addInitializer(RelayChannelInitializer initializer) {
        initializers.add(initializer);
    }

    public static void removeInitializer(RelayChannelInitializer initializer) {
        initializers.remove(initializer);
    }

    public static void clearInitializer() {
        initializers.clear();
    }
}
