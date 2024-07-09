package com.hyf.proxyserver.server.ssl.cert;

import com.hyf.proxyserver.server.ProxyUtils;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.internal.SystemPropertyUtil;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SelfSignedCertificateManager {

    private static final Map<String, CustomSelfSignedCertificate> cache = new ConcurrentHashMap<>();

    public static final String ROOT = "HYF MITM Root CA";
    public static final String ALGORITHM = "RSA";

    public static final String JKS_PATH = ProxyUtils.HOME + File.separator + "crt";
    public static final String CRT_NAME = "proxyserver.crt";
    public static final String KEY_NAME = "proxyserver.key";

    private static final Date DEFAULT_NOT_BEFORE = new Date(SystemPropertyUtil.getLong("io.netty.selfSignedCertificate.defaultNotBefore", System.currentTimeMillis() - 31536000000L));
    private static final Date DEFAULT_NOT_AFTER = new Date(SystemPropertyUtil.getLong("io.netty.selfSignedCertificate.defaultNotAfter", 253402300799000L));
    private static final int DEFAULT_KEY_LENGTH_BITS = SystemPropertyUtil.getInt("io.netty.handler.ssl.util.selfSignedKeyStrength", 2048);

    private static Method toPrivateKeyMethod = null;

    static {
        try {
            Method toPrivateKeyMethod = SslContext.class.getDeclaredMethod("toPrivateKey", File.class, String.class);
            toPrivateKeyMethod.setAccessible(true);
            SelfSignedCertificateManager.toPrivateKeyMethod = toPrivateKeyMethod;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        getRoot(); // load root crt
    }

    public static CustomSelfSignedCertificate get(String fqdn) {
        return cache.computeIfAbsent(fqdn, k -> {
            CustomSelfSignedCertificate root = getRoot();
            KeyPair keyPair = generateKeyPair();
            return createSSC(ROOT, fqdn, new KeyPair(root.cert().getPublicKey(), root.key()), keyPair);
        });
    }

    public static CustomSelfSignedCertificate getRoot() {
        return cache.computeIfAbsent(ROOT, k -> {
            File defaultCrtFile = defaultCertificate();
            File defaultKeyFile = defaultPrivateKey();
            if (defaultCrtFile.exists() && defaultKeyFile.exists()) {
                try (FileInputStream fis = new FileInputStream(defaultCrtFile)) {
                    X509Certificate crt = (X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(fis);
                    PrivateKey privateKey = (PrivateKey) toPrivateKeyMethod.invoke(null, defaultKeyFile, null);
                    return new CustomSelfSignedCertificate(defaultCrtFile, defaultKeyFile, crt, privateKey);
                } catch (Exception e) {
                    throw new RuntimeException("getRoot failed", e);
                }
            }

            try {
                KeyPair keyPair = generateKeyPair();
                CustomSelfSignedCertificate ssc = createSSC(ROOT, ROOT, keyPair, keyPair);
                File certificate = ssc.certificate();
                writeTo(certificate, defaultCrtFile);
                File privateKey = ssc.privateKey();
                writeTo(privateKey, defaultKeyFile);
                return new CustomSelfSignedCertificate(defaultCrtFile, defaultKeyFile, ssc.cert(), ssc.key());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static CustomSelfSignedCertificate createSSC(String owner, String subject, KeyPair ownerKeyPair, KeyPair subjectKeyPair) {
        try {
            Object[] info = OpenJdkSelfSignedCertGenerator.generate(owner, subject, ownerKeyPair, subjectKeyPair, ThreadLocalInsecureRandom.current(), DEFAULT_NOT_BEFORE, DEFAULT_NOT_AFTER, ALGORITHM, ROOT.equals(subject));
            String crtPath = (String) info[0];
            String keyPath = (String) info[1];
            X509Certificate crt = (X509Certificate) info[2];
            PrivateKey key = (PrivateKey) info[3];
            return new CustomSelfSignedCertificate(new File(crtPath), new File(keyPath), crt, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static KeyPair generateKeyPair() {
        KeyPair keypair;
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            keyGen.initialize(DEFAULT_KEY_LENGTH_BITS, ThreadLocalInsecureRandom.current());
            keypair = keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new Error(e);
        }
        return keypair;
    }

    private static File defaultCertificate() {
        return new File(JKS_PATH + File.separator + CRT_NAME);
    }

    private static File defaultPrivateKey() {
        return new File(JKS_PATH + File.separator + KEY_NAME);
    }

    public static void clearCache() {
        if (!defaultCertificate().delete()) {
            throw new RuntimeException("delete crt file failed");
        }
        if (!defaultPrivateKey().delete()) {
            throw new RuntimeException("delete key file failed");
        }
    }

    public static void writeTo(File source, File target) throws CertificateException {
        File parentFile = target.getParentFile();
        if (!parentFile.exists()) {
            if (!parentFile.mkdirs()) {
                throw new CertificateException("target directory create failed");
            }
        }
        try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(source.toPath()));
             BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(target.toPath()))) {

            int len;
            byte[] bytes = new byte[1024];
            while ((len = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }

            bos.flush();
        } catch (IOException e) {
            throw new CertificateException("store crt file failed", e);
        }
    }

}
