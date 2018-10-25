package com.lc.msf.common.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigCenter {
    private Properties properties;

    private ConfigCenter() {
        properties = new Properties();
    }

    private static class ConfigCenterInner {
        private ConfigCenterInner() {}
        private final static ConfigCenter configCenter = new ConfigCenter();
    }

    public static ConfigCenter getInstance() {
        return ConfigCenterInner.configCenter;
    }

    public void initProperties(InputStream... inputStreams) throws IOException {
        for (InputStream inputStream : inputStreams) {
            properties.load(inputStream);
        }
    }

    public String getProperty(String key){
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
