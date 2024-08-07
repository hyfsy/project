package com.hyf.proxyserver.server.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpMethod;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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

    public static boolean isHttpPacket2(ByteBuf buffer) {
        int maxRawLen = 1042; // 7 + 1 + 1024 + 1 + 8 + 1
        int lineEndIdx = -1;
        for (int i = 0; i < maxRawLen; i++) {
            if (buffer.readableBytes() <= i) {
                break;
            }
            if (buffer.getByte(i) == '\r' || buffer.getByte(i) == '\n') {
                lineEndIdx = i;
                break;
            }
        }
        if (lineEndIdx == -1) {
            return false;
        }

        String firstLine = buffer.toString(0, lineEndIdx, StandardCharsets.UTF_8);
        String[] segments = firstLine.split(" ");
        if (segments.length != 3) {
            return false;
        }

        return isRequestPacket(segments) || isResponsePacket(segments);
    }

    // GET http://www.baidu.com/ HTTP/1.1
    private static boolean isRequestPacket(String[] segments) {
        boolean a = false;
        boolean b = false;
        boolean c = false;
        String segment1 = segments[0];
        String segment2 = segments[1];
        String segment3 = segments[2];
        for (HttpMethod method : methods) {
            if (method.asciiName().contentEqualsIgnoreCase(segment1)) {
                a = true;
                break;
            }
        }
        if (segment2.startsWith("http")) {
            b = true;
        }
        if (segment3.startsWith("HTTP/")) {
            c = true;
        }
        return a && b && c;
    }

    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");

    // HTTP/1.1 200 OK
    private static boolean isResponsePacket(String[] segments) {
        String segment1 = segments[0];
        String segment2 = segments[1];

        boolean a = segment1.startsWith("HTTP/");
        boolean b = NUMBER_PATTERN.matcher(segment2).matches();

        return a && b;
    }
}
