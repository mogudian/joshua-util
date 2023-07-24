package com.mogudiandian.util.javac;

import lombok.SneakyThrows;

import java.net.URI;

/**
 * URI工具类
 * @author Joshua Sun
 */
final class URIUtils {

    private URIUtils() {
        super();
    }

    /**
     * 创建URI对象 避免抛异常
     * @param uri URI
     * @return URI对象
     */
    @SneakyThrows
    public static URI create(String uri) {
        return new URI(uri);
    }

}