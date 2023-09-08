package com.mogudiandian.util.codec;

import lombok.SneakyThrows;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 计算MD5
 *
 * @author Joshua Sun
 * @since 1.0.0
 */
public final class MD5 {

    private MD5() {
        super();
    }

    /**
     * 生成摘要摘要
     * @param message 原文
     * @return MD5小写
     */
    @SneakyThrows
    public static String digest(String message) {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytes =  md.digest(message.getBytes(StandardCharsets.UTF_8));
        String hexString = new BigInteger(1, bytes).toString(16);

        StringBuilder builder = new StringBuilder(32);
        for (int i = hexString.length(); i < 32; i++) {
            builder.append(0);
        }
        builder.append(hexString);
        return builder.toString();
    }

    /**
     * MD5另一种实现 网上找的 当是学习了
     * @param message 原文
     * @return MD5小写
     */
    @SneakyThrows
    private static String digestAnotherImpl(String message) {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(message.getBytes(StandardCharsets.UTF_8));
        byte[] bytes = md.digest();

        StringBuilder builder = new StringBuilder(32);

        for (int i = 0, len = bytes.length; i < len; i++) {
            int b = bytes[i];
            if (b < 0) {
                b += 256;
            }
            if (b < 16) {
                builder.append(0);
            }
            builder.append(Integer.toHexString(b));
        }
        return builder.toString();
    }

}
