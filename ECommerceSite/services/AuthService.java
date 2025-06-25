package services;

import models.User;

import java.util.HashMap;

public class AuthService {
    private HashMap<String, User> users = new HashMap<>();

    public boolean register(String username, String password) {
        if (users.containsKey(username)) return false;
        users.put(username, new User(username, password));
        return true;
    }

    public User login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.checkPassword(password)) return user;
        return null;
    }
}
