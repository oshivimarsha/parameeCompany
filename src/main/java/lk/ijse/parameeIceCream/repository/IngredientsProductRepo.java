package lk.ijse.parameeIceCream.repository;

import lk.ijse.parameeIceCream.db.DbConnection;
import lk.ijse.parameeIceCream.model.IngredientsProduct;
import lk.ijse.parameeIceCream.model.OrderDetail;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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
}
