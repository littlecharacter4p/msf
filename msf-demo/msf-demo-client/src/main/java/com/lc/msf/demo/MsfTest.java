package com.lc.msf.demo;

import com.lc.msf.client.factory.MsfProxyFactory;
import com.lc.msf.demo.contract.HelloService;

public class MsfTest {
    public static void main(String[] args) {
        HelloService helloService = MsfProxyFactory.create(HelloService.class);
        System.out.println(helloService.sayHello("zhangsan"));
    }
}
