package com.lc.msf.register;

public interface ServiceRegistry {
    /**
     * 服务注册
     * @param serviceName 服务名称
     * @param serviceAddress ip:port
     */
    void registy(String serviceName, String serviceAddress);
}
