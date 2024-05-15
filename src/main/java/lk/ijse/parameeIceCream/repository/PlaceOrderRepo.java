package lk.ijse.parameeIceCream.repository;

import lk.ijse.parameeIceCream.db.DbConnection;
import lk.ijse.parameeIceCream.model.PlaceOrder;

import java.sql.Connection;
import java.sql.SQLException;

public class PlaceOrderRepo {
    public static boolean placeOrder(PlaceOrder po) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
            System.out.println(po.getOrder());
            boolean isOrderSaved = OrderRepo.save(po.getOrder());
            System.out.println(isOrderSaved);
            if (isOrderSaved) {
                System.out.println(po.getOdList());
                boolean isQtyUpdated = ProductRepo.update(po.getOdList());
                System.out.println(isQtyUpdated);
                if (isQtyUpdated) {
                    System.out.println(po.getOdList());
                    boolean isOrderDetailSaved = OrderDetailRepo.save(po.getOdList());
                    System.out.println(isOrderDetailSaved);
                    if (isOrderDetailSaved) {
                        connection.commit();
                        return true;
                    }
                }
            }
            connection.rollback();
            return false;
        } catch (Exception e) {
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
