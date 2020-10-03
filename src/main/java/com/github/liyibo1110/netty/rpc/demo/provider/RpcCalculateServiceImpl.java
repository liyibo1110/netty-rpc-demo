package com.github.liyibo1110.netty.rpc.demo.provider;

import com.github.liyibo1110.netty.rpc.demo.api.RpcCalculateService;

/**
 * @author liyibo
 */
public class RpcCalculateServiceImpl implements RpcCalculateService {

    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int sub(int a, int b) {
        return a - b;
    }

    @Override
    public int multi(int a, int b) {
        return a * b;
    }

    @Override
    public int div(int a, int b) {
        return a / b;
    }
}
