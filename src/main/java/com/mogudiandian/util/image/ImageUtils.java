package com.mogudiandian.util.image;

import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * 图片工具类
 *
 * @author Joshua Sun
 * @since 1.0.0
 */
public final class ImageUtils {

    private ImageUtils() {}

    /**
     * 调整模式
     */
    public enum ResizeMode {
        /**
         * 固定宽
         */
        FIX_WIDTH,

        /**
         * 固定高
         */
        FIX_HEIGHT,

        /**
         * 自动
         */
        AUTO;
    }

    /**
     * 缩小图片
     * @param src 原图片
     * @param maxSize 最大尺寸
     * @param resizeMode 调整模式
     * @param forceZoom 是否强制缩放（true表示本来没有那么大 强制撑到那么大）
     * @return 缩小后的图片
     */
    public static BufferedImage zoomOut(BufferedImage src, int maxSize, ResizeMode resizeMode, boolean forceZoom) {
        int width = src.getWidth(), height = src.getHeight(), newWidth = 0, newHeight = 0;

        // 是否需要调整 当图片尺寸不足并且不需要强制缩放时 不需要调整 直接返回原图
        boolean resize = false;

        // 如果是自动的 横图（宽>高）时固定宽 竖图（高>宽）时固定高
        if (resizeMode == ResizeMode.AUTO) {
            if (width >= height) {
                resizeMode = ResizeMode.FIX_WIDTH;
            } else {
                resizeMode = ResizeMode.FIX_HEIGHT;
            }
        }

        if (resizeMode == ResizeMode.FIX_WIDTH) {
            // 固定宽时 新高 / 新宽 = 原高 / 原宽  =>  新高 = 新宽 * 原高 / 原宽
            if (width > maxSize || forceZoom) {
                newWidth = maxSize;
                newHeight = Math.toIntExact((long) maxSize * height / width);
                resize = true;
            }
        } else if (resizeMode == ResizeMode.FIX_HEIGHT) {
            // 固定高时 新宽 / 新高 = 原宽 / 原高  =>  新宽 = 新高 * 原宽 / 原高
            if (height > maxSize || forceZoom) {
                newHeight = maxSize;
                newWidth = Math.toIntExact((long) maxSize * width / height);
                resize = true;
            }
        }

        // 不需要调整时直接返回原图
        if (!resize) {
            return src;
        }

        // 调整为新的宽高
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        newImage.getGraphics().drawImage(src.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT), 0, 0, null);
        return newImage;
    }

    /**
     * 缩小图片
     * @see ImageUtils#zoomOut(BufferedImage, int, ResizeMode, boolean)
     *
     * @param in 输入流
     * @param maxSize 最大尺寸
     * @param resizeMode 调整模式
     * @param forceZoom 是否强制缩放（true表示本来没有那么大 强制撑到那么大）
     * @return 缩小后的图片
     */
    @SneakyThrows
    public static byte[] zoomOut(InputStream in, int maxSize, ResizeMode resizeMode, boolean forceZoom) {
        BufferedImage dist = zoomOut(ImageIO.read(in), maxSize, resizeMode, forceZoom);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(dist, "jpg", out);
            return out.toByteArray();
        }
    }

    /**
     * 缩小图片
     * @see ImageUtils#zoomOut(BufferedImage, int, ResizeMode, boolean)
     *
     * @param bytes 原图片
     * @param maxSize 最大尺寸
     * @param resizeMode 调整模式
     * @param forceZoom 是否强制缩放（true表示本来没有那么大 强制撑到那么大）
     * @return 缩小后的图片
     */
    @SneakyThrows
    public static byte[] zoomOut(byte[] bytes, int maxSize, ResizeMode resizeMode, boolean forceZoom) {
        try (ByteArrayInputStream in = new ByteArrayInputStream(bytes)) {
            return zoomOut(in, maxSize, resizeMode, forceZoom);
        }
    }

}
