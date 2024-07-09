package com.hyf.proxyserver.server.ssl;

import io.netty.util.Mapping;

import java.net.IDN;
import java.util.Locale;
import java.util.function.Function;

public class DynamicDomainWildcardMapping<V> implements Mapping<String, V> {

    private final Function<String, V> mapping;
    private final V defaultValue;

    public DynamicDomainWildcardMapping(Function<String, V> mapping, V defaultValue) {
        this.mapping = mapping;
        this.defaultValue = defaultValue;
    }

    @Override
    public V map(String hostname) {
        if (hostname != null) {
            hostname = normalize(hostname);

            // Let's try an exact match first
            V value = mapping.apply(hostname);
            if (value != null) {
                return value;
            }

            // No exact match, let's try a wildcard match.
            int idx = hostname.indexOf('.');
            if (idx != -1) {
                value = mapping.apply(hostname.substring(idx));
                if (value != null) {
                    return value;
                }
            }
        }

        return defaultValue;
    }

    private static String normalize(String hostname) {
        if (needsNormalization(hostname)) {
            hostname = IDN.toASCII(hostname, 1);
        }

        return hostname.toLowerCase(Locale.US);
    }

    private static boolean needsNormalization(String hostname) {
        int length = hostname.length();

        for (int i = 0; i < length; ++i) {
            int c = hostname.charAt(i);
            if (c > 127) {
                return true;
            }
        }

        return false;
    }
}
