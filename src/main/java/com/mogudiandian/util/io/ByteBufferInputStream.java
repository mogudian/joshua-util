package com.mogudiandian.util.io;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.function.Consumer;

/**
 * 包装ByteBuffer的InputStream
 * 注意：ByteBuffer的操作需要自行完成或通过带initFn的构造来完成
 *
 * @author sunbo
 */
public final class ByteBufferInputStream extends InputStream {

    private final ByteBuffer byteBuffer;

    public ByteBufferInputStream(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    /**
     * 可以通过这个构造对ByteBuffer进行初始化 比如 rewind 和 flip
     */
    public ByteBufferInputStream(ByteBuffer byteBuffer, Consumer<ByteBuffer> initFn) {
        this(byteBuffer);
        initFn.accept(this.byteBuffer);
    }

    @Override
    public int available() {
        return byteBuffer.remaining();
    }

    @Override
    public int read() {
        return byteBuffer.hasRemaining() ? (byteBuffer.get() & 0xFF) : -1;
    }

    @Override
    public int read(byte[] bytes, int off, int len) {
        if (!byteBuffer.hasRemaining()) {
            return -1;
        }
        len = Math.min(len, byteBuffer.remaining());
        byteBuffer.get(bytes, off, len);
        return len;
    }
}