package com.example.trial;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class signupController {

    @FXML private TextField tf_email;
    @FXML private TextField tf_username1;
    @FXML private PasswordField tf_passwordset;
    @FXML private PasswordField tf_password1;
    @FXML private Button button_sign_up;
    @FXML private Button button_login;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    @FXML
    public void initialize() {
        button_sign_up.setOnAction(event -> handleSignUp());
        button_login.setOnAction(event -> DButils.changeScene(
                new javafx.event.ActionEvent(button_login, null),
                "login.fxml", "Login", null));
    }

    private void handleSignUp() {
        String email = tf_email.getText();
        String username = tf_username1.getText();
        String password = tf_passwordset.getText();
        String confirmPassword = tf_password1.getText();

        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(AlertType.ERROR, "Form Error!", "Please fill in all fields.");
            return;
        }

        if (!Pattern.matches(EMAIL_REGEX, email)) {
            showAlert(AlertType.ERROR, "Email Error", "Please enter a valid email.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(AlertType.ERROR, "Password Error", "Passwords do not match.");
            return;
        }

        // Save to DB
        try (Connection conn = DButils.getConnection()) {
            String query = "INSERT INTO users(username,email,password) VALUES(?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                showAlert(AlertType.INFORMATION, "Success", "Registration successful!");
                clearFields();
            } else {
                showAlert(AlertType.ERROR, "Failed", "Could not register. Try again.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    private void showAlert(AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void clearFields() {
        tf_email.clear();
        tf_username1.clear();
        tf_passwordset.clear();
        tf_password1.clear();
    }
}

