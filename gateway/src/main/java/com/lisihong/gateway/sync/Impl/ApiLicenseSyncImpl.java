package com.lisihong.gateway.sync.Impl;

import com.lisihong.api.TableSync;
import com.lisihong.gateway.sync.cache.LocalCache;
import org.springframework.stereotype.Component;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

@Component(value = "lisihong.cloud_api_license")
public record ApiLicenseSyncImpl()
        implements TableSync {
    @Override
    public void insert(List<RowData> rowDataList) {
        String apath = null;
        Integer lid = null;
        Integer gid = null;
        for (RowData rowData : rowDataList) {
            List<Column> columnList = rowData.getAfterColumnsList();
            for (Column column : columnList) {
                switch (column.getName()) {
                    case "apath" -> apath = column.getValue();
                    case "lid" -> lid = Integer.parseInt(column.getValue());
                    case "gid" -> gid = Integer.parseInt(column.getValue());
                }
            }
            LocalCache.apiLicenseMap.computeIfAbsent(apath, key -> new HashMap<>())
                    .computeIfAbsent(gid, key -> new HashSet<>()).add(lid);
        }
    }

    @Override
    public void delete(List<RowData> rowDataList) {
        String apath = null;
        Integer lid = null;
        Integer gid = null;
        for (RowData rowData : rowDataList) {
            List<Column> columnList = rowData.getBeforeColumnsList();
            for (Column column : columnList) {
                switch (column.getName()) {
                    case "apath" -> apath = column.getValue();
                    case "lid" -> lid = Integer.parseInt(column.getValue());
                    case "gid" -> gid = Integer.parseInt(column.getValue());
                }
            }
            Map<Integer, Set<Integer>> groupList = LocalCache.apiLicenseMap.get(apath);
            Set<Integer> licenseList = groupList.get(gid);
            licenseList.remove(lid);
            if (licenseList.isEmpty()) {
                groupList.remove(gid);
            }
            if (groupList.isEmpty()) {
                LocalCache.apiLicenseMap.remove(apath);
            }
        }
    }

    @Override
    public void update(List<RowData> rowDataList) {
        delete(rowDataList);
        insert(rowDataList);
    }
}
