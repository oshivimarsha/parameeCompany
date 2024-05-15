package lk.ijse.parameeIceCream.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SidePannelFormController {
    public AnchorPane rootNode;
    public AnchorPane childRootNode;
    public Label lblUserName;

    public void initialize() throws IOException {
        loadDashBoard();
    }

    private void loadDashBoard() throws IOException {
        AnchorPane dashPane = FXMLLoader.load(this.getClass().getResource("/view/dashboard_form.fxml"));
        childRootNode.getChildren().clear();
        childRootNode.getChildren().add(dashPane);
    }

    public void btnDashboardOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane dashPane = FXMLLoader.load(this.getClass().getResource("/view/dashboard_form.fxml"));
        childRootNode.getChildren().clear();
        childRootNode.getChildren().add(dashPane);
    }

    public void btnCustomerOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane customerPane = FXMLLoader.load(this.getClass().getResource("/view/customer_form.fxml"));
        childRootNode.getChildren().clear();
        childRootNode.getChildren().add(customerPane);
    }

    public void btnEmployeeOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane employeePane = FXMLLoader.load(this.getClass().getResource("/view/employee_form.fxml"));
        childRootNode.getChildren().clear();
        childRootNode.getChildren().add(employeePane);
    }

    public void btnOrderOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane orderPane = FXMLLoader.load(this.getClass().getResource("/view/order_form.fxml"));
        childRootNode.getChildren().clear();
        childRootNode.getChildren().add(orderPane);
    }

    public void btnPaymentOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane paymentPane = FXMLLoader.load(this.getClass().getResource("/view/payment_form.fxml"));
        childRootNode.getChildren().clear();
        childRootNode.getChildren().add(paymentPane);
    }

    public void btnIngredientsOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane ingredientPane = FXMLLoader.load(this.getClass().getResource("/view/ingredients_form.fxml"));
        childRootNode.getChildren().clear();
        childRootNode.getChildren().add(ingredientPane);
    }

    public void btnProductOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane productsPane = FXMLLoader.load(this.getClass().getResource("/view/products_form.fxml"));
        childRootNode.getChildren().clear();
        childRootNode.getChildren().add(productsPane);
    }

    public void btnSupplierOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane supplierPane = FXMLLoader.load(this.getClass().getResource("/view/suppliers_form.fxml"));
        childRootNode.getChildren().clear();
        childRootNode.getChildren().add(supplierPane);
    }

    public void btnMachineOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane machinePane = FXMLLoader.load(this.getClass().getResource("/view/machine_form.fxml"));
        childRootNode.getChildren().clear();
        childRootNode.getChildren().add(machinePane);
    }

    public void btnTransportOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane transportPane = FXMLLoader.load(this.getClass().getResource("/view/department_form.fxml"));
        childRootNode.getChildren().clear();
        childRootNode.getChildren().add(transportPane);
    }

    public void btnReportsOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane reportPane = FXMLLoader.load(this.getClass().getResource("/view/report_form.fxml"));
        childRootNode.getChildren().clear();
        childRootNode.getChildren().add(reportPane);
    }

    public void btnLogoutOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Log in Form");
    }

    public void setUserName(String userName){
        lblUserName.setText(userName);
    }
}
