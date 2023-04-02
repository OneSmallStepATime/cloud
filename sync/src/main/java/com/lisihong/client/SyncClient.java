package com.lisihong.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import com.lisihong.api.TableSync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@EnableScheduling
@Component
public record SyncClient(CanalConnector canalConnector,
                         Map<String, TableSync> dataSyncMap) {
    @Scheduled(cron = "0/1 * * * * ?")
    private void sync() {
        Message message = canalConnector.getWithoutAck(1000);
        long batchId = message.getId();
        int size = message.getEntries().size();

        if (batchId == -1 || size == 0) {
            return;
        }
        syncEntries(message.getEntries());
        canalConnector.ack(batchId);
        //
    }

    private void syncEntries(List<Entry> entries) {
        for (Entry entry : entries) {
            EntryType entryType = entry.getEntryType();
            if (entryType == EntryType.TRANSACTIONBEGIN
                    || entryType == EntryType.TRANSACTIONEND) {
                continue;
            }
            RowChange rowChange;
            try {
                rowChange = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eroManga-event " +
                        "has an error , data:" + entry, e);
            }
            EventType eventType = rowChange.getEventType();
            List<RowData> rowDataList = rowChange.getRowDatasList();
            TableSync tableSync = dataSyncMap.get(entry.getHeader().getSchemaName()
                    + "." + entry.getHeader().getTableName());
            switch (eventType) {
                case INSERT -> tableSync.insert(rowDataList);
                case DELETE -> tableSync.delete(rowDataList);
                case UPDATE -> tableSync.update(rowDataList);
            }
        }
    }
}