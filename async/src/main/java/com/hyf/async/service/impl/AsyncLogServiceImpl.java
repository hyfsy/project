package com.hyf.async.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyf.async.constant.AsyncLogConstants;
import com.hyf.async.entity.AsyncLog;
import com.hyf.async.mapper.AsyncLogMapper;
import com.hyf.async.service.IAsyncLogService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Service
public class AsyncLogServiceImpl extends ServiceImpl<AsyncLogMapper, AsyncLog> implements IAsyncLogService {

    @Override
    public void addLog(AsyncLog asyncLog) {
        asyncLog.setRetryCount(0);
        asyncLog.setStatus(AsyncLogConstants.STATUS_NOT_START);
        asyncLog.setCreateTime(LocalDateTime.now());
        asyncLog.setVersion(0);
        asyncLog.setSha1(getSHA1(asyncLog.getPayload()));
        save(asyncLog);
    }

    @Override
    public boolean updateLog(AsyncLog asyncLog) {
        asyncLog.setUpdateTime(LocalDateTime.now());
        return updateById(asyncLog);
    }

    private String getSHA1(String str) {
        return DigestUtils.sha1Hex(str);
    }
}
