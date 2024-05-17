package lk.ijse.parameeIceCream.repository;

import lk.ijse.parameeIceCream.db.DbConnection;
import lk.ijse.parameeIceCream.model.Customer;
import lk.ijse.parameeIceCream.model.Employee;
import lk.ijse.parameeIceCream.model.OrderDetail;
import lk.ijse.parameeIceCream.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepo {

    public static boolean save(Product product) throws SQLException {
        String sql = "INSERT INTO product VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, product.getId());
        pstm.setObject(2, product.getName());
        pstm.setObject(3, product.getCategory());
        pstm.setObject(4, product.getDescription());
        pstm.setObject(5, product.getQtyAvailable());
        pstm.setObject(6, product.getUnitPrice());
        pstm.setObject(7, product.getDepartmentId());
        pstm.setObject(8, product.getPath());

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Product product) throws SQLException {
        String sql = "UPDATE product SET  name = ?, category = ?, description = ?, qtyAvailable = ?, unitPrice = ?, departmentId = ?, path = ? WHERE productId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setObject(1, product.getName());
        pstm.setObject(2, product.getCategory());
        pstm.setObject(3, product.getDescription());
        pstm.setObject(4, product.getQtyAvailable());
        pstm.setObject(5, product.getUnitPrice());
        pstm.setObject(6, product.getDepartmentId());
        pstm.setObject(7, product.getPath());
        pstm.setObject(8, product.getId());


        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String name) throws SQLException {
        String sql = "DELETE FROM product WHERE name = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, name);

        return pstm.executeUpdate() > 0;
    }

    public static List<Product> getAll() throws SQLException {
        String sql = "SELECT * FROM product";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Product> proList = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String category = resultSet.getString(3);
            String description = resultSet.getString(4);
            int qtyAvailable = Integer.parseInt(resultSet.getString(5));
            double unitPrice = Double.parseDouble(resultSet.getString(6));
            String departmentId = resultSet.getString(7);
            String path = resultSet.getString(8);

            Product product = new Product(id, name, category, description, qtyAvailable, unitPrice, departmentId, path);
            proList.add(product);
        }
        return proList;
    }

    public static Product searchByName(String name) throws SQLException {
        String sql = "SELECT * FROM product WHERE name = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, name);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String pId = resultSet.getString(1);
            String pName = resultSet.getString(2);
            String category = resultSet.getString(3);
            String description = resultSet.getString(4);
            int qtyAvailable = Integer.parseInt(resultSet.getString(5));
            double unitPrice = Double.parseDouble(resultSet.getString(6));
            String departmentId = resultSet.getString(7);
            String path = resultSet.getString(8);

            Product product = new Product(pId, pName, category, description, qtyAvailable, unitPrice, departmentId, path);

            return product;
        }

        return null;
    }

    public static Product searchById(String id) throws SQLException {
        String sql = "SELECT * FROM product WHERE productId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String pId = resultSet.getString(1);
            String pName = resultSet.getString(2);
            String category = resultSet.getString(3);
            String description = resultSet.getString(4);
            int qtyAvailable = Integer.parseInt(resultSet.getString(5));
            double unitPrice = Double.parseDouble(resultSet.getString(6));
            String departmentId = resultSet.getString(7);
            String path = resultSet.getString(8);

            Product product = new Product(pId, pName, category, description, qtyAvailable, unitPrice, departmentId, path);

            return product;
        }

        return null;
    }

    public static List<String> getName() throws SQLException {
        String sql = "SELECT name FROM product";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        List<String> nameList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            String name = resultSet.getString(1);
            nameList.add(name);
        }
        return nameList;
    }

    public static boolean update(List<OrderDetail> isList) throws SQLException {
        for (OrderDetail is : isList) {
            System.out.println("qtyUpdate Item");
            boolean isUpdateQty = updateQty(is.getProductId(), is.getQty());
            if(!isUpdateQty) {
                return false;
            }
        }
        return true;
    }

    private static boolean updateQty(String Code, int qty) throws SQLException {
        String sql = "UPDATE product SET qtyAvailable = qtyAvailable - ? WHERE productId = ?";
        System.out.println("update Item QTY");
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setInt(1, qty);
        pstm.setString(2, Code);

        return pstm.executeUpdate() > 0;
    }

}
