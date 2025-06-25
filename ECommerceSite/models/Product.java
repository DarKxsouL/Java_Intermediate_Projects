package models;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {
    private int id;
    private String name;
    private double price;
    private ArrayList<Review> reviews = new ArrayList<>();

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public double getAverageRating() {
        if (reviews.isEmpty()) return 0;
        return reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
    }
}
