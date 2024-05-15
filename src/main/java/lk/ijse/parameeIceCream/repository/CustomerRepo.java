package lk.ijse.parameeIceCream.repository;

import com.mysql.cj.xdevapi.PreparableStatement;
import lk.ijse.parameeIceCream.db.DbConnection;
import lk.ijse.parameeIceCream.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepo {
    public static boolean save(Customer customer) throws SQLException {
        String sql = "INSERT INTO customer VALUES(?, ?, ?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, customer.getId());
        pstm.setObject(2, customer.getName());
        pstm.setObject(3, customer.getNic());
        pstm.setObject(4, customer.getAddress());
        pstm.setObject(5, customer.getEmail());
        pstm.setObject(6, customer.getTel());

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Customer customer) throws SQLException {
        String sql = "UPDATE customer SET customerId = ?, name = ?, address = ?, tel = ?, email = ? WHERE nic = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, customer.getId());
        pstm.setObject(2, customer.getName());
        pstm.setObject(3, customer.getAddress());
        pstm.setObject(4, customer.getEmail());
        pstm.setObject(5, customer.getTel());
        pstm.setObject(6, customer.getNic());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String nic) throws SQLException {
        String sql = "DELETE FROM customer WHERE nic = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, nic);

        return pstm.executeUpdate() > 0;
    }

    public static Customer searchByNic(String nic) throws SQLException {
        String sql = "SELECT * FROM customer WHERE nic = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, nic);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {

            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            nic = resultSet.getString(3);
            String address = resultSet.getString(4);
            String email = resultSet.getString(5);
            String tel = resultSet.getString(6);

            Customer customer = new Customer(id, name, nic, address, email, tel);

            return customer;
        }

        return null;
    }

    public static List<Customer> getAll() throws SQLException {
        String sql = "SELECT * FROM customer";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Customer> cusList = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String nic = resultSet.getString(3);
            String address = resultSet.getString(4);
            String email = resultSet.getString(5);
            String tel = resultSet.getString(6);

            Customer customer = new Customer(id, name, nic, address, email, tel);
            cusList.add(customer);
        }
        return cusList;
    }

    public static Customer searchByTel(String tel) throws SQLException {
        String sql = "SELECT * FROM customer WHERE tel = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, tel);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String cId = resultSet.getString(1);
            String name = resultSet.getString(2);
            String nic = resultSet.getString(3);
            String address = resultSet.getString(4);
            String email = resultSet.getString(5);
            String cTel = resultSet.getString(6);

            Customer customer = new Customer(cId, name, nic, address, email, cTel);

            return customer;
        }

        return null;
    }

    public static List<String> getTels() throws SQLException {
        String sql = "SELECT tel FROM customer";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        List<String> telList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            String tel = resultSet.getString(1);
            telList.add(tel);
        }
        return telList;
    }
}
