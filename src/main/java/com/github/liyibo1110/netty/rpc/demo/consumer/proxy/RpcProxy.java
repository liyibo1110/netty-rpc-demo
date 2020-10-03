package com.github.liyibo1110.netty.rpc.demo.consumer.proxy;

import com.github.liyibo1110.netty.rpc.demo.protocol.InvokerProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author liyibo
 */
public class RpcProxy {

    public static<T> T create(Class<?> clazz) {

        MethodProxy proxy = new MethodProxy(clazz);
        Class<?>[] interfaces = clazz.isInterface() ? new Class<?>[]{clazz} : clazz.getInterfaces();
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, proxy);
    }

    private static class MethodProxy implements InvocationHandler {

        private Class<?> clazz;

        public MethodProxy(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            if(Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(proxy, args);
            }
            return rpcInvoke(proxy, method, args);
        }

        private Object rpcInvoke(Object proxy, Method method, Object[] args) {
            InvokerProtocol message = new InvokerProtocol();
            message.setClassName(clazz.getName());
            message.setMethodName(method.getName());
            message.setArgumentClasses(method.getParameterTypes());
            message.setArguments(args);

            EventLoopGroup workerGroup = null;
            RpcProxyHandler proxyHandler = new RpcProxyHandler();
            try {
                workerGroup = new NioEventLoopGroup();
                Bootstrap client = new Bootstrap();
                client.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                ChannelPipeline pipeline = socketChannel.pipeline();
                                pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                                pipeline.addLast(new LengthFieldPrepender(4));
                                // 处理参数
                                pipeline.addLast("encoder", new ObjectEncoder());
                                pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                                // 自定义
                                pipeline.addLast(proxyHandler);
                            }
                        });
                ChannelFuture future = client.connect("127.0.0.1", 8080).sync();
                future.channel().writeAndFlush(message).sync();
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }
            return proxyHandler.getResponse();
        }
    }
}
