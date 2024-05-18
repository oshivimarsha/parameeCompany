package lk.ijse.parameeIceCream.controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import lk.ijse.parameeIceCream.db.DbConnection;
import lk.ijse.parameeIceCream.model.Department;
import lk.ijse.parameeIceCream.model.Product;
import lk.ijse.parameeIceCream.model.tm.DashboardTm;
import lk.ijse.parameeIceCream.repository.DashboardRepo;
import lk.ijse.parameeIceCream.repository.DepartmentRepo;
import lk.ijse.parameeIceCream.repository.ProductRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DashboardFormController {

    public Label lblCustomerCount;
    @FXML
    public AnchorPane rootNode;
    public Label lblEmployeeCount;
    public LineChart<?, ?> lineChart;
    public PieChart pieChart;
    public Label lblIncomeCount;
    public Label lblOrderCount;
    public Label lblOrderDate;
    public Label lblOrderTime;
    public Label lblProductCount;

    private int customerCount;
    private int employeeCount;
    private int orderCount;

    private int productCount;

    public int orderList;

    public void initialize() throws SQLException {
        try {
            customerCount = getCustomerCount();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        setCustomerCount(customerCount);



        try {
            employeeCount = getEmployeeCount();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        setEmployeeCount(employeeCount);



        try {
            orderCount = getOrderCount();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        setOrderCount(orderCount);


        try {
            productCount = getProductCount();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        serProductCount(productCount);


        lineChart();
        pieChartConnect();
        setDate();
        setTime();
    }

    public void lineChart(){
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Paramee Company");

        PreparedStatement stm = null;
        try {
            stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT\n" +
                    "    DATE_FORMAT(MIN(o.orderDate), '%Y-%m-%d') AS WeekStartDate,\n" +
                    "    DATE_FORMAT(MAX(o.orderDate), '%Y-%m-%d') AS WeekEndDate,\n" +
                    "    COUNT(DISTINCT o.orderId) AS WeeklyOrders,\n" +
                    "    SUM(opd.qty * opd.unitPrice) AS TotalAmount\n" +
                    "FROM\n" +
                    "    orders o\n" +
                    "JOIN\n" +
                    "    orderProductDetail opd ON o.orderId = opd.orderId\n" +
                    "WHERE\n" +
                    "    o.orderDate BETWEEN (SELECT MIN(orderDate) FROM orders) AND (SELECT MAX(orderDate) FROM orders)\n" +
                    "GROUP BY\n" +
                    "    YEARWEEK(o.orderDate, 1)\n" +
                    "ORDER BY\n" +
                    "    WeekStartDate;\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet rst = null;
        try {
            rst = stm.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                if (!rst.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String date = null;
            try {
                date = rst.getString(2);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            int count = 0;
            try {
                count = rst.getInt(4);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            series1.getData().add(new XYChart.Data<>(date, count));
        }
        lineChart.getData().addAll(series1);
    }


    private void setDate() {
        LocalDate now = LocalDate.now();
        lblOrderDate.setText(String.valueOf(now));
    }

    private void setTime() {
        LocalTime now = LocalTime.now();
        lblOrderTime.setText(String.valueOf(now));
    }

    private void setCustomerCount(int customerCount) {
        lblCustomerCount.setText(String.valueOf(customerCount));
    }

    private int getCustomerCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS customerCount FROM customer";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return resultSet.getInt("customerCount");
        }
        return 0;
    }

    private void setEmployeeCount(int employeeCount) {
        lblEmployeeCount.setText(String.valueOf(employeeCount));
    }

    private int getEmployeeCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS employeeCount FROM employee";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return resultSet.getInt("employeeCount");
        }
        return 0;
    }

    private void serProductCount(int employeeCount) {
        lblProductCount.setText(String.valueOf(employeeCount));
    }

    private int getProductCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS productCount FROM product";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return resultSet.getInt("productCount");
        }
        return 0;
    }

    private int getOrderCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS orderCount FROM orders";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return resultSet.getInt("orderCount");
        }
        return 0;
    }

    private void setOrderCount(int orderCount) {
        lblOrderCount.setText(String.valueOf(orderCount));
    }

    public void pieChartConnect() throws SQLException {
        ObservableList<DashboardTm> obList = FXCollections.observableArrayList();

        List<DashboardTm> productList = DashboardRepo.getMostProduct();
        Product product;
        for (DashboardTm sellProduct : productList) {
            product = ProductRepo.searchById(sellProduct.getProductId());

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data(product.getName(), sellProduct.getQty())
            );
            pieChartData.forEach(data ->
                    data.nameProperty().bind(
                            Bindings.concat(
                                    data.getName(), " amount: ", data.pieValueProperty()
                            )
                    )
            );

            pieChart.getData().addAll(pieChartData);
        }
    }
}
