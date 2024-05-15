package lk.ijse.parameeIceCream.repository;

import lk.ijse.parameeIceCream.db.DbConnection;
import lk.ijse.parameeIceCream.model.tm.DashboardTm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DashboardRepo {
    public static List<DashboardTm> getMostProduct() throws SQLException, SQLException {
        String sql = "SELECT productId,COUNT(orderId),SUM(qty) FROM orderProductDetail GROUP BY productId ORDER BY SUM(qty) DESC LIMIT 5;";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<DashboardTm> sellItem = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            int oId = resultSet.getInt(2);
            int qty = resultSet.getInt(3);

            DashboardTm mostSellItem = new DashboardTm(id, oId, qty);
            sellItem.add(mostSellItem);
        }
        return sellItem;
    }
}
