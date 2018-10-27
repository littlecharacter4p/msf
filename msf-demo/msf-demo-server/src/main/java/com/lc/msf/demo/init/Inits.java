package com.lc.msf.demo.init;

import com.lc.msf.common.config.ConfigHelper;

import java.io.InputStream;

public class Inits {
    public void init() {
        try {
            InputStream inputStream = Inits.class.getClassLoader().getResourceAsStream("jdbc.properties");
            ConfigHelper.getInstance().initProperties(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
