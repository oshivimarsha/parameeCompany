package lk.ijse.parameeIceCream.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lk.ijse.parameeIceCream.db.DbConnection;
import lk.ijse.parameeIceCream.model.Customer;
import lk.ijse.parameeIceCream.model.Employee;
import lk.ijse.parameeIceCream.model.Product;
import lk.ijse.parameeIceCream.model.Salary;
import lk.ijse.parameeIceCream.model.tm.EmployeeTm;
import lk.ijse.parameeIceCream.model.tm.SalaryTm;
import lk.ijse.parameeIceCream.repository.CustomerRepo;
import lk.ijse.parameeIceCream.repository.EmployeeRepo;
import lk.ijse.parameeIceCream.repository.ProductRepo;
import lk.ijse.parameeIceCream.repository.SalaryRepo;
import lk.ijse.parameeIceCream.util.Regex;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SalaryFormController {

    public AnchorPane rootNode;

    public TextField txtId;

    public TextField txtName;
    public TextField txtBasicSalary;
    public TextField txtAllowances;
    public TextField txtGrossSalary;
    public TextField txtAdvance;
    public TextField txtNetPayable;
    public TextField txtNic;
    public TableView<SalaryTm> tblSalaryCart;

    public TableColumn<?, ?> colId;

    public TableColumn<?, ?> colName;
    public TableColumn<?, ?> colNic;
    public TableColumn<?, ?> colBasicSalary;
    public TableColumn<?, ?> colAllowances;
    public TableColumn<?, ?> colGrossSalary;
    public TableColumn<?, ?> colAdvance;
    public TableColumn<?, ?> colNetPayable;

    public TextField txtSearchHere;

    public Image image;
    public ImageView imageView;
    public JFXButton btnBack;
    public JFXButton btnReport;
    public JFXButton btnSave;


    public void initialize() {
        setCellValueFactory();
        loadAllEmployees();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colBasicSalary.setCellValueFactory(new PropertyValueFactory<>("basicSalary"));
        colAllowances.setCellValueFactory(new PropertyValueFactory<>("allowences"));
        //colGrossSalary.setCellValueFactory(new PropertyValueFactory<>("grossSalary"));
        colAdvance.setCellValueFactory(new PropertyValueFactory<>("advance"));

    }

    private void loadAllEmployees() {
        ObservableList<SalaryTm> obList = FXCollections.observableArrayList();

        try {
            List<Salary> salaryList = SalaryRepo.getAll();
            Employee employee;
            SalaryTm tm;
            for (Salary salary : salaryList) {
                employee = EmployeeRepo.searchById(salary.getId());

                tm = new SalaryTm(
                        salary.getId(),
                        employee.getName(),
                        employee.getNic(),
                        salary.getBasicSalary(),
                        salary.getAllowences(),
                        salary.getAdvance()
                );

                obList.add(tm);
            }
            tblSalaryCart.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public void btnSaveOnAction(ActionEvent actionEvent) {
        String id = txtId.getText();
        double basicSalary = Double.parseDouble(txtBasicSalary.getText());
        double allowences = Double.parseDouble((txtAllowances.getText()));
        double advance = Double.parseDouble(txtAdvance.getText());

        if (isValied()) {
            Salary salary = new Salary(id, basicSalary, allowences, advance);

            try {
                boolean isSaved = SalaryRepo.save(salary);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Salary saved!").show();
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

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtNic.setText("");
        txtBasicSalary.setText("");
        txtAllowances.setText("");
        txtGrossSalary.setText("");
        txtAdvance.setText("");

    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtId.getText();
        // String name = txtName.getText();
        // String nic = txtNic.getText();
        double basicSalary = Double.parseDouble(txtBasicSalary.getText());
        double allowences = Double.parseDouble((txtAllowances.getText()));
        //double grossSalary = Double.parseDouble(txtGrossSalary.getText());
        double advance = Double.parseDouble(txtAdvance.getText());
        // String path = image.getUrl();

        Salary salary = new Salary(id, basicSalary, allowences, advance);

        boolean isUpdated = SalaryRepo.update(salary);
        if(isUpdated) {
            new Alert(Alert.AlertType.CONFIRMATION, "Salary updated!").show();
            clearFields();
            initialize();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtId.getText();

        boolean isDeleted = SalaryRepo.delete(id);
        if(isDeleted) {
            new Alert(Alert.AlertType.CONFIRMATION, "Salary deleted!").show();
            clearFields();
            initialize();
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) throws SQLException {
        clearFields();
    }

    public void txtIdOnAction(ActionEvent actionEvent) {
        String id = txtId.getText();
        try {
            Employee employee = EmployeeRepo.searchById(id);

            txtName.setText(employee.getName());
            txtNic.setText(employee.getNic());
            Image image = new Image(employee.getPath());
            imageView.setImage(image);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        txtName.requestFocus();
    }

    public void txtNameOnAction(ActionEvent actionEvent) {
        txtNic.requestFocus();
    }

    public void txtBasicSalaryOnAction(ActionEvent actionEvent) {
        txtAllowances.requestFocus();
    }

    public void txtAllowencesOnAction(ActionEvent actionEvent) {
        txtAdvance.requestFocus();
    }

    public void txtAdvanceOnAction(ActionEvent actionEvent) {

    }

    public void txtNicOnAction(ActionEvent actionEvent) {
        txtBasicSalary.requestFocus();
    }

    public void txtSearchHereOnAction(ActionEvent actionEvent) {
        btnSave.requestFocus();
    }

    public void btnReportPrintOnAction(ActionEvent actionEvent) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/reports/salary.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        Map<String,Object> data = new HashMap<>();
        data.put("id",txtId.getText());


        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, data, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane employeePane = FXMLLoader.load(this.getClass().getResource("/view/employee_form.fxml"));
        rootNode.getChildren().clear();
        rootNode.getChildren().add(employeePane);
    }

    public void txtIdOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.SID, txtId);
    }

    public void txtNameOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.NAME, txtName);
    }

    public void txtSalaryOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.PRICE, txtBasicSalary);
    }

    public void txtAllowencesOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.PRICE, txtAllowances);
    }

    public void txtAdvance(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.PRICE, txtAdvance);
    }

    public void txtNicOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.NIC, txtNic);
    }

    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.SID, txtId)) return false;
        if (!Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.NAME, txtName)) return false;
        if (!Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.NIC, txtNic)) return false;
        if (!Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.PRICE, txtBasicSalary)) return false;
        if (!Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.PRICE, txtAllowances)) return false;
        if (!Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.PRICE, txtAdvance)) return false;
        return true;
    }
}
