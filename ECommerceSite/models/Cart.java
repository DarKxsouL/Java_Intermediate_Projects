package models;

import java.io.Serializable;
import java.util.HashMap;

public class Cart implements Serializable {
    private HashMap<Product, Integer> items = new HashMap<>();

    public void addProduct(Product product, int qty) {
        items.put(product, items.getOrDefault(product, 0) + qty);
    }

    public void viewCart() {
        System.out.println("Cart Contents:");
        for (Product p : items.keySet()) {
            System.out.println(p.getName() + " x" + items.get(p));
        }
    }

    public HashMap<Product, Integer> getItems() {
        return items;
    }

    public double getTotal() {
        return items.entrySet().stream().mapToDouble(e -> e.getKey().getPrice() * e.getValue()).sum();
    }
}
