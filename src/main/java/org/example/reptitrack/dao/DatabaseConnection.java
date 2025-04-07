package org.example.reptitrack.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.example.reptitrack.utils.DatabaseConfig;

/**
 * Utility class for managing the database connection using JDBC.
 * <p>
 * Builds a dynamic connection string using values stored in the .env file
 * and accessed via {@link DatabaseConfig}.
 * </p>
 * <p>
 * This class is used throughout the application by all DAO classes.
 * </p>
 *
 * Example usage:
 * <pre>{@code
 *     try (Connection conn = DatabaseConnection.getConnection()) {
 *         // Use the connection
 *     }
 * }</pre>
 *
 * @author Jarrod
 * @since 2025-04-06
 */
public class DatabaseConnection {

    // ─────────────────────────────────────────────────────────────
    // Database Credentials Loaded from .env
    // ─────────────────────────────────────────────────────────────

    private static final String URL = "jdbc:mysql://" + DatabaseConfig.get("DB_HOST") + ":" + DatabaseConfig.get("DB_PORT")
            + "/" + DatabaseConfig.get("DB_NAME") + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static final String USER = DatabaseConfig.get("DB_USERNAME");
    private static final String PASSWORD = DatabaseConfig.get("DB_PASSWORD");

    // ─────────────────────────────────────────────────────────────
    // Public Method to Get a JDBC Connection
    // ─────────────────────────────────────────────────────────────

    /**
     * Establishes a connection to the MySQL database using credentials from .env.
     *
     * @return a valid {@link Connection} object
     * @throws SQLException if the connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
