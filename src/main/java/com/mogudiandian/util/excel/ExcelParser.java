package com.mogudiandian.util.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;

import java.io.InputStream;
import java.util.List;

/**
 * Excel解析器
 * @author Joshua Sun
 * @since 1.0.0
 * @deprecated 目前使用的easyexcel停止维护了，后续换成其他的工具
 */
@Deprecated
public final class ExcelParser<T> {

    /**
     * 数据要解析成的对象类型
     */
    private Class<T> clazz;

    public ExcelParser(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 解析
     * @param inputStream 输入流
     * @return 解析后的数据
     */
    public List<T> parse(InputStream inputStream) {
        return parse(inputStream, false);
    }

    /**
     * 解析
     * @param inputStream 输入流
     * @param noHead 是否没有表头
     * @return 解析后的数据
     */
    public List<T> parse(InputStream inputStream, boolean noHead) {
        return parse(inputStream, null, null, noHead);
    }

    /**
     * 解析
     * @param inputStream 输入流
     * @param sheetIndex 表（sheet）的索引 从0开始
     * @return 解析后的数据
     */
    public List<T> parse(InputStream inputStream, Integer sheetIndex) {
        return parse(inputStream, sheetIndex, false);
    }

    /**
     * 解析
     * @param inputStream 输入流
     * @param sheetName 表（sheet）的名称
     * @return 解析后的数据
     */
    public List<T> parse(InputStream inputStream, String sheetName) {
        return parse(inputStream, sheetName, false);
    }

    /**
     * 解析
     * @param inputStream 输入流
     * @param sheetIndex 表（sheet）的索引 从0开始
     * @param noHead 是否没有表头
     * @return 解析后的数据
     */
    public List<T> parse(InputStream inputStream, Integer sheetIndex, boolean noHead) {
        return parse(inputStream, null, sheetIndex, noHead);
    }

    /**
     * 解析
     * @param inputStream 输入流
     * @param sheetName 表（sheet）的名称
     * @param noHead 是否没有表头
     * @return 解析后的数据
     */
    public List<T> parse(InputStream inputStream, String sheetName, boolean noHead) {
        return parse(inputStream, sheetName, null, noHead);
    }

    /**
     * 解析
     * @param inputStream 输入流
     * @param sheetName 表（sheet）的名称 与表的索引二选一
     * @param sheetIndex 表（sheet）的索引 从0开始 与表的名称二选一
     * @param noHead 是否没有表头
     * @return 解析后的数据
     */
    public List<T> parse(InputStream inputStream, String sheetName, Integer sheetIndex, boolean noHead) {
        BufferedExcelListener<T> listener = new BufferedExcelListener<>();

        ExcelReaderBuilder builder = EasyExcel.read(inputStream, clazz, listener);

        if (sheetName != null) {
            builder.sheet(sheetName);
        }
        if (sheetIndex != null) {
            builder.sheet(sheetIndex);
        }

        if (noHead) {
            builder.headRowNumber(0);
        }

        builder.doReadAll();

        return listener.getData();
    }

}
