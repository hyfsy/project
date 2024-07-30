
package com.hyf.proxyserver;

import java.io.File;
import java.net.InetSocketAddress;

import com.hyf.proxyserver.server.util.ProxyUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

public final class SocksClient {

    static final InetSocketAddress PROXY_ADDR = new InetSocketAddress(System.getProperty("host", "localhost"),
            Integer.parseInt(System.getProperty("port", "8083")));
    static final InetSocketAddress REAL_ADDR = new InetSocketAddress("www.baidu.com", 443);

    public static void main(String[] args) throws Exception {

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(workerGroup).channel(NioSocketChannel.class).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .option(ChannelOption.SO_KEEPALIVE, true).handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        // // SOCKS5
                        // pipeline.addLast(new Socks5ProxyHandler(PROXY_ADDR));
                        // // TLS
                        // pipeline.addLast(ClientSslUtil.getClientSslContext().newHandler(ch.alloc(),
                        //         SocksClient.REAL_ADDR.getHostString(), SocksClient.REAL_ADDR.getPort()));
                        // HTTP
                        // pipeline.addLast(new HttpProxyHandler(PROXY_ADDR));
                        pipeline.addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                receiveMessage(msg);
                                super.channelRead(ctx, msg);
                            }
                        });
                    }
                });

        ChannelFuture channelFuture = b.connect(REAL_ADDR).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    // Connection established use handler provided results
                } else {
                    // Close the connection if the connection attempt has failed.
                }
            }
        });

        Channel channel = channelFuture.sync().channel();
        channel.writeAndFlush(ProxyUtils.toByteBuf(body())).sync();

    }

    private static void receiveMessage(Object msg) {
        if (msg instanceof ByteBuf) {
            byte[] bytes = ProxyUtils.getContent((ByteBuf) msg);
            System.out.print(new String(bytes));
        }
    }

    private static String body() {
        return "GET / HTTP/1.1\nHost: www.baidu.com\n" + "Connection: keep-alive\n" + "Cache-Control: max-age=0\n"
                + "sec-ch-ua: \"Google Chrome\";v=\"123\", \"Not:A-Brand\";v=\"8\", \"Chromium\";v=\"123\"\n"
                + "sec-ch-ua-mobile: ?0\n" + "sec-ch-ua-platform: \"Windows\"\n" + "Upgrade-Insecure-Requests: 1\n"
                + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36\n"
                + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\n"
                + "Sec-Fetch-Site: none\n" + "Sec-Fetch-Mode: navigate\n" + "Sec-Fetch-User: ?1\n"
                + "Sec-Fetch-Dest: document\n"
                // http协议的加解密
                // + "Accept-Encoding: gzip, deflate, br, zstd\n"
                + "Accept-Language: zh-CN,zh;q=0.9\n"
                + "Cookie: BDUSS=XNRMXRVZjZkRzJQbE96LXVvemt0Y28zdjRFZmNqc3pnMkhVc2ZxdkJIUnZ0R1psSVFBQUFBJCQAAAAAAAAAAAEAAADjQFR917nC5LfJz-g3AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAG8nP2VvJz9lZ; BDUSS_BFESS=XNRMXRVZjZkRzJQbE96LXVvemt0Y28zdjRFZmNqc3pnMkhVc2ZxdkJIUnZ0R1psSVFBQUFBJCQAAAAAAAAAAAEAAADjQFR917nC5LfJz-g3AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAG8nP2VvJz9lZ; BAIDUID=91E9413B9233D6E80663191D12ED19BC:FG=1; BD_UPN=12314753; PSTM=1710206110; BIDUPSID=E7D25DBB147D804BA52D207CB818D9C6; H_PS_PSSID=40080_40374_40416_40303_39661_40505_40510_40514_40397_60045_60028_60031; H_WISE_SIDS=40080_40374_40416_40303_39661_40505_40510_40514_40397_60045_60028_60031; H_WISE_SIDS_BFESS=40080_40374_40416_40303_39661_40505_40510_40514_40397_60045_60028_60031; MCITY=-289%3A; BDSFRCVID=aWIOJexroG3hZf5tLGOFrUDTcWxl6ajTDYLEOwXPsp3LGJLVYapLEG0Ptf1Cwl_MMPqsogKK0eOTH6KF_2uxOjjg8UtVJeC6EG0Ptf8g0M5; H_BDCLCKID_SF=JnCeoCLafID3H48k-4QEbbQH-UnLqb3O22OZ04n-ah05jMcIjJOdQU4rKRoRLPJUMItq3Rcm3UTdsq76Wh35K5tTQP6rLqcK-gJ4KKJxbnLaEP31BUcY0b_yhUJiB5O-Ban7_qvIXKohJh7FM4tW3J0ZyxomtfQxtNRJ0DnjtpChbC-lDTL-jjvLeU5eetjK2CntsJOOaCvRVKnOy4oWK441DU7zKjQJBgv9-4oG3UbjSPn1j4Q83M04X-o9-hvT-54e2p3FBUQZHtJsQft20b0EDtbf2pRuBGRDQb7jWhk2eq72y5jvQlRX5q79atTMfNTJ-qcH0KQpsIJM5-DWbT8IjHCJJ6tDJJIOoCvt-5rDHJTg5DTjhPrM-JJAWMT-MTryKKOSyJLhfhRjXUjK3f07-nJO2T5K2HnRhlRNB-3iV-OxDUvnyxAZWfQjWUQxtNRJ0DjnbbRr8q6MKJbobUPUDMo9LUkqW2cdot5yBbc8eIna5hjkbfJBQttjQn3hfIkj2CKLK-oj-D8mjjAW3j; delPer=0; BD_CK_SAM=1; PSINO=5; BDSFRCVID_BFESS=aWIOJexroG3hZf5tLGOFrUDTcWxl6ajTDYLEOwXPsp3LGJLVYapLEG0Ptf1Cwl_MMPqsogKK0eOTH6KF_2uxOjjg8UtVJeC6EG0Ptf8g0M5; H_BDCLCKID_SF_BFESS=JnCeoCLafID3H48k-4QEbbQH-UnLqb3O22OZ04n-ah05jMcIjJOdQU4rKRoRLPJUMItq3Rcm3UTdsq76Wh35K5tTQP6rLqcK-gJ4KKJxbnLaEP31BUcY0b_yhUJiB5O-Ban7_qvIXKohJh7FM4tW3J0ZyxomtfQxtNRJ0DnjtpChbC-lDTL-jjvLeU5eetjK2CntsJOOaCvRVKnOy4oWK441DU7zKjQJBgv9-4oG3UbjSPn1j4Q83M04X-o9-hvT-54e2p3FBUQZHtJsQft20b0EDtbf2pRuBGRDQb7jWhk2eq72y5jvQlRX5q79atTMfNTJ-qcH0KQpsIJM5-DWbT8IjHCJJ6tDJJIOoCvt-5rDHJTg5DTjhPrM-JJAWMT-MTryKKOSyJLhfhRjXUjK3f07-nJO2T5K2HnRhlRNB-3iV-OxDUvnyxAZWfQjWUQxtNRJ0DjnbbRr8q6MKJbobUPUDMo9LUkqW2cdot5yBbc8eIna5hjkbfJBQttjQn3hfIkj2CKLK-oj-D8mjjAW3j; BAIDUID_BFESS=91E9413B9233D6E80663191D12ED19BC:FG=1; BA_HECTOR=8ka00l0425a40gak818k212gc40sub1j16lrf1s; ZFY=Qkhppr:A1xpofbI8ALgORELOhO2FhK:BiTEFOuVzwgzw8:C; B64_BOT=1; H_PS_645EC=1842hZgKyjwYUgbmmwnAgIH5c5a7DnBe4OQ1p9hN0l%2BZHC%2Fmau2dknyNmzo; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598\n"
                + "\r\n";
    }

    public static class ClientSslUtil {

        private static volatile SslContext clientSslCtx;

        public static SslContext getClientSslContext() {
            if (clientSslCtx == null) {
                synchronized (ProxyUtils.class) {
                    if (clientSslCtx == null) {
                        try {
                            clientSslCtx =
                                    SslContextBuilder.forClient().trustManager(new File("C:\\Users\\user\\.proxyserver\\crt\\proxyserver.crt")).build();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            return clientSslCtx;
        }
    }
}
