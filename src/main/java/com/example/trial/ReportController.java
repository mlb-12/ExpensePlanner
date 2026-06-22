package com.example.trial;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class ReportController {

    @FXML private TableView<Transaction> tableView;
    @FXML private TableColumn<Transaction, Integer> idCol;
    @FXML private TableColumn<Transaction, String> titleCol;
    @FXML private TableColumn<Transaction, String> categoryCol;
    @FXML private TableColumn<Transaction, Double> amountCol;
    @FXML private TableColumn<Transaction, String> typeCol;
    @FXML private TableColumn<Transaction, String> dateCol;

    @FXML private Button buttonDelete;
    @FXML private Label totalLabel;

    private final StorageModule storage = new StorageModule();
    private int userId;

    @FXML
    public void initialize() {
        setupTable();

    }

    public void setUserId(int userId) {
        this.userId = userId;
        loadData();
    }

    private void setupTable() {
        tableView.setEditable(true);

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        titleCol.setOnEditCommit(event -> {
            Transaction t = event.getRowValue();
            t.setTitle(event.getNewValue());
            updateTransaction(t);
        });

        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setCellFactory(TextFieldTableCell.forTableColumn());
        categoryCol.setOnEditCommit(event -> {
            Transaction t = event.getRowValue();
            t.setCategory(event.getNewValue());
            updateTransaction(t);
        });

        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountCol.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.DoubleStringConverter()));
        amountCol.setOnEditCommit(event -> {
            Transaction t = event.getRowValue();
            t.setAmount(event.getNewValue());
            updateTransaction(t);
        });

        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        typeCol.setOnEditCommit(event -> {
            Transaction t = event.getRowValue();
            t.setType(event.getNewValue());
            updateTransaction(t);
        });

        dateCol.setCellValueFactory(cell -> cell.getValue().dateProperty().asString());
        dateCol.setCellFactory(TextFieldTableCell.forTableColumn());
        dateCol.setOnEditCommit(event -> {
            Transaction t = event.getRowValue();
            try {
                LocalDate newDate = LocalDate.parse(event.getNewValue());
                t.setDate(newDate);
                updateTransaction(t);
            } catch (Exception e) {
                showAlert("Invalid Date", "Please enter a valid date in the format YYYY-MM-DD.");
                tableView.refresh();
            }
        });
    }

    private void updateTransaction(Transaction t) {
        storage.updateTransaction(
                t.getId(),
                t.getUserId(),
                t.getTitle(),
                t.getCategory(),
                t.getAmount(),
                Date.valueOf(t.getDate()),
                t.getType()
        );
        updateTotals(tableView.getItems());
    }

    private void loadData() {
        List<Transaction> transactions = storage.getTransactionsByUser(userId);
        ObservableList<Transaction> observableList = FXCollections.observableArrayList(transactions);
        tableView.setItems(observableList);
        updateTotals(observableList);
    }


    @FXML
    private void handleDelete(ActionEvent event) {
        Transaction selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a transaction to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete this transaction?");
        alert.setContentText("This action cannot be undone.");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {

            storage.deleteTransaction(selected.getId(), userId);

            int index = tableView.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                tableView.getItems().remove(index);
                tableView.refresh();
            }

            updateTotals(tableView.getItems());
        }
    }


    private void updateTotals(ObservableList<Transaction> transactions) {
        double totalExpenses = ProcessingModule.calculateTotal(transactions);
        double totalIncome = ProcessingModule.calculateTotalIncome(transactions);
        double balance = totalIncome - totalExpenses;

        totalLabel.setText(String.format(
                "Total Expenses: %.2f | Total Income: %.2f | Balance: %.2f",
                totalExpenses, totalIncome, balance
        ));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void refresh() {
        loadData();
    }
}
