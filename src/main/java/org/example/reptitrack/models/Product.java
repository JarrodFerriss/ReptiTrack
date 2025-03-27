package org.example.reptitrack.models;

/**
 * Represents an inventory product in the ReptiTrack system.
 * Includes product ID, name, quantity, supplier name, and price.
 */
public class Product {
    private int id;
    private String name;
    private int quantity;
    private String supplier;
    private double price;

    public Product(int id, String name, int quantity, String supplier, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.supplier = supplier;
        this.price = price;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}