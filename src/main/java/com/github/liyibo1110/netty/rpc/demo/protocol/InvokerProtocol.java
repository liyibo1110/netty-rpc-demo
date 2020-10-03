package com.github.liyibo1110.netty.rpc.demo.protocol;

import java.io.Serializable;

/**
 * @author liyibo
 */
public class InvokerProtocol implements Serializable {

    private String className;
    private String methodName;
    private Class<?>[] argumentClasses;
    private Object[] arguments;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getArgumentClasses() {
        return argumentClasses;
    }

    public void setArgumentClasses(Class<?>[] argumentClasses) {
        this.argumentClasses = argumentClasses;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }
}
