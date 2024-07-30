
package com.hyf.proxyserver.extensions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

import com.hyf.proxyserver.server.capturer.TrafficCapturer;
import com.hyf.proxyserver.server.util.ProxyUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public final class FileDumpTrafficCapturer implements TrafficCapturer {

    public static final String DUMP_PATH = ProxyUtils.HOME + File.separator + "proxy-listen-data";

    private static final InternalLogger LOG = InternalLoggerFactory.getInstance(FileDumpTrafficCapturer.class);

    private final TrafficFilter trafficFilter = new DefaultTrafficFilter(new DefaultRuleStore());

    private final ExecutorService executor = Executors.newSingleThreadExecutor(new DefaultThreadFactory("traffic-dump", true));

    static {
        File dir = new File(DUMP_PATH);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                LOG.error("dump directory mkdirs failed: {}", dir.getAbsolutePath());
            }
        }
    }

    public static void main(String[] args) {
        printDumpPath();
        // clearDumpFiles();
    }

    @Override
    public boolean accept(Channel inboundChannel, Channel outboundChannel, Object msg, boolean fromClient) {
        return trafficFilter.filter(inboundChannel, outboundChannel, msg, fromClient);
    }

    @Override
    public Object capture(Channel inboundChannel, Channel outboundChannel, Object msg, boolean fromClient) {
        if (!(msg instanceof ByteBuf)) {
            return msg;
        }
        dumpToFile(inboundChannel, outboundChannel, (ByteBuf) msg, fromClient);
        return msg;
    }

    private void dumpToFile(Channel inboundChannel, Channel outboundChannel, ByteBuf buf, boolean fromClient) {
        CaptureData captureData = new CaptureData();
        captureData.inboundAddress = inboundChannel.remoteAddress();
        captureData.outboundAddress = outboundChannel.remoteAddress();
        captureData.inboundId = inboundChannel.id();
        captureData.msg = ProxyUtils.getContent(buf);
        captureData.fromClient = fromClient;
        executor.execute(() -> {
            String readableText = getAddressString(captureData.inboundAddress) + "__"
                    + getAddressString(captureData.outboundAddress) + "__" + (captureData.fromClient ? "req" : "resp");
            String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String saveFilePath = DUMP_PATH + File.separator + yyyyMMdd + File.separator + captureData.inboundId.asLongText() + "___" + readableText;
            File file = new File(saveFilePath);
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    LOG.error("dump directory mkdirs failed: {}", file.getParentFile().getAbsolutePath());
                }
            }
            try (FileOutputStream fis = new FileOutputStream(file, true)) {
                fis.write(captureData.msg);
            } catch (IOException e) {
                LOG.error("dump msg failed, channel: {}, buf: {}", captureData.inboundAddress, captureData.msg, e);
            }
        });

    }

    private String getAddressString(SocketAddress address) {
        return address.toString().replace("/", "").replace(":", "#");
    }

    private static void clearDumpFiles() {
        File dir = new File(DUMP_PATH);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.delete()) {
                    throw new RuntimeException("delete file failed: " + file.getAbsolutePath());
                }
            }
        }
        if (!new File(DUMP_PATH).delete()) {
            throw new RuntimeException("delete dump dir failed: " + DUMP_PATH);
        }
    }

    private static void printDumpPath() {
        System.out.println(DUMP_PATH);
    }

    private static class CaptureData {
        private SocketAddress inboundAddress;
        private SocketAddress outboundAddress;
        private ChannelId inboundId;
        private byte[] msg;
        private boolean fromClient;
    }
}
