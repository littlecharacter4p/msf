package com.lc.msf.common.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigHelper {
    private Properties properties;

    private ConfigHelper() {
        properties = new Properties();
    }

    private static class ConfigCenterInner {
        private ConfigCenterInner() {}
        private final static ConfigHelper CONFIG_HELPER = new ConfigHelper();
    }

    public static ConfigHelper getInstance() {
        return ConfigCenterInner.CONFIG_HELPER;
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
