package com.mogudiandian.util.javac;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * 动态字节数组对象
 * 在 Java™ 编程语言源和类文件上进行操作的工具的文件抽象
 * @author Joshua Sun
 * @since 1.0.0
 */
class DynamicByteArrayObject extends SimpleJavaFileObject {

    /**
     * 临时保存字节数组用的
     */
    private final ByteArrayOutputStream outputStream;

    /**
     * 构造
     * @param filePath 文件全路径
     * @param kind 类型
     */
    public DynamicByteArrayObject(String filePath, Kind kind) {
        super(URIUtils.create(filePath), kind);
        outputStream = new ByteArrayOutputStream();
    }

    @Override
    public OutputStream openOutputStream() {
        return outputStream;
    }

    public byte[] getBytes() {
        return outputStream.toByteArray();
    }
}
