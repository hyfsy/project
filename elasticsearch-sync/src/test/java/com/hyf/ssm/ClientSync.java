package com.hyf.ssm;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;

import java.net.InetSocketAddress;
import java.util.List;

import static com.alibaba.otter.canal.protocol.CanalEntry.EntryType.ROWDATA;

/**
 * @author baB_hyf
 * @date 2022/04/01
 */
public class ClientSync {

    public static void main(String[] args) {

        // 连接到canal服务端
        CanalConnector canalConnector = CanalConnectors.newSingleConnector(
                new InetSocketAddress(AddressUtils.getHostIp(), 11111), // canal ip and port
                "example", // canal conf directory name
                "", // canal instance username
                "" // canal instance password
        );

        try {
            canalConnector.connect();
            // 匹配信息，表名、字段等
            canalConnector.subscribe(".*\\..*");
            canalConnector.rollback(); // waitClientRunning & rollback to 0

            int batchSize = 100;
            int emptyCount = 0;
            int totalEmptyCount = 120;

            while (emptyCount < totalEmptyCount) {
                Message message = canalConnector.getWithoutAck(batchSize); // 手动提交
                // Message message = canalConnector.get(batchSize); // 自动提交
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    emptyCount++;
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                    }
                }
                else {
                    emptyCount = 0;
                    processEntities(message.getEntries());
                }
                canalConnector.ack(batchId); // 提交确认
                // canalConnector.rollback(batchId); // 回滚
            }

            System.out.println("empty too many times, exit");
        } finally {
            canalConnector.disconnect();
        }

    }

    public static void processEntities(List<CanalEntry.Entry> entries) {

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
            System.out.printf("================&gt; binlog[%s:%s] , name[%s,%s] , eventType : %s%n",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType);

            List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
            for (CanalEntry.RowData rowData : rowDatasList) {
                if (eventType == CanalEntry.EventType.DELETE) {
                    printColumn(rowData.getBeforeColumnsList());
                }
                else if (eventType == CanalEntry.EventType.INSERT) {
                    printColumn(rowData.getAfterColumnsList());
                }
                else if (eventType == CanalEntry.EventType.UPDATE) {
                    System.out.println("-------&gt; before");
                    printColumn(rowData.getBeforeColumnsList());
                    System.out.println("-------&gt; after");
                    printColumn(rowData.getAfterColumnsList());
                }
                else if (eventType == CanalEntry.EventType.QUERY) {
                    System.out.println("-------&gt; before");
                    printColumn(rowData.getBeforeColumnsList());
                    System.out.println("-------&gt; after");
                    printColumn(rowData.getAfterColumnsList());
                }
            }
        }
    }

    private static void printColumn(List<CanalEntry.Column> columns) {
        for (CanalEntry.Column column : columns) {
            System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }
}
