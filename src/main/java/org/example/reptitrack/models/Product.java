package org.example.reptitrack.models;

import org.example.reptitrack.dao.ProductDAO;

/**
 * Represents an inventory product in the ReptiTrack system.
 * Includes product ID, name, quantity, supplier name, and price.
 */
public class Product {
    private int id;
    private String productName;
    private String category;
    private int stockQuantity;
    private String supplier;
    private double price;
    private int minStockLevel;

    public Product(int id, String productName, String category, int stockQuantity, String supplier, double price, int minStockLevel) {
        this.id = id;
        this.productName = productName;
        this.category = category;
        this.stockQuantity = stockQuantity;
        this.supplier = supplier;
        this.price = price;
        this.minStockLevel = minStockLevel;
    }

    public int getStockQuantityInDB() {
        return ProductDAO.getQuantityById(this.id);
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

    public int getMinStockLevel() {
        return minStockLevel;
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

    public void setMinStockLevel(int minStockLevel) {
        this.minStockLevel = minStockLevel;
    }
}
