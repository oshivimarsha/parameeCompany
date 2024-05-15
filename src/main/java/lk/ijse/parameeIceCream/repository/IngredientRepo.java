package lk.ijse.parameeIceCream.repository;

import lk.ijse.parameeIceCream.db.DbConnection;
import lk.ijse.parameeIceCream.model.Employee;
import lk.ijse.parameeIceCream.model.Ingredient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientRepo {

    public static boolean save(Ingredient ingredient) throws SQLException {
        String sql = "INSERT INTO ingredient VALUES(?, ?, ?, ?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, ingredient.getId());
        pstm.setObject(2, ingredient.getName());
        pstm.setObject(3, ingredient.getQtyInStock());
        pstm.setObject(4, ingredient.getUnitOfMeasure());
        pstm.setObject(5, ingredient.getUnitPrice());
        pstm.setObject(6, ingredient.getPrice());
        pstm.setObject(7, ingredient.getSupplierId());
        //  pstm.setObject(12, employee.getDepartmentName());

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Ingredient ingredient) throws SQLException {
        String sql = "UPDATE ingredient SET ingredientId = ?, qtyInStock = ?, unitOfMeasure = ?, unitPrice = ?, price = ?, supplierId = ? WHERE name = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, ingredient.getId());
        pstm.setObject(2, ingredient.getQtyInStock());
        pstm.setObject(3, ingredient.getUnitOfMeasure());
        pstm.setObject(4, ingredient.getUnitPrice());
        pstm.setObject(5, ingredient.getPrice());
        pstm.setObject(6, ingredient.getSupplierId());
        pstm.setObject(7, ingredient.getName());
        //pstm.setObject(11, employee.getDepartmentName());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String name) throws SQLException {
        String sql = "DELETE FROM ingredient WHERE name = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, name);

        return pstm.executeUpdate() > 0;
    }

    public static Ingredient searchByName(String name) throws SQLException {
        String sql = "SELECT * FROM ingredient WHERE name = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, name);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {

            String id = resultSet.getString(1);
            String iName = resultSet.getString(2);
            String qtyInStock = resultSet.getString(3);
            String unitOfMeasure = resultSet.getString(4);
            double unitPrice = Double.parseDouble(resultSet.getString(5));
            double price = Double.parseDouble(resultSet.getString(6));
            String supplierId = resultSet.getString(7);

            Ingredient ingredient = new Ingredient(id, iName, qtyInStock, unitOfMeasure, unitPrice, price, supplierId);

            return ingredient;
        }

        return null;
    }

    public static List<Ingredient> getAll() throws SQLException {
        String sql = "SELECT * FROM ingredient";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Ingredient> ingList = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String iName = resultSet.getString(2);
            String qtyInStock = resultSet.getString(3);
            String unitOfMeasure = resultSet.getString(4);
            double unitPrice = Double.parseDouble(resultSet.getString(5));
            double price = Double.parseDouble(resultSet.getString(6));
            String supplierId = resultSet.getString(7);
            // String departmentName = resultSet.getString(12);

            Ingredient ingredient = new Ingredient(id, iName, qtyInStock, unitOfMeasure, unitPrice, price, supplierId);
            ingList.add(ingredient);
        }
        return ingList;
    }

    public static List<String> getNames() throws SQLException {
        String sql = "SELECT name FROM ingredient";
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
