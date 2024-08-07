package com.hyf.proxyserver;

import com.hyf.proxyserver.extensions.http.HttpTrafficCapturer;
import com.hyf.proxyserver.server.capturer.ByteBufTrafficCapturer;
import com.hyf.proxyserver.server.capturer.TrafficCapturer;
import com.hyf.proxyserver.server.relay.RelayContext;
import com.hyf.proxyserver.server.ssl.cert.CustomSelfSignedCertificate;
import com.hyf.proxyserver.server.ssl.cert.SelfSignedCertificateManager;
import com.hyf.proxyserver.server.util.ProxyUtils;
import com.hyf.proxyserver.server.util.ServerUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.AsciiString;

import java.io.*;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class MobileMitmServerTests {
    public static void main(String[] args) throws Exception {
        FileInputStream mitmFile = new FileInputStream("C:\\Users\\user\\Desktop\\mitm-server.crt");
        FileInputStream fiddlerFile = new FileInputStream("C:\\Users\\user\\Desktop\\FiddlerRoot.cer");
        FileInputStream proxyFile = new FileInputStream("C:\\Users\\user\\Desktop\\proxyserver.crt");
        FileInputStream nettyFile = new FileInputStream("C:\\Users\\user\\Desktop\\netty.crt");
        CertificateFactory factory = CertificateFactory.getInstance("X509");
        X509Certificate mitm = (X509Certificate) factory.generateCertificate(mitmFile);
        X509Certificate fiddler = (X509Certificate) factory.generateCertificate(fiddlerFile);
        X509Certificate proxy = (X509Certificate) factory.generateCertificate(proxyFile);
        X509Certificate netty = (X509Certificate) factory.generateCertificate(nettyFile);
        System.out.println();
    }

    public static void main2(String[] args) {
        MitmServerConfig mitmServerConfig = new MitmServerConfig();
        mitmServerConfig.setPort(8883);
        MitmServer mitmServer = new MitmServer(mitmServerConfig);
        // mitmServer.addCapturers(new ByteBufTrafficCapturer() {
        //     @Override
        //     protected ByteBuf captureByteBuf(Channel inboundChannel, Channel outboundChannel, ByteBuf msg, boolean fromClient) {
        //         System.out.println(ProxyUtils.getContentString(msg));
        //         return msg;
        //     }
        // });
        ServerUtils.initSimpleHttpCapability(mitmServer, new HttpTrafficCapturer() {
            @Override
            public void captureClientRequest(RelayContext<FullHttpRequest> context, FullHttpRequest request) {
                System.out.println(request);
            }

            @Override
            public void captureRemoteResponse(RelayContext<FullHttpResponse> context, FullHttpRequest request, FullHttpResponse response) {
                System.out.println(response);
            }
        });
        mitmServer.start();
    }

}
