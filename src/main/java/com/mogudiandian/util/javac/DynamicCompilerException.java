package com.mogudiandian.util.javac;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.List;

/**
 * 动态编译期间产生的异常
 * @author Joshua Sun
 */
public final class DynamicCompilerException extends Exception {

    private List<Diagnostic<? extends JavaFileObject>> diagnostics;

    public DynamicCompilerException(String message, List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        super(message);
        this.diagnostics = diagnostics;
    }

    public DynamicCompilerException(Throwable e, List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        super(e);
        this.diagnostics = diagnostics;
    }

    /**
     * 获取编译器诊断的错误
     * @return 错误信息
     */
    public String getDiagnosticsError() {
        StringBuilder builder = new StringBuilder();
        if (diagnostics != null) {
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
                builder.append(String.format("Error on line %d: %s\n", diagnostic.getLineNumber(), diagnostic.getMessage(null)));
            }
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return getDiagnosticsError();
    }
}
