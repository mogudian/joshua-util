package com.mogudiandian.util.javac;

import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

/**
 * Java动态编译器
 * 注意：需要运行环境是JDK，不能是JRE(没有tools.jar)
 * @author Joshua Sun
 * @since 1.0.0
 */
public final class DynamicCompiler {

    private DynamicCompiler() {
        super();
    }

    /**
     * 编译Java代码
     * @param classFullName 类全限定名
     * @param sourceCode 源代码
     * @return 编译后的类
     * @throws DynamicCompilerException 编译异常
     */
    public static Class<?> compile(String classFullName, String sourceCode) throws DynamicCompilerException {
        // javac
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        // javac的诊断信息收集器
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        // 用于生成非致命错误信息
        StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnostics, null, null);

        DynamicClassLoader dynamicClassLoader = new DynamicClassLoader(Optional.ofNullable(Thread.currentThread().getContextClassLoader()).orElse(DynamicCompiler.class.getClassLoader()));

        DynamicFileManager dynamicFileManager = new DynamicFileManager(standardFileManager, dynamicClassLoader);

        // 获取类全限定名的最后一个点号 并用这个点号来拆分包名和类名 再转换为包路径和类文件名
        int index = classFullName.lastIndexOf('.');
        String className = classFullName.substring(index + 1);
        String classFileName = className + JavaFileObject.Kind.SOURCE.extension;
        String packageName = classFullName.substring(0, classFullName.length() - className.length());
        String packagePath = packageName.replace('.', '/');

        DynamicStringObject dynamicStringObject = new DynamicStringObject(packagePath + '/' + classFileName, sourceCode);
        dynamicFileManager.putFileForInput(StandardLocation.SOURCE_PATH, packagePath, classFileName, dynamicStringObject);

        // 创建编译任务
        CompilationTask compilationTask = compiler.getTask(null, dynamicFileManager, diagnostics, null, null, Collections.singletonList(dynamicStringObject));

        // 执行编译任务 call返回true表示编译成功
        if (compilationTask.call()) {
            try {
                // 成功后用自定义的加载器加载类
                return dynamicClassLoader.loadClass(classFullName);
            } catch (ClassNotFoundException e) {
                throw new DynamicCompilerException(e, diagnostics.getDiagnostics());
            }
        }

        // 执行到这表示编译失败 抛出异常并包含javac输出的诊断信息
        try {
            throw new DynamicCompilerException("Compilation failure", diagnostics.getDiagnostics());
        } finally {
            try {
                dynamicFileManager.close();
            } catch (IOException ignored) {
            }
        }
    }
}
