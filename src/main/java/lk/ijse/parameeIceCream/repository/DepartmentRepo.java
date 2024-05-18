package lk.ijse.parameeIceCream.repository;

import lk.ijse.parameeIceCream.db.DbConnection;
import lk.ijse.parameeIceCream.model.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentRepo {
    public static boolean save(Department department) throws SQLException {
        String sql = "INSERT INTO department VALUES(?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, department.getId());
        pstm.setObject(2, department.getName());
        pstm.setObject(3, department.getDescription());
        pstm.setObject(4, department.getNumOfEmp());

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Department department) throws SQLException {
        String sql = "UPDATE department SET departmentId = ?, description = ?, numberOfEmployees = ? WHERE name = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, department.getId());
        pstm.setObject(2, department.getDescription());
        pstm.setObject(3, department.getNumOfEmp());
        pstm.setObject(4, department.getName());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String name) throws SQLException {
        String sql = "DELETE FROM department WHERE name = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, name);

        return pstm.executeUpdate() > 0;
    }

    public static List<Department> getAll() throws SQLException {
        String sql = "SELECT * FROM department";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Department> depList = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String description = resultSet.getString(3);
            int emp = Integer.parseInt(resultSet.getString(4));

            Department department = new Department(id, name, description, emp);
            depList.add(department);
        }
        return depList;
    }

    public static Department searchByName(String name) throws SQLException {
        String sql = "SELECT * FROM department WHERE name = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, name);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {

            String dId = resultSet.getString(1);
            String dName = resultSet.getString(2);
            String description = resultSet.getString(3);
            int emp = Integer.parseInt(resultSet.getString(4));

            Department department = new Department(dId, dName, description, emp);

            return department;
        }

        return null;
    }

    public static Department searchById(String id) throws SQLException {
        String sql = "SELECT * FROM department WHERE departmentId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {

            String dId = resultSet.getString(1);
            String dName = resultSet.getString(2);
            String description = resultSet.getString(3);
            int emp = Integer.parseInt(resultSet.getString(4));

            Department department = new Department(dId, dName, description, emp);

            return department;
        }

        return null;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT departmentId FROM department";
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


    public static List<String> getName() throws SQLException {
        String sql = "SELECT name FROM department";
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
}
