package org.example.reptitrack.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.reptitrack.models.Product;

public class CartService {
    private static CartService instance;
    private final ObservableList<Product> cartItems = FXCollections.observableArrayList();

    private CartService() {}

    public static CartService getInstance() {
        if (instance == null) {
            instance = new CartService();
        }
        return instance;
    }

    public ObservableList<Product> getCartItems() {
        return cartItems;
    }

    public void addItem(Product product) {
        for (Product item : cartItems) {
            if (item.getId() == product.getId()) {
                item.setStockQuantity(item.getStockQuantity() + 1);
                return;
            }
        }
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

    public void removeItem(Product product) {
        cartItems.remove(product);
    }

    public void clearCart() {
        cartItems.clear();
    }

    public double getCartTotal() {
        return cartItems.stream()
                .mapToDouble(p -> p.getPrice() * p.getStockQuantity())
                .sum();
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
}
