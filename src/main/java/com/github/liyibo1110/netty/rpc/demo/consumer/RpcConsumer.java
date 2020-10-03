package com.github.liyibo1110.netty.rpc.demo.consumer;

import com.github.liyibo1110.netty.rpc.demo.api.RpcCalculateService;
import com.github.liyibo1110.netty.rpc.demo.api.RpcHelloService;
import com.github.liyibo1110.netty.rpc.demo.consumer.proxy.RpcProxy;

/**
 * @author liyibo
 */
public class RpcConsumer {

    public static void main(String[] args) {
        RpcHelloService helloService = RpcProxy.create(RpcHelloService.class);
        helloService.hello("老王");

        RpcCalculateService calculateService = RpcProxy.create(RpcCalculateService.class);
        System.out.println("10 + 2 = " + calculateService.add(10, 2));
        System.out.println("10 - 2 = " + calculateService.sub(10, 2));
        System.out.println("10 * 2 = " + calculateService.multi(10, 2));
        System.out.println("10 / 2 = " + calculateService.div(10, 2));
    }
}
