package lk.ijse.parameeIceCream.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.parameeIceCream.model.*;
import lk.ijse.parameeIceCream.model.tm.EmployeeTm;
import lk.ijse.parameeIceCream.model.tm.IngredientTm;
import lk.ijse.parameeIceCream.repository.*;
import org.controlsfx.control.textfield.TextFields;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class IngredientsFormController {
    public TableView<IngredientTm> tblIngredientsCart;
    public TableColumn<?, ?> colId;
    public TableColumn<?, ?> colName;
    public TableColumn<?, ?> colQtyInStock;
    public TableColumn<?, ?> colUnitOfMeasure;
    public TableColumn<?, ?> colUnitPrice;
    public TableColumn<?, ?> colPrice;
    public TableColumn<?, ?> colSupplierId;

    public TextField txtId;

    public TextField txtName;
    public TextField txtQtyInStock;
    public TextField txtUnitOfMeasure;
    public TextField txtPrice;
    public TextField txtUnitPrice;
    public JFXComboBox<String> cmbSupplierId;
    public Label lblSupplierName;
    public TextField txtSearchHere;

    public void initialize() {
        setCellValueFactory();
        loadAllEmployees();
        getSupplierId();
        getIngredientsNames();

    }

    private void getIngredientsNames() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> nameList = IngredientRepo.getNames();

            for(String name : nameList) {
                obList.add(name);
            }
            TextFields.bindAutoCompletion(txtSearchHere, obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAllEmployees() {
        ObservableList<IngredientTm> obList = FXCollections.observableArrayList();

        try {
            List<Ingredient> ingredientList = IngredientRepo.getAll();
            for (Ingredient ingredient : ingredientList) {
                IngredientTm tm = new IngredientTm(
                        ingredient.getId(),
                        ingredient.getName(),
                        ingredient.getQtyInStock(),
                        ingredient.getUnitOfMeasure(),
                        ingredient.getUnitPrice(),
                        ingredient.getPrice(),
                        ingredient.getSupplierId()
                        //employee.getDepartmentName()
                );

                obList.add(tm);
            }

            tblIngredientsCart.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQtyInStock.setCellValueFactory(new PropertyValueFactory<>("qtyInStock"));
        colUnitOfMeasure.setCellValueFactory(new PropertyValueFactory<>("unitOfMeasure"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("SupplierId"));
    }

    public void txtSearchOnAction(ActionEvent actionEvent) throws SQLException {
        String name = txtName.getText();

        Ingredient ingredient = IngredientRepo.searchByName(name);
        if (ingredient != null) {
            txtId.setText(ingredient.getId());
            txtName.setText(ingredient.getName());
            txtQtyInStock.setText(ingredient.getQtyInStock());
            txtUnitOfMeasure.setText(ingredient.getUnitOfMeasure());
            txtUnitPrice.setText(String.valueOf(ingredient.getUnitPrice()));
            txtPrice.setText(String.valueOf(ingredient.getPrice()));
            cmbSupplierId.setValue(ingredient.getSupplierId());
            // lblDepartmentName.setText(employee.getDepartmentName());

        } else {
            new Alert(Alert.AlertType.INFORMATION, "employee not found!").show();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String id = txtId.getText();
        String name = txtName.getText();
        String qtyInStock = txtQtyInStock.getText();
        String unitOfMeasure = txtUnitOfMeasure.getText();
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        double price = Double.parseDouble(txtPrice.getText());
        String supplierId = cmbSupplierId.getValue();
        // String departmentName = lblDepartmentName.getText();

        Ingredient ingredient = new Ingredient(id, name, qtyInStock, unitOfMeasure, unitPrice, price, supplierId);

        try {
            boolean isSaved = IngredientRepo.save(ingredient);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "employee saved!").show();
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
        txtQtyInStock.setText("");
        txtUnitOfMeasure.setText("");
        txtUnitPrice.setText("");
        txtPrice.setText("");
        cmbSupplierId.setValue("");
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtId.getText();
        String name = txtName.getText();
        String qtyInStock = txtQtyInStock.getText();
        String unitOfMeasure = txtUnitOfMeasure.getText();
        double unitPrice = Double.parseDouble(((txtUnitPrice.getText())));
        double price = Double.parseDouble(txtPrice.getText());
        String supplierId = cmbSupplierId.getValue();
        //String departmentName = lblDepartmentName.getText();

        Ingredient ingredient = new Ingredient(id, name, qtyInStock, unitOfMeasure, unitPrice, price, supplierId);

        boolean isUpdated = IngredientRepo.update(ingredient);
        if(isUpdated) {
            new Alert(Alert.AlertType.CONFIRMATION, "Pro updated!").show();
            clearFields();
            initialize();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException {
        String name = txtName.getText();

        boolean isDeleted = IngredientRepo.delete(name);
        if(isDeleted) {
            new Alert(Alert.AlertType.CONFIRMATION, "employee deleted!").show();
            clearFields();
            initialize();
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    private void getSupplierId() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> supList = SupplierRepo.getIds();

            for(String supplier : supList) {
                obList.add(supplier);
            }

            cmbSupplierId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cmbSupplierIdOnAction(ActionEvent actionEvent) {
        String id = cmbSupplierId.getValue();
        try {
            Supplier supplier = SupplierRepo.searchById(id);

            lblSupplierName.setText(supplier.getName());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void txtSearchHereOnAction(ActionEvent actionEvent) throws SQLException {
        String name = txtSearchHere.getText();

        Ingredient ingredient = IngredientRepo.searchByName(String.valueOf(name));
        if (ingredient != null) {
            txtId.setText(ingredient.getId());
            txtName.setText(ingredient.getName());
            txtQtyInStock.setText(ingredient.getQtyInStock());
            txtUnitOfMeasure.setText(ingredient.getUnitOfMeasure());
            txtUnitPrice.setText(String.valueOf(ingredient.getUnitPrice()));
            txtPrice.setText(String.valueOf(ingredient.getPrice()));
            cmbSupplierId.setValue(ingredient.getSupplierId());
        } else {
            new Alert(Alert.AlertType.INFORMATION, "customer not found!").show();
        }
    }
}
