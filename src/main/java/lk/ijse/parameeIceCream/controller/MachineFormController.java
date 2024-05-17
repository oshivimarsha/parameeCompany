package lk.ijse.parameeIceCream.controller;

import com.google.protobuf.StringValue;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import lk.ijse.parameeIceCream.model.Customer;
import lk.ijse.parameeIceCream.model.Department;
import lk.ijse.parameeIceCream.model.Employee;
import lk.ijse.parameeIceCream.model.Machine;
import lk.ijse.parameeIceCream.model.tm.CustomerTm;
import lk.ijse.parameeIceCream.model.tm.MachineTm;
import lk.ijse.parameeIceCream.repository.CustomerRepo;
import lk.ijse.parameeIceCream.repository.DepartmentRepo;
import lk.ijse.parameeIceCream.repository.EmployeeRepo;
import lk.ijse.parameeIceCream.repository.MachineRepo;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MachineFormController {
    public TextField txtId;
    public TextField txtMachineType;
    public TextField txtSerialNumber;
    public TextField txtPurchaseCost;
    public TextField txtMaintainCost;
    public TableView<MachineTm> tblMachineCart;

    public TableColumn<?, ?> colId;
    public TableColumn<?, ?> colType;
    public TableColumn<?, ?> colSerialNumber;
    public TableColumn<?, ?> colPurchaseDate;
    public TableColumn<?, ?> colPurchaseCost;
    public TableColumn<?, ?> colMaintainCost;
    public TableColumn<?, ?> colDepartmentId;
    public JFXComboBox<String> cmbDepartmentId;
    public Label lblDepartmentName;
    public DatePicker txtPurchaseDate;
    public Image image;
    public AnchorPane main_form;
    public ImageView imageView;
    public TextField txtSearchHere;

    public void initialize() {
        setCellValueFactory();
        loadAllMachines();
        getDepartmentId();
        getMachineTels();

    }

    private void getMachineTels() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> numList = MachineRepo.getNum();

            for(String num : numList) {
                obList.add(num);
            }
            TextFields.bindAutoCompletion(txtSearchHere, obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAllMachines() {
        ObservableList<MachineTm> obList = FXCollections.observableArrayList();

        try {
            List<Machine> machineList = MachineRepo.getAll();
            for (Machine machine : machineList) {
                MachineTm tm = new MachineTm(
                        machine.getId(),
                        machine.getType(),
                        machine.getSerialNumber(),
                        machine.getPurchaseDate(),
                        machine.getPurchaseCost(),
                        machine.getMaintainCost(),
                        machine.getDepartmentId()
                );

                obList.add(tm);
            }

            tblMachineCart.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colSerialNumber.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        colPurchaseDate.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        colPurchaseCost.setCellValueFactory(new PropertyValueFactory<>("purchaseCost"));
        colMaintainCost.setCellValueFactory(new PropertyValueFactory<>("maintainCost"));
        colDepartmentId.setCellValueFactory(new PropertyValueFactory<>("departmentId"));
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String id = txtId.getText();
        String type = txtMachineType.getText();
        int serialNumber = Integer.parseInt(txtSerialNumber.getText());
        Date purchaseDate = Date.valueOf(txtPurchaseDate.getValue());
        double purchaseCost = Double.parseDouble(txtPurchaseCost.getText());
        double maintainCost = Double.parseDouble(txtMaintainCost.getText());
        String departmentId = cmbDepartmentId.getValue();
        String path = image.getUrl();

        Machine machine = new Machine(id, type, serialNumber, purchaseDate, purchaseCost, maintainCost, departmentId, path);

        try {
            boolean isSaved = MachineRepo.save(machine);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Machine saved!").show();
                clearFields();
                initialize();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtMachineType.setText("");
        txtSerialNumber.setText("");
        txtPurchaseDate.setValue(null);
        txtPurchaseCost.setText("");
        txtMaintainCost.setText("");
        cmbDepartmentId.setValue("");
        txtSearchHere.setText("");
        imageView.setImage(null);
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtId.getText();
        String type = txtMachineType.getText();
        int serialNumber = Integer.parseInt(txtSerialNumber.getText());
        Date purchaseDate = Date.valueOf(txtPurchaseDate.getValue());
        double purchaseCost = Double.parseDouble(txtPurchaseCost.getText());
        double maintainCost = Double.parseDouble(txtMaintainCost.getText());
        String departmentId = cmbDepartmentId.getValue();
        String path = image.getUrl();

        Machine machine = new Machine(id, type, serialNumber, purchaseDate, purchaseCost, maintainCost, departmentId, path);

        boolean isUpdated = MachineRepo.update(machine);
        if(isUpdated) {
            new Alert(Alert.AlertType.CONFIRMATION, "Machine updated!").show();
            clearFields();
            initialize();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException {
        String serialNumber = txtSerialNumber.getText();

        boolean isDeleted = MachineRepo.delete(serialNumber);
        if(isDeleted) {
            new Alert(Alert.AlertType.CONFIRMATION, "Machine deleted!").show();
            clearFields();
            initialize();
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    private void getDepartmentId() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> machineList = DepartmentRepo.getIds();

            for(String machines : machineList) {
                obList.add(machines);
            }

            cmbDepartmentId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cmbDepartmentIdOnAction(ActionEvent actionEvent) {
        String name = cmbDepartmentId.getValue();
        try {
            Department department = DepartmentRepo.searchByName(name);

            lblDepartmentName.setText(department.getName());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnImportImageOnAction(ActionEvent actionEvent) {
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File", new String[]{"*png", "*jpg"}));
        File file = openFile.showOpenDialog(this.main_form.getScene().getWindow());
        if (file != null) {
            //data.path = file.getAbsolutePath();
            this.image = new Image(file.toURI().toString(), 120.0, 127.0, false, true);
            this.imageView.setImage(this.image);
        }
    }

    public void txtSearchOnAction(ActionEvent actionEvent) throws SQLException {
        String serialNumber = txtSerialNumber.getText();

        Machine machine = MachineRepo.searchByNum(serialNumber);
        Department department = DepartmentRepo.searchById(machine.getDepartmentId());

        if (machine != null) {
            txtId.setText(machine.getId());
            txtMachineType.setText(machine.getType());
            txtSerialNumber.setText(String.valueOf(machine.getSerialNumber()));
            txtPurchaseDate.setValue(LocalDate.parse(String.valueOf(machine.getPurchaseDate())));
            txtPurchaseCost.setText(String.valueOf(machine.getPurchaseCost()));
            txtMaintainCost.setText(String.valueOf(machine.getMaintainCost()));
            cmbDepartmentId.setValue(machine.getDepartmentId());
            lblDepartmentName.setText(department.getName());
            Image image = new Image(machine.getPath());
            imageView.setImage(image);

        } else {
            new Alert(Alert.AlertType.INFORMATION, "employee not found!").show();
        }
    }

    public void txtSearchHereOnAction(ActionEvent actionEvent) throws SQLException {
        String num = txtSearchHere.getText();

        Machine machine = MachineRepo.searchByNum(String.valueOf(num));
        Department department = DepartmentRepo.searchById(machine.getDepartmentId());
        if (machine != null) {
            txtId.setText(machine.getId());
            txtMachineType.setText(machine.getType());
            txtSerialNumber.setText(String.valueOf(machine.getSerialNumber()));
            txtPurchaseDate.setValue(LocalDate.parse(String.valueOf(machine.getPurchaseDate())));
            txtPurchaseCost.setText(String.valueOf(machine.getPurchaseCost()));
            txtMaintainCost.setText(String.valueOf(machine.getMaintainCost()));
            cmbDepartmentId.setValue(machine.getDepartmentId());
            lblDepartmentName.setText(department.getName());
            Image image = new Image(machine.getPath());
            imageView.setImage(image);

        } else {
            new Alert(Alert.AlertType.INFORMATION, "customer not found!").show();
        }
    }

    public void txtIdOnKeyReleased(KeyEvent keyEvent) {

    }
}
