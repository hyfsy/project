package com.hyf.ddos.log;

import com.hyf.ddos.constants.ConfigConstants;
import com.hyf.ddos.constants.PathConstants;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author baB_hyf
 * @date 2021/09/21
 */
public class Log {

    public static final    File    LOG_FILE = createNewFile(PathConstants.LOG);
    public static volatile Integer LEVEL    = ConfigConstants.LOG_LEVEL_DEFAULT;

    public static File createNewFile(String filePath) {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
            return file;
        } catch (IOException e) {
            throw new IllegalStateException("failed to create log file.");
        }
    }

    public static void error(String msg, Throwable t) {
        error(msg);

        try (PrintWriter ps = new PrintWriter(new BufferedWriter(new FileWriter(LOG_FILE, true)))) {
            t.printStackTrace(ps);
        } catch (IOException e) {
            System.out.println("failed to write exception log.");
            e.printStackTrace();
        }
    }

    public static void error(String msg, Object... args) {
        log(1, msg, args);
    }

    public static void warn(String msg, Object... args) {
        log(2, msg, args);
    }

    public static void info(String msg, Object... args) {
        log(3, msg, args);
    }

    public static void debug(String msg, Object... args) {
        log(4, msg, args);
    }

    public static void trace(String msg, Object... args) {
        log(5, msg, args);
    }

    private static void log(int type, String msg, Object... args) {
        if (LEVEL < type) {
            return;
        }

        if (args != null) {
            for (Object arg : args) {
                msg = msg.replaceFirst("\\{}", arg.toString());
            }
        }
        String prefix = "";
        switch (type) {
            case 1:
                prefix = "[ERROR]";
                break;
            case 2:
                prefix = "[WARN]";
                break;
            case 3:
                prefix = "[INFO]";
                break;
            case 4:
                prefix = "[DEBUG]";
                break;
            case 5:
                prefix = "[TRACE]";
                break;
            default:
                prefix = "[xxx]";
                break;
        }

        prefix += new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        append(prefix + " " + msg);
    }

    private static void append(String content) {
        try (Writer writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(content + "\r\n");
        } catch (IOException e) {
            System.out.println("write log failed");
            e.printStackTrace();
        }
    }
}
