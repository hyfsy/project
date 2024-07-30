package com.hyf.proxyserver;

import com.hyf.proxyserver.server.ssl.cert.SelfSignedCertificateManager;

import java.net.Proxy;

public class MitmClientConfig {

    private String host = "www.baidu.com";
    private int port = 443;

    private int connectTimeoutMillis = 3000;

    private boolean proxyEnabled = false;
    private Proxy.Type proxyType = Proxy.Type.SOCKS;
    private String proxyHost = "localhost";
    private int proxyPort = 1080;

    private boolean sslEnabled = true;
    private String trustCrtPath = SelfSignedCertificateManager.getRoot().certificate().getAbsolutePath();

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public void setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public boolean isProxyEnabled() {
        return proxyEnabled;
    }

    public void setProxyEnabled(boolean proxyEnabled) {
        this.proxyEnabled = proxyEnabled;
    }

    public Proxy.Type getProxyType() {
        return proxyType;
    }

    public void setProxyType(Proxy.Type proxyType) {
        this.proxyType = proxyType;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public boolean isSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    public String getTrustCrtPath() {
        return trustCrtPath;
    }

    public void setTrustCrtPath(String trustCrtPath) {
        this.trustCrtPath = trustCrtPath;
    }
}
