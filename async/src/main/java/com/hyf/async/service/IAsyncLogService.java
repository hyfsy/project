package com.hyf.async.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hyf.async.entity.AsyncLog;

/**
 * @author Administrator
 */
public interface IAsyncLogService extends IService<AsyncLog> {

    void addLog(AsyncLog asyncLog);

    boolean updateLog(AsyncLog asyncLog);
}
