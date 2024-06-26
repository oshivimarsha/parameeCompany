package lk.ijse.parameeIceCream.repository;

import lk.ijse.parameeIceCream.db.DbConnection;
import lk.ijse.parameeIceCream.model.CreateProduct;

import java.sql.Connection;
import java.sql.SQLException;

public class CreateProductRepo {
    public static boolean createProduct(CreateProduct cp) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
          //  System.out.println("product -  "+cp.getProduct());
            boolean isProductSaved = ProductRepo.save(cp.getProduct());
          //  System.out.println(isProductSaved);
            if (isProductSaved) {
             //   System.out.println( "IP List = "+cp.getIPList());
                boolean isQtyUpdated = IngredientRepo.update(cp.getIPList());

             //   System.out.println(isQtyUpdated);
                if (isQtyUpdated) {
                 //   System.out.println(cp.getIPList());
                    boolean isProductrDetailSaved = IngredientsProductRepo.save(cp.getIPList());

                  //  System.out.println(isProductrDetailSaved);
                    if (isProductrDetailSaved) {
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

    public static boolean updateProduct(CreateProduct cp) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {

            boolean isProductSaved = ProductRepo.update(cp.getProduct());

            if (isProductSaved) {
               // System.out.println( "IP List = "+cp.getIPList());
                boolean isQtyUpdated = IngredientRepo.updatePlus(cp.getIPList());

                //  System.out.println(isQtyUpdated);
                if (isQtyUpdated) {
                  //  System.out.println(cp.getIPList());
                    boolean isProductrDetailSaved = IngredientsProductRepo.update(cp.getIPList());

                  //  System.out.println(isProductrDetailSaved);
                    if (isProductrDetailSaved) {
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
