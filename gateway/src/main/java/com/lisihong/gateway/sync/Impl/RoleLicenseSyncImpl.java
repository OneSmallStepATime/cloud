package com.lisihong.gateway.sync.Impl;
import com.lisihong.api.TableSync;
import com.lisihong.gateway.sync.cache.LocalCache;
import org.springframework.stereotype.Component;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component(value = "lisihong.cloud_role_license")
public record RoleLicenseSyncImpl() implements TableSync {
    @Override
    public void insert(List<RowData> rowDataList) {
        Integer rid = null;
        Integer lid = null;
        for (RowData rowData : rowDataList) {
            List<Column> columnList = rowData.getAfterColumnsList();
            for (Column column : columnList) {
                String columnName = column.getName();
                switch (columnName) {
                    case "rid" -> rid = Integer.parseInt(column.getValue());
                    case "lid" -> lid = Integer.parseInt(column.getValue());
                }
            }
            LocalCache.roleLicenseMap
                    .computeIfAbsent(rid, key -> new HashSet<>()).add(lid);
        }
    }

    @Override
    public void delete(List<RowData> rowDataList) {
        Integer rid = null;
        Integer lid = null;
        for (RowData rowData : rowDataList) {
            List<Column> columnList = rowData.getBeforeColumnsList();
            for (Column column : columnList) {
                String columnName = column.getName();
                switch (columnName) {
                    case "rid" -> rid = Integer.parseInt(column.getValue());
                    case "lid" -> lid = Integer.parseInt(column.getValue());
                }
            }
            Set<Integer> licenseList = LocalCache.roleLicenseMap.get(rid);
            licenseList.remove(lid);
            if (licenseList.isEmpty()) {
                LocalCache.roleLicenseMap.remove(rid);
            }
        }
    }

    @Override
    public void update(List<RowData> rowDataList) {
        delete(rowDataList);
        insert(rowDataList);
    }
}
