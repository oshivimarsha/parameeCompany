package lk.ijse.parameeIceCream.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.parameeIceCream.db.DbConnection;
import lk.ijse.parameeIceCream.model.Customer;
import lk.ijse.parameeIceCream.model.Employee;
import lk.ijse.parameeIceCream.model.tm.CustomerTm;
import lk.ijse.parameeIceCream.repository.CustomerRepo;
import lk.ijse.parameeIceCream.repository.EmployeeRepo;
import org.controlsfx.control.textfield.TextFields;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerFormController {


    public TextField txtTel;
    public TextField txtEmail;
    public AnchorPane rootNode;

    public TextField txtId;

    public TextField txtName;
    public TextField txtNIC;
    public TextField txtAddress;

    public TableColumn<?, ?> colId;

    public TableColumn<?, ?> colName;
    public TableColumn<?, ?> colNic;
    public TableColumn<?, ?> colAddress;
    public TableColumn<?, ?> colTel;
    public TableColumn<?, ?> colEmail;
    public TableView<CustomerTm> tblCustomerCart;
    public TextField txtSearchHere;
    public JFXButton btnSave;


    public void initialize() {
        setCellValueFactory();
        loadAllCustomers();
        getCustomerTels();
    }

    private void getCustomerTels() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> telList = CustomerRepo.getTels();

            for(String tel : telList) {
                obList.add(tel);
            }
            TextFields.bindAutoCompletion(txtSearchHere, obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAllCustomers() {
        ObservableList<CustomerTm> obList = FXCollections.observableArrayList();

        try {
            List<Customer> customerList = CustomerRepo.getAll();
            for (Customer customer : customerList) {
                CustomerTm tm = new CustomerTm(
                        customer.getId(),
                        customer.getName(),
                        customer.getNic(),
                        customer.getAddress(),
                        customer.getEmail(),
                        customer.getTel()
                );

                obList.add(tm);
            }

            tblCustomerCart.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
    }


    public void btnSaveOnAction(ActionEvent actionEvent) {
        String id = txtId.getText();
        String name = txtName.getText();
        String nic = txtNIC.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();
        String tel = txtTel.getText();

        Customer customer = new Customer(id, name, nic, address, email, tel);

        try {
            boolean isSaved = CustomerRepo.save(customer);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "customer saved!").show();
                clearFields();
                initialize();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtNIC.setText("");
        txtAddress.setText("");
        txtEmail.setText("");
        txtTel.setText("");
        txtSearchHere.setText("");
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtId.getText();
        String name = txtName.getText();
        String nic = txtNIC.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();
        String tel = txtTel.getText();

        Customer customer = new Customer(id, name, nic, address, email, tel);

        boolean isUpdated = CustomerRepo.update(customer);
        if(isUpdated) {
            new Alert(Alert.AlertType.CONFIRMATION, "customer updated!").show();
            clearFields();
            initialize();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException {
        String nic = txtNIC.getText();

        boolean isDeleted = CustomerRepo.delete(nic);
        if(isDeleted) {
            new Alert(Alert.AlertType.CONFIRMATION, "customer deleted!").show();
            clearFields();
            initialize();
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) throws SQLException {
        txtAddress.requestFocus();
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT id FROM customer";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        List<String> idList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString(1);
            idList.add(id);
        }
        return idList;
    }

    public void txtSearchHereOnAction(ActionEvent actionEvent) throws SQLException {
        String tel = txtSearchHere.getText();
        //String tell = txtSearchHere.getText();

        Customer customer = CustomerRepo.searchByTel(tel);
        // Department department = DepartmentRepo.searchById(employee.getDepartmentId());
        if (customer != null) {
            txtId.setText(customer.getId());
            txtName.setText(customer.getName());
            txtNIC.setText(customer.getNic());
            txtAddress.setText(customer.getAddress());
            txtEmail.setText(customer.getEmail());
            txtTel.setText(customer.getTel());

        } else {
            new Alert(Alert.AlertType.INFORMATION, "employee not found!").show();
        }
    }

    public void txtIdOnAction(ActionEvent actionEvent) {
        txtName.requestFocus();
    }

    public void txtNameOnAction(ActionEvent actionEvent) {
        txtNIC.requestFocus();
    }

    public void txtAddressOnAction(ActionEvent actionEvent) {
        txtEmail.requestFocus();
    }


    public void txtEmailOnAction(ActionEvent actionEvent) {
        txtTel.requestFocus();
    }

    public void txtIdOnKeyReleased(KeyEvent keyEvent) {

    }

    public void txtNameOnKeyReleased(KeyEvent keyEvent) {

    }

    public void txtNicOnKeyReleased(KeyEvent keyEvent) {

    }

    public void txtAddressOnKeyReleased(KeyEvent keyEvent) {

    }

    public void txtTelOnKeyReleased(KeyEvent keyEvent) {

    }

    public void txtEmailOnKeyReleased(KeyEvent keyEvent) {

    }
}
