/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License, version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the License at:
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.hyf.proxyserver;

import com.hyf.proxyserver.server.ProxyServerInitializer;

import com.hyf.proxyserver.server.ssl.cert.SelfSignedCertificateManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.File;
import java.security.cert.CertificateException;

/**
 * <pre>
 *     https://zhuanlan.zhihu.com/p/438521117
 *     https://wiyi.org/socks5-protocol-in-deep.html
 *     https://blog.csdn.net/rpsate/article/details/131769396
 *     https://www.openwrt.pro/post-93.html
 * </pre>
 */
public final class SocksServer {

    static final int PORT = Integer.parseInt(System.getProperty("port", "1080"));

    public static void main(String[] args) throws Exception {
        if (resolveArgs(args)) {
            return;
        }
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ProxyServerInitializer());
            b.bind(PORT).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    // --crt C:\\Users\\user\Desktop
    private static boolean resolveArgs(String[] args) {

        String downloadPath = null;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("--crt".equals(arg) && i + 1 < args.length) {
                downloadPath = args[i + 1];
            }
        }
        if (downloadPath != null) {
            try {
                File certificate = SelfSignedCertificateManager.getRoot().certificate();
                SelfSignedCertificateManager.writeTo(certificate, new File(downloadPath + File.separator + certificate.getName()));
            } catch (CertificateException e) {
                throw new RuntimeException("Failed to download crt file, reason: " + e.getMessage(), e);
            }
            return true;
        }

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("--generate-new-crt".equals(arg) && i + 1 < args.length) {
                SelfSignedCertificateManager.clearCache();
                return true;
            }
        }

        return false;
    }
}
