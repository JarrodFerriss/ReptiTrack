package org.example.reptitrack.dao;

import org.example.reptitrack.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for managing records in the Enclosures table.
 * <p>
 * Provides CRUD operations and keeps data synced with the Products table
 * using a shared product_id foreign key.
 * </p>
 * This ensures consistency between the general product record and the
 * specific data for enclosures.
 *
 * @author Jarrod
 * @since 2025-04-06
 */
public class EnclosureDAO {

    // ───────────────────────────────────────────────────────
    // READ
    // ───────────────────────────────────────────────────────

    /**
     * Retrieves all enclosures by joining Enclosures with Products table.
     *
     * @return list of enclosure products
     */
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
                enclosures.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        "Enclosures",
                        rs.getInt("stock_quantity"),
                        rs.getString("supplier"),
                        rs.getDouble("price"),
                        rs.getInt("min_stock_level")
                ));
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch enclosures: " + e.getMessage());
        }

        return enclosures;
    }

    // ───────────────────────────────────────────────────────
    // CREATE
    // ───────────────────────────────────────────────────────

    /**
     * Inserts a new enclosure into the Enclosures table and the Products table.
     *
     * @param product product data representing an enclosure
     */
    public static void insertEnclosure(Product product) {
        int productId = ProductDAO.insertProductAndReturnId(product);

        String sql = """
            INSERT INTO Enclosures (product_name, category, stock_quantity, 
                                    supplier, price, min_stock_level, product_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getCategory());
            stmt.setInt(3, product.getStockQuantity());
            stmt.setString(4, product.getSupplier());
            stmt.setDouble(5, product.getPrice());
            stmt.setInt(6, product.getMinStockLevel());
            stmt.setInt(7, productId);

            stmt.executeUpdate();
            System.out.println("✅ Enclosure added to Enclosures and Products.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to insert enclosure: " + e.getMessage());
        }
    }

    // ───────────────────────────────────────────────────────
    // UPDATE
    // ───────────────────────────────────────────────────────

    /**
     * Updates an existing enclosure in both the Enclosures and Products tables.
     *
     * @param product updated product object
     */
    public static void updateEnclosure(Product product) {
        ProductDAO.updateProduct(product);

        String sql = """
            UPDATE Enclosures
            SET product_name = ?, category = ?, stock_quantity = ?, 
                supplier = ?, price = ?, min_stock_level = ?
            WHERE product_id = ?
        """;

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

    // ───────────────────────────────────────────────────────
    // DELETE
    // ───────────────────────────────────────────────────────

    /**
     * Deletes the enclosure with the given product ID from both Enclosures and Products.
     *
     * @param productId product ID to delete
     */
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
