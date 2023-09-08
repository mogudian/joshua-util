package com.mogudiandian.util.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 带缓冲的Excel解析监听器
 * @param <T> 数据类型
 * @author Joshua Sun
 * @since 1.0.0
 */
public class BufferedExcelListener<T> extends AnalysisEventListener<T> {

    /**
     * 缓冲区存放数据
     */
    private List<T> buffer = new ArrayList<>();

    @Override
    public void invoke(T t, AnalysisContext context) {
        buffer.add(t);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
    }

    /**
     * 获取所有数据
     * @return 所有数据
     */
    public List<T> getData() {
        return Optional.ofNullable(buffer)
                       .orElseGet(Collections::emptyList);
    }

    /**
     * 获取分批/分页数据
     * @param partitionSize 分批/分页大小
     * @return 每批数据集合
     */
    @Deprecated
    public List<List<T>> getPartitionData(int partitionSize) {
        return Lists.partition(getData(), partitionSize);
    }
}
