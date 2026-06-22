package com.example.trial;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.sql.Date;
import java.time.LocalDate;

public class TransactionController {

    @FXML private ToggleButton Toggle_expense;
    @FXML private ToggleButton Toggle_income;
    @FXML private ToggleGroup transactionTypeGroup;
    @FXML private DatePicker date_box;
    @FXML private TextField button_title;
    @FXML private TextField button_amount;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private Button button_add;

    private DashboardController dashboardController;
    private ReportController reportController;
    private final StorageModule storage = new StorageModule();
    private int userId;

    public void setUserId(int userId) {
        this.userId = userId;
    }
    @FXML
    public void initialize() {
        transactionTypeGroup = new ToggleGroup();
        Toggle_expense.setToggleGroup(transactionTypeGroup);
        Toggle_income.setToggleGroup(transactionTypeGroup);

        categoryComboBox.getItems().addAll("Bills", "Food", "Other", "Salary", "Shopping", "Transport", "Travel");

        categoryComboBox.setCellFactory(listView -> new ListCell<String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                    setText(item);
                    switch (item) {
                        case "Bills" -> imageView.setImage(new Image(getClass().getResource("/com/example/trial/bill.png").toExternalForm()));
                        case "Food" -> imageView.setImage(new Image(getClass().getResource("/com/example/trial/food.png").toExternalForm()));
                        case "Other" -> imageView.setImage(new Image(getClass().getResource("/com/example/trial/other.png").toExternalForm()));
                        case "Salary" -> imageView.setImage(new Image(getClass().getResource("/com/example/trial/salary.png").toExternalForm()));
                        case "Shopping" -> imageView.setImage(new Image(getClass().getResource("/com/example/trial/shopping.png").toExternalForm()));
                        case "Transport" -> imageView.setImage(new Image(getClass().getResource("/com/example/trial/transport.png").toExternalForm()));
                        case "Travel" -> imageView.setImage(new Image(getClass().getResource("/com/example/trial/travel.png").toExternalForm()));
                    }
                    imageView.setFitWidth(18);
                    imageView.setFitHeight(18);
                    setGraphic(imageView);
                } else {
                    setText(null);
                    setGraphic(null);
                }
            }
        });

        categoryComboBox.setButtonCell(categoryComboBox.getCellFactory().call(null));
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public void setReportController(ReportController reportController) {
        this.reportController = reportController;
    }

    @FXML
    public void handleAddTransaction(ActionEvent event) {
        try {
            String title = button_title.getText();
            String category = categoryComboBox.getValue();
            double amount = Double.parseDouble(button_amount.getText());
            LocalDate date = date_box.getValue();
            String type = transactionTypeGroup.getSelectedToggle() == Toggle_expense ? "expense" : "income";

            storage.saveTransaction(userId, title, category, amount, Date.valueOf(date), type);
            System.out.println("Saving transaction for userId = " + userId);

            if (dashboardController != null) dashboardController.refreshData();

            button_title.clear();
            button_amount.clear();
            categoryComboBox.getSelectionModel().clearSelection();
            date_box.setValue(null);
            transactionTypeGroup.selectToggle(null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
