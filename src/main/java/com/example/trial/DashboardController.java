package com.example.trial;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DashboardController {

    @FXML private TableView<Transaction> tableView;
    @FXML private TableColumn<Transaction, Integer> idCol;
    @FXML private TableColumn<Transaction, String> titleCol;
    @FXML private TableColumn<Transaction, String> categoryCol;
    @FXML private TableColumn<Transaction, Double> amountCol;
    @FXML private TableColumn<Transaction, String> typeCol;
    @FXML private TableColumn<Transaction, String> dateCol;

    @FXML private Label totalLabel;
    @FXML private PieChart categoryChart;
    @FXML private BarChart<String, Number> monthlyChart;

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
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        titleCol.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        categoryCol.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        amountCol.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
        typeCol.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty().asString());
    }

    private void loadData() {
        List<Transaction> transactions = storage.getTransactionsByUser(userId);
        ObservableList<Transaction> observableList = FXCollections.observableArrayList(transactions);
        tableView.setItems(observableList);

        updateTotals(transactions);
        updateCharts(transactions);
    }

    @FXML
    public void openAddForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trial/transaction.fxml"));
            Parent root = loader.load();

            TransactionController controller = loader.getController();
            controller.setDashboardController(this);
            controller.setUserId(userId);

            Stage stage = new Stage();
            stage.setTitle("Add Transaction");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshData() {
        loadData(); 
    }

    private void updateTotals(List<Transaction> transactions) {
        double totalExpenses = ProcessingModule.calculateTotal(transactions);
        double totalIncome = ProcessingModule.calculateTotalIncome(transactions);
        double balance = totalIncome - totalExpenses;

        totalLabel.setText(String.format(
                "Total Expenses: %.2f | Total Income: %.2f | Balance: %.2f",
                totalExpenses, totalIncome, balance));
    }

    private void updateCharts(List<Transaction> transactions) {

        Map<String, Double> categoryData = ProcessingModule.calculateCategoryBreakdown(transactions);
        categoryChart.getData().clear();
        categoryData.forEach((category, amount) ->
                categoryChart.getData().add(new PieChart.Data(category, amount))
        );


        Map<String, Double> monthlyData = ProcessingModule.calculateMonthlyTotals(transactions);
        monthlyChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Expenses");
        monthlyData.forEach((month, amount) ->
                series.getData().add(new XYChart.Data<>(month, amount))
        );
        monthlyChart.getData().add(series);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
