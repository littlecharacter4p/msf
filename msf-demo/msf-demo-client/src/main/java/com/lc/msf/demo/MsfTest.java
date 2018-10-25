package com.lc.msf.demo;

public class MsfTest {
    public static void main(String[] args) {
        // 初始化容器,dsf.config/dsf.xml为客户端配置文件(支持xml格式),具体配置见下节
        // DSFInit.init(PathUtil.getCurrentPath() +"dsf.config");
        // 创建接口代理，第一个参数为接口，第二个参数分为三部分：前部分固定,中间的为要访问的服务名(echo)，后边的是接口实现类别名(Ping)
        // HelloService helloService = DSFProxyFactory.create(HelloService.class, HelloService.URL);
        // System.out.println(helloService.sayHello("zhangsan"));
    }
}
