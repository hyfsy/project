package com.hyf.ssm.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static com.alibaba.otter.canal.protocol.CanalEntry.EntryType.ROWDATA;

/**
 * @author baB_hyf
 * @date 2022/04/01
 */
@Component
public class CanalSubscriber {

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("canal-subscribe");
            thread.setDaemon(false);
            return thread;
        }
    });

    private int batchSize = 100;

    @Autowired(required = false)
    private List<CanalDataHandler> canalDataHandlers = new ArrayList<>();

    @Resource
    private CanalConnector canalConnector;

    @PostConstruct
    private void subscribe() {
        executorService.scheduleWithFixedDelay(() -> {
            Message message = canalConnector.getWithoutAck(batchSize);
            long batchId = message.getId();
            try {

                int size = message.isRaw() ? message.getRawEntries().size() : message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    return;
                }

                // if (!message.isRaw()) {
                //     System.out.println("only support raw data");
                //     return;
                // }

                List<CanalEntry.Entry> entries = message.getEntries();
                for (CanalEntry.Entry entry : entries) {
                    if (!(entry.getEntryType() == ROWDATA)) {
                        continue;
                    }

                    CanalEntry.RowChange rowChange;
                    try {
                        rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                    } catch (InvalidProtocolBufferException e) {
                        throw new RuntimeException("parse row change data error" + entry.toString());
                    }

                    CanalEntry.EventType eventType = rowChange.getEventType();

                    if (eventType == CanalEntry.EventType.QUERY || rowChange.getIsDdl()) {
                        System.out.println("sql: " + rowChange.getSql());
                    }

                    List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();

                    for (CanalEntry.RowData rowData : rowDatasList) {


                        Map<String, String> beforeRowData = new HashMap<>();
                        Map<String, String> afterRowData = new HashMap<>();

                        switch (eventType) {
                            case INSERT:
                                fill(afterRowData, rowData.getAfterColumnsList());
                                break;
                            case UPDATE:
                                fill(beforeRowData, rowData.getBeforeColumnsList());
                                fill(afterRowData, rowData.getAfterColumnsList());
                                break;
                            case DELETE:
                                fill(beforeRowData, rowData.getBeforeColumnsList());
                                break;
                            case QUERY:
                                fill(beforeRowData, rowData.getBeforeColumnsList());
                                fill(afterRowData, rowData.getAfterColumnsList());
                                break;
                            default:
                                System.out.println("not allow: " + eventType);
                                break;
                        }

                        for (CanalDataHandler canalDataHandler : canalDataHandlers) {
                            boolean interest = canalDataHandler.interest(entry.getHeader().getSchemaName(), entry.getHeader().getTableName(), entry.getHeader().getEventType().toString());
                            if (interest) {
                                canalDataHandler.process(beforeRowData, afterRowData, eventType.name());
                            }
                        }
                    }
                }
            } catch (RuntimeException e) {
                canalConnector.rollback(batchId);
                e.printStackTrace();
            } finally {
                canalConnector.ack(batchId);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void fill(Map<String, String> map, List<CanalEntry.Column> columns) {
        for (CanalEntry.Column column : columns) {
            map.put(column.getName(), column.getValue());
        }
    }

    public void register(CanalDataHandler canalDataHandler) {
        canalDataHandlers.add(canalDataHandler);
    }

    public void unregister(CanalDataHandler canalDataHandler) {
        canalDataHandlers.remove(canalDataHandler);
    }
}
