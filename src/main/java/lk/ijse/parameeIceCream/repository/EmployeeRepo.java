package lk.ijse.parameeIceCream.repository;

import lk.ijse.parameeIceCream.db.DbConnection;
import lk.ijse.parameeIceCream.model.Customer;
import lk.ijse.parameeIceCream.model.Department;
import lk.ijse.parameeIceCream.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EmployeeRepo {
    public static boolean save(Employee employee) throws SQLException {
        String sql = "INSERT INTO employee VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, employee.getId());
        pstm.setObject(2, employee.getName());
        pstm.setObject(3, employee.getNic());
        pstm.setObject(4, employee.getAddress());
        pstm.setObject(5, employee.getEmail());
        pstm.setObject(6, employee.getTel());
        pstm.setObject(7, employee.getDob());
        pstm.setObject(8, employee.getRegisterDate());
        pstm.setObject(9, employee.getPosition());
        pstm.setObject(10, employee.getSalary());
        pstm.setObject(11, employee.getDepartmentId());
        pstm.setObject(12, employee.getPath());

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Employee employee) throws SQLException {
        String sql = "UPDATE employee SET employeeId = ?, name = ?, address = ?, email = ?, tel = ?, DOB = ?, registerDate = ?, position = ?, salary = ?, departmentId = ?, path = ? WHERE nic = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, employee.getId());
        pstm.setObject(2, employee.getName());
        pstm.setObject(3, employee.getAddress());
        pstm.setObject(4, employee.getEmail());
        pstm.setObject(5, employee.getTel());
        pstm.setObject(6, employee.getDob());
        pstm.setObject(7, employee.getRegisterDate());
        pstm.setObject(8, employee.getPosition());
        pstm.setObject(9, employee.getSalary());
        pstm.setObject(10, employee.getDepartmentId());
        //pstm.setObject(11, employee.getDepartmentName());
        pstm.setObject(11, employee.getPath());
        pstm.setObject(12, employee.getNic());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String nic) throws SQLException {
        String sql = "DELETE FROM employee WHERE nic = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, nic);

        return pstm.executeUpdate() > 0;
    }

    public static Employee searchByNic(String nic) throws SQLException {
        String sql = "SELECT * FROM employee WHERE nic = ?";

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
            Date dob = Date.valueOf(resultSet.getString(7));
            Date registerDate = Date.valueOf(resultSet.getString(8));
            String position = resultSet.getString(9);
            double salary = Double.parseDouble(resultSet.getString(10));
            String departmentId = resultSet.getString(11);
            String path = resultSet.getString(12);

            Employee employee = new Employee(id, name, nic, address, email, tel, dob, registerDate, position, salary, departmentId, path);

            return employee;
        }

        return null;
    }

    public static List<Employee> getAll() throws SQLException {
        String sql = "SELECT * FROM employee";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Employee> empList = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String nic = resultSet.getString(3);
            String address = resultSet.getString(4);
            String email = resultSet.getString(5);
            String tel = resultSet.getString(6);
            Date dob = Date.valueOf(resultSet.getString(7));
            Date registerDate = Date.valueOf(resultSet.getString(8));
            String position = resultSet.getString(9);
            double salary = Double.parseDouble((resultSet.getString(10)));
            String departmentId = resultSet.getString(11);
            String path = resultSet.getString(12);

            Employee employee = new Employee(id, name, nic, address, email, tel, dob, registerDate, position, salary, departmentId, path);
            empList.add(employee);
        }
        return empList;
    }

    public static List<String> getTels() throws SQLException {
        String sql = "SELECT tel FROM employee";
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

    public static Employee searchByTel(String tel) throws SQLException {
        String sql = "SELECT * FROM employee WHERE tel = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, tel);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {

            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String nic = resultSet.getString(3);
            String address = resultSet.getString(4);
            String email = resultSet.getString(5);
            String eTel = resultSet.getString(6);
            Date dob = Date.valueOf(resultSet.getString(7));
            Date registerDate = Date.valueOf(resultSet.getString(8));
            String position = resultSet.getString(9);
            double salary = Double.parseDouble(resultSet.getString(10));
            String departmentId = resultSet.getString(11);
            String path = resultSet.getString(12);

            Employee employee = new Employee(id, name, nic, address, email, eTel, dob, registerDate, position, salary, departmentId, path);

            return employee;
        }

        return null;
    }

    public static Employee searchById(String id) throws SQLException {
        String sql = "SELECT * FROM employee WHERE employeeId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {

            String eId = resultSet.getString(1);
            String name = resultSet.getString(2);
            String nic = resultSet.getString(3);
            String address = resultSet.getString(4);
            String email = resultSet.getString(5);
            String eTel = resultSet.getString(6);
            Date dob = Date.valueOf(resultSet.getString(7));
            Date registerDate = Date.valueOf(resultSet.getString(8));
            String position = resultSet.getString(9);
            double salary = Double.parseDouble(resultSet.getString(10));
            String departmentId = resultSet.getString(11);
            String path = resultSet.getString(12);

            Employee employee = new Employee(eId, name, nic, address, email, eTel, dob, registerDate, position, salary, departmentId, path);

            return employee;
        }

        return null;
    }
}
