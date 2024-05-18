package lk.ijse.parameeIceCream.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import lk.ijse.parameeIceCream.db.DbConnection;
import lk.ijse.parameeIceCream.model.*;
import lk.ijse.parameeIceCream.model.tm.ProductManageTm;
import lk.ijse.parameeIceCream.repository.*;
import lk.ijse.parameeIceCream.util.Regex;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductManageFormController {
    public JFXComboBox<String> cmbDepartmentId;
    public TextField txtQtyAvailable;
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
    public TableView<ProductManageTm> tblIngredientCart;
    public TableColumn<?, ?> colIngredientsName;
    public TableColumn<?, ?> colUnitPrice;
    public TableColumn<?, ?> colQty;
    public TableColumn<?, ?> colTotal;
    public TableColumn<?, ?> colRemove;
    public TableColumn<?, ?> colId;
    public Label lblProductUnitPrice;
    public JFXButton btnAdd;
    public JFXButton btnBack;
    public AnchorPane rootNode;
    public TextField txtProductSearch;
    public JFXButton btnReport;

    private ObservableList<ProductManageTm> pList = FXCollections.observableArrayList();


    public void initialize() {
        setCellValueFactory();
        getProductName();
        getDepartmentId();
        getIngredientsName();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("ingId"));
        colIngredientsName.setCellValueFactory(new PropertyValueFactory<>("ingName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colRemove.setCellValueFactory(new PropertyValueFactory<>("remove"));

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

    public void btnAddIngrediantOnAction(ActionEvent actionEvent) {
        String id = lblIngredientsId.getText();
        String name = txtIngredientsName.getText();
        double unitPrice = Double.parseDouble(lblUnitPrice.getText());
        int qty = Integer.parseInt(txtQtyAvailable.getText());
        double total = qty * unitPrice;

        JFXButton btnRemove = new JFXButton("remove");
        btnRemove.setCursor(Cursor.HAND);

        btnRemove.setOnAction((e) -> {
            ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                int selectedIndex = tblIngredientCart.getSelectionModel().getSelectedIndex();
                pList.remove(selectedIndex);

                tblIngredientCart.refresh();

            }
        });

        for (int i = 0; i < tblIngredientCart.getItems().size(); i++) {
            if (name.equals(colIngredientsName.getCellData(i))) {

                ProductManageTm tm = pList.get(i);
                qty += tm.getQty();
                total = qty * unitPrice;

                tm.setQty(qty);
                tm.setTotal(total);

                tblIngredientCart.refresh();

                return;
            }
        }

        if (isValid()) {
                ProductManageTm tm = new ProductManageTm(id,name, unitPrice, qty, total,btnRemove);
                pList.add(tm);
            } else {
                new Alert(Alert.AlertType.CONFIRMATION, "Wrong Input!").show();
            }

        tblIngredientCart.setItems(pList);
        calculateNetTotal();
        txtIngredientsName.requestFocus();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String id = txtId.getText();
        String name = txtName.getText();
        String category = txtCategory.getText();
        String description = txtDescription.getText();
        int qtyAvailable = Integer.parseInt(txtQtyAvailable.getText());
        double unitPrice = calculateNetTotal();
        String departmentId = cmbDepartmentId.getValue();
        String path = image.getUrl();

        var product = new Product(id, name, category, description, qtyAvailable, unitPrice, departmentId, path);

        List<IngredientsProduct> ipList = new ArrayList<>();

        for (int i = 0; i < tblIngredientCart.getItems().size(); i++) {
            ProductManageTm tm = pList.get(i);

            IngredientsProduct od = new IngredientsProduct(
                    product.getId(),
                    tm.getIngId(),
                    tm.getQty()*Integer.parseInt(txtQty.getText()),
                    tm.getUnitPrice()
            );

            ipList.add(od);
        }

        CreateProduct cp = new CreateProduct(product, ipList);

        try {
            System.out.println("cp = "+cp);
            boolean isPlaced = CreateProductRepo.createProduct(cp);
            if(isPlaced) {
                new Alert(Alert.AlertType.CONFIRMATION, "Create Product!").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "Create Product Unsuccessfully!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String id = txtId.getText();
        String name = txtName.getText();
        String category = txtCategory.getText();
        String description = txtDescription.getText();
        int qtyAvailable = Integer.parseInt(txtQtyAvailable.getText());
        double unitPrice = calculateNetTotal();
        String departmentId = cmbDepartmentId.getValue();
        String path = image.getUrl();

        var product = new Product(id, name, category, description, qtyAvailable, unitPrice, departmentId, path);

        List<IngredientsProduct> ipList = new ArrayList<>();

        for (int i = 0; i < tblIngredientCart.getItems().size(); i++) {
            ProductManageTm tm = pList.get(i);

            IngredientsProduct od = new IngredientsProduct(
                    product.getId(),
                    tm.getIngId(),
                    tm.getQty()*Integer.parseInt(txtQty.getText()),
                    tm.getUnitPrice()
            );

            ipList.add(od);
        }

        CreateProduct cp = new CreateProduct(product, ipList);

        try {
            System.out.println("cp = "+cp);
            boolean isPlaced = CreateProductRepo.updateProduct(cp);
            if(isPlaced) {
                new Alert(Alert.AlertType.CONFIRMATION, "Updated Product!").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "Update Product Unsuccessfully!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {

    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtCategory.setText("");
        txtDescription.setText("");
        txtQtyAvailable.setText("");
        cmbDepartmentId.setValue(null);
        lblDepartmentName.setText("");
        txtProductSearch.setText("");
        txtIngredientsName.setText("");
        lblIngredientsId.setText("");
        lblUnitPrice.setText("");
        lblQtyOnHand.setText("");
        txtQty.setText("");
        imageView.setImage(null);
    }

    private double calculateNetTotal() {
        double netTotal = 0.0; // Change to double to ensure proper calculation
        for (int i = 0; i < tblIngredientCart.getItems().size(); i++) {
            netTotal += (double) colTotal.getCellData(i);
        }

        // Calculate the profit
        double profit = netTotal * 0.20;

        // Add profit to the net total
        double totalWithProfit = netTotal + profit;

        // Update the UI with the new total
        lblProductUnitPrice.setText(String.valueOf(totalWithProfit));

        return totalWithProfit;
    }

    public void txtQty(ActionEvent actionEvent) {
        btnAdd.requestFocus();
    }

    public void txtIdOnAction(ActionEvent actionEvent) {
        txtName.requestFocus();
    }

    public void txtNameOnAction(ActionEvent actionEvent) {
        txtCategory.requestFocus();
    }

    public void txtDescriptionOnAction(ActionEvent actionEvent) {
        txtQtyAvailable.requestFocus();
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane employeePane = FXMLLoader.load(this.getClass().getResource("/view/products_form.fxml"));
        rootNode.getChildren().clear();
        rootNode.getChildren().add(employeePane);
    }

    private void getProductName() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> nameList = ProductRepo.getName();

            for(String name : nameList) {
                obList.add(name);
            }
            TextFields.bindAutoCompletion(txtProductSearch, obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    List<ProductManageTm> pm = new ArrayList<>();

    public void txtProductSearchOnAction(ActionEvent actionEvent) throws SQLException {
        // Create a "Remove" button with a hand cursor
        JFXButton btnRemove = new JFXButton("remove");
        btnRemove.setCursor(Cursor.HAND);

        // Set up the action for the remove button
        btnRemove.setOnAction((e) -> {
            ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                int selectedIndex = tblIngredientCart.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0) {
                    pList.remove(selectedIndex);
                    tblIngredientCart.refresh();
                }
            }
        });

        String name = txtProductSearch.getText();

        Product product = ProductRepo.searchByName(name);
        if (product != null) {

            List<IngredientsProduct> ingredientsProduct = IngredientsProductRepo.searchById(product.getId());
            Department department = DepartmentRepo.searchById(product.getDepartmentId());

            txtId.setText(product.getId());
            txtName.setText(product.getName());
            txtCategory.setText(product.getCategory());
            txtDescription.setText(product.getDescription());
            txtQtyAvailable.setText(String.valueOf(product.getQtyAvailable()));
            cmbDepartmentId.setValue(department.getId());
            lblDepartmentName.setText(department.getName());
            Image image = new Image(product.getPath());
            imageView.setImage(image);
            lblProductUnitPrice.setText(String.valueOf(product.getUnitPrice()));

            pList.clear();

            for (IngredientsProduct inp : ingredientsProduct) {
                List<Ingredient> ingredientList = IngredientRepo.searchById(inp.getIngredientId());
                for (Ingredient ingre : ingredientList) {
                    ProductManageTm tm = new ProductManageTm(
                            ingre.getId(),
                            ingre.getName(),
                            inp.getUnitPrice(),
                            inp.getQty(),
                            inp.getUnitPrice() * inp.getQty(),
                            btnRemove
                    );
                    pList.add(tm);


                }
            }

            tblIngredientCart.setItems(pList);
            setCellValueFactory();
            txtIngredientsName.requestFocus();
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Item not found!").show();
        }
    }

    public void txtQtyOnKeyRelesed(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.QTY, txtQtyAvailable);
    }

    public void txtIdOnKeyRelesed(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.PID, txtId);
    }

    public boolean isValid() {
        if (!Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.QTY, txtQtyAvailable)) return false;
        if (!Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.PID, txtId)) return false;
        return true;
    }
}
