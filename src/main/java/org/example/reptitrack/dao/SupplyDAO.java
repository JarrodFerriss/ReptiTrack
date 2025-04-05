package org.example.reptitrack.dao;

import org.example.reptitrack.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplyDAO {

    public static List<Product> getAllSupplies() {
        List<Product> supplies = new ArrayList<>();
        String sql = "SELECT * FROM Supplies";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("supply_id"),
                        rs.getString("product_name"),
                        "Supplies",
                        rs.getInt("stock_quantity"),
                        rs.getString("supplier"),
                        rs.getDouble("price"),
                        rs.getInt("min_stock_level")
                );
                supplies.add(product);
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch supplies: " + e.getMessage());
        }

        return supplies;
    }

    public static void insertSupply(Product product) {
        String sql = "INSERT INTO Supplies (product_name, category, stock_quantity, supplier, price, min_stock_level) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getCategory());
            stmt.setInt(3, product.getStockQuantity());
            stmt.setString(4, product.getSupplier());
            stmt.setDouble(5, product.getPrice());
            stmt.setInt(6, product.getMinStockLevel());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    product.setId(keys.getInt(1));
                    product.setCategory("Supplies");
                    ProductDAO.insertProduct(product);
                }
            }

            System.out.println("✅ Supply added and synced to Products.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to insert supply: " + e.getMessage());
        }
    }

    public static void updateSupply(Product product) {
        String sql = "UPDATE Supplies SET product_name = ?, stock_quantity = ?, supplier = ?, price = ?, min_stock_level = ? WHERE supply_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getProductName());
            stmt.setInt(2, product.getStockQuantity());
            stmt.setString(3, product.getSupplier());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getMinStockLevel());
            stmt.setInt(6, product.getId());

            stmt.executeUpdate();
            ProductDAO.updateProduct(product);

            System.out.println("✅ Supply updated and synced to Products.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to update supply: " + e.getMessage());
        }
    }

    public static void deleteSupply(int id) {
        String sql = "DELETE FROM Supplies WHERE supply_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            ProductDAO.deleteProductById(id);

            System.out.println("🗑️ Supply deleted and removed from Products.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to delete supply: " + e.getMessage());
        }
    }
}
