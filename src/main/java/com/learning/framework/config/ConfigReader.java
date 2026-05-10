package com.learning.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads framework configuration from config/config.properties with system
 * property overrides.
 *
 * Module 11 introduces configuration precedence:
 *
 * 1. Maven/JVM system property, such as -Dheadless=false.
 * 2. config.properties default value.
 *
 * This lets the repository keep sensible defaults while allowing local runs,
 * CI jobs, and future suite files to override behavior without editing source.
 */
public final class ConfigReader {

    private static final String CONFIG_FILE = "config/config.properties";
    private static final Properties PROPERTIES = loadProperties();

    private ConfigReader() {
        // Utility class: do not instantiate.
    }

    public static String get(String key) {
        String overrideValue = System.getProperty(key);
        if (overrideValue != null && !overrideValue.isBlank()) {
            return overrideValue.trim();
        }

        String fileValue = PROPERTIES.getProperty(key);
        if (fileValue == null || fileValue.isBlank()) {
            throw new IllegalArgumentException("Missing configuration value for key: " + key);
        }
        return fileValue.trim();
    }

    public static String getBaseUrl() {
        return get("baseUrl");
    }

    public static String getBrowser() {
        return get("browser").toLowerCase();
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public static int getExplicitWaitSeconds() {
        return getInt("explicitWaitSeconds");
    }

    public static int getPageLoadTimeoutSeconds() {
        return getInt("pageLoadTimeoutSeconds");
    }

    public static int getImplicitWaitSeconds() {
        return getInt("implicitWaitSeconds");
    }

    public static int getWindowWidth() {
        return getInt("windowWidth");
    }

    public static int getWindowHeight() {
        return getInt("windowHeight");
    }

    public static int getRetryCount() {
        return getInt("retryCount");
    }

    public static int getInt(String key) {
        String value = get(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "Configuration key '" + key + "' must be an integer but was: " + value,
                    exception
            );
        }
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream inputStream = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            if (inputStream == null) {
                throw new IllegalStateException("Configuration file not found on classpath: " + CONFIG_FILE);
            }

            properties.load(inputStream);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load configuration file: " + CONFIG_FILE, exception);
        }
    }
}
