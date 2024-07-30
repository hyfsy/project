package com.hyf.proxyserver;

public class MitmServerTests {

    public static void main(String[] args) {
        MitmServerConfig mitmServerConfig = new MitmServerConfig();
        MitmServer mitmServer = new MitmServer(mitmServerConfig);
        mitmServer.start();
    }
}
