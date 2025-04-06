package org.example.reptitrack.dao;

import org.example.reptitrack.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all CRUD operations related to the Animals table.
 * Automatically syncs with the Products table.
 */
public class AnimalDAO {

    // ───────────────────────────────
    // READ
    // ───────────────────────────────

    public static List<Product> getAllAnimals() {
        List<Product> animals = new ArrayList<>();
        String sql = """
            SELECT a.animal_id, p.product_id, p.product_name, p.stock_quantity, 
                   p.supplier, p.price, p.min_stock_level
            FROM Animals a
            JOIN Products p ON a.product_id = p.product_id
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        "Animals",
                        rs.getInt("stock_quantity"),
                        rs.getString("supplier"),
                        rs.getDouble("price"),
                        rs.getInt("min_stock_level")
                );
                animals.add(product);
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch animals: " + e.getMessage());
        }

        return animals;
    }

    // ───────────────────────────────
    // CREATE
    // ───────────────────────────────

    public static void insertAnimal(Product product) {
        int productId = ProductDAO.insertProductAndReturnId(product);

        String sql = "INSERT INTO Animals (product_name, category, stock_quantity, supplier, price, min_stock_level) VALUES (?, ?, ?, ? ,?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getCategory());
            stmt.setInt(3, product.getStockQuantity());
            stmt.setString(4, product.getSupplier());
            stmt.setDouble(5, product.getPrice());
            stmt.setInt(6, product.getMinStockLevel());

            stmt.executeUpdate();
            System.out.println("✅ Animal added to Animals and Products (linked by product_id = " + productId + ")");

        } catch (SQLException e) {
            System.err.println("❌ Failed to insert animal: " + e.getMessage());
        }
    }

    // ───────────────────────────────
    // UPDATE
    // ───────────────────────────────

    public static void updateAnimal(Product product) {
        ProductDAO.updateProduct(product);

        String sql = "UPDATE Animals SET product_name = ?, category = ?, stock_quantity = ?, supplier = ?, price = ?, min_stock_level = ? WHERE product_id = ?";

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
            System.out.println("✅ Animal updated in Animals and Products.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to update animal: " + e.getMessage());
        }
    }

    // ───────────────────────────────
    // DELETE
    // ───────────────────────────────

    public static void deleteAnimal(int productId) {
        String sql = "DELETE FROM Animals WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            stmt.executeUpdate();

            ProductDAO.deleteProductById(productId);
            System.out.println("🗑️ Animal deleted from Animals and Products.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to delete animal: " + e.getMessage());
        }
    }
}
