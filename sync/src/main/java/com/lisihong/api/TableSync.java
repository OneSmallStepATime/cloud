package com.lisihong.api;

import com.alibaba.otter.canal.protocol.CanalEntry.RowData;

import java.util.List;

public interface TableSync {
    void insert(List<RowData> rowDataList);

    void update(List<RowData> rowDataList);

    void delete(List<RowData> rowDataList);
}
