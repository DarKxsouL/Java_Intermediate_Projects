package services;

import models.Product;

import java.util.ArrayList;

public class ProductService {
    private ArrayList<Product> productList = new ArrayList<>();

    public ProductService() {
        productList.add(new Product(1, "Phone", 15999));
        productList.add(new Product(2, "Laptop", 58999));
        productList.add(new Product(3, "Headphones", 2499));
    }

    public ArrayList<Product> getAllProducts() {
        return productList;
    }

    public Product getById(int id) {
        return productList.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }
}
