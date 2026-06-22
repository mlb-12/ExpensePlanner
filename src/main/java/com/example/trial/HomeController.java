package com.example.trial;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class HomeController {

    @FXML private BorderPane mainPane;
    @FXML private Button button_logout;

    private int userId;
    private String username;

    public void setUser(int userId, String username) {
        this.userId = userId;
        this.username = username;
        showDashboard(); // load dashboard immediately
    }


    @FXML
    private void showDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.setUserId(userId);
            mainPane.setCenter(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void showTransactions(ActionEvent event) {
        loadContentWithUser("income.fxml");
    }

    @FXML
    private void showReport(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("report.fxml"));
            Parent root = loader.load();

            ReportController controller = loader.getController();
            controller.setUserId(userId);

            mainPane.setCenter(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadContentWithUser(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Object controller = loader.getController();
            try {
                controller.getClass().getMethod("setUserId", int.class).invoke(controller, userId);
            } catch (NoSuchMethodException ignored) {

            }

            mainPane.setCenter(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        DButils.changeScene(event, "login.fxml", "Login", null);
    }
}
