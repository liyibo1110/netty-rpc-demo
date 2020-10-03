package com.github.liyibo1110.netty.rpc.demo.api;

/**
 * @author liyibo
 */
public interface RpcCalculateService {

    int add(int a, int b);
    int sub(int a, int b);
    int multi(int a, int b);
    int div(int a, int b);
}
