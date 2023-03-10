package com.mogudiandian.util.random;

import com.mogudiandian.util.codec.Base58;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * UUID的工具类
 * @author sunbo
 */
public final class UuidUtils {

    private UuidUtils() {
        super();
    }

    /**
     * 生成UUID
     * @return UUID
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成Base58的UUID
     * @return 22位的UUID(base58)
     */
    public static String base58Uuid() {
        return encodeToBase58(UUID.randomUUID());
    }

    /**
     * 将UUID进行base58编码
     * @param uuid UUID
     * @return 编码后的UUID
     */
    public static String encodeToBase58(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return Base58.encode(bb.array());
    }

    /**
     * 将UUID进行base58编码
     * @param uuidString UUID字符串
     * @return 编码后的UUID
     */
    public static String encodeToBase58(String uuidString) {
        UUID uuid = UUID.fromString(uuidString);
        return encodeToBase58(uuid);
    }

    /**
     * 将base58的UUID解码
     * @param base58uuid base58编码过的UUID
     * @return UUID原文
     */
    public static UUID decodeBase58Uuid(String base58uuid) {
        byte[] byUuid = Base58.decode(base58uuid);
        ByteBuffer bb = ByteBuffer.wrap(byUuid);
        return new UUID(bb.getLong(), bb.getLong());
    }
}