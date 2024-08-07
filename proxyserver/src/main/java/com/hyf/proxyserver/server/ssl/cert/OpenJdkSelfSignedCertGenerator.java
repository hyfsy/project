package com.hyf.proxyserver.server.ssl.cert;

/*
 * Copyright 2014 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SuppressJava6Requirement;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.math.BigInteger;
import java.security.cert.CertificateException;
import java.util.Vector;

/**
 * Generates a self-signed certificate using {@code sun.security.x509} package provided by OpenJDK.
 */
final class OpenJdkSelfSignedCertGenerator {
    private static final InternalLogger logger =
            InternalLoggerFactory.getInstance(OpenJdkSelfSignedCertGenerator.class);
    private static final Method CERT_INFO_SET_METHOD;
    private static final Constructor<?> ISSUER_NAME_CONSTRUCTOR;
    private static final Constructor<X509CertImpl> CERT_IMPL_CONSTRUCTOR;
    private static final Method CERT_IMPL_GET_METHOD;
    private static final Method CERT_IMPL_SIGN_METHOD;

    // Use reflection as JDK20+ did change things quite a bit.
    static {
        Method certInfoSetMethod = null;
        Constructor<?> issuerNameConstructor = null;
        Constructor<X509CertImpl> certImplConstructor = null;
        Method certImplGetMethod = null;
        Method certImplSignMethod = null;
        try {
            Object maybeCertInfoSetMethod = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    try {
                        return X509CertInfo.class.getMethod("set", String.class, Object.class);
                    } catch (Throwable cause) {
                        return cause;
                    }
                }
            });
            if (maybeCertInfoSetMethod instanceof Method) {
                certInfoSetMethod = (Method) maybeCertInfoSetMethod;
            } else {
                throw (Throwable) maybeCertInfoSetMethod;
            }

            Object maybeIssuerNameConstructor = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    try {
                        Class<?> issuerName = Class.forName("sun.security.x509.CertificateIssuerName", false,
                                PlatformDependent.getClassLoader(OpenJdkSelfSignedCertGenerator.class));
                        return issuerName.getConstructor(X500Name.class);
                    } catch (Throwable cause) {
                        return cause;
                    }
                }
            });
            if (maybeIssuerNameConstructor instanceof Constructor) {
                issuerNameConstructor = (Constructor<?>) maybeIssuerNameConstructor;
            } else {
                throw (Throwable) maybeIssuerNameConstructor;
            }

            Object maybeCertImplConstructor = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    try {
                        return X509CertImpl.class.getConstructor(X509CertInfo.class);
                    } catch (Throwable cause) {
                        return cause;
                    }
                }
            });
            if (maybeCertImplConstructor instanceof Constructor) {
                @SuppressWarnings("unchecked")
                Constructor<X509CertImpl> constructor = (Constructor<X509CertImpl>) maybeCertImplConstructor;
                certImplConstructor = constructor;
            } else {
                throw (Throwable) maybeCertImplConstructor;
            }

            Object maybeCertImplGetMethod = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    try {
                        return X509CertImpl.class.getMethod("get", String.class);
                    } catch (Throwable cause) {
                        return cause;
                    }
                }
            });
            if (maybeCertImplGetMethod instanceof Method) {
                certImplGetMethod = (Method) maybeCertImplGetMethod;
            } else {
                throw (Throwable) maybeCertImplGetMethod;
            }

            Object maybeCertImplSignMethod = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    try {
                        return X509CertImpl.class.getMethod("sign", PrivateKey.class, String.class);
                    } catch (Throwable cause) {
                        return cause;
                    }
                }
            });
            if (maybeCertImplSignMethod instanceof Method) {
                certImplSignMethod = (Method) maybeCertImplSignMethod;
            } else {
                throw (Throwable) maybeCertImplSignMethod;
            }
        } catch (Throwable cause) {
            logger.debug(OpenJdkSelfSignedCertGenerator.class.getSimpleName() + " not supported", cause);
        }
        CERT_INFO_SET_METHOD = certInfoSetMethod;
        ISSUER_NAME_CONSTRUCTOR = issuerNameConstructor;
        CERT_IMPL_CONSTRUCTOR = certImplConstructor;
        CERT_IMPL_GET_METHOD = certImplGetMethod;
        CERT_IMPL_SIGN_METHOD = certImplSignMethod;
    }

    @SuppressJava6Requirement(reason = "Usage guarded by dependency check")
    static Object[] generate(String ownerFqdn, String subjectFqdn, KeyPair caKeyPair, KeyPair key, SecureRandom random, Date notBefore, Date notAfter,
                             String algorithm, boolean root) throws Exception {
        if (CERT_INFO_SET_METHOD == null || ISSUER_NAME_CONSTRUCTOR == null ||
                CERT_IMPL_CONSTRUCTOR == null || CERT_IMPL_GET_METHOD == null || CERT_IMPL_SIGN_METHOD == null) {
            throw new UnsupportedOperationException(
                    OpenJdkSelfSignedCertGenerator.class.getSimpleName() + " not supported on the used JDK version");
        }

        // Prepare the information required for generating an X.509 certificate.
        X509CertInfo info = new X509CertInfo();
        X500Name subject = new X500Name(subjectFqdn, ownerFqdn, ownerFqdn, ownerFqdn, ownerFqdn, ownerFqdn);
        X500Name owner = new X500Name(ownerFqdn, ownerFqdn, ownerFqdn, ownerFqdn, ownerFqdn, ownerFqdn);

        CERT_INFO_SET_METHOD.invoke(info, X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
        CERT_INFO_SET_METHOD.invoke(info, X509CertInfo.SERIAL_NUMBER,
                new CertificateSerialNumber(new BigInteger(127, random)));
        // CERT_INFO_SET_METHOD.invoke(info, X509CertInfo.SERIAL_NUMBER,
        //         new CertificateSerialNumber(new BigInteger(64, random)));
        try {
            CERT_INFO_SET_METHOD.invoke(info, X509CertInfo.SUBJECT, new CertificateSubjectName(subject));
        } catch (InvocationTargetException ex) {
            if (ex.getCause() instanceof CertificateException) {
                CERT_INFO_SET_METHOD.invoke(info, X509CertInfo.SUBJECT, subject);
            } else {
                throw ex;
            }
        }
        try {
            CERT_INFO_SET_METHOD.invoke(info, X509CertInfo.ISSUER, ISSUER_NAME_CONSTRUCTOR.newInstance(owner));
        } catch (InvocationTargetException ex) {
            if (ex.getCause() instanceof CertificateException) {
                CERT_INFO_SET_METHOD.invoke(info, X509CertInfo.ISSUER, owner);
            } else {
                throw ex;
            }
        }
        CERT_INFO_SET_METHOD.invoke(info, X509CertInfo.VALIDITY, new CertificateValidity(notBefore, notAfter));
        CERT_INFO_SET_METHOD.invoke(info, X509CertInfo.KEY, new CertificateX509Key(key.getPublic()));
        CERT_INFO_SET_METHOD.invoke(info, X509CertInfo.ALGORITHM_ID,
                // sha256WithRSAEncryption
                new CertificateAlgorithmId(AlgorithmId.get("1.2.840.113549.1.1.11")));

        // ROOT CA
        if (root) {
            // TODO 根证书不能添加extensions字段，否则会有问题，目前不明确是什么问题
            CertificateExtensions extensions = new CertificateExtensions();
            extensions.set(BasicConstraintsExtension.NAME, new BasicConstraintsExtension(true, true, Integer.MAX_VALUE));
            extensions.set(KeyUsageExtension.NAME, new KeyUsageExtension(new boolean[]{true, false, true, false, false, true, true}));
            extensions.set(SubjectAlternativeNameExtension.NAME, new SubjectAlternativeNameExtension(new GeneralNames().add(new GeneralName(new DNSName("proxyserver")))));
            extensions.set(SubjectKeyIdentifierExtension.NAME, new SubjectKeyIdentifierExtension(new KeyIdentifier(caKeyPair.getPublic()).getIdentifier()));
            CERT_INFO_SET_METHOD.invoke(info, X509CertInfo.EXTENSIONS, extensions);
        }
        // Child CA
        else {
            CertificateExtensions extensions = new CertificateExtensions();
            // 重要：添加扩展属性，否则浏览器不识别 SubjectAlternativeName
            // extensions.set(SubjectAlternativeNameExtension.NAME, new SubjectAlternativeNameExtension(new GeneralNames().add(new GeneralName(new DNSName(subjectFqdn)))));
            // 其他不必要
            extensions.set(BasicConstraintsExtension.NAME, new BasicConstraintsExtension(true, false, -1));
            extensions.set(KeyUsageExtension.NAME, new KeyUsageExtension(new boolean[]{true, false, true}));
            extensions.set(SubjectKeyIdentifierExtension.NAME, new SubjectKeyIdentifierExtension(new KeyIdentifier(key.getPublic()).getIdentifier()));
            extensions.set(AuthorityKeyIdentifierExtension.NAME, new AuthorityKeyIdentifierExtension(new KeyIdentifier(caKeyPair.getPublic()), null, null));
            Vector<ObjectIdentifier> objectIdentifiers = new Vector<>();
            objectIdentifiers.add(new ObjectIdentifier("1.3.6.1.5.5.7.3.1"));
            extensions.set(ExtendedKeyUsageExtension.NAME, new ExtendedKeyUsageExtension(objectIdentifiers));
            CERT_INFO_SET_METHOD.invoke(info, X509CertInfo.EXTENSIONS, extensions);
        }

        // Sign the cert to identify the algorithm that's used.
        X509CertImpl cert = CERT_IMPL_CONSTRUCTOR.newInstance(info);
        CERT_IMPL_SIGN_METHOD.invoke(cert, caKeyPair.getPrivate(), algorithm.equalsIgnoreCase("EC") ? "SHA256withECDSA" : "SHA256withRSA");

        // Update the algorithm and sign again.
        CERT_INFO_SET_METHOD.invoke(info, CertificateAlgorithmId.NAME + ".algorithm",
                CERT_IMPL_GET_METHOD.invoke(cert, "x509.algorithm"));
        cert = CERT_IMPL_CONSTRUCTOR.newInstance(info);
        CERT_IMPL_SIGN_METHOD.invoke(cert, caKeyPair.getPrivate(),
                algorithm.equalsIgnoreCase("EC") ? "SHA256withECDSA" : "SHA256withRSA");

        cert.verify(caKeyPair.getPublic());

        return newSelfSignedCertificate(subjectFqdn, key.getPrivate(), cert);
    }

    static Object[] newSelfSignedCertificate(String fqdn, PrivateKey key, X509Certificate cert) throws IOException, CertificateEncodingException {
        // Encode the private key into a file.
        ByteBuf wrappedBuf = Unpooled.wrappedBuffer(key.getEncoded());
        ByteBuf encodedBuf;
        final String keyText;
        try {
            encodedBuf = Base64.encode(wrappedBuf, true);
            try {
                keyText = "-----BEGIN PRIVATE KEY-----\n" + encodedBuf.toString(CharsetUtil.US_ASCII) + "\n-----END PRIVATE KEY-----\n";
            } finally {
                encodedBuf.release();
            }
        } finally {
            wrappedBuf.release();
        }

        // Change all asterisk to 'x' for file name safety.
        fqdn = fqdn.replaceAll("[^\\w.-]", "x");

        File keyFile = PlatformDependent.createTempFile("keyutil_" + fqdn + '_', ".key", null);
        keyFile.deleteOnExit();

        OutputStream keyOut = new FileOutputStream(keyFile);
        try {
            keyOut.write(keyText.getBytes(CharsetUtil.US_ASCII));
            keyOut.close();
            keyOut = null;
        } finally {
            if (keyOut != null) {
                safeClose(keyFile, keyOut);
                safeDelete(keyFile);
            }
        }

        wrappedBuf = Unpooled.wrappedBuffer(cert.getEncoded());
        final String certText;
        try {
            encodedBuf = Base64.encode(wrappedBuf, true);
            try {
                // Encode the certificate into a CRT file.
                certText = "-----BEGIN CERTIFICATE-----\n" + encodedBuf.toString(CharsetUtil.US_ASCII) + "\n-----END CERTIFICATE-----\n";
            } finally {
                encodedBuf.release();
            }
        } finally {
            wrappedBuf.release();
        }

        File certFile = PlatformDependent.createTempFile("keyutil_" + fqdn + '_', ".crt", null);
        certFile.deleteOnExit();

        OutputStream certOut = new FileOutputStream(certFile);
        try {
            certOut.write(certText.getBytes(CharsetUtil.US_ASCII));
            certOut.close();
            certOut = null;
        } finally {
            if (certOut != null) {
                safeClose(certFile, certOut);
                safeDelete(certFile);
                safeDelete(keyFile);
            }
        }

        return new Object[]{certFile.getPath(), keyFile.getPath(), cert, key};
    }

    private static void safeDelete(File certFile) {
        if (!certFile.delete()) {
            if (logger.isWarnEnabled()) {
                logger.warn("Failed to delete a file: " + certFile);
            }
        }
    }

    private static void safeClose(File keyFile, OutputStream keyOut) {
        try {
            keyOut.close();
        } catch (IOException e) {
            if (logger.isWarnEnabled()) {
                logger.warn("Failed to close a file: " + keyFile, e);
            }
        }
    }

    private OpenJdkSelfSignedCertGenerator() {
    }
}
