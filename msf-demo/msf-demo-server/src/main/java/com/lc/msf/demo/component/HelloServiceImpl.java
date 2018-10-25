package com.lc.msf.demo.component;


import com.lc.msf.demo.contract.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "hell " + name;
    }
}
