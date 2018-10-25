package com.lc.msf.client.factory;

import com.lc.msf.protocol.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;

public class MsfProxyFactory {
    private static ConcurrentHashMap<String, Object> proxyMap = new ConcurrentHashMap<>();

    public static <T> T create(Class<?> clazz) {
        String key = clazz.getName();
        Object proxy;
        if (proxyMap.containsKey(key)) {
            proxy = proxyMap.get(key);
        } else {
            proxy = Proxy.newProxyInstance(clazz.getClassLoader(),
                    new Class<?>[]{clazz}, new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            RpcRequest rpcRequest = new RpcRequest();
                            rpcRequest.setClassName(method.getDeclaringClass().getName());
                            rpcRequest.setMethodName(method.getName());
                            rpcRequest.setTypes(method.getParameterTypes());
                            rpcRequest.setParams(args);
                            return null;
                        }
                    });
        }
        if (proxy != null) {
            proxyMap.put(key, proxy);
        }
        return (T) proxy;
    }
}
