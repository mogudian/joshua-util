package com.mogudiandian.util.io;

import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.function.Consumer;

/**
 * 包装ByteBuffer的OutputStream
 * 注意：ByteBuffer的操作需要自行完成或通过带initFn的构造来完成
 *
 * @author Joshua Sun
 * @since 1.0.0
 */
public final class ByteBufferOutputStream extends OutputStream {

    private final ByteBuffer byteBuffer;

    public ByteBufferOutputStream(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    /**
     * 可以通过这个构造对ByteBuffer进行初始化 比如 clear 和 compact
     */
    public ByteBufferOutputStream(ByteBuffer byteBuffer, Consumer<ByteBuffer> initFn) {
        this(byteBuffer);
        initFn.accept(this.byteBuffer);
    }

    @Override
    public void write(int b) {
        byteBuffer.put((byte) b);
    }

    @Override
    public void write(byte[] bytes, int off, int len) {
        byteBuffer.put(bytes, off, len);
    }
}