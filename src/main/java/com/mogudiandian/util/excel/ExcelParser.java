package com.mogudiandian.util.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.google.common.collect.Lists;

import java.io.InputStream;
import java.util.List;

/**
 * Excel解析器
 * @author sunbo
 */
public final class ExcelParser<T> {

    /**
     * 输入流 与文件名二选一
     */
    private InputStream inputStream;

    /**
     * 文件名 与输入流二选一
     */
    private String fileName;

    /**
     * 表（sheet）的名称 与表的索引二选一
     */
    private String sheetName;

    /**
     * 表（sheet）的索引 从0开始 与表的名称二选一
     */
    private Integer sheetIndex;

    /**
     * 数据要解析成的对象类型
     */
    private Class<T> clazz;

    public ExcelParser(String fileName, Class<T> clazz) {
        this(null, fileName, null, null, clazz);
    }

    public ExcelParser(String fileName, String sheetName, Class<T> clazz) {
        this(null, fileName, sheetName, null, clazz);
    }

    public ExcelParser(String fileName, Integer sheetIndex, Class<T> clazz) {
        this(null, fileName, null, sheetIndex, clazz);
    }

    public ExcelParser(InputStream inputStream, Class<T> clazz) {
        this(inputStream, null, null, null, clazz);
    }

    public ExcelParser(InputStream inputStream, String sheetName, Class<T> clazz) {
        this(inputStream, null, sheetName, null, clazz);
    }

    public ExcelParser(InputStream inputStream, Integer sheetIndex, Class<T> clazz) {
        this(inputStream, null, null, sheetIndex, clazz);
    }

    public ExcelParser(InputStream inputStream, String fileName, String sheetName, Integer sheetIndex, Class<T> clazz) {
        this.inputStream = inputStream;
        this.fileName = fileName;
        this.sheetName = sheetName;
        this.sheetIndex = sheetIndex;
        this.clazz = clazz;
    }

    /**
     * 解析
     * @see ExcelParser#parse(boolean)
     * @return 解析后的数据
     */
    public List<T> parse() {
        return parse(false);
    }

    /**
     * 解析
     * @param noHead 是否没有表头
     * @return 解析后的数据
     */
    public List<T> parse(boolean noHead) {
        BufferedExcelListener<T> listener = new BufferedExcelListener<>();

        ExcelReaderBuilder builder;

        if (inputStream != null) {
            builder = EasyExcel.read(inputStream, clazz, listener);
        } else if (fileName != null) {
            builder = EasyExcel.read(fileName, clazz, listener);
        } else {
            throw new RuntimeException("inputStream or fileName must be specified");
        }

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

    /**
     * 解析并分批
     * @see ExcelParser#parseAndPartition(int, boolean)
     * @param partitionSize 分批大小
     * @return 解析后的分批数据
     */
    public List<List<T>> parseAndPartition(int partitionSize) {
        return parseAndPartition(partitionSize, false);
    }

    /**
     * 解析并分批
     * @param partitionSize 分批大小
     * @param noHead 是否没有表头
     * @return 解析后的分批数据
     */
    public List<List<T>> parseAndPartition(int partitionSize, boolean noHead) {
        return Lists.partition(parse(noHead), partitionSize);
    }

}
