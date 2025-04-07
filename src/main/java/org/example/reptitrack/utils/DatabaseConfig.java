package org.example.reptitrack.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class to load and retrieve configuration properties from the `.env` file.
 * <p>
 * Used to access sensitive values like database credentials at runtime.
 * This class loads the `.env` file once on first access.
 * </p>
 *
 * Example usage:
 * <pre>{@code
 *     String dbUrl = DatabaseConfig.get("DB_URL");
 * }</pre>
 *
 * @author Jarrod
 * @since 2025-04-06
 */
public class DatabaseConfig {

    // Properties object to store loaded configuration
    private static final Properties properties = new Properties();

    // Static block runs once to load the .env file
    static {
        try {
            properties.load(new FileInputStream(".env"));
        } catch (IOException e) {
            System.err.println("❌ Failed to load .env file: " + e.getMessage());
        }
    }

    /**
     * Retrieves a value from the loaded .env configuration.
     *
     * @param key the name of the property to fetch
     * @return the value associated with the key, or null if not found
     */
    public static String get(String key) {
        return properties.getProperty(key);
    }
}
