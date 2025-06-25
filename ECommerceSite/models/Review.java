package models;

import java.io.Serializable;

public class Review implements Serializable {
    private String username;
    private String comment;
    private int rating;

    public Review(String username, String comment, int rating) {
        this.username = username;
        this.comment = comment;
        this.rating = rating;
    }

    public int getRating() { return rating; }

    public String toString() {
        return username + " rated " + rating + "/5 - " + comment;
    }
}
