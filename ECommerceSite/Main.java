import models.*;
import services.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AuthService authService = new AuthService();
        ProductService productService = new ProductService();
        User currentUser = null;

        System.out.println("Welcome to Java E-Commerce!");
        while (true) {
            System.out.println("\n1. Register\n2. Login\n3. Exit");
            int ch = sc.nextInt();
            sc.nextLine();
            if (ch == 1) {
                System.out.print("Username: ");
                String u = sc.nextLine();
                System.out.print("Password: ");
                String p = sc.nextLine();
                if (authService.register(u, p)) System.out.println("Registered successfully.");
                else System.out.println("User already exists.");
            } else if (ch == 2) {
                System.out.print("Username: ");
                String u = sc.nextLine();
                System.out.print("Password: ");
                String password = sc.nextLine();
                currentUser = authService.login(u, password);
                if (currentUser != null) {
                    System.out.println("Welcome " + currentUser.getUsername());
                    Cart cart = new Cart();

                    while (true) {
                        System.out.println("\n1. Browse Products\n2. View Cart\n3. Checkout\n4. View Orders\n5. Logout");
                        int op = sc.nextInt();
                        sc.nextLine();

                        if (op == 1) {
                            for (Product prod : productService.getAllProducts()) {
                                System.out.printf("%d. %s - ₹%.2f | Avg Rating: %.1f\n", prod.getId(), prod.getName(), prod.getPrice(), prod.getAverageRating());
                            }
                            System.out.print("Enter product ID to add to cart: ");
                            int pid = sc.nextInt();
                            System.out.print("Quantity: ");
                            int qty = sc.nextInt();
                            Product p = productService.getById(pid);
                            if (p != null) cart.addProduct(p, qty);
                        }

                        else if (op == 2) cart.viewCart();

                        else if (op == 3) {
                            double total = cart.getTotal();
                            System.out.println("Total amount: ₹" + total);
                            System.out.print("Confirm Order? (y/n): ");
                            if (sc.next().equalsIgnoreCase("y")) {
                                Order order = new Order(cart.getItems(), total);
                                currentUser.addOrder(order);
                                cart = new Cart(); // clear cart
                                System.out.println("Order placed!");
                            }
                        }

                        else if (op == 4) {
                            for (Order o : currentUser.getOrders()) {
                                System.out.println(o);
                            }
                        }

                        else if (op == 5) break;
                    }
                } else {
                    System.out.println("Login failed.");
                }
            } else {
                break;
            }
        }
        sc.close();
    }
}
