package lk.ijse.parameeIceCream.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.parameeIceCream.db.DbConnection;
import lk.ijse.parameeIceCream.model.*;
import lk.ijse.parameeIceCream.model.tm.OrderTm;
import lk.ijse.parameeIceCream.repository.*;
import lk.ijse.parameeIceCream.util.Regex;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class OrderFormController {
    public Label lblTotal;
    public Label lblCustomerName;
    public JFXComboBox<String> cmbProductId;
    public TextField txtQty;
    public Label lblUnitPrice;
    public Label lblProductName;
    public Label lblQtyOnHand;
    public Label lblOrderId;
    public Label lblOrderDate;
    public TableView<OrderTm> tblOrderCart;
    public TableColumn<?, ?> colId;
    public TableColumn<?, ?> colProductName;
    public TableColumn<?, ?> colQty;
    public TableColumn<?, ?> colUnitPrice;
    public TableColumn<?, ?> colPrice;
    public TableColumn<?, ?> colAction;
    public Label lblOrderTime;
    public Label lblChange;
    public TextField txtProductName;
    public TextField txtCustomerTel;
    public Label lblProductId;
    public Label lblCustomerId;
    public TextField txtAmount;
    public ImageView imageView;
    public Label lblOrderName;

    public Image image;
    public AnchorPane rootNode;
    public JFXButton btnAddToCart;
    public JFXButton btnPlaceOrder;

    private ObservableList<OrderTm> obList = FXCollections.observableArrayList();


    public void initialize() {
        setDate();
        setTime();
        getCurrentOrderId();
        getCustomerTels();
        getProductName();
        setCellValueFactory();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));
    }

    private void getCurrentOrderId() {
        try {
            String currentId = OrderRepo.getCurrentId();

            String nextOrderId = generateNextOrderId(currentId);
            lblOrderId.setText(nextOrderId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNextOrderId(String currentId) {
        if(currentId != null) {
            String[] split = currentId.split("O");
            int idNum = Integer.parseInt(split[1]);
            return "O" + ++idNum;
        }
        return "O1";
    }

    private void setDate() {
        LocalDate now = LocalDate.now();
        lblOrderDate.setText(String.valueOf(now));
    }

    private void setTime() {
        LocalTime now = LocalTime.now();
        lblOrderTime.setText(String.valueOf(now));
    }
    public void btnNewCustomerOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane employeePane = FXMLLoader.load(this.getClass().getResource("/view/customer_form.fxml"));
        rootNode.getChildren().clear();
        rootNode.getChildren().add(employeePane);
    }

    public void txtQtyOnAction(ActionEvent actionEvent) {
        btnAddToCart.requestFocus();
    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        String orderId = lblOrderId.getText();
        Date date = Date.valueOf(LocalDate.now());
        double unitPrice = Double.parseDouble(lblUnitPrice.getText());
        String customerId = lblCustomerId.getText();

        var order = new Order(orderId, date, unitPrice, customerId);

        List<OrderDetail> odList = new ArrayList<>();

        for (int i = 0; i < tblOrderCart.getItems().size(); i++) {
            OrderTm tm = obList.get(i);

            OrderDetail od = new OrderDetail(
                    orderId,
                    tm.getId(),
                    tm.getQty(),
                    tm.getUnitPrice()
            );

            odList.add(od);

            txtAmountOnAction(actionEvent);
        }

        PlaceOrder po = new PlaceOrder(order, odList);

        try {
            boolean isPlaced = PlaceOrderRepo.placeOrder(po);
            if(isPlaced) {
                new Alert(Alert.AlertType.CONFIRMATION, "Order Placed!").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "Order Placed Unsuccessfully!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        txtCustomerTel.setText("");
        lblCustomerName.setText("");
        lblCustomerId.setText("");
        lblTotal.setText("");
        txtAmount.setText("");
        lblChange.setText("");
        getCurrentOrderId();

    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        String id = lblProductId.getText();
        String name = txtProductName.getText();
        int qty = Integer.parseInt(txtQty.getText());
        double unitPrice = Double.parseDouble(lblUnitPrice.getText());
        double price = qty * unitPrice;
        JFXButton btnRemove = new JFXButton("remove");
        btnRemove.setCursor(Cursor.HAND);

            btnRemove.setOnAction((e) -> {
                ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

                Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

                if (type.orElse(no) == yes) {
                    int selectedIndex = tblOrderCart.getSelectionModel().getSelectedIndex();
                    obList.remove(selectedIndex);

                    tblOrderCart.refresh();
                    calculateNetTotal();

                }
            });

            for (int i = 0; i < tblOrderCart.getItems().size(); i++) {
                if (id.equals(colId.getCellData(i))) {

                    OrderTm tm = obList.get(i);
                    qty += tm.getQty();
                    price = qty * unitPrice;

                    tm.setQty(qty);
                    tm.setPrice(price);

                    tblOrderCart.refresh();

                    calculateNetTotal();
                    return;
                }
            }

            //if (isValid()) {
                OrderTm tm = new OrderTm(id, name, qty, unitPrice, price, btnRemove);
                obList.add(tm);
            /*} else {
                new Alert(Alert.AlertType.CONFIRMATION, "Wrong Input!").show();
            }*/

                tblOrderCart.setItems(obList);
                calculateNetTotal();
                clearFields();
                txtProductName.requestFocus();

    }

    private double calculateNetTotal() {
        int netTotal = 0;
        for (int i = 0; i < tblOrderCart.getItems().size(); i++) {
            netTotal += (double) colPrice.getCellData(i);
        }
        lblTotal.setText(String.valueOf(netTotal));

        return netTotal;
    }

    public void orderReceiptOnAction(ActionEvent actionEvent) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/reports/orderBill.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

         Map<String,Object> data = new HashMap<>();
         /*data.put("id", lblOrderId.getText());
         data.put("total", String.valueOf(getNetTotal()));*/
        data.put("id", getLastOrderId());
        data.put("total", String.valueOf(gettNetTotal()));


        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, data, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);

    }

    private String getLastOrderId(){
        String orderId;
        try {
            orderId = OrderRepo.getLastOId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orderId;
    }

    private double gettNetTotal() {
        double netTotal = 0.0;
        try {
            System.out.println(getLastOrderId());
            netTotal = OrderRepo.getNetTot(getLastOrderId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return netTotal;
    }

    public void txtAmountOnAction(ActionEvent actionEvent) {
        double amount = Double.parseDouble(txtAmount.getText());
        double total = calculateNetTotal();
        double change = (amount - total);

        lblChange.setText(String.valueOf(change));
        btnPlaceOrder.requestFocus();
    }

    private void getProductName() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> nameList = ProductRepo.getName();

            for(String name : nameList) {
                obList.add(name);
            }

            TextFields.bindAutoCompletion(txtProductName, obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getCustomerTels() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> telList = CustomerRepo.getTels();

            for(String tel : telList) {
                obList.add(tel);
            }
            TextFields.bindAutoCompletion(txtCustomerTel, obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void txtProductNameOnAction(ActionEvent actionEvent) {
        String name = txtProductName.getText();
        try {
            Product product = ProductRepo.searchByName(name);

            lblProductId.setText(product.getId());
            lblUnitPrice.setText(String.valueOf(product.getUnitPrice()));
            lblQtyOnHand.setText(String.valueOf(product.getQtyAvailable()));
            lblOrderName.setText(product.getName());
            Image image = new Image(product.getPath());
            imageView.setImage(image);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        txtQty.requestFocus();
    }

    public void txtCustomerTelOnAction(ActionEvent actionEvent) {
        String tel = txtCustomerTel.getText();
        try {
            Customer customer = CustomerRepo.searchByTel(tel);

            lblCustomerName.setText(customer.getName());
            lblCustomerId.setText(customer.getId());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        txtProductName.requestFocus();
    }

    private void clearFields() {
        txtProductName.setText("");
        lblProductId.setText("");
      //  lblUnitPrice.setText("");
        lblQtyOnHand.setText("");
        txtQty.setText("");
        imageView.setImage(null);

    }

    public void txtQtyOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.QTY, txtQty);
    }

    public void txtTelOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.TEL, txtCustomerTel);
    }

    public void txtNameOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.NAME, txtProductName);
    }

    public void txtAmountOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.PRICE, txtAmount);
    }

    public boolean isValid() {
        if (!Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.QTY, txtQty)) return true;
        if (!Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.TEL, txtCustomerTel)) return true;
        if (!Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.NAME, txtProductName)) return true;
        if (!Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.PRICE, txtAmount)) return true;
        return false;
    }
}
