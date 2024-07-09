package com.hyf.ddos.config;

import com.hyf.ddos.constants.PathConstants;
import com.hyf.ddos.log.Log;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

/**
 * @author baB_hyf
 * @date 2021/09/20
 */
public class DDOSConfig {

    private static volatile Properties properties = null;

    public static Properties get() {
        if (properties == null) {
            synchronized (DDOSConfig.class) {
                if (properties == null) {
                    load();
                }
            }
        }
        return properties;
    }

    public static boolean persistence() {
        try {
            File file = new File(PathConstants.CONFIG);
            file.getParentFile().mkdirs();
            file.createNewFile();
            try (OutputStream os = new FileOutputStream(file)) {
                properties.store(os, "ddos relative config");
            }
            return true;
        } catch (IOException e) {
            Log.error("an IO error occur in persistence.", e);
        }

        return false;
    }

    public static void load() {
        Properties prop = new Properties();
        Path path = Paths.get(PathConstants.CONFIG);
        File configFile = path.toFile();
        if (configFile.exists()) {
            try (InputStream is = Files.newInputStream(path)) {
                prop.load(is);
            } catch (IOException e) {
                Log.error("an IO error occur in load.", e);
            }
        }

        properties = prop;
    }

    public static String get(String key) {
        Properties configMap = get();
        return configMap.getProperty(key);
    }

    public static String get(String key, Object defaultValue) {
        if (defaultValue == null) {
            throw new IllegalArgumentException();
        }

        Properties configMap = get();
        String val = configMap.getProperty(key);

        // 更新配置
        if (val == null) {
            val = defaultValue.toString();
            configMap.setProperty(key, val);
        }

        return val;
    }

    public static void put(String key, String value) {
        Properties properties = get();
        properties.setProperty(key, value);
    }

    public static void putAll(Map<String, String> params) {
        params.forEach(DDOSConfig::put);
    }
}
