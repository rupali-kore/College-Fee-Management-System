package com.proj.fees.properties;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.io.IOException;

public class PropertyReader {
    private Properties prop;
    private final String fileName = "feeMang.properties";

    public PropertyReader() {
        prop = new Properties();
        loadProperties();
    }

    private void loadProperties() {
       // PropertyReader.java मध्ये हे बदल करा
try {
    InputStream input = new FileInputStream("feeMang.properties"); // फाईल थेट रूटमध्ये शोधेल
    prop = new Properties();
    prop.load(input);
} catch (Exception e) {
    System.out.println("Property File Not Found!");
}
    }

    public String getProperty(String key) {
        if (prop == null || key == null) return "";
        return prop.getProperty(key);
    }
}