package com.hyf.proxyserver.server.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.http.*;

import java.util.ArrayList;
import java.util.List;

public class HttpCodecUtils {

    public static List<Object> encodeRequest(ChannelHandlerContext ctx, Object msg) {
        return new ExportHttpRequestEncoder().encode(ctx, msg);
    }

    public static List<Object> encodeResponse(ChannelHandlerContext ctx, Object msg) {
        return new ExportHttpResponseEncoder().encode(ctx, msg);
    }

    public static FullHttpRequest decodeRequest(ChannelHandlerContext ctx, Object msg) {
        ExportHttpRequestDecoder decoder = new ExportHttpRequestDecoder();
        List<Object> decode = decoder.decode(ctx, msg);
        ExportHttpObjectAggregator aggregator = new ExportHttpObjectAggregator(65535);
        return aggregator.decodeToRequest(ctx, decode);
    }

    public static FullHttpResponse decodeResponse(ChannelHandlerContext ctx, Object msg) {
        ExportHttpResponseDecoder decoder = new ExportHttpResponseDecoder();
        List<Object> decode = decoder.decode(ctx, msg);
        ExportHttpObjectAggregator aggregator = new ExportHttpObjectAggregator(65535);
        return aggregator.decodeToResponse(ctx, decode);
    }

    private static class ExportHttpRequestEncoder extends HttpRequestEncoder {

        public List<Object> encode(ChannelHandlerContext ctx, Object msg) {
            try {
                List<Object> out = new ArrayList<>();
                encode(ctx, msg, out);
                return out;
            } catch (EncoderException e) {
                throw e;
            } catch (Throwable t) {
                throw new EncoderException(t);
            }
        }
    }

    private static class ExportHttpResponseEncoder extends HttpResponseEncoder {

        public List<Object> encode(ChannelHandlerContext ctx, Object msg) {
            try {
                List<Object> out = new ArrayList<>();
                encode(ctx, msg, out);
                return out;
            } catch (EncoderException e) {
                throw e;
            } catch (Throwable t) {
                throw new EncoderException(t);
            }
        }
    }

    private static class ExportHttpResponseDecoder extends HttpResponseDecoder {

        public List<Object> decode(ChannelHandlerContext ctx, Object msg) {
            if (!(msg instanceof ByteBuf)) {
                throw new IllegalStateException("msg not a ByteBuf");
            }
            try {
                List<Object> out = new ArrayList<>();
                callDecode(ctx, (ByteBuf) msg, out);
                return out;
            } catch (EncoderException e) {
                throw e;
            } catch (Throwable t) {
                throw new EncoderException(t);
            }
        }
    }

    private static class ExportHttpRequestDecoder extends HttpRequestDecoder {

        public List<Object> decode(ChannelHandlerContext ctx, Object msg) {
            if (!(msg instanceof ByteBuf)) {
                throw new IllegalStateException("msg not a ByteBuf");
            }
            try {
                List<Object> out = new ArrayList<>();
                callDecode(ctx, (ByteBuf) msg, out);
                return out;
            } catch (EncoderException e) {
                throw e;
            } catch (Throwable t) {
                throw new EncoderException(t);
            }
        }

    }

    private static class ExportHttpObjectAggregator extends HttpObjectAggregator {

        public ExportHttpObjectAggregator(int maxContentLength) {
            super(maxContentLength);
        }

        public ExportHttpObjectAggregator(int maxContentLength, boolean closeOnExpectationFailed) {
            super(maxContentLength, closeOnExpectationFailed);
        }

        public FullHttpRequest decodeToRequest(ChannelHandlerContext ctx, List<Object> msg) {
            return (FullHttpRequest) decode(ctx, msg);
        }

        public FullHttpResponse decodeToResponse(ChannelHandlerContext ctx, List<Object> msg) {
            return (FullHttpResponse) decode(ctx, msg);
        }

        private FullHttpMessage decode(ChannelHandlerContext ctx, List<Object> msg) {
            try {
                List<Object> out = new ArrayList<>();
                for (Object o : msg) {
                    if (!(o instanceof HttpObject)) {
                        throw new IllegalStateException("msg not a HttpObject");
                    }
                    decode(ctx, (HttpObject) o, out);
                }
                return (FullHttpMessage) out.get(0);
            } catch (EncoderException e) {
                throw e;
            } catch (Throwable t) {
                throw new EncoderException(t);
            }
        }
    }
}
