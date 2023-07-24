package com.mogudiandian.util.javac;

import javax.tools.JavaFileObject.Kind;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态的类加载器，用来加载动态编译的class
 * @author Joshua Sun
 */
final class DynamicClassLoader extends ClassLoader {

    /**
     * 存放动态编译过的类对象
     */
    private final Map<String, DynamicByteArrayObject> compiledClasses = new HashMap<>();

    /**
     * 初始化 需要父加载器保证不破坏双亲委派原则
     * @param parentClassLoader 父加载器
     */
    public DynamicClassLoader(ClassLoader parentClassLoader) {
        super(parentClassLoader);
    }

    /**
     * 增加编译好的类
     * @param compiledObject 编译好的类
     */
    public void addClass(DynamicByteArrayObject compiledObject) {
        compiledClasses.put(compiledObject.getName(), compiledObject);
    }

    @Override
    public Class<?> findClass(String className) throws ClassNotFoundException {
        DynamicByteArrayObject byteObject = compiledClasses.get(className);
        if (byteObject != null) {
            byte[] bytes = byteObject.getBytes();
            return defineClass(className, bytes, 0, bytes.length);
        }

        return super.findClass(className);
    }

    @Override
    public InputStream getResourceAsStream(final String name) {
        if (name.endsWith(Kind.CLASS.extension)) {
            String classFullName = name.substring(0, name.length() - Kind.CLASS.extension.length()).replace('/', '.');
            DynamicByteArrayObject file = compiledClasses.get(classFullName);
            if (file != null) {
                return new ByteArrayInputStream(file.getBytes());
            }
        }
        return super.getResourceAsStream(name);
    }
}
