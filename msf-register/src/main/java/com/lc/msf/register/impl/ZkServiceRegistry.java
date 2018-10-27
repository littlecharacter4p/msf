package com.lc.msf.register.impl;

import com.lc.msf.register.ServiceRegistry;
import com.lc.msf.common.config.ConfigHelper;
import com.lc.msf.common.constant.Constants;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class ZkServiceRegistry implements ServiceRegistry {
    private CuratorFramework zkClient;

    public ZkServiceRegistry() {
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(ConfigHelper.getInstance().getString(Constants.ZK_ADDRESS))
                .sessionTimeoutMs(3000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        zkClient.start();
    }

    @Override
    public void registy(String serviceName, String serviceAddress) {
        String servicePath = Constants.ZK_REGISTRY_PATH + "/" + serviceName;
        try {
            if (zkClient.checkExists().forPath(servicePath) == null) {
                zkClient.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(servicePath, "0".getBytes());
                System.out.println("服务注册成功：" + servicePath);
            } else {
                System.out.println("现有注册服务：" + servicePath);
            }
            String addressPath = servicePath + "/" + serviceAddress;
            String node = zkClient.create()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(addressPath, "0".getBytes());
            System.out.println("服务节点创建成功：" + node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
