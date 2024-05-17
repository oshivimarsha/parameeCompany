package lk.ijse.parameeIceCream.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
//import jdk.internal.icu.text.UCharacterIterator;
import javafx.stage.Stage;
import lk.ijse.parameeIceCream.db.DbConnection;
import lk.ijse.parameeIceCream.util.Regex;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {
    public TextField txtPassword;
    public AnchorPane rootNode;
    public TextField txtUserName;
    public Button btnLogin;

    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {
        String userId = txtUserName.getText();
        String pw = txtPassword.getText();

            try {
                if (isValied()) {
                    checkCredential(userId, pw);
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }

    }

    private void checkCredential(String userId, String password) throws SQLException, IOException {
        String sql = "SELECT userName, password, email FROM users WHERE userName = ?";

        Connection connection;
        connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, userId);


        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String dbPw = resultSet.getString("password");

            if(password.equals(dbPw)) {
                navigateToTheDashboard(userId);
            } else {
                new Alert(Alert.AlertType.ERROR, "sorry! password is incorrect!").show();
            }

        } else {
            new Alert(Alert.AlertType.INFORMATION, "sorry! user id can't be find!").show();
        }
    }
    private void navigateToTheDashboard(String user_name) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/sidepannel_form.fxml"));
        Parent dashboardRoot = loader.load();
        SidePannelFormController controller = loader.getController();
        controller.setUserName(user_name); // Pass the username to the DashboardFormController
        Scene scene = new Scene(dashboardRoot);
        Stage stage = (Stage) rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Dashboard Form");
    }

    public void linkRegistrationOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/register_form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Register Form");
    }

    public void txtEnterUserNameOnAction(ActionEvent actionEvent) {
        txtPassword.requestFocus();
    }

    public void txtEnterPasswordOnAction(ActionEvent actionEvent) {
        btnLogin.requestFocus();
    }

    public void txtUserNameOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.NAME,txtUserName);
    }

    public void txtPasswordOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.PASSWORD, txtPassword);
    }

    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.NAME, txtUserName)) return false;
        //if (!Regex.setTextColor(lk.ijse.parameeIceCream.util.TextField.PASSWORD, txtPassword)) return false;
        return true;
    }

}
