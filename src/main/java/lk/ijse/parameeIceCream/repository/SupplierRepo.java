package lk.ijse.parameeIceCream.repository;

import lk.ijse.parameeIceCream.db.DbConnection;
import lk.ijse.parameeIceCream.model.Customer;
import lk.ijse.parameeIceCream.model.Department;
import lk.ijse.parameeIceCream.model.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepo {

    public static boolean save(Supplier supplier) throws SQLException {
        String sql = "INSERT INTO supplier VALUES(?, ?, ?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, supplier.getId());
        pstm.setObject(2, supplier.getName());
        pstm.setObject(3, supplier.getNic());
        pstm.setObject(4, supplier.getAddress());
        pstm.setObject(5, supplier.getEmail());
        pstm.setObject(6, supplier.getTel());

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Supplier supplier) throws SQLException {
        String sql = "UPDATE supplier SET supplierId = ?, name = ?, address = ?, email = ?, tel = ? WHERE nic = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, supplier.getId());
        pstm.setObject(2, supplier.getName());
        pstm.setObject(3, supplier.getAddress());
        pstm.setObject(4, supplier.getEmail());
        pstm.setObject(5, supplier.getTel());
        pstm.setObject(6, supplier.getNic());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String nic) throws SQLException {
        String sql = "DELETE FROM supplier WHERE nic = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, nic);

        return pstm.executeUpdate() > 0;
    }

    public static Supplier searchByTel(String tel) throws SQLException {
        String sql = "SELECT * FROM supplier WHERE tel = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, tel);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {

            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String nic = resultSet.getString(3);
            String address = resultSet.getString(4);
            String sTel = resultSet.getString(5);
            String email = resultSet.getString(6);

            Supplier supplier = new Supplier(id, name, nic, address, email, sTel);

            return supplier;
        }

        return null;
    }

    public static List<Supplier> getAll() throws SQLException {
        String sql = "SELECT * FROM supplier";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Supplier> supList = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String nic = resultSet.getString(3);
            String address = resultSet.getString(4);
            String email = resultSet.getString(5);
            String tel = resultSet.getString(6);

            Supplier supplier = new Supplier(id, name, nic, address, email, tel);
            supList.add(supplier);
        }
        return supList;
    }

    public static Supplier searchById(String id) throws SQLException {
        String sql = "SELECT * FROM supplier WHERE supplierId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {

            String sId = resultSet.getString(1);
            String name = resultSet.getString(2);
            String nic = resultSet.getString(3);
            String address = resultSet.getString(4);
            String email = resultSet.getString(5);
            String tel = resultSet.getString(6);

            Supplier supplier = new Supplier(sId, name, nic, address, email, tel);

            return supplier;
        }

        return null;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT supplierId FROM supplier";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        List<String> idList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString(1);
            idList.add(id);
        }
        return idList;
    }

    public static List<String> getTels() throws SQLException {
        String sql = "SELECT tel FROM supplier";
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
