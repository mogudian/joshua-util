package com.mogudiandian.util.javac;

import javax.tools.*;
import javax.tools.JavaFileObject.Kind;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态编译的文件管理器
 * @author Joshua Sun
 */
final class DynamicFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    private final DynamicClassLoader classLoader;

    /**
     * URI对应的文件
     */
    private final Map<URI, JavaFileObject> fileObjects = new HashMap<>();

    public DynamicFileManager(JavaFileManager fileManager, DynamicClassLoader classLoader) {
        super(fileManager);
        this.classLoader = classLoader;
    }

    @Override
    public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
        FileObject o = fileObjects.get(createURI(location, packageName, relativeName));
        if (o != null) {
            return o;
        }
        return super.getFileForInput(location, packageName, relativeName);
    }

    /**
     * 保存URI对应的文件
     * @param location 位置
     * @param path 文件路径
     * @param fileName 文件名
     * @param file 文件
     */
    public void putFileForInput(StandardLocation location, String path, String fileName, JavaFileObject file) {
        fileObjects.put(createURI(location, path, fileName), file);
    }

    /**
     * 根据路径和文件名获取URI
     * @param location 位置
     * @param path 文件路径
     * @param fileName 文件名
     * @return URI
     */
    private URI createURI(Location location, String path, String fileName) {
        return URIUtils.create(location.getName() + '/' + path + '/' + fileName);
    }


    @Override
    public JavaFileObject getJavaFileForOutput(Location location,
                                               String qualifiedName, Kind kind, FileObject outputFile) {
        DynamicByteArrayObject dynamicByteArrayObject = new DynamicByteArrayObject(qualifiedName, kind);
        classLoader.addClass(dynamicByteArrayObject);
        return dynamicByteArrayObject;
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return classLoader;
    }

    @Override
    public String inferBinaryName(Location loc, JavaFileObject file) {
        if (file instanceof DynamicByteArrayObject) {
            return file.getName();
        }
        return super.inferBinaryName(loc, file);
    }
}
