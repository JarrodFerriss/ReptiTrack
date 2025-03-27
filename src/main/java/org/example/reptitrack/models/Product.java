package org.example.reptitrack.models;

/**
 * Represents an inventory product in the ReptiTrack system.
 * Includes product ID, name, quantity, supplier name, and price.
 */
public class Product {
    private int id;               // Maps to animal_id, product_id, etc.
    private String productName;   // Maps to product_name
    private String category;      // Optional, maps to category
    private int stockQuantity;    // Maps to stock_quantity
    private String supplier;
    private double price;

    public Product(int id, String productName, String category, int stockQuantity, String supplier, double price) {
        this.id = id;
        this.productName = productName;
        this.category = category;
        this.stockQuantity = stockQuantity;
        this.supplier = supplier;
        this.price = price;
    }

    // Getters and setters
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
}
