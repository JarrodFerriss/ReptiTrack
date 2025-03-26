package org.example.reptitrack.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.example.reptitrack.utils.DatabaseConfig;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://" + DatabaseConfig.get("DB_HOST") + ":" + DatabaseConfig.get("DB_PORT")
            + "/" + DatabaseConfig.get("DB_NAME") + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static final String USER = DatabaseConfig.get("DB_USERNAME");
    private static final String PASSWORD = DatabaseConfig.get("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
