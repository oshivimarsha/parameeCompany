package lk.ijse.parameeIceCream.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import lk.ijse.parameeIceCream.db.DbConnection;
import lk.ijse.parameeIceCream.model.Department;
import lk.ijse.parameeIceCream.model.Product;
import lk.ijse.parameeIceCream.model.tm.ProductTm;
import lk.ijse.parameeIceCream.repository.DepartmentRepo;
import lk.ijse.parameeIceCream.repository.ProductRepo;
import lk.ijse.parameeIceCream.util.Regex;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsFormController {

    public Image image;
    public ImageView imageView;
    public TableView<ProductTm> tblProductCart;

    public TableColumn<?, ?> colId;

    public TableColumn<?, ?> colName;
    public TableColumn<?, ?> colCategory;
    public TableColumn<?, ?> colDescription;
    public TableColumn<?, ?> colQtyAvailable;
    public TableColumn<?, ?> colUnitPrice;
    public TableColumn<?, ?> colDepartmentId;
    public TableColumn<?, ?> colDepartmentName;
    public JFXComboBox<String> cmbDepartmentId;
    public TextField txtQtyAvailable;
    public TextField txtUnitPrice;
    public Label lblDepartmentName;

    public TextField txtId;

    public TextField txtName;
    public TextField txtDescription;
    public AnchorPane main_form;
    public TextField txtSearchHere;
    public TextField txtCategory;
    public AnchorPane rootNode;
    public JFXButton btnReport;


    public void initialize() {
        setCellValueFactory();
        loadAllProduct();
        getDepartmentId();
        getProductName();

    }

    private void getProductName() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> nameList = ProductRepo.getName();

            for(String name : nameList) {
                obList.add(name);
            }
            TextFields.bindAutoCompletion(txtSearchHere, obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAllProduct() {
        ObservableList<ProductTm> obList = FXCollections.observableArrayList();

        try {
            List<Product> productList = ProductRepo.getAll();
            for (Product product : productList) {
                ProductTm tm = new ProductTm(
                        product.getId(),
                        product.getName(),
                        product.getCategory(),
                        product.getDescription(),
                        product.getQtyAvailable(),
                        product.getUnitPrice(),
                        product.getDepartmentId()
                );

                obList.add(tm);
            }

            tblProductCart.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQtyAvailable.setCellValueFactory(new PropertyValueFactory<>("qtyAvailable"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colDepartmentId.setCellValueFactory(new PropertyValueFactory<>("departmentId"));
    }

    private void getDepartmentId() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> idList = DepartmentRepo.getIds();

            for(String id : idList) {
                obList.add(id);
            }

            cmbDepartmentId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnImportOnAction(ActionEvent actionEvent) {
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File", new String[]{"*png", "*jpg"}));
        File file = openFile.showOpenDialog(this.main_form.getScene().getWindow());
        if (file != null) {
            //data.path = file.getAbsolutePath();
            this.image = new Image(file.toURI().toString(), 120.0, 127.0, false, true);
            this.imageView.setImage(this.image);
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtCategory.setText("");
        txtDescription.setText("");
        txtQtyAvailable.setText("");
        txtUnitPrice.setText("");
        cmbDepartmentId.setValue("");
        txtSearchHere.setText("");
        imageView.setImage(null);
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException {
        String name = txtName.getText();

        boolean isDeleted = ProductRepo.delete(name);
        if(isDeleted) {
            new Alert(Alert.AlertType.CONFIRMATION, "Product deleted!").show();
            clearFields();
            initialize();
        }
    }
    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtId.getText();
        String name = txtName.getText();
        String category = txtCategory.getText();
        String description = txtDescription.getText();
        int qtyAvailable = Integer.parseInt(txtQtyAvailable.getText());
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        String departmentId = cmbDepartmentId.getValue();
        String path = image.getUrl();

        if (isValid()) {
            Product product = new Product(id, name, category, description, qtyAvailable, unitPrice, departmentId, path);

            boolean isUpdated = ProductRepo.update(product);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "employee updated!").show();
                clearFields();
                initialize();
            }
        } else {
            new Alert(Alert.AlertType.CONFIRMATION, "Wrong Input!").show();
        }
    }

    public void cmbDepartmentIdOnAction(ActionEvent actionEvent) {
        String id = cmbDepartmentId.getValue();
        try {
            Department department = DepartmentRepo.searchById(id);

            lblDepartmentName.setText(department.getName());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void txtSearchHereOnAction(ActionEvent actionEvent) throws SQLException {
        String name = txtSearchHere.getText();

        Product product = ProductRepo.searchByName(name);

        if (product != null) {
            txtId.setText(product.getId());
            txtName.setText(product.getName());
            txtCategory.setText(product.getCategory());
            txtDescription.setText(product.getDescription());
            txtQtyAvailable.setText(String.valueOf(product.getQtyAvailable()));
            txtUnitPrice.setText(String.valueOf(product.getUnitPrice()));
            cmbDepartmentId.setValue(product.getDepartmentId());
            Image image = new Image(product.getPath());
            imageView.setImage(image);

        } else {
            new Alert(Alert.AlertType.INFORMATION, "Product not found!").show();
        }
    }

    public void txtQtyOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.QTY, txtQtyAvailable);
    }

    public void txtPriceOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.PRICE, txtUnitPrice);
    }

    public void txtIdOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.PID, txtId);
    }

    public void txtNameOnKeyReleased(KeyEvent keyEvent) {
      //  Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.NAME, txtName);
    }

    public boolean isValid() {
        if (!Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.QTY, txtQtyAvailable)) return false;
        if (!Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.PRICE, txtUnitPrice)) return false;
        if (!Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.PID, txtId)) return false;
      //  if (!Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.NAME, txtName)) return false;
        return true;
    }

    public void btnProductManageOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane orderPane = FXMLLoader.load(this.getClass().getResource("/view/Productmanage.fxml"));
        rootNode.getChildren().clear();
        rootNode.getChildren().add(orderPane);
    }

    public void btnReportPrintOnAction(ActionEvent actionEvent) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/reports/DailyProfit.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);


        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, null, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
    }
}
