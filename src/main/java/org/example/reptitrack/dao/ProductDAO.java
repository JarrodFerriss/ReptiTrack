package org.example.reptitrack.dao;

import org.example.reptitrack.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles direct access to the 'Products' table only.
 * Category-specific logic should be handled in their respective DAOs (Animals, Enclosures, etc).
 */
public class ProductDAO {

    // ─────────────────────────────────────────────────────────────
    // READ
    // ─────────────────────────────────────────────────────────────

    /**
     * Returns all products from the Products table.
     */
    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Products";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("category"),
                        rs.getInt("stock_quantity"),
                        rs.getString("supplier"),
                        rs.getDouble("price"),
                        rs.getInt("min_stock_level")
                );
                products.add(product);
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch products: " + e.getMessage());
        }

        return products;
    }

    // ─────────────────────────────────────────────────────────────
    // CREATE
    // ─────────────────────────────────────────────────────────────

    /**
     * Inserts a new product into the Products table.
     */
    public static void insertProduct(Product product) {
        String sql = "INSERT INTO Products (product_name, category, stock_quantity, supplier, price, min_stock_level) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getCategory());
            stmt.setInt(3, product.getStockQuantity());
            stmt.setString(4, product.getSupplier());
            stmt.setDouble(5, product.getPrice());
            stmt.setInt(6, product.getMinStockLevel());

            stmt.executeUpdate();
            System.out.println("✅ Product inserted into Products table.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to insert into Products: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────
    // UPDATE
    // ─────────────────────────────────────────────────────────────

    /**
     * Updates an existing product in the Products table.
     */
    public static void updateProduct(Product product) {
        String sql = "UPDATE Products SET product_name = ?, category = ?, stock_quantity = ?, supplier = ?, price = ?, min_stock_level = ? WHERE product_id = ?";

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
            System.out.println("✅ Product updated in Products table.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to update Products: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────────────────────────────

    /**
     * Deletes a product from the Products table by ID.
     */
    public static void deleteProductById(int productId) {
        String sql = "DELETE FROM Products WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            stmt.executeUpdate();
            System.out.println("🗑️ Product deleted from Products table.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to delete from Products: " + e.getMessage());
        }
    }
}
