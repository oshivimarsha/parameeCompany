package lk.ijse.parameeIceCream.repository;

import lk.ijse.parameeIceCream.db.DbConnection;
import lk.ijse.parameeIceCream.model.Customer;
import lk.ijse.parameeIceCream.model.Employee;
import lk.ijse.parameeIceCream.model.Machine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MachineRepo {
    public static boolean save(Machine machine) throws SQLException {
        String sql = "INSERT INTO machine VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, machine.getId());
        pstm.setObject(2, machine.getType());
        pstm.setObject(3, machine.getSerialNumber());
        pstm.setObject(4, machine.getPurchaseDate());
        pstm.setObject(5, machine.getPurchaseCost());
        pstm.setObject(6, machine.getMaintainCost());
        pstm.setObject(7, machine.getDepartmentId());
        pstm.setObject(8, machine.getPath());

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Machine machine) throws SQLException {
        String sql = "UPDATE machine SET machineId = ?, type = ?, purchaseDate = ?, purchaseCost = ?, maintenanceCost = ?, departmentId = ?, path = ? WHERE serialNumber = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, machine.getId());
        pstm.setObject(2, machine.getType());
        pstm.setObject(3, machine.getPurchaseDate());
        pstm.setObject(4, machine.getPurchaseCost());
        pstm.setObject(5, machine.getMaintainCost());
        pstm.setObject(6, machine.getDepartmentId());
        pstm.setObject(7, machine.getPath());
        pstm.setObject(8, machine.getSerialNumber());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String serialNumber) throws SQLException {
        String sql = "DELETE FROM machine WHERE serialNumber = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, serialNumber);

        return pstm.executeUpdate() > 0;
    }

    public static List<Machine> getAll() throws SQLException {
        String sql = "SELECT * FROM machine";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Machine> masList = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String type = resultSet.getString(2);
            int serialNumber = Integer.parseInt(resultSet.getString(3));
            Date purchaseDate = Date.valueOf(resultSet.getString(4));
            double purchaseCost = Double.parseDouble(resultSet.getString(5));
            double maintainCost = Double.parseDouble(resultSet.getString(6));
            String departmentId = resultSet.getString(7);
            String path = resultSet.getString(8);

            Machine machine = new Machine(id, type, serialNumber, purchaseDate, purchaseCost, maintainCost, departmentId, path);
            masList.add(machine);
        }
        return masList;
    }

    public static Machine searchByNum(String serialNumber) throws SQLException {
        String sql = "SELECT * FROM machine WHERE serialNumber = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, serialNumber);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {

            String id = resultSet.getString(1);
            String type = resultSet.getString(2);
            int mSerialNumber = Integer.parseInt(resultSet.getString(3));
            Date purchaseDate = Date.valueOf(resultSet.getString(4));
            double purchaseCost = Double.parseDouble(resultSet.getString(5));
            double maintainCost = Double.parseDouble(resultSet.getString(6));
            String departmentId = resultSet.getString(7);
            String path = resultSet.getString(8);

            Machine machine = new Machine(id, type, mSerialNumber, purchaseDate, purchaseCost, maintainCost, departmentId, path);

            return machine;
        }

        return null;
    }

    public static List<String> getNum() throws SQLException {
        String sql = "SELECT serialNumber FROM machine";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        List<String> numList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            String num = resultSet.getString(1);
            numList.add(num);
        }
        return numList;
    }
}
