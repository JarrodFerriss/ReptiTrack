package org.example.reptitrack.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.reptitrack.models.Product;

/**
 * Singleton service class to manage the shopping cart in ReptiTrack.
 * <p>
 * Supports adding, removing, clearing items, and calculating the cart total.
 * Used to persist the cart state across views like MainDashboard and Checkout.
 * </p>
 *
 * @author Jarrod
 * @since 2025-04-06
 */
public class CartService {

    // Singleton instance
    private static CartService instance;

    // Observable list of items in the cart
    private final ObservableList<Product> cartItems = FXCollections.observableArrayList();

    /**
     * Private constructor to enforce singleton pattern.
     */
    private CartService() {}

    /**
     * Gets the singleton instance of CartService.
     *
     * @return shared instance of CartService
     */
    public static CartService getInstance() {
        if (instance == null) {
            instance = new CartService();
        }
        return instance;
    }

    /**
     * Returns the current cart item list.
     *
     * @return observable list of products in the cart
     */
    public ObservableList<Product> getCartItems() {
        return cartItems;
    }

    /**
     * Adds a product to the cart. If the product already exists,
     * increments the quantity by one instead of duplicating the entry.
     *
     * @param product the product to add
     */
    public void addItem(Product product) {
        for (Product item : cartItems) {
            if (item.getId() == product.getId()) {
                item.setStockQuantity(item.getStockQuantity() + 1);
                return;
            }
        }

        // Add new product with quantity 1
        cartItems.add(new Product(
                product.getId(),
                product.getProductName(),
                product.getCategory(),
                1,
                product.getSupplier(),
                product.getPrice(),
                product.getMinStockLevel()
        ));
    }

    /**
     * Removes the specified product from the cart.
     *
     * @param product the product to remove
     */
    public void removeItem(Product product) {
        cartItems.remove(product);
    }

    /**
     * Clears all items from the cart.
     */
    public void clearCart() {
        cartItems.clear();
    }

    /**
     * Calculates the total cost of the cart.
     *
     * @return total value (price × quantity for each product)
     */
    public double getCartTotal() {
        return cartItems.stream()
                .mapToDouble(p -> p.getPrice() * p.getStockQuantity())
                .sum();
    }

    /**
     * Checks if the cart is currently empty.
     *
     * @return true if cart has no items, false otherwise
     */
    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
}
