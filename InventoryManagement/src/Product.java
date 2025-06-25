public class Product {
    private int id;
    private String name;
    private int quantity;
    private double price;

    // Constructors, getters, setters
    public Product() {}
    public Product(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
    public Product(int id, String name, int quantity, double price) {
        this(name, quantity, price);
        this.id = id;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setPrice(double price) { this.price = price; }
}
