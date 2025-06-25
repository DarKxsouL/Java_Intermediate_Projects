package models;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class Order implements Serializable {
    private Date orderDate;
    private Map<Product, Integer> items;
    private double totalAmount;

    public Order(Map<Product, Integer> items, double totalAmount) {
        this.items = items;
        this.totalAmount = totalAmount;
        this.orderDate = new Date();
    }

    public String toString() {
        return "Ordered on: " + orderDate + " | Total: ₹" + totalAmount;
    }
}
