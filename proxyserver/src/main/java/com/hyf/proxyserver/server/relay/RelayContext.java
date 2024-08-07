package com.hyf.proxyserver.server.relay;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class RelayContext<T> {

    /**
     * 入站上下文
     */
    private final ChannelHandlerContext ctx;
    /**
     * 入站通道
     */
    private final Channel inboundChannel;
    /**
     * 出站通道
     */
    private final Channel outboundChannel;
    /**
     * 监听的消息
     */
    private final T relayMsg;
    private final boolean fromClient;

    private Object result;
    private boolean continueCaptureWhenFinished = false;
    private boolean fireNext = true;

    public RelayContext(ChannelHandlerContext ctx, Channel outboundChannel, T relayMsg) {
        this.ctx = ctx;
        this.inboundChannel = ctx.channel();
        this.outboundChannel = outboundChannel;
        this.relayMsg = relayMsg;
        this.fromClient = ctx.channel().parent() != null;
    }

    /**
     * 消息是客户端发的返回true，否则返回false
     *
     * @return 消息是客户端发的返回true，否则返回false
     */
    public boolean fromClient() {
        return fromClient;
    }

    public boolean finished() {
        return result != null;
    }

    public void finishWith(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public Channel getInboundChannel() {
        return inboundChannel;
    }

    public Channel getOutboundChannel() {
        return outboundChannel;
    }

    public T getRelayMsg() {
        return relayMsg;
    }

    public void enableFireNext() {
        this.fireNext = true;
    }

    public void disableFireNext() {
        this.fireNext = false;
    }

    public void setContinueCaptureWhenFinished(boolean continueCaptureWhenFinished) {
        this.continueCaptureWhenFinished = continueCaptureWhenFinished;
    }

    public boolean isContinueCaptureWhenFinished() {
        return continueCaptureWhenFinished;
    }

    /* package */ void fireRelayFinished() {
        if (finished()) {
            ctx.writeAndFlush(result);
        } else {
            outboundChannel.writeAndFlush(relayMsg);
            if (fireNext) {
                ctx.fireChannelRead(relayMsg);
            }
        }
    }
}
