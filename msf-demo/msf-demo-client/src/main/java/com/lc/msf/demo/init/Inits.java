package com.lc.msf.demo.init;

import com.lc.msf.common.config.ConfigCenter;

import java.io.InputStream;

public class Inits {
    public void init() {
        try {
            InputStream inputStream = Inits.class.getClassLoader().getResourceAsStream("jdbc.properties");
            ConfigCenter.getInstance().initProperties(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
