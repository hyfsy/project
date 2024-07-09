package com.hyf.ssm;

import com.alibaba.otter.canal.client.rocketmq.RocketMQCanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.exception.CanalClientException;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.alibaba.otter.canal.protocol.CanalEntry.EntryType.ROWDATA;

/**
 * @author baB_hyf
 * @date 2022/04/02
 */
public class RocketMQSync {

    public static String  nameServers        = "127.0.0.1:9876";
    public static String  topic              = "learn_hello"; // 库_表 的topic
    public static String  groupId            = "group";
    public static String  accessKey          = "";
    public static String  secretKey          = "";
    public static boolean enableMessageTrace = false;
    public static String  accessChannel      = "local";
    public static String  namespace          = "";

    public static void main(String[] args) {
        RocketMQCanalConnector connector = new RocketMQCanalConnector(
                nameServers, topic, groupId, accessKey, secretKey, -1, false,
                enableMessageTrace, "", accessChannel, namespace);

        try {
            connector.connect();
            connector.subscribe(".*\\..*");
            connector.rollback();

            while (true) {
                try {
                    List<Message> messageList = connector.getListWithoutAck(1000L, TimeUnit.MILLISECONDS);
                    for (Message message : messageList) {
                        long messageId = message.getId();
                        List<CanalEntry.Entry> entries = message.getEntries();
                        if (messageId == -1 || entries.isEmpty()) {
                            try {
                                Thread.sleep(1000L);
                            } catch (InterruptedException ignored) {
                            }
                            continue;
                        }

                        processEntities(entries);
                    }

                    connector.ack();
                } catch (CanalClientException e) {
                    connector.rollback();
                }
            }


        } finally {
            connector.disconnect();
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
