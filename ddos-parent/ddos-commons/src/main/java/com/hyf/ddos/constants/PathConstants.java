package com.hyf.ddos.constants;

import java.io.File;

/**
 * @author baB_hyf
 * @date 2021/09/21
 */
public class PathConstants {

    public static final String HOME = System.getProperty("user.home") + File.separator + ".ddos";

    public static final String LOG = HOME + File.separator + "log" + File.separator + "ddos.log";

    public static final String CONFIG = HOME + File.separator + "config" + File.separator + "ddos.properties";

    public static final String URL_CONFIG = "/ddos/config";

    public static final String URL_INVOKE = "/ddos/invoke";
}
