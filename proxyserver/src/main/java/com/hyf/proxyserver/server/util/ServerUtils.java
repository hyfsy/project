package com.hyf.proxyserver.server.util;

import com.hyf.proxyserver.MitmServer;
import com.hyf.proxyserver.extensions.FileDumpTrafficListener;
import com.hyf.proxyserver.extensions.http.DefaultHttpTrafficListener;
import com.hyf.proxyserver.extensions.http.DefaultHttpRelayChannelInitializer;
import com.hyf.proxyserver.extensions.http.HttpTrafficListener;

public class ServerUtils {

    public static void initSimpleHttpCapability(MitmServer mitmServer, HttpTrafficListener... httpTrafficListeners) {
        mitmServer.addChannelInitializer(new DefaultHttpRelayChannelInitializer());
        mitmServer.addListeners(new DefaultHttpTrafficListener(httpTrafficListeners));
    }

    public static void initFileDumpCapability(MitmServer mitmServer) {
        mitmServer.addListeners(new FileDumpTrafficListener());
    }
}
