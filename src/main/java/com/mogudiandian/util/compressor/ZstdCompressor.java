package com.mogudiandian.util.compressor;

import com.github.luben.zstd.Zstd;

/**
 * facebook z-standard压缩器
 * (C++) https://facebook.github.io/zstd/
 * (Java-JNI) https://github.com/luben/zstd-jni
 * (Java-Port) https://github.com/airlift/aircompressor/tree/master/src/main/java/io/airlift/compress/zstd
 * @author sunbo
 */
public final class ZstdCompressor implements Compressor {

    public byte[] compress(byte[] bytes) {
        return Zstd.compress(bytes);
    }

    public byte[] decompress(byte[] bytes) {
        return Zstd.decompress(bytes, Math.toIntExact(Zstd.decompressedSize(bytes)));
    }

}
