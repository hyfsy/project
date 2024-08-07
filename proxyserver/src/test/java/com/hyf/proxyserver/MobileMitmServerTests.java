package com.hyf.proxyserver;

import com.hyf.proxyserver.extensions.http.HttpTrafficListener;
import com.hyf.proxyserver.server.relay.RelayContext;
import com.hyf.proxyserver.server.util.ServerUtils;
import io.netty.handler.codec.http.*;

import java.io.*;
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
        ServerUtils.initSimpleHttpCapability(mitmServer, new HttpTrafficListener() {
            @Override
            public void listenClientRequest(RelayContext<FullHttpRequest> context, FullHttpRequest request) {
                System.out.println(request);
            }

            @Override
            public void listenRemoteResponse(RelayContext<FullHttpResponse> context, FullHttpRequest request, FullHttpResponse response) {
                System.out.println(response);
            }
        });
        mitmServer.start();
    }

}
