import java.util.*;

public class InventoryApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        InventoryDAO dao = new InventoryDAO();

        while (true) {
            System.out.println("\n--- Inventory Management System ---");
            System.out.println("1. Add Product");
            System.out.println("2. Update Product");
            System.out.println("3. Delete Product");
            System.out.println("4. View All Products");
            System.out.println("5. Generate Report");
            System.out.println("6. Exit");
            System.out.print("Choose: ");

            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1 -> {
                        System.out.print("Product Name: ");
                        String name = sc.nextLine();
                        System.out.print("Quantity: ");
                        int qty = sc.nextInt();
                        System.out.print("Price: ");
                        double price = sc.nextDouble();
                        dao.addProduct(new Product(name, qty, price));
                        System.out.println("Product added.");
                    }
                    case 2 -> {
                        System.out.print("Product ID: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Updated Name: ");
                        String name = sc.nextLine();
                        System.out.print("Updated Quantity: ");
                        int qty = sc.nextInt();
                        System.out.print("Updated Price: ");
                        double price = sc.nextDouble();
                        dao.updateProduct(new Product(id, name, qty, price));
                        System.out.println("Product updated.");
                    }
                    case 3 -> {
                        System.out.print("Product ID to delete: ");
                        int id = sc.nextInt();
                        dao.deleteProduct(id);
                        System.out.println("Product deleted.");
                    }
                    case 4 -> {
                        for (Product p : dao.getAllProducts()) {
                            System.out.println(p.getId() + " - " + p.getName() +
                                    " | Qty: " + p.getQuantity() + " | Price: ₹" + p.getPrice());
                        }
                    }
                    case 5 -> dao.printReport();
                    case 6 -> System.exit(0);
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
