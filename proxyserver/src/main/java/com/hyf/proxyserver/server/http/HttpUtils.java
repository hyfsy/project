package com.hyf.proxyserver.server.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.AsciiString;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HttpUtils {

    static final List<HttpMethod> methods = new ArrayList<>();

    static {
        methods.add(HttpMethod.GET);
        methods.add(HttpMethod.POST);
        methods.add(HttpMethod.CONNECT);
        methods.add(HttpMethod.DELETE);
        methods.add(HttpMethod.PUT);
        methods.add(HttpMethod.PATCH);
        methods.add(HttpMethod.OPTIONS);
        methods.add(HttpMethod.HEAD);
        methods.add(HttpMethod.TRACE);
    }

    public static ByteBuf isHttpPacket(ByteBuf buffer) {
        int readableBytes = buffer.readableBytes();
        if (readableBytes < 8) { // max method length + empty space
            return null;
        }
        int spaceIdx = -1;
        for (int i = 0; i < 8; i++) {
            if (buffer.getByte(i) == ' ') {
                spaceIdx = i;
                break;
            }
        }
        if (spaceIdx == -1) {
            return null;
        }
        boolean matchHttp = false;
        String methodString = buffer.toString(0, spaceIdx, StandardCharsets.UTF_8);
        for (HttpMethod method : methods) {
            if (method.asciiName().contentEqualsIgnoreCase(methodString)) {
                matchHttp = true;
                break;
            }
        }

        return matchHttp ? buffer : null;
    }
}
