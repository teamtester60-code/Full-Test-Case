package com.ferpfirstcode.utils.dataReader;

import com.ferpfirstcode.utils.logs.LogsManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.Properties;

public class PropertyReader {
    private static Properties properties;

    static {
        loadProperties();
    }

    public static void loadProperties() {
        try {
            properties = new Properties();
            Collection<File> propertiesFiles = FileUtils.listFiles(
                new File("src/main/resources"), 
                new String[]{"properties"}, 
                true
            );
            
            for (File file : propertiesFiles) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    properties.load(fis);
                    LogsManager.info("Loaded properties from: " + file.getAbsolutePath());
                } catch (Exception e) {
                    LogsManager.error("Error loading properties file: " + file.getName() + " | " + e.getMessage());
                }
            }
            
            // Copy system properties to our properties (system properties take precedence)
            properties.putAll(System.getProperties());
            
        } catch (Exception e) {
            LogsManager.error("Error in loadProperties", e.getMessage());
            throw new RuntimeException("Failed to load properties", e);
        }
    }

    public static String getProperty(String key) {
        if (properties == null) {
            loadProperties();
        }
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            LogsManager.warn("Property not found or empty: " + key);
            return "";
        }
        return value.trim();
    }
}