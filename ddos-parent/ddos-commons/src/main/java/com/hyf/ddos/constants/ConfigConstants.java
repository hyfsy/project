package com.hyf.ddos.constants;

/**
 * @author baB_hyf
 * @date 2021/09/21
 */
public class ConfigConstants {

    public static final String THREAD_SIZE = "thread_size";

    public static final int THREAD_SIZE_DEFAULT = 1000;

    public static final String AWAIT_TERMINATION = "await_termination";

    public static final int AWAIT_TERMINATION_DEFAULT = 5000; // ms

    public static final String RUNNABLE_COUNT = "runnable_count";

    public static final int RUNNABLE_COUNT_DEFAULT = 10000;

    public static final String RUNNABLE_FAILED_COUNT = "runnable_failed_count";

    public static final int RUNNABLE_FAILED_COUNT_DEFAULT = 1000;

    public static final String STOP = "stop";

    public static final boolean STOP_DEFAULT = false;

    public static final String RUNNABLE_TYPE = "runnable_type";

    public static final String RUNNABLE_TYPE_DEFAULT = "mysql";

    public static final String JDBC_SQL = "sql";

    public static final String JDBC_SQL_DEFAULT = "select 1";

    public static final String JDBC_DRIVER = "jdbc_driver";

    public static final String JDBC_DRIVER_DEFAULT = "com.mysql.cj.jdbc.Driver";

    public static final String JDBC_URL = "jdbc_url";

    public static final String JDBC_URL_DEFAULT = "jdbc:mysql://localhost:3306/learn?serverTimezone=UTC&useSSL=true&useUnicode=true&characterEncoding=UTF-8&nullNamePatternMatchesAll=true&allowMultiQueries=true";

    public static final String JDBC_USERNAME = "jdbc_username";

    public static final String JDBC_USERNAME_DEFAULT = "root";

    public static final String JDBC_PASSWORD = "jdbc_password";

    public static final String JDBC_PASSWORD_DEFAULT = "hyflearn";

    public static final String JDBC_ONLY_GET_CONNECTION = "jdbc_only_get_connection";

    public static final boolean JDBC_ONLY_GET_CONNECTION_DEFAULT = false;

    public static final String LOG_LEVEL = "log_level";

    public static final int LOG_LEVEL_DEFAULT = 3;

    public static final String HTTP_URL = "http_url";

    public static final String HTTP_URL_DEFAULT = "http://www.baidu.com";


}
