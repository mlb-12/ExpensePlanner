package com.example.trial;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class loginController implements Initializable {

    @FXML private TextField tf_username;
    @FXML private PasswordField tf_password_hidden;
    @FXML private TextField tf_password_visible;
    @FXML private CheckBox cb_show;
    @FXML private Button button_login;
    @FXML private Button button_sign_up;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tf_password_visible.setManaged(false);
        tf_password_visible.setVisible(false);
        tf_password_visible.textProperty().bindBidirectional(tf_password_hidden.textProperty());

        cb_show.setOnAction(event -> {
            boolean show = cb_show.isSelected();
            tf_password_visible.setVisible(show);
            tf_password_visible.setManaged(show);
            tf_password_hidden.setVisible(!show);
            tf_password_hidden.setManaged(!show);
        });

        button_sign_up.setOnAction(event ->
                DButils.changeScene(event, "sign-up.fxml", "Sign Up", null)
        );

        button_login.setOnAction(this::handleLogin);
    }

    private void handleLogin(ActionEvent event) {
        String username = tf_username.getText();
        String password = tf_password_hidden.isVisible()
                ? tf_password_hidden.getText()
                : tf_password_visible.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter both username and password.");
            return;
        }

        if (DButils.validateLogin(username, password)) {
            int userId = DButils.getUserId(username); // now works
            if (userId == -1) {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "User not found.");
                return;
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
                Parent root = loader.load();
                HomeController homeController = loader.getController();
                homeController.setUser(userId, username);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setTitle("Home");
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

