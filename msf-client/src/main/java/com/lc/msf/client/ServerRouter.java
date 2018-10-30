package com.lc.msf.client;

import com.lc.msf.discover.impl.ZkServiceDiscovery;

import java.util.List;

public class ServerRouter {
    /**
     * 实际中根据配置文件来，这里先指定
     */
    private final String SERVER_NAME = "HelloService";

    public Server getServer() {
        Server server = new Server();
        ZkServiceDiscovery discovery = new ZkServiceDiscovery();
        List<String> servers = discovery.discover(SERVER_NAME);
        // 这里省略负载均衡策略
        String[] url = servers.get(0).split(":");
        server.setIp(url[0]);
        server.setPort(Integer.parseInt(url[1]));
        return server;
    }
}
