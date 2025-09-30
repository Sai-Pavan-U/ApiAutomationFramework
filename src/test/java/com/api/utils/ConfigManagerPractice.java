package com.api.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManagerPractice {
    static FileReader fr;
    static Properties prop;
    static Properties prop1;

    // Approach 1
    public static String getProperty(String key) {
        File fi = new File(System.getProperty("user.dir") + "/src/test/resources/config/config.dev.properties");

        try {
            fr = new FileReader(fi);
            prop = new Properties();
            prop.load(fr);
            fr.close();
            return prop.getProperty(key);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Approach 2
    public static String getProperty1(String key) {
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("config/config.dev.properties");
        try {
            prop1.load(is);
            is.close();
            return prop1.getProperty(key);
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getProperty("BASE_URI"));
        System.out.println(getProperty1("BASE_URI"));
    }
}
