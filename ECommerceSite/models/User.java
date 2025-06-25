package models;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String username, password;
    private ArrayList<Order> orders = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean checkPassword(String pass) {
        return this.password.equals(pass);
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }
}
