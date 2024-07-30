package com.hyf.proxyserver;

import com.hyf.proxyserver.server.*;
import com.hyf.proxyserver.server.capturer.TrafficCapturer;
import com.hyf.proxyserver.server.relay.Intermediary;
import com.hyf.proxyserver.server.relay.RelayChannelInitializer;
import com.hyf.proxyserver.server.relay.RelayChannelInitializers;
import com.hyf.proxyserver.server.ssl.cert.SelfSignedCertificateManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.File;
import java.security.cert.CertificateException;

public class MitmServer {

    private final MitmServerConfig mitmServerConfig;

    public MitmServer(MitmServerConfig mitmServerConfig) {
        this.mitmServerConfig = mitmServerConfig;
    }

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public void start() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ProxyServerInitializer());
            b.bind(mitmServerConfig.getPort()).sync().channel().closeFuture().sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }

    public void addCapturers(TrafficCapturer... trafficCapturers) {
        for (TrafficCapturer trafficCapturer : trafficCapturers) {
            Intermediary.addListener(trafficCapturer);
        }
    }

    public void removeCapturers(TrafficCapturer... trafficCapturers) {
        for (TrafficCapturer trafficCapturer : trafficCapturers) {
            Intermediary.removeListener(trafficCapturer);
        }
    }

    public void addChannelInitializer(RelayChannelInitializer... relayChannelInitializers) {
        for (RelayChannelInitializer relayChannelInitializer : relayChannelInitializers) {
            RelayChannelInitializers.addInitializer(relayChannelInitializer);
        }
    }

    public void removeChannelInitializer(RelayChannelInitializer... relayChannelInitializers) {
        for (RelayChannelInitializer relayChannelInitializer : relayChannelInitializers) {
            RelayChannelInitializers.removeInitializer(relayChannelInitializer);
        }
    }

    public void dumpRootCrt(String storePath) {
        try {
            File certificate = SelfSignedCertificateManager.getRoot().certificate();
            SelfSignedCertificateManager.writeTo(certificate, new File(storePath + File.separator + certificate.getName()));
        } catch (CertificateException e) {
            throw new RuntimeException("Failed to download crt file, reason: " + e.getMessage(), e);
        }
    }

    public void generateNewCrt() {
        SelfSignedCertificateManager.clearCache();
    }
}
