package lk.ijse.parameeIceCream.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import lk.ijse.parameeIceCream.model.Customer;
import lk.ijse.parameeIceCream.model.Department;
import lk.ijse.parameeIceCream.model.tm.CustomerTm;
import lk.ijse.parameeIceCream.model.tm.DepartmentTm;
import lk.ijse.parameeIceCream.repository.CustomerRepo;
import lk.ijse.parameeIceCream.repository.DepartmentRepo;
import lk.ijse.parameeIceCream.util.Regex;
import org.controlsfx.control.textfield.TextFields;

import java.sql.SQLException;
import java.util.List;

public class DepartmentFormController {

    public TextField txtId;

    public TextField txtName;
    public TextField txtDescription;
    public TableView<DepartmentTm> tblDepartmentCart;
    public TableColumn<?, ?> colId;
    public TableColumn<?, ?> colName;
    public TableColumn<?, ?> colDescription;
    public TextField txtSearchHere;

    public void initialize() {
        setCellValueFactory();
        loadAllDepartment();
        getdepartmentName();
    }

    private void getdepartmentName() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> nameList = DepartmentRepo.getName();

            for(String name : nameList) {
                obList.add(name);
            }
            TextFields.bindAutoCompletion(txtSearchHere, obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAllDepartment() {
        ObservableList<DepartmentTm> obList = FXCollections.observableArrayList();

        try {
            List<Department> departmentList = DepartmentRepo.getAll();
            for (Department department : departmentList) {
                DepartmentTm tm = new DepartmentTm(
                        department.getId(),
                        department.getName(),
                        department.getDescription()
                );

                obList.add(tm);
            }

            tblDepartmentCart.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String id = txtId.getText();
        String name = txtName.getText();
        String description = txtDescription.getText();

        if (isValied()) {
            Department department = new Department(id, name, description);

            try {
                boolean isSaved = DepartmentRepo.save(department);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Department saved!").show();
                    clearFields();
                    initialize();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            new Alert(Alert.AlertType.CONFIRMATION, "Wrong Input!").show();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtId.getText();
        String name = txtName.getText();
        String description = txtDescription.getText();

        Department department = new Department(id, name, description);

        boolean isUpdated = DepartmentRepo.update(department);
        if(isUpdated) {
            new Alert(Alert.AlertType.CONFIRMATION, "Department updated!").show();
            clearFields();
            initialize();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException {
        String name = txtName.getText();

        boolean isDeleted = DepartmentRepo.delete(name);
        if(isDeleted) {
            new Alert(Alert.AlertType.CONFIRMATION, "Department deleted!").show();
            clearFields();
            initialize();
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtDescription.setText("");
    }

    public void txtSearchOnAction(ActionEvent actionEvent) throws SQLException {
        String name = txtName.getText();

        Department department = DepartmentRepo.searchByName(name);
        if (department != null) {
            txtId.setText(department.getId());
            txtName.setText(department.getName());
            txtDescription.setText(department.getDescription());
        } else {
            new Alert(Alert.AlertType.INFORMATION, "customer not found!").show();
        }
    }




    public void txtSearchHereOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtSearchHere.getText();

        Department department = DepartmentRepo.searchById(id);
        if (department != null) {
            txtId.setText(department.getId());
            txtName.setText(department.getName());
            txtDescription.setText(department.getDescription());
        } else {
            new Alert(Alert.AlertType.INFORMATION, "department not found!").show();
        }
    }

    public void txtIdOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.DID, txtId);
    }

    public void txtNameOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.NAME, txtName);
    }

    public boolean isValied() {
        if (!Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.CID, txtId)) return false;
        if (!Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.NAME, txtName)) return false;
        return true;
    }
}
