package com.mogudiandian.util.compressor;

import java.nio.charset.StandardCharsets;

/**
 * 压缩器
 * @author sunbo
 */
public interface Compressor {

    /**
     * 压缩
     * @param bytes 压缩前
     * @return 压缩后
     */
    byte[] compress(byte[] bytes);

    /**
     * 解压
     * @param bytes 解压前
     * @return 解压后
     */
    byte[] decompress(byte[] bytes);

    /**
     * 解压 decompress的别名
     * @param bytes 解压前
     * @return 解压后
     */
    default byte[] uncompress(byte[] bytes) {
        return decompress(bytes);
    }

    /**
     * 压缩字符串
     * @param source 字符串
     * @return 压缩后
     */
    default byte[] compressString(String source) {
        return compress(source.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 解压字符串
     * @param bytes 解压前
     * @return 解压后
     */
    default String decompressString(byte[] bytes) {
        return new String(decompress(bytes), StandardCharsets.UTF_8);
    }

    /**
     * 解压字符串 decompressString的别名
     * @param bytes 解压前
     * @return 解压后
     */
    default String uncompressString(byte[] bytes) {
        return decompressString(bytes);
    }

}
