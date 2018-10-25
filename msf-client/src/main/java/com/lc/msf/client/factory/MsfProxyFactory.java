package com.lc.msf.client.factory;

import java.util.concurrent.ConcurrentHashMap;

public class MsfProxyFactory {
    private static ConcurrentHashMap<String, Object> proxyMap = new ConcurrentHashMap<>();

    public static <T> T create(Class<?> type, String strUrl) {
        return create(type, strUrl, false);
    }

    public static <T> T create(Class<?> type, String strUrl, boolean async) {//<T> T返回任意类型的数据？  返回代理的实例  泛型
        String key = strUrl.toLowerCase();
        Object proxy = null;
        if (async) {
            if (asyncCache.containsKey(key)) {
                proxy = asyncCache.get(key);
            }
            if (proxy == null) {
                proxy = createStandardProxy(strUrl, type, async);
                if (proxy != null) {
                    asyncCache.put(key, proxy);
                }
            }
        } else {
            if (cache.containsKey(key)) {
                proxy = cache.get(key);
            }
            if (proxy == null) {
                proxy = createStandardProxy(strUrl, type);
                if (proxy != null) {
                    cache.put(key, proxy);
                }
            }
        }
        return (T)proxy;
    }

    private static Object createStandardProxy(String strUrl, Class<?> type) {
        return createStandardProxy(strUrl, type, false);
    }
}
