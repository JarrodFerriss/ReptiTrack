package org.example.reptitrack.dao;

import org.example.reptitrack.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for managing the Supplies category table.
 * <p>
 * Handles all CRUD operations and ensures that any changes
 * are mirrored in the Products table using product_id as a foreign key.
 * </p>
 *
 * @author Jarrod
 * @since 2025-04-06
 */
public class SupplyDAO {

    // ───────────────────────────────────────────────────────
    // READ
    // ───────────────────────────────────────────────────────

    /**
     * Retrieves all supply products by joining Supplies and Products tables.
     *
     * @return list of supply product objects
     */
    public static List<Product> getAllSupplies() {
        List<Product> supplies = new ArrayList<>();

        String sql = """
            SELECT s.supply_id, p.product_id, p.product_name, p.category, 
                   p.stock_quantity, p.supplier, p.price, p.min_stock_level
            FROM Supplies s
            JOIN Products p ON s.product_id = p.product_id
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                supplies.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        "Supplies",
                        rs.getInt("stock_quantity"),
                        rs.getString("supplier"),
                        rs.getDouble("price"),
                        rs.getInt("min_stock_level")
                ));
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch supplies: " + e.getMessage());
        }

        return supplies;
    }

    // ───────────────────────────────────────────────────────
    // CREATE
    // ───────────────────────────────────────────────────────

    /**
     * Inserts a new supply into both the Supplies and Products tables.
     *
     * @param product the product data to insert
     */
    public static void insertSupply(Product product) {
        int productId = ProductDAO.insertProductAndReturnId(product);

        String sql = """
            INSERT INTO Supplies (product_name, category, stock_quantity, 
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
            System.out.println("✅ Supply added to Supplies and Products.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to insert supply: " + e.getMessage());
        }
    }

    // ───────────────────────────────────────────────────────
    // UPDATE
    // ───────────────────────────────────────────────────────

    /**
     * Updates a supply product in both the Supplies and Products tables.
     *
     * @param product the updated product data
     */
    public static void updateSupply(Product product) {
        ProductDAO.updateProduct(product);

        String sql = """
            UPDATE Supplies
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
            System.out.println("✅ Supply updated in Supplies and Products.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to update supply: " + e.getMessage());
        }
    }

    // ───────────────────────────────────────────────────────
    // DELETE
    // ───────────────────────────────────────────────────────

    /**
     * Deletes a supply product from both the Supplies and Products tables.
     *
     * @param productId the ID of the product to delete
     */
    public static void deleteSupply(int productId) {
        String sql = "DELETE FROM Supplies WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            stmt.executeUpdate();

            ProductDAO.deleteProductById(productId);
            System.out.println("🗑️ Supply deleted from Supplies and Products.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to delete supply: " + e.getMessage());
        }
    }
}
