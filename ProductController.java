package controller;

import model.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductController {
    private ArrayList<Product> products;
    private int nextId;

    public ProductController() {
        products = new ArrayList<>();
        nextId = 1;
    }

    public void addProduct(String name, String description, double price, int quantity) {
        Product product = new Product(nextId++, name, description, price, quantity, true);
        products.add(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Product> searchProducts(String query) {
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(query.toLowerCase()) ||
                             p.getDescription().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void toggleProductStatus(int id) {
        Product product = findProductById(id);
        if (product != null) {
            product.setStatus(!product.getStatus()); // Toggle the status
        }
    }

    private Product findProductById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }
}
