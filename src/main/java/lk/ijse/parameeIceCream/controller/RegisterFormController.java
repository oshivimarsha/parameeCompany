package lk.ijse.parameeIceCream.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.parameeIceCream.db.DbConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterFormController {
    public AnchorPane rootNode;
    public TextField txtEmail;
    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPw;

    public void btnRegisterOnAction(ActionEvent actionEvent) {
        String name = txtName.getText();
        String password = txtPw.getText();
        String email = txtEmail.getText();

        try {
            boolean isSaved = saveUser(name, password, email);
            if(isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "user saved!").show();
                navigateLogin();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (IOException e) {


        }
    }

    public void navigateLogin() throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Login Form");
    }

    private boolean saveUser(String name, String password, String email) throws SQLException {
        String sql = "INSERT INTO users VALUES(?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, name);
        pstm.setObject(2, password);
        pstm.setObject(3, email);

        return pstm.executeUpdate() > 0;
    }

    public void txtPasswordOnAction(ActionEvent actionEvent) {
        txtEmail.requestFocus();
    }

    public void txtUserNameOnAction(ActionEvent actionEvent) {
        txtPw.requestFocus();
    }
}
