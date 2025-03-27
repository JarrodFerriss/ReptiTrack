package org.example.reptitrack.dao;

import org.example.reptitrack.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeederDAO {

    public static List<Product> getAllFeeders() {
        List<Product> feeders = new ArrayList<>();
        String sql = "SELECT * FROM Feeders";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("feeder_id"),
                        rs.getString("product_name"),
                        "Feeders",
                        rs.getInt("stock_quantity"),
                        rs.getString("supplier"),
                        rs.getDouble("price")
                );
                feeders.add(product);
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch feeders: " + e.getMessage());
        }

        return feeders;
    }

    public static void insertFeeder(Product product) {
        String sql = "INSERT INTO Feeders (product_name, category, stock_quantity, supplier, price) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getCategory());
            stmt.setInt(3, product.getStockQuantity());
            stmt.setString(4, product.getSupplier());
            stmt.setDouble(5, product.getPrice());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    product.setId(keys.getInt(1));
                    product.setCategory("Feeders");
                    ProductDAO.insertProduct(product);
                }
            }

            System.out.println("✅ Feeder added and synced to Products.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to insert feeder: " + e.getMessage());
        }
    }

    public static void updateFeeder(Product product) {
        String sql = "UPDATE Feeders SET product_name = ?, stock_quantity = ?, supplier = ?, price = ? WHERE feeder_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getProductName());
            stmt.setInt(2, product.getStockQuantity());
            stmt.setString(3, product.getSupplier());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getId());

            stmt.executeUpdate();
            ProductDAO.updateProduct(product);

            System.out.println("✅ Feeder updated and synced to Products.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to update feeder: " + e.getMessage());
        }
    }

    public static void deleteFeeder(int id) {
        String sql = "DELETE FROM Feeders WHERE feeder_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            ProductDAO.deleteProductById(id);

            System.out.println("🗑️ Feeder deleted and removed from Products.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to delete feeder: " + e.getMessage());
        }
    }
}
