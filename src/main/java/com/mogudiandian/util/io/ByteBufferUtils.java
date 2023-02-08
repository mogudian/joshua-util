package com.mogudiandian.util.io;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * ByteBuffer的工具类
 *
 * @author sunbo
 */
public final class ByteBufferUtils {

    private ByteBufferUtils() {}

    /**
     * 用内存中的byte[]构造ByteBuffer
     * @param bytes 堆内存中的byte[]
     * @return 堆内存的ByteBuffer对象
     */
    public static ByteBuffer newByteBuffer(byte[] bytes) {
        int len = bytes.length;
        ByteBuffer byteBuffer = ByteBuffer.allocate(len);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        return byteBuffer;
    }

    /**
     * 将ByteBuffer转换为堆内存中的byte[]，兼容堆外内存的情况，也因为有堆外内存，需要慎重使用，避免OOM
     * @param byteBuffer ByteBuffer对象
     * @return 堆内存的byte[]
     */
    public static byte[] toByteArray(ByteBuffer byteBuffer) {
        if (byteBuffer.isDirect()) {
            byteBuffer.rewind();
            try (ByteBufferInputStream input = new ByteBufferInputStream(byteBuffer);
                 ByteArrayOutputStream output = new ByteArrayOutputStream()) {
                IOUtils.copy(input, output);
                return output.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return byteBuffer.array();
        }
    }

}
