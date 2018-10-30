package com.lc.msf.discover.impl;

import com.lc.msf.common.config.ConfigHelper;
import com.lc.msf.common.constant.Constants;
import com.lc.msf.discover.ServiceDiscovery;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ZkServiceDiscovery implements ServiceDiscovery {
    private CuratorFramework zkClient;
    private List<String> serviceNodes = new ArrayList<>();

    public ZkServiceDiscovery() {
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(ConfigHelper.getInstance().getProperty(Constants.ZK_ADDRESS))
                .sessionTimeoutMs(3000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        zkClient.start();
    }

    @Override
    public List<String> discover(String serviceName) {
        if (!serviceNodes.isEmpty()) {
            System.out.println("服务(" + serviceName + ")节点：" + serviceNodes);
            return serviceNodes;
        }
        String servicePath = Constants.ZK_REGISTRY_PATH + "/" + serviceName;
        try {
            serviceNodes = zkClient.getChildren().forPath(servicePath);
            System.out.println("服务(" + serviceName + ")节点：" + serviceNodes);
            final PathChildrenCache watcher = new PathChildrenCache(zkClient, servicePath, true);
            watcher.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
            watcher.getListenable().addListener((client, event) -> {
                List<String> nodes = zkClient.getChildren().forPath(servicePath);
                if (nodes != null) {
                    serviceNodes.clear();
                    Collections.copy(serviceNodes, nodes);
                    System.out.println("服务(" + serviceName + ")节点变更：" + serviceNodes);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceNodes;
    }
}
