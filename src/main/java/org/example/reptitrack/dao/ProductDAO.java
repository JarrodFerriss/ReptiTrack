package org.example.reptitrack.dao;

import org.example.reptitrack.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for performing CRUD operations on the Products table.
 * <p>
 * This class is used by all category DAO classes to ensure that each
 * product has a matching entry in the central Products table.
 * </p>
 *
 * @author Jarrod
 * @since 2025-04-06
 */
public class ProductDAO {

    // ─────────────────────────────────────────────────────────────
    // READ
    // ─────────────────────────────────────────────────────────────

    /**
     * Retrieves all products from the Products table.
     *
     * @return list of Product objects
     */
    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Products";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("category"),
                        rs.getInt("stock_quantity"),
                        rs.getString("supplier"),
                        rs.getDouble("price"),
                        rs.getInt("min_stock_level")
                ));
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch products: " + e.getMessage());
        }

        return products;
    }

    /**
     * Retrieves the stock quantity for a specific product.
     *
     * @param productId the ID of the product
     * @return stock quantity or 0 if not found
     */
    public static int getQuantityById(int productId) {
        String query = "SELECT stock_quantity FROM Products WHERE product_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("stock_quantity");
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to get quantity: " + e.getMessage());
        }

        return 0;
    }

    // ─────────────────────────────────────────────────────────────
    // CREATE
    // ─────────────────────────────────────────────────────────────

    /**
     * Inserts a new product into the Products table.
     *
     * @param product the product to insert
     */
    public static void insertProduct(Product product) {
        String sql = """
            INSERT INTO Products (product_name, category, stock_quantity, 
                                  supplier, price, min_stock_level)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

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
            System.err.println("❌ Failed to insert product: " + e.getMessage());
        }
    }

    /**
     * Inserts a product and returns its generated product ID.
     *
     * @param product the product to insert
     * @return generated product_id or -1 if failed
     */
    public static int insertProductAndReturnId(Product product) {
        String sql = """
            INSERT INTO Products (product_name, category, stock_quantity, 
                                  supplier, price, min_stock_level)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getCategory());
            stmt.setInt(3, product.getStockQuantity());
            stmt.setString(4, product.getSupplier());
            stmt.setDouble(5, product.getPrice());
            stmt.setInt(6, product.getMinStockLevel());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("✅ Product inserted with ID: " + id);
                return id;
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to insert and fetch product ID: " + e.getMessage());
        }

        return -1;
    }

    // ─────────────────────────────────────────────────────────────
    // UPDATE
    // ─────────────────────────────────────────────────────────────

    /**
     * Updates an existing product's details in the Products table.
     *
     * @param product the updated product object
     */
    public static void updateProduct(Product product) {
        String sql = """
            UPDATE Products 
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
            System.out.println("✅ Product updated in Products table.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to update product: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────────────────────────────

    /**
     * Deletes a product by its product ID from the Products table.
     *
     * @param productId the ID of the product to delete
     */
    public static void deleteProductById(int productId) {
        String sql = "DELETE FROM Products WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            stmt.executeUpdate();
            System.out.println("🗑️ Product deleted from Products table.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to delete product: " + e.getMessage());
        }
    }
}
