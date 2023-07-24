package com.mogudiandian.util.compressor;

import lombok.SneakyThrows;
import org.xerial.snappy.Snappy;

/**
 * google snappy压缩器
 * (C++) https://github.com/google/snappy/
 * (Java-JNI) https://github.com/xerial/snappy-java
 * (Java-Port) https://github.com/dain/snappy
 * @author Joshua Sun
 */
public final class SnappyCompressor implements Compressor {

    @SneakyThrows
    public byte[] compress(byte[] bytes) {
        return Snappy.compress(bytes);
    }

    @SneakyThrows
    public byte[] decompress(byte[] bytes) {
        return Snappy.uncompress(bytes);
    }

}
