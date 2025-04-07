package org.example.reptitrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class responsible for user authentication.
 *
 * Provides a method to verify credentials against the Users table.
 * This version uses plaintext passwords for simplicity.
 * In a production app, use hashing and secure comparison.
 *
 * @author Jarrod
 * @since 2025-04-06
 */
public class UserDAO {

    /**
     * Validates the provided username and password against the database.
     *
     * @param username the username entered by the user
     * @param password the plaintext password entered
     * @return true if valid credentials were found, false otherwise
     */
    public static boolean authenticate(String username, String password) {
        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // ✅ Valid login if a result exists
            }

        } catch (SQLException e) {
            System.err.println("❌ Error authenticating user: " + e.getMessage());
            return false;
        }
    }
}
