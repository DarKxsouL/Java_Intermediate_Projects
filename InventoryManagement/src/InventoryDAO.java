import java.sql.*;
import java.util.*;

public class InventoryDAO {
    private Connection conn = DatabaseConnection.getConnection();

    public void addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products (name, quantity, price) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, product.getName());
        ps.setInt(2, product.getQuantity());
        ps.setDouble(3, product.getPrice());
        ps.executeUpdate();
    }

    public void updateProduct(Product product) throws SQLException {
        String sql = "UPDATE products SET name=?, quantity=?, price=? WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, product.getName());
        ps.setInt(2, product.getQuantity());
        ps.setDouble(3, product.getPrice());
        ps.setInt(4, product.getId());
        ps.executeUpdate();
    }

    public void deleteProduct(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    public List<Product> getAllProducts() throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            list.add(new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getDouble("price")
            ));
        }
        return list;
    }

    public void printReport() throws SQLException {
        List<Product> list = getAllProducts();
        System.out.printf("%-5s %-20s %-10s %-10s%n", "ID", "Name", "Qty", "Price");
        for (Product p : list) {
            System.out.printf("%-5d %-20s %-10d %-10.2f%n",
                    p.getId(), p.getName(), p.getQuantity(), p.getPrice());
        }
    }
}
