package com.github.liyibo1110.netty.rpc.demo.registry;

import com.github.liyibo1110.netty.rpc.demo.protocol.InvokerProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liyibo
 */
public class RegistryHandler extends ChannelInboundHandlerAdapter {

    private List<String> classNames = new ArrayList<>();
    private Map<String, Object> registryMap = new ConcurrentHashMap<>();

    public RegistryHandler() {
        scanClass("com.github.liyibo1110.netty.rpc.demo.provider");
        doRegistry();
    }

    private void doRegistry() {
        if(classNames.isEmpty()) return;
        for(String className : classNames) {
            try {
                Class<?> clazz = Class.forName(className);
                Class<?> interfaceClass = clazz.getInterfaces()[0];
                String serviceName = interfaceClass.getName();
                registryMap.put(serviceName, clazz.newInstance());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private void scanClass(String packageName) {

        URL resource = this.getClass().getClassLoader()
                                    .getResource(packageName.replaceAll("\\.", "/"));
        File classPath = new File(resource.getFile());
        for(File file : classPath.listFiles()) {
            if(file.isDirectory()) {
                scanClass(packageName + "." + file.getName());
            }else {
                classNames.add(packageName + "." + file.getName().replace(".class", ""));
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        Object result = new Object();
        InvokerProtocol request = (InvokerProtocol)msg;
        if(registryMap.containsKey(request.getClassName())) {
            Object service = registryMap.get(request.getClassName());
            Method method = service.getClass().getMethod(request.getMethodName(), request.getArgumentClasses());
            result = method.invoke(service, request.getArguments());
        }
        ctx.write(result);
        ctx.flush();
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      
    }
}
