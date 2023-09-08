package com.mogudiandian.util.javac;

import javax.tools.SimpleJavaFileObject;

/**
 * 动态字符串对象
 * 在 Java™ 编程语言源和类文件上进行操作的工具的文件抽象
 * @since 1.0.0
 */
class DynamicStringObject extends SimpleJavaFileObject {

    private final String sourceCode;

    /**
     * 构造
     * @param filePath 文件全路径
     * @param sourceCode 源码
     */
    DynamicStringObject(String filePath, String sourceCode) {
        super(URIUtils.create(filePath), Kind.SOURCE);
        this.sourceCode = sourceCode;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return sourceCode;
    }
}
