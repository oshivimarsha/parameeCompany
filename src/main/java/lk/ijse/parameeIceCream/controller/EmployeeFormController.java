package lk.ijse.parameeIceCream.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
//import com.sun.javafx.tk.quantum.PaintRenderJob;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.parameeIceCream.db.DbConnection;
import lk.ijse.parameeIceCream.model.Customer;
import lk.ijse.parameeIceCream.model.Department;
import lk.ijse.parameeIceCream.model.Employee;
import lk.ijse.parameeIceCream.model.tm.CustomerTm;
import lk.ijse.parameeIceCream.model.tm.EmployeeTm;
import lk.ijse.parameeIceCream.repository.CustomerRepo;
import lk.ijse.parameeIceCream.repository.DepartmentRepo;
import lk.ijse.parameeIceCream.repository.EmployeeRepo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeFormController {
    public ImageView imageView;
    public AnchorPane main_form;

    public Image image;
    public TextField txtEmail;
    public TextField txtPosition;
    public TextField txtSalary;
    public TextField txtId;
    public TextField txtName;
    public TextField txtNic;
    public JFXComboBox<String> cmbDepartmentId;
    public Label lblDepartmentName;
    public TextField txtAddress;
    public TextField txtTel;
    public TextField txtSearchHere;
    public DatePicker txtDOB;
    public DatePicker txtRegisterDate;
    public TableColumn<?, ?> colId;
    public TableColumn<?, ?> colName;
    public TableColumn<?, ?> colNic;
    public TableColumn<?, ?> colAddress;
    public TableColumn<?, ?> colEmail;
    public TableColumn<?, ?> colTel;
    public TableColumn<?, ?> colDob;
    public TableColumn<?, ?> colRegisterDate;
    public TableColumn<?, ?> colPosition ;
    public TableColumn<?, ?> colSalary;
    public TableColumn<?, ?> colDepartmentId;
    public TableView<EmployeeTm> tblEmployeeCart;
    public JFXButton btnManageSalary;
    public AnchorPane rootNode;


    public void initialize() {
        setCellValueFactory();
        loadAllEmployees();
        getDepartmentId();
        getEmployeeTels();

    }

    private void getEmployeeTels() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> telList = EmployeeRepo.getTels();

            for(String tel : telList) {
                obList.add(tel);
            }
            TextFields.bindAutoCompletion(txtSearchHere, obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void loadAllEmployees() {
        ObservableList<EmployeeTm> obList = FXCollections.observableArrayList();

        try {
            List<Employee> employeeList = EmployeeRepo.getAll();
            for (Employee employee : employeeList) {
                EmployeeTm tm = new EmployeeTm(
                        employee.getId(),
                        employee.getName(),
                        employee.getNic(),
                        employee.getAddress(),
                        employee.getEmail(),
                        employee.getTel(),
                        employee.getDob(),
                        employee.getRegisterDate(),
                        employee.getPosition(),
                        employee.getSalary(),
                        employee.getDepartmentId()
                        //employee.getDepartmentName()
                );

                obList.add(tm);
            }

            tblEmployeeCart.setItems(obList);
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
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colRegisterDate.setCellValueFactory(new PropertyValueFactory<>("registerDate"));
        colPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colDepartmentId.setCellValueFactory(new PropertyValueFactory<>("departmentId"));
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
        String nic = txtNic.getText();

        Employee employee = EmployeeRepo.searchByNic(String.valueOf(txtNic));
        if (employee != null) {
            txtId.setText(employee.getId());
            txtName.setText(employee.getName());
            txtNic.setText(employee.getNic());
            txtAddress.setText(employee.getAddress());
            txtEmail.setText(employee.getEmail());
            txtTel.setText(employee.getTel());
            txtDOB.setValue(employee.getDob().toLocalDate());
            txtRegisterDate.setValue(employee.getRegisterDate().toLocalDate());
            txtPosition.setText(employee.getPosition());
            txtSalary.setText(String.valueOf(employee.getSalary()));
            cmbDepartmentId.setValue(employee.getDepartmentId());
           // lblDepartmentName.setText(employee.getDepartmentName());


        } else {
            new Alert(Alert.AlertType.INFORMATION, "employee not found!").show();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String id = txtId.getText();
        String name = txtName.getText();
        String nic = txtNic.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();
        String tel = txtTel.getText();
        Date dob = Date.valueOf(txtDOB.getValue());
        Date registerDate = Date.valueOf(txtRegisterDate.getValue());
        String position = txtPosition.getText();
        double salary = Double.parseDouble(txtSalary.getText());
        String departmentId = cmbDepartmentId.getValue();
        String path = image.getUrl();

        Employee employee = new Employee(id, name, nic, address, email, tel, dob, registerDate, position, salary, departmentId, path);

        try {
            boolean isSaved = EmployeeRepo.save(employee);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "employee saved!").show();
                clearFields();
                initialize();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtId.getText();
        String name = txtName.getText();
        String nic = txtNic.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();
        String tel = txtTel.getText();
        Date dob = Date.valueOf(txtDOB.getValue());
        Date registerDate = Date.valueOf(txtRegisterDate.getValue());
        String position = txtPosition.getText();
        double salary = Double.parseDouble(txtSalary.getText());
        String departmentId = cmbDepartmentId.getValue();
        String path = image.getUrl();

        Employee employee = new Employee(id, name, nic, address, email, tel, dob, registerDate, position, salary, departmentId, path);

        boolean isUpdated = EmployeeRepo.update(employee);
        if(isUpdated) {
            new Alert(Alert.AlertType.CONFIRMATION, "employee updated!").show();
            clearFields();
            initialize();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException {
        String nic = txtNic.getText();

        boolean isDeleted = EmployeeRepo.delete(nic);
        if(isDeleted) {
            new Alert(Alert.AlertType.CONFIRMATION, "employee deleted!").show();
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
        txtNic.setText("");
        txtAddress.setText("");
        txtEmail.setText("");
        txtTel.setText("");
        txtDOB.setValue(null);
        txtRegisterDate.setValue(null);
        txtPosition.setText("");
        txtSalary.setText("");
        cmbDepartmentId.setValue("");
        lblDepartmentName.setText("");
        txtSearchHere.setText("");
        imageView.setImage(null);
    }

    private void getDepartmentId() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> cateList = DepartmentRepo.getIds();

            for(String category : cateList) {
                obList.add(category);
            }

            cmbDepartmentId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cmbDepartmentIdOnAction(ActionEvent actionEvent){
        String id = cmbDepartmentId.getValue();
        try {
            Department department = DepartmentRepo.searchById(id);

            lblDepartmentName.setText(department.getName());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void txtSearchHereOnAction(ActionEvent actionEvent) throws SQLException {
        String tel = txtSearchHere.getText();
        //String tell = txtSearchHere.getText();

        Employee employee = EmployeeRepo.searchByTel(tel);
       // Department department = DepartmentRepo.searchById(employee.getDepartmentId());
        if (employee != null) {
            txtId.setText(employee.getId());
            txtName.setText(employee.getName());
            txtNic.setText(employee.getNic());
            txtAddress.setText(employee.getAddress());
            txtEmail.setText(employee.getEmail());
            txtTel.setText(employee.getTel());
            txtDOB.setValue(employee.getDob().toLocalDate());
            txtRegisterDate.setValue(employee.getRegisterDate().toLocalDate());
            txtPosition.setText(employee.getPosition());
            txtSalary.setText(String.valueOf(employee.getSalary()));
            cmbDepartmentId.setValue(employee.getDepartmentId());
          //  lblDepartmentName.setText(department.getName());
            Image image = new Image(employee.getPath());
            imageView.setImage(image);


        } else {
            new Alert(Alert.AlertType.INFORMATION, "employee not found!").show();
        }
    }

    public void btnReportPrintOnAction(ActionEvent actionEvent) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/reports/EmployeeReports.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        Map<String,Object> data = new HashMap<>();
        data.put("employeeId",txtId.getText());


        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, data, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);

    }

    public void btnManageSalaryOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane orderPane = FXMLLoader.load(this.getClass().getResource("/view/salaryForm.fxml"));
        rootNode.getChildren().clear();
        rootNode.getChildren().add(orderPane);
    }
}
