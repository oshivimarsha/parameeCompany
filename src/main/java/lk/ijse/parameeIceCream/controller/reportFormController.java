package lk.ijse.parameeIceCream.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import lk.ijse.parameeIceCream.model.Department;
import lk.ijse.parameeIceCream.model.Ingredient;
import lk.ijse.parameeIceCream.model.Product;
import lk.ijse.parameeIceCream.model.tm.ReportTm;
import lk.ijse.parameeIceCream.repository.DepartmentRepo;
import lk.ijse.parameeIceCream.repository.IngredientRepo;
import lk.ijse.parameeIceCream.repository.ProductRepo;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class reportFormController {
    public JFXComboBox<String> cmbDepartmentId;
    public TextField txtQtyAvailable;
    public TextField txtUnitPrice;
    public Label lblDepartmentName;
    public TextField txtId;
    public TextField txtName;
    public TextField txtDescription;
    public TextField txtCategory;
    public TextField txtIngredientsName;
    public Label lblIngredientsId;
    public Label lblUnitPrice;
    public Label lblQtyOnHand;
    public TextField txtQty;
    public AnchorPane main_form;

    public Image image;

    public ImageView imageView;
    public TableView<ReportTm> tblIngredientCart;
    public TableColumn<?, ?> colIngredientsName;
    public TableColumn<?, ?> colUnitPrice;
    public TableColumn<?, ?> colQty;


    public void initialize() {
       // setCellValueFactory();
       // loadAllDepartment();
        getDepartmentId();
        getIngredientsName();
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

    public void cmbDepartmentIdOnAction(ActionEvent actionEvent) {
        String id = cmbDepartmentId.getValue();
        try {
            Department department = DepartmentRepo.searchById(id);

            lblDepartmentName.setText(department.getName());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void txtCategoryOnAction(ActionEvent actionEvent) {

    }

    public void txtSearchOnAction(ActionEvent actionEvent) {

    }

    public void txtQtyOnAction(ActionEvent actionEvent) {

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

    private void getIngredientsName() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> nameList = IngredientRepo.getNames();

            for(String name : nameList) {
                obList.add(name);
            }

            TextFields.bindAutoCompletion(txtIngredientsName, obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void txtIngredientsNameOnAction(ActionEvent actionEvent) {
        String name = txtIngredientsName.getText();
        try {
            Ingredient ingredient = IngredientRepo.searchByName(name);

            lblIngredientsId.setText(ingredient.getId());
            lblUnitPrice.setText(String.valueOf(ingredient.getUnitPrice()));
            lblQtyOnHand.setText(String.valueOf(ingredient.getQtyInStock()));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        txtQty.requestFocus();
    }
}
