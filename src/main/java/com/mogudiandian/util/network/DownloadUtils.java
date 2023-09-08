package com.mogudiandian.util.network;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件下载工具
 * 使用方法：
 * <pre>
 * Map.Entry<String, ByteBuffer> download = DownloadUtils.download(url, 超时时间, 是否优先使用原始文件名);
 * String fileName = download.getKey() // 原始文件名
 * ByteBuffer byteBuffer = download.getValue(); // 内容
 *
 * // 获取内容方式一：
 * byteBuffer.rewind();
 * byte[] bytes = new byte[byteBuffer.remaining()];
 * byteBuffer.get(bytes, 0, bytes.length);
 * // 上面代码可将buffer一次性读入到内存 然后直接使用bytes即可
 *
 * // 获取内容方式二：
 * import java.io.InputStream;
 * import com.mogudiandian.util.io.ByteBufferInputStream
 * byteBuffer.rewind();
 * InputStream input = new ByteBufferInputStream(byteBuffer, ByteBuffer::rewind);
 * // 上面将ByteBuffer包装为InputStream 然后可按照流的方式来访问
 * </pre>
 *
 * @author Joshua Sun
 * @since 1.0.0
 */
public final class DownloadUtils {

    /**
     * HTTPS不验证主机
     */
    private static final HostnameVerifier NO_HOST_VERIFY = (hostname, session) -> true;

