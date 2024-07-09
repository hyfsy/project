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

package com.hyf.proxyserver.server.http;

import com.hyf.proxyserver.server.socks.SocksUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.AttributeKey;

@ChannelHandler.Sharable
public final class HttpProtocolUnificationServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 用了socks代理，这边就不进行http代理的流程了
        if (!SocksUtils.useSocks(ctx)) {
            if (msg instanceof ByteBuf) {
                // 判断是否为http报文，是则向下执行
                if (HttpUtils.isHttpPacket((ByteBuf) msg) != null) {
                    // 协议升级 h2/quic ? 协议自动升级，此处无需考虑
                    // 解析第一个http报文用
                    ctx.pipeline().addAfter(ctx.name(), HttpObjectAggregator.class.getName(), new HttpObjectAggregator(Integer.MAX_VALUE));
                    ctx.pipeline().addAfter(ctx.name(), HttpRequestDecoder.class.getName(), new HttpRequestDecoder());
                    ctx.pipeline().addAfter(ctx.name(), HttpResponseEncoder.class.getName(), new HttpResponseEncoder());
                }
            }
        }

        ctx.pipeline().remove(this);
        ctx.fireChannelRead(msg);
    }

}