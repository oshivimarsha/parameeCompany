package lk.ijse.parameeIceCream.repository;

import lk.ijse.parameeIceCream.db.DbConnection;
import lk.ijse.parameeIceCream.model.IngredientsProduct;
import lk.ijse.parameeIceCream.model.OrderDetail;
import lk.ijse.parameeIceCream.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IngredientsProductRepo {
    public static boolean save(List<IngredientsProduct> ipList) throws SQLException {
        for (IngredientsProduct ip : ipList) {
            boolean isSaved = save(ip);
            if(!isSaved) {
                return false;
            }
        }
        return true;
    }

    private static boolean save(IngredientsProduct ip) throws SQLException {
        String sql = "INSERT INTO ingredientProductDetail VALUES(?, ?, ?, ?)";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setString(1, ip.getIngredientId());
        pstm.setString(2, ip.getProductId());
        pstm.setInt(3, ip.getQty());
        pstm.setDouble(4, ip.getUnitPrice());

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(List<IngredientsProduct> ipList) throws SQLException {
        for (IngredientsProduct ip : ipList) {
            boolean isSaved = update(ip);
            if(!isSaved) {
                return false;
            }
        }
        return true;
    }

    private static boolean update(IngredientsProduct ip) throws SQLException {
        String sql = "UPDATE ingredientProductDetail SET productId = ?, qty = ?, unitPrice = ? WHERE ingredientId = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setString(1, ip.getProductId());
        pstm.setInt(2, ip.getQty());
        pstm.setDouble(3, ip.getUnitPrice());
        pstm.setString(4, ip.getIngredientId());

        return pstm.executeUpdate() > 0;
    }

    public static List<IngredientsProduct> searchById(String id) throws SQLException {
        String sql = "SELECT * FROM ingredientProductDetail WHERE ingredientId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        List<IngredientsProduct> product1 = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String ingId = resultSet.getString(1);
            String pId = resultSet.getString(2);
            int qty = resultSet.getInt(3);
            double unitPrice = resultSet.getDouble(4);

            IngredientsProduct product = new IngredientsProduct(ingId, pId, qty, unitPrice);

            product1.add(product);
        }

        return product1;
    }
}
