package org.example.reptitrack.dao;

import org.example.reptitrack.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnclosureDAO {

    public static List<Product> getAllEnclosures() {
        List<Product> enclosures = new ArrayList<>();
        String sql = """
            SELECT e.enclosure_id, p.product_id, p.product_name, p.category, 
                   p.stock_quantity, p.supplier, p.price, p.min_stock_level
            FROM Enclosures e
            JOIN Products p ON e.product_id = p.product_id
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        "Enclosures",
                        rs.getInt("stock_quantity"),
                        rs.getString("supplier"),
                        rs.getDouble("price"),
                        rs.getInt("min_stock_level")
                );
                enclosures.add(product);
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch enclosures: " + e.getMessage());
        }

        return enclosures;
    }

    public static void insertEnclosure(Product product) {
        int productId = ProductDAO.insertProductAndReturnId(product);

        String sql = "INSERT INTO Enclosures (product_name, category, stock_quantity, supplier, price, min_stock_level) VALUES (?, ?, ?, ? ,?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getCategory());
            stmt.setInt(3, product.getStockQuantity());
            stmt.setString(4, product.getSupplier());
            stmt.setDouble(5, product.getPrice());
            stmt.setInt(6, product.getMinStockLevel());

            stmt.executeUpdate();
            System.out.println("✅ Enclosure added to Enclosures and Products.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to insert enclosure: " + e.getMessage());
        }
    }

    public static void updateEnclosure(Product product) {
        ProductDAO.updateProduct(product);

        String sql = "UPDATE Enclosures SET product_name = ?, category = ?, stock_quantity = ?, supplier = ?, price = ?, min_stock_level = ? WHERE product_id = ?";

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
            System.out.println("✅ Enclosure updated in Enclosures and Products.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to update enclosure: " + e.getMessage());
        }
    }

    public static void deleteEnclosure(int productId) {
        String sql = "DELETE FROM Enclosures WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            stmt.executeUpdate();

            ProductDAO.deleteProductById(productId);
            System.out.println("🗑️ Enclosure deleted from Enclosures and Products.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to delete enclosure: " + e.getMessage());
        }
    }
}
