package org.example.reptitrack.models;

import org.example.reptitrack.dao.ProductDAO;

/**
 * Represents a product in the ReptiTrack inventory system.
 * <p>
 * This model includes general product data that applies to all inventory categories,
 * such as product name, quantity, supplier, and pricing.
 * <p>
 * The class also contains a method to fetch live quantity from the database.
 *
 * @author Jarrod
 * @since 2025-04-06
 */
public class Product {

    // ─────────────────────────────────────────────────────────────
    // Fields
    // ─────────────────────────────────────────────────────────────

    private int id;
    private String productName;
    private String category;
    private int stockQuantity;
    private String supplier;
    private double price;
    private int minStockLevel;

    // ─────────────────────────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────────────────────────

    /**
     * Constructs a new Product instance.
     *
     * @param id             Product ID
     * @param productName    Name of the product
     * @param category       Product category (e.g. Animals, Feeders)
     * @param stockQuantity  Current quantity in stock
     * @param supplier       Supplier name
     * @param price          Unit price
     * @param minStockLevel  Minimum stock level before alerting
     */
    public Product(int id, String productName, String category, int stockQuantity, String supplier, double price, int minStockLevel) {
        this.id = id;
        this.productName = productName;
        this.category = category;
        this.stockQuantity = stockQuantity;
        this.supplier = supplier;
        this.price = price;
        this.minStockLevel = minStockLevel;
    }

    // ─────────────────────────────────────────────────────────────
    // Utility Methods
    // ─────────────────────────────────────────────────────────────

    /**
     * Fetches the latest quantity from the database.
     * Useful when real-time syncing with database changes is needed.
     *
     * @return latest quantity from the Products table
     */
    public int getStockQuantityInDB() {
        return ProductDAO.getQuantityById(this.id);
    }

    // ─────────────────────────────────────────────────────────────
    // Getters
    // ─────────────────────────────────────────────────────────────

    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategory() {
        return category;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getSupplier() {
        return supplier;
    }

    public double getPrice() {
        return price;
    }

    public int getMinStockLevel() {
        return minStockLevel;
    }

    // ─────────────────────────────────────────────────────────────
    // Setters
    // ─────────────────────────────────────────────────────────────

    public void setId(int id) {
        this.id = id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setMinStockLevel(int minStockLevel) {
        this.minStockLevel = minStockLevel;
    }
}
