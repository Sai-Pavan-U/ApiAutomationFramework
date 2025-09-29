
package com.api.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties PROPS = new Properties();

    static {
        String env = System.getProperty("env", "dev").toLowerCase().trim();
        String configFile = "config/config." + env + ".properties";

        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFile);

        if (in == null) {
            System.out.println("ConfigManager: Environment-specific config '" + configFile
                    + "' not found, trying default config.properties");
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/config.properties");
        } else {
            System.out.println("ConfigManager: Loading configuration from " + configFile);
        }

        if (in == null) {
            throw new RuntimeException(
                    "ConfigManager: could not find config.properties or " + configFile + " in classpath");
        }

        try {
            PROPS.load(in);
            in.close();
        } catch (IOException e) {
            System.err.println("ConfigManager: could not load configuration file: " + e.getMessage());
            throw new RuntimeException("ConfigManager: Failed to load configuration", e);
        }
    }

    private ConfigManager() {
    }

    public static String getProperty(String key) {
        return PROPS.getProperty(key);
    }

    public static void main(String[] args) {
        String env = System.getProperty("env", "dev");
        System.out.println("Current environment: " + env);
        System.out.println("Loaded config properties: ");
        System.out.println("BASE_URI=" + getProperty("BASE_URI"));
    }
}