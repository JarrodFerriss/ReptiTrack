package org.example.reptitrack.dao;

import org.example.reptitrack.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeederDAO {

    public static List<Product> getAllFeeders() {
        List<Product> feeders = new ArrayList<>();

        String sql = """
            SELECT f.feeder_id, p.product_id, p.product_name, p.category, 
                   p.stock_quantity, p.supplier, p.price, p.min_stock_level
            FROM Feeders f
            JOIN Products p ON f.product_id = p.product_id
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        "Feeders",
                        rs.getInt("stock_quantity"),
                        rs.getString("supplier"),
                        rs.getDouble("price"),
                        rs.getInt("min_stock_level")
                );
                feeders.add(product);
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch feeders: " + e.getMessage());
        }

        return feeders;
    }

    public static void insertFeeder(Product product) {
        int productId = ProductDAO.insertProductAndReturnId(product);

        String sql = "INSERT INTO Feeders (product_name, category, stock_quantity, supplier, price, min_stock_level) VALUES (?, ?, ?, ? ,?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getCategory());
            stmt.setInt(3, product.getStockQuantity());
            stmt.setString(4, product.getSupplier());
            stmt.setDouble(5, product.getPrice());
            stmt.setInt(6, product.getMinStockLevel());

            stmt.executeUpdate();
            System.out.println("✅ Feeder added to Feeders and Products.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to insert feeder: " + e.getMessage());
        }
    }

    public static void updateFeeder(Product product) {
        ProductDAO.updateProduct(product);

        String sql = "UPDATE Feeders SET product_name = ?, category = ?, stock_quantity = ?, supplier = ?, price = ?, min_stock_level = ? WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getCategory());
            stmt.setInt(3, product.getStockQuantity());
            stmt.setString(4, product.getSupplier());
            stmt.setDouble(5, product.getPrice());
            stmt.setInt(6, product.getMinStockLevel());
            stmt.setInt(7, product.getId());

            stmt.executeUpdate();
            System.out.println("✅ Feeder updated in Feeders and Products.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to update feeder: " + e.getMessage());
        }
    }

    public static void deleteFeeder(int productId) {
        String sql = "DELETE FROM Feeders WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            stmt.executeUpdate();
            ProductDAO.deleteProductById(productId);
            System.out.println("🗑️ Feeder deleted from Feeders and Products.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to delete feeder: " + e.getMessage());
        }
    }
}
