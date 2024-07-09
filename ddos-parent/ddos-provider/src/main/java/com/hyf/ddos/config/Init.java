package com.hyf.ddos.config;

import com.hyf.ddos.concurrent.DDOSExecutorServiceFactory;
import com.hyf.ddos.concurrent.DDOSRunnable;
import com.hyf.ddos.concurrent.InstructionFactory;
import com.hyf.ddos.concurrent.DDOSControl;
import com.hyf.ddos.constants.ConfigConstants;
import com.hyf.ddos.log.Log;

/**
 * @author baB_hyf
 * @date 2021/09/21
 */
public class Init {

    public static void reload() {

        // thread

        DDOSControl.RUNNABLE_COUNT = Integer.valueOf(DDOSConfig.get(ConfigConstants.RUNNABLE_COUNT, ConfigConstants.RUNNABLE_COUNT_DEFAULT));
        DDOSControl.THREAD_SIZE = Integer.valueOf(DDOSConfig.get(ConfigConstants.THREAD_SIZE, ConfigConstants.THREAD_SIZE_DEFAULT));
        DDOSControl.stop();
        DDOSControl.EXECUTOR_SERVICE = DDOSExecutorServiceFactory.createExecutorService();

        // ddos runnable

        DDOSRunnable.FAILED_COUNT = Integer.valueOf(DDOSConfig.get(ConfigConstants.RUNNABLE_FAILED_COUNT, ConfigConstants.RUNNABLE_FAILED_COUNT_DEFAULT));
        DDOSRunnable.STOP = Boolean.valueOf(DDOSConfig.get(ConfigConstants.STOP, ConfigConstants.STOP_DEFAULT));

        // db

        InstructionFactory.TYPE = DDOSConfig.get(ConfigConstants.RUNNABLE_TYPE, ConfigConstants.RUNNABLE_TYPE_DEFAULT);
        InstructionFactory.DB_DRIVER = DDOSConfig.get(ConfigConstants.JDBC_DRIVER, ConfigConstants.JDBC_DRIVER_DEFAULT);
        InstructionFactory.DB_URL = DDOSConfig.get(ConfigConstants.JDBC_URL, ConfigConstants.JDBC_URL_DEFAULT);
        InstructionFactory.DB_USERNAME = DDOSConfig.get(ConfigConstants.JDBC_USERNAME, ConfigConstants.JDBC_USERNAME_DEFAULT);
        InstructionFactory.DB_PASSWORD = DDOSConfig.get(ConfigConstants.JDBC_PASSWORD, ConfigConstants.JDBC_PASSWORD_DEFAULT);
        InstructionFactory.DB_SQL = DDOSConfig.get(ConfigConstants.JDBC_SQL, ConfigConstants.JDBC_SQL_DEFAULT);
        InstructionFactory.DB_ONLY_GET_CONN = Boolean.valueOf(DDOSConfig.get(ConfigConstants.JDBC_ONLY_GET_CONNECTION, ConfigConstants.JDBC_ONLY_GET_CONNECTION_DEFAULT));
        try {
            Class.forName(InstructionFactory.DB_DRIVER);
        } catch (ClassNotFoundException e) {
            Log.error("failed to load jdbc driver class", e);
        }

        // http
        InstructionFactory.HTTP_URL = DDOSConfig.get(ConfigConstants.HTTP_URL, ConfigConstants.HTTP_URL_DEFAULT);

        // log

        Log.LEVEL = Integer.valueOf(DDOSConfig.get(ConfigConstants.LOG_LEVEL, ConfigConstants.LOG_LEVEL_DEFAULT));
    }
}
