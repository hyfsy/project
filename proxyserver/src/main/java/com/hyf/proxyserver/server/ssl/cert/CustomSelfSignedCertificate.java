package com.hyf.proxyserver.server.ssl.cert;

import java.io.File;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class CustomSelfSignedCertificate {

    private final File certificate;
    private final File privateKey;
    private final X509Certificate cert;
    private final PrivateKey key;

    public CustomSelfSignedCertificate(File certificate, File privateKey, X509Certificate cert, PrivateKey key) {
        this.certificate = certificate;
        this.privateKey = privateKey;
        this.cert = cert;
        this.key = key;
    }

    public File certificate() {
        return certificate;
    }

    public File privateKey() {
        return privateKey;
    }

    public X509Certificate cert() {
        return cert;
    }

    public PrivateKey key() {
        return key;
    }

}
