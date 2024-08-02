package com.hyf.proxyserver.server.util;

import com.hyf.proxyserver.MitmServer;
import com.hyf.proxyserver.extensions.FileDumpTrafficCapturer;
import com.hyf.proxyserver.extensions.http.DefaultHttpTrafficCapturer;
import com.hyf.proxyserver.extensions.http.DefaultHttpRelayChannelInitializer;
import com.hyf.proxyserver.extensions.http.HttpTrafficCapturer;

public class ServerUtils {

    public static void initSimpleHttpCapability(MitmServer mitmServer, HttpTrafficCapturer... httpTrafficCapturers) {
        mitmServer.addChannelInitializer(new DefaultHttpRelayChannelInitializer());
        mitmServer.addCapturers(new DefaultHttpTrafficCapturer(httpTrafficCapturers));
    }

    public static void initFileDumpCapability(MitmServer mitmServer) {
        mitmServer.addCapturers(new FileDumpTrafficCapturer());
    }
}
