package lk.ijse.parameeIceCream.repository;

import lk.ijse.parameeIceCream.db.DbConnection;
import lk.ijse.parameeIceCream.model.Customer;
import lk.ijse.parameeIceCream.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRepo {
    public static String getCurrentId() throws SQLException {
        String sql = "SELECT CONCAT('O', MAX(CAST(SUBSTRING(orderId, 2) AS UNSIGNED))) AS max_order_id FROM orders";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String orderId = resultSet.getString(1);
            return orderId;
        }
        return null;
    }

    public static boolean save(Order order) throws SQLException {
        String sql = "INSERT INTO orders VALUES(?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, order.getId());
        pstm.setObject(2, order.getDate());
        pstm.setObject(3, order.getUnitPrice());
        pstm.setObject(4, order.getCustomrId());

        return pstm.executeUpdate() > 0;
    }

    public static String getLastOId() throws SQLException {
        String sql = "SELECT orderId FROM orders ORDER BY CAST(SUBSTRING(orderId, 2) AS UNSIGNED) DESC LIMIT 1;";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String orderId = resultSet.getString(1);
            return orderId;
        }
        return null;
    }

    public static double getNetTot(String oId) throws SQLException {
        String sql = "SELECT SUM(od.Qty * od.unitPrice) AS net_total FROM orders o JOIN orderProductDetail od ON o.orderId = od.orderId WHERE o.orderId = ? GROUP BY o.orderId;";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);
        System.out.println(oId);
        pstm.setString(1, oId);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            double netTot = resultSet.getDouble(1);
            System.out.println(netTot);
            return netTot;
        }
        return 0.0;
    }
}
