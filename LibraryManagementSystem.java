// LibraryManagementSystem.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

// Book class
class Book implements Serializable {
    private String isbn, title, author;
    private boolean isBorrowed;

    public Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.isBorrowed = false;
    }

    public String getISBN() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isBorrowed() { return isBorrowed; }
    public void borrow() { isBorrowed = true; }
    public void returnBook() { isBorrowed = false; }
    public String getStatus() { return isBorrowed ? "Borrowed" : "Available"; }
}

// User class
class User implements Serializable {
    private String username, password, role;
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}

// Library class
class Library {
    private ArrayList<Book> books;
    private ArrayList<User> users;
    private final String BOOK_FILE = "books.ser";
    private final String USER_FILE = "users.ser";

    public Library() {
        books = loadBooks();
        users = loadUsers();
        if (users.isEmpty()) {
            users.add(new User("admin", "admin", "admin"));
            saveUsers();
        }
    }

    public void addBook(Book book) {
        books.add(book);
        saveBooks();
    }

    public void removeBook(String isbn) {
        books.removeIf(b -> b.getISBN().equals(isbn));
        saveBooks();
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public ArrayList<Book> searchByTitle(String title) {
        ArrayList<Book> result = new ArrayList<>();
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(title.toLowerCase())) {
                result.add(b);
            }
        }
        return result;
    }

    public void borrowBook(String isbn) {
        for (Book b : books) {
            if (b.getISBN().equals(isbn) && !b.isBorrowed()) {
                b.borrow();
                break;
            }
        }
        saveBooks();
    }

    public void returnBook(String isbn) {
        for (Book b : books) {
            if (b.getISBN().equals(isbn) && b.isBorrowed()) {
                b.returnBook();
                break;
            }
        }
        saveBooks();
    }

    public User validateLogin(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public boolean createAccount(String username, String password, String role) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return false; // Username exists
            }
        }
        users.add(new User(username, password, role));
        saveUsers();
        return true;
    }

    private void saveBooks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOK_FILE))) {
            oos.writeObject(books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Book> loadBooks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BOOK_FILE))) {
            return (ArrayList<Book>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private ArrayList<User> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_FILE))) {
            return (ArrayList<User>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}

// GUI
public class LibraryManagementSystem {
    private Library library;

    public LibraryManagementSystem() {
        library = new Library();
        showLogin();
    }

    private void showLogin() {
        JFrame frame = new JFrame("Library Login");
        frame.setSize(300, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new GridLayout(5, 1));

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        JButton createAccBtn = new JButton("Create Account");

        panel.add(new JLabel("Username:"));
        panel.add(userField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);
        panel.add(loginBtn);
        panel.add(createAccBtn);
        frame.add(panel);

        loginBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            User u = library.validateLogin(user, pass);
            if (u != null) {
                frame.dispose();
                if (u.getRole().equals("admin")) showAdminPanel();
                else showMemberPanel();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials");
            }
        });

        createAccBtn.addActionListener(e -> {
            String newUser = JOptionPane.showInputDialog("Enter New Username:");
            String newPass = JOptionPane.showInputDialog("Enter Password:");
            String[] roles = {"member", "admin"};
            String role = (String) JOptionPane.showInputDialog(frame, "Select Role:", "Role", JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]);
            if (newUser != null && newPass != null && role != null) {
                boolean created = library.createAccount(newUser, newPass, role);
                if (created) JOptionPane.showMessageDialog(frame, "Account created successfully.");
                else JOptionPane.showMessageDialog(frame, "Username already exists.");
            }
        });

        frame.setVisible(true);
    }

    private void showAdminPanel() {
        JFrame frame = new JFrame("Admin Panel");
        frame.setSize(500, 400);
        JPanel panel = new JPanel(new BorderLayout());

        JTextArea display = new JTextArea();
        JButton add = new JButton("Add Book");
        JButton remove = new JButton("Remove Book");

        add.addActionListener(e -> {
            String isbn = JOptionPane.showInputDialog("Enter ISBN:");
            String title = JOptionPane.showInputDialog("Enter Title:");
            String author = JOptionPane.showInputDialog("Enter Author:");
            if (isbn != null && title != null && author != null)
                library.addBook(new Book(isbn, title, author));
        });

        remove.addActionListener(e -> {
            String isbn = JOptionPane.showInputDialog("Enter ISBN to remove:");
            library.removeBook(isbn);
        });

        JButton refresh = new JButton("Refresh List");
        refresh.addActionListener(e -> {
            display.setText("");
            for (Book b : library.getBooks()) {
                display.append(b.getISBN() + ", " + b.getTitle() + ", " + b.getAuthor() + ", " + b.getStatus() + "\n");
            }
        });

        JPanel top = new JPanel();
        top.add(add); top.add(remove); top.add(refresh);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(display), BorderLayout.CENTER);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void showMemberPanel() {
        JFrame frame = new JFrame("Member Panel");
        frame.setSize(500, 400);
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea display = new JTextArea();

        JTextField searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        JButton borrow = new JButton("Borrow");
        JButton ret = new JButton("Return");

        searchBtn.addActionListener(e -> {
            display.setText("");
            for (Book b : library.searchByTitle(searchField.getText())) {
                display.append(b.getISBN() + ", " + b.getTitle() + ", " + b.getAuthor() + ", " + b.getStatus() + "\n");
            }
        });

        borrow.addActionListener(e -> {
            String isbn = JOptionPane.showInputDialog("Enter ISBN to borrow:");
            library.borrowBook(isbn);
        });

        ret.addActionListener(e -> {
            String isbn = JOptionPane.showInputDialog("Enter ISBN to return:");
            library.returnBook(isbn);
        });

        JPanel top = new JPanel();
        top.add(searchField); top.add(searchBtn); top.add(borrow); top.add(ret);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(display), BorderLayout.CENTER);

        frame.add(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryManagementSystem::new);
    }
}
