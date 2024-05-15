package lk.ijse.parameeIceCream.repository;

import lk.ijse.parameeIceCream.db.DbConnection;
import lk.ijse.parameeIceCream.model.Employee;
import lk.ijse.parameeIceCream.model.Salary;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalaryRepo {

    public static boolean save(Salary salary) throws SQLException {
        String sql = "INSERT INTO salary VALUES(?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, salary.getId());
        pstm.setObject(2, salary.getBasicSalary());
        pstm.setObject(3, salary.getAllowences());
        pstm.setObject(4, salary.getAdvance());

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Salary salary) throws SQLException {
        String sql = "UPDATE salary SET basicSalary = ?, allowences = ?, grossSalary = ?, advance = ? WHERE employeeId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, salary.getId());
        pstm.setObject(2, salary.getBasicSalary());
        pstm.setObject(3, salary.getAllowences());
        pstm.setObject(4, salary.getAdvance());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM salary WHERE employeeId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static List<Salary> getAll() throws SQLException {
        String sql = "SELECT * FROM salary";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Salary> salaryList = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            double basicSalary = Double.parseDouble(resultSet.getString(2));
            double allowences = Double.parseDouble(resultSet.getString(3));
            //double grossSalary = Double.parseDouble(resultSet.getString(2));
            double advance = Double.parseDouble(resultSet.getString(4));

            Salary salary = new Salary(id, basicSalary, allowences, advance);
            salaryList.add(salary);
        }
        return salaryList;
    }
}
