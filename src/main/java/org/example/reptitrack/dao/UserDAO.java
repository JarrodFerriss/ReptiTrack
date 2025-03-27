package org.example.reptitrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    /**
     * Checks if a user with the given credentials exists in the database.
     *
     * @param username The username entered
     * @param password The password entered (plaintext for now)
     * @return true if credentials are valid, false otherwise
     */
    public static boolean authenticate(String username, String password) {
        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // ✅ returns true if user is found
            }

        } catch (SQLException e) {
            System.err.println("❌ Error authenticating user: " + e.getMessage());
            return false;
        }
    }
}
