package com.github.liyibo1110.netty.rpc.demo.provider;

import com.github.liyibo1110.netty.rpc.demo.api.RpcHelloService;

/**
 * @author liyibo
 */
public class RpcHelloServiceImpl implements RpcHelloService {

    @Override
    public String hello(String name) {
        return "Hello, " + name + "!";
    }
}
