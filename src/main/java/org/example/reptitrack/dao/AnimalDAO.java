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

    // ─────────────────────────────────────────────────────────────
    // READ
    // ─────────────────────────────────────────────────────────────

    /**
     * Fetches all animal products.
     */
    public static List<Product> getAllAnimals() {
        List<Product> animals = new ArrayList<>();
        String sql = "SELECT * FROM Animals";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("animal_id"),
                        rs.getString("product_name"),
                        "Animals", // Explicit category
                        rs.getInt("stock_quantity"),
                        rs.getString("supplier"),
                        rs.getDouble("price")
                );
                animals.add(product);
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch animals: " + e.getMessage());
        }

        return animals;
    }

    // ─────────────────────────────────────────────────────────────
    // CREATE
    // ─────────────────────────────────────────────────────────────

    /**
     * Inserts a new animal product and syncs with Products.
     */
    public static void insertAnimal(Product product) {
        String sql = "INSERT INTO Animals (product_name, category, stock_quantity, supplier, price) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getCategory());
            stmt.setInt(3, product.getStockQuantity());
            stmt.setString(4, product.getSupplier());
            stmt.setDouble(5, product.getPrice());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int animalId = generatedKeys.getInt(1);
                    product.setId(animalId);
                    product.setCategory("Animals");
                    ProductDAO.insertProduct(product); // Sync
                }
            }

            System.out.println("✅ Animal added to Animals and Products.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to insert animal: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────
    // UPDATE
    // ─────────────────────────────────────────────────────────────

    /**
     * Updates an animal entry and its synced Product entry.
     */
    public static void updateAnimal(Product product) {
        String sql = "UPDATE Animals SET product_name = ?, stock_quantity = ?, supplier = ?, price = ? WHERE animal_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getProductName());
            stmt.setInt(2, product.getStockQuantity());
            stmt.setString(3, product.getSupplier());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getId());

            stmt.executeUpdate();
            ProductDAO.updateProduct(product); // Sync

            System.out.println("✅ Animal updated in Animals and Products.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to update animal: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────────────────────────────

    /**
     * Deletes an animal by ID and removes from Products.
     */
    public static void deleteAnimal(int animalId) {
        String sql = "DELETE FROM Animals WHERE animal_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, animalId);
            stmt.executeUpdate();

            ProductDAO.deleteProductById(animalId); // Sync
            System.out.println("🗑️ Animal deleted from Animals and Products.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to delete animal: " + e.getMessage());
        }
    }
}
