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

package com.hyf.proxyserver.server;

import com.hyf.proxyserver.server.http.HttpProtocolUnificationServerHandler;
import com.hyf.proxyserver.server.http.HttpServerHandler;
import com.hyf.proxyserver.server.socks.SocksPortUnificationServerHandler;
import com.hyf.proxyserver.server.socks.SocksServerHandler;
import com.hyf.proxyserver.server.ssl.SniSupportOptionalSslHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ProxyServerInitializer extends ChannelInitializer<SocketChannel> {

    public static final ProxyServerInitializer INSTANCE = new ProxyServerInitializer();

    private static final List<RelayChannelInitializer> initializers = new CopyOnWriteArrayList<>();

    static {
        ServiceLoader.load(RelayChannelInitializer.class).forEach(initializers::add);
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        // SocksPortUnificationServerHandler -> socks协议编解码选择
        // SocksServerHandler -> socks协议连接处理
        // SniSupportOptionalSslHandler -> ssl支持
        // HttpProtocolUnificationServerHandler -> http协议编解码选择
        // HttpServerHandler -> http协议连接处理
        ch.pipeline().addLast(new SocksPortUnificationServerHandler());
        ch.pipeline().addLast(SocksServerHandler.INSTANCE);
        ch.pipeline().addLast(new SniSupportOptionalSslHandler());
        ch.pipeline().addLast(new HttpProtocolUnificationServerHandler());
        ch.pipeline().addLast(HttpServerHandler.INSTANCE);
    }

    public void initChannelRelay(Channel inboundChannel, Channel outboundChannel) {
        inboundChannel.pipeline().addLast(RelayHandler.class.getName(), new RelayHandler(outboundChannel));
        outboundChannel.pipeline().addLast(RelayHandler.class.getName(), new RelayHandler(inboundChannel));

        // 此处可添加针对特定协议的编解码的功能
        initializers.forEach(i -> i.initChannel(inboundChannel, outboundChannel));
    }
}
