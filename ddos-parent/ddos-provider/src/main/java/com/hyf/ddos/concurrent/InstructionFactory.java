package com.hyf.ddos.concurrent;

import com.hyf.ddos.log.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author baB_hyf
 * @date 2021/09/21
 */
public class InstructionFactory {

    public static volatile String TYPE = null;

    // db
    public static volatile String  DB_DRIVER        = null;
    public static volatile String  DB_URL           = null;
    public static volatile String  DB_USERNAME      = null;
    public static volatile String  DB_PASSWORD      = null;
    public static volatile String  DB_SQL           = null;
    public static volatile Boolean DB_ONLY_GET_CONN = null;

    // http
    public static volatile String HTTP_URL    = null;
    public static volatile String HTTP_METHOD = null;
    public static volatile String HTTP_HEADER = null;
    public static volatile String HTTP_BODY   = null;


    public static Runnable getInstruction() {

        switch (TYPE) {
            case "db":
                return runJdbc();
            case "http":
                return runHttp();
        }

        throw new IllegalArgumentException("unknown runnable type");
    }

    private static Runnable runJdbc() {
        return () -> {
            try {
                try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                    if (!DB_ONLY_GET_CONN) {
                        try (Statement statement = connection.createStatement()) {
                            while (true && !DDOSRunnable.STOP) {
                                statement.executeQuery(DB_SQL);
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                Log.debug("failed to execute sql");
                throw new RuntimeException(e);
            }
        };
    }

    // TODO
    private static Runnable runHttp() {
        return () -> {
            if (true) {
                throw new UnsupportedOperationException("http");
            }

            try {
                URL url = new URL(HTTP_URL);
                URLConnection conn = url.openConnection();
                try (BufferedInputStream bis = new BufferedInputStream(conn.getInputStream())) {
                    byte[] bytes = new byte[1024];
                    int len = -1;
                    StringBuilder sb = new StringBuilder();
                    if ((len = bis.read()) != -1) {
                        sb.append(new String(bytes, 0, len));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
}
