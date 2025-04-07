package org.example.reptitrack.dao;

import org.example.reptitrack.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for managing records in the Animals table.
 * <p>
 * This class provides full CRUD operations and ensures all changes
 * are reflected in both the Animals and Products tables via a shared product_id.
 * </p>
 *
 * @author Jarrod
 * @since 2025-04-06
 */
public class AnimalDAO {

    // ───────────────────────────────────────────────────────
    // READ
    // ───────────────────────────────────────────────────────

    /**
     * Fetches all animals and joins with the Products table.
     *
     * @return list of Product objects representing animals
     */
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
                animals.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        "Animals",
                        rs.getInt("stock_quantity"),
                        rs.getString("supplier"),
                        rs.getDouble("price"),
                        rs.getInt("min_stock_level")
                ));
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch animals: " + e.getMessage());
        }

        return animals;
    }

    // ───────────────────────────────────────────────────────
    // CREATE
    // ───────────────────────────────────────────────────────

    /**
     * Inserts a new animal into the Animals and Products tables.
     *
     * @param product product object representing the animal
     */
    public static void insertAnimal(Product product) {
        int productId = ProductDAO.insertProductAndReturnId(product);

        String sql = """
            INSERT INTO Animals (product_name, category, stock_quantity, 
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
            System.out.println("✅ Animal added to Animals and Products (linked by product_id = " + productId + ")");

        } catch (SQLException e) {
            System.err.println("❌ Failed to insert animal: " + e.getMessage());
        }
    }

    // ───────────────────────────────────────────────────────
    // UPDATE
    // ───────────────────────────────────────────────────────

    /**
     * Updates an existing animal entry in both Animals and Products tables.
     *
     * @param product updated product object
     */
    public static void updateAnimal(Product product) {
        ProductDAO.updateProduct(product);

        String sql = """
            UPDATE Animals
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
            System.out.println("✅ Animal updated in Animals and Products.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to update animal: " + e.getMessage());
        }
    }

    // ───────────────────────────────────────────────────────
    // DELETE
    // ───────────────────────────────────────────────────────

    /**
     * Deletes the specified animal from both the Animals and Products tables.
     *
     * @param productId the shared product_id to delete
     */
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
