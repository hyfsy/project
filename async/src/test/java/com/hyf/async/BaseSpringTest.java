package com.hyf.async;

import com.hyf.async.alarm.FeishuAlarm;
import com.hyf.async.constant.AsyncLogConstants;
import com.hyf.async.core.AsyncTask;
import com.hyf.async.entity.AsyncLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@SpringBootTest(classes = AsyncApplication.class)
@RunWith(SpringRunner.class)
public class BaseSpringTest {

    @Autowired
    private FeishuAlarm alarm;

    @Test
    public void alarmTest() {
        AsyncLog asyncLog = new AsyncLog();
        asyncLog.setId(1L);
        asyncLog.setFailCause("failCause");
        asyncLog.setName("测试异步任务");
        asyncLog.setPayload("测试payload");
        asyncLog.setUpdateTime(LocalDateTime.now());
        asyncLog.setStatus(AsyncLogConstants.STATUS_SUCCEED);
        AsyncTask task = new AsyncTask(asyncLog);
        alarm.alarm(task);
    }
}
