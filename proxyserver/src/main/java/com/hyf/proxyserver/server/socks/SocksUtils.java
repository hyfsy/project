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

package com.hyf.proxyserver.server.socks;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.socksx.v4.Socks4ServerEncoder;
import io.netty.handler.codec.socksx.v5.Socks5ServerEncoder;

import java.util.List;

public final class SocksUtils {

    public static boolean useSocks(ChannelHandlerContext ctx) {
        List<String> names = ctx.pipeline().names();
        boolean useSocks = names.contains(Socks5ServerEncoder.class.getName());
        useSocks |= names.contains(Socks4ServerEncoder.class.getName());
        return useSocks;
    }
}
