package com.lc.msf.discover;

import java.util.List;

public interface ServiceDiscovery {
    /**
     * 服务发现
     * @param serviceName 服务名称
     * @return
     */
    List<String> discover(String serviceName);
}