    /**
     * 信任所有证书
     */
    private static final TrustManager TRUST_ALL_CERTS = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };

    /**
     * 每个MIME最常用的格式
     */
    private static final Map<String, String> MIME_TYPE_MOST_USED_FORMAT = new HashMap<>();

    static {

    }

    private DownloadUtils() {
    }

    /**
     * 下载
     *
     * @param url                   URL
     * @param readTimeoutMillis     下载超时
     * @param priorityUseSourceName 优先使用源文件名 如果不使用 则会自动生成文件名
     * @return 文件名 -> 下载内容
     */
    public static Map.Entry<String, ByteBuffer> download(String url, int readTimeoutMillis, boolean priorityUseSourceName) {
        URL urlObject;
        try {
            urlObject = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return download(urlObject, readTimeoutMillis, priorityUseSourceName);
    }

    /**
     * 下载
     *
     * @param url                   URL
     * @param readTimeoutMillis     下载超时
     * @param priorityUseSourceName 优先使用源文件名
     * @return 文件名 -> 下载内容
     */
    public static Map.Entry<String, ByteBuffer> download(URL url, int readTimeoutMillis, boolean priorityUseSourceName) {
        // 使用JDK自带的，兼容性最好
        URLConnection connection = getUrlConnection(url, readTimeoutMillis);
        // 下载内容
        ByteBuffer byteBuffer = getBuffer(connection);
        // 获取文件名
        String fileName = getFileName(connection, byteBuffer, priorityUseSourceName);

        return new AbstractMap.SimpleImmutableEntry<>(fileName, byteBuffer);
    }

    /**
     * 获取URL连接
     *
     * @param url               URL
     * @param readTimeoutMillis 读取超时
     * @return URL连接
     */
    @SneakyThrows
    private static URLConnection getUrlConnection(URL url, int readTimeoutMillis) {
        URLConnection connection = url.openConnection();
        // 默认连接超时10秒 对于下载的一个经验值
        connection.setConnectTimeout(10_000);
        connection.setReadTimeout(readTimeoutMillis);

        // https的情况
        if (connection instanceof HttpsURLConnection) {
            HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
            // 信任所有域名
            httpsConnection.setHostnameVerifier(NO_HOST_VERIFY);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{TRUST_ALL_CERTS}, new SecureRandom());
            httpsConnection.setSSLSocketFactory(sslContext.getSocketFactory());
        }

        // 模拟浏览器
        connection.setRequestProperty("User-Agent", "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.74 Safari/537.36");

        return connection;
    }

    /**
     * 获取内容缓冲区
     *
     * @param connection URL连接
     * @return 内容二进制数组
     */
    @SneakyThrows
    private static ByteBuffer getBuffer(URLConnection connection) {
        // 获取响应的content-length
        int contentLength = connection.getContentLength();
        // System.out.println("contentLength: " + contentLength);

        ByteBuffer byteBuffer;

        // 返回了数据量
        if (contentLength > 0) {
            // 是否使用直接内存/堆外内存，默认不使用
            boolean allocateDirect = false;

            // 如果文件超过4MB（经验值）使用堆外内存 否则都使用Java堆内存
            if (contentLength >= 4 * 1024 * 1024) {
                allocateDirect = true;
            }
            // System.out.println("allocateDirect: " + allocateDirect);

            // 使用堆外内存
            if (allocateDirect) {
                byteBuffer = ByteBuffer.allocateDirect(contentLength);
            } else {
                // 使用堆内存
                byteBuffer = ByteBuffer.allocate(contentLength);
            }

            // 写入到缓冲区
            try (InputStream inputStream = connection.getInputStream()) {
                byte[] buf = new byte[4096];
                for (int len; (len = inputStream.read(buf)) >= 0; ) {
                    byteBuffer.put(buf, 0, len);
                }
                byteBuffer.flip();
            }
        } else {
            // 没返回数据量 只能用jvm内存来存储
            try (InputStream inputStream = connection.getInputStream();
                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();) {
                // 每次读4k
                byte[] buf = new byte[4096];
                for (int len; (len = inputStream.read(buf)) >= 0; ) {
                    byteArrayOutputStream.write(buf, 0, len);
                }
                byteBuffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
            }
        }

        return byteBuffer;
    }

    /**
     * 获取文件名
     * 1.先看header中是否有Content-Disposition 有就用这个作为文件名
     * 2.拿url的path中最后一段作为文件名
     * 3.如果有文件名就直接截取basename和extension
     * 4.如果没有extension则尝试通过Content-Type拿MIME
     * 5.如果没有Content-Type则根据文件内容的文件头识别MIME
     * 6.拿到MIME则获取MIME对应的扩展名
     * 7.如果需要生成随机basename或没有识别到文件名则生成basename
     * 8.如果有扩展名则拼接文件全名
     * 9.如果实在没有文件名，则使用URL作为文件名
     *
     * @param connection            URL连接
     * @param byteBuffer            内容缓冲区
     * @param priorityUseSourceName 优先使用源名
     * @return 文件名
     */
    @SneakyThrows
    private static String getFileName(URLConnection connection, ByteBuffer byteBuffer, boolean priorityUseSourceName) {
        // 文件名和扩展名
        String fileName = null, extension = null;

        // header中返回的文件名
        String contentDisposition = connection.getHeaderField("Content-Disposition");
        if (StringUtils.isNotEmpty(contentDisposition)) {
            String mark = "filename=";
            int index = contentDisposition.indexOf(mark);
            if (index >= 0) {
                fileName = contentDisposition.substring(index + mark.length());
                if (StringUtils.isNotEmpty(fileName) && fileName.charAt(0) == '"' && fileName.charAt(fileName.length() - 1) == '"') {
                    fileName = fileName.substring(1, Math.max(fileName.length() - 1, 1));
                }
                // TODO 这里可能会乱码 需要识别编码后转成UTF8
            }
        }
        // System.out.println("fileName from content-disposition: " + fileName);

        // 如果没有从header中解析到文件名
        if (StringUtils.isEmpty(fileName)) {
            // 从URL中解析文件名
            String path = connection.getURL().getPath();
            if (StringUtils.isNotEmpty(path) && !"/".equals(path)) {
                // 因为是URL上的 可能存在编码过 需要解回来
                path = URLDecoder.decode(path, StandardCharsets.UTF_8.displayName());
                fileName = path.substring(path.lastIndexOf('/') + 1);
                // System.out.println("fileName from url: " + fileName);
            } else {
                fileName = "index";
            }
        }

        // 通过文件名快速获取扩展名（如果有）
        int index = fileName.lastIndexOf('.');
        if (index >= 0) {
            // 这个地方必须先取扩展名 再改文件名 否则原始fileName就丢了
            extension = fileName.substring(index + 1);
            fileName = fileName.substring(0, index);
            // System.out.println("fileName = " + fileName + ", extension = " + extension);
        }

        // 没有扩展名的话 猜测扩展名
        if (StringUtils.isEmpty(extension)) {
            //  先尝试获取header中的Content-Type
            String mime = connection.getContentType();
            if (StringUtils.isNotEmpty(mime)) {
                // 去掉;charset=xxx
                mime = mime.substring(0, Math.min(mime.indexOf(';') & 0x7fffffff, mime.length()));
                // System.out.println("mime from content-type: " + mime);
            }

            // header没有返回的话 尝试读取
            if (StringUtils.isEmpty(mime)) {
                // 读8个字节 一个字节是8位(2个16进制) 总共是16个16进制数/字符
                byte[] bytes = new byte[8];
                byteBuffer.rewind();
                byteBuffer.get(bytes, 0, Math.min(bytes.length, byteBuffer.remaining()));
                byteBuffer.rewind();

                Tika tika = new Tika();
                mime = tika.detect(bytes);
                // System.out.println("mime from file header magic: " + mime);
            }

            // 如果读取到了mime 则获取mime对应的扩展名
            if (StringUtils.isNotEmpty(mime)) {
                // 这里进行了定制优化 如果一个mime对应多个extension 比如jpg/jpeg 则选一个最常用的
                extension = MIME_TYPE_MOST_USED_FORMAT.get(mime);
                // 如果没有最常用的 则获取tika的第一个
                if (extension == null) {
                    try {
                        MimeType mimeType = MimeTypes.getDefaultMimeTypes().forName(mime);
                        // 目前是拿tika默认的第一个
                        extension = mimeType.getExtension();
                        if (StringUtils.isNotEmpty(extension) && extension.charAt(0) == '.') {
                            extension = extension.substring(1);
                        }
                        // System.out.println("extension from mime: " + extension);
                    } catch (MimeTypeException ignored) {
                        // System.out.println("mime type for name error, mime = " + mime);
                    }
                }
            }
        }

        // 如果不优先使用原始文件名 或 没有读取到原始文件名 则随机生成文件名
        if (!priorityUseSourceName || StringUtils.isEmpty(fileName)) {
            fileName = UUID.randomUUID().toString().replace("-", "");
            // TODO 有一个潜在问题 无法判断源文件是否没有basename只有extension 这种文件也会加上随机
        }
        // System.out.println("base name: " + fileName);

        // 有扩展名的话将扩展名拼上
        if (StringUtils.isNotEmpty(extension)) {
            fileName += "." + extension.toLowerCase();
        }
        // System.out.println("file name: " + fileName);

        // 如果实在没有 使用URL整体作为文件名
        if (StringUtils.isEmpty(fileName)) {
            fileName = URLEncoder.encode(connection.getURL().toString(), StandardCharsets.UTF_8.displayName());
        }
        // System.out.println("final file name: " + fileName);

        return fileName;
    }
}
