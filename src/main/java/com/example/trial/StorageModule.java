package com.example.trial;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StorageModule {

    private static final String URL = "jdbc:mysql://localhost:3306/expense_planner";
    private static final String USER = "root";
    private static final String PASSWORD = "skkwxws12";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public int saveTransaction(int userId, String title, String category, double amount, Date date, String type) {
        String sql = "INSERT INTO transactions (user_id, title, category, amount, date, type) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, userId);
            stmt.setString(2, title);
            stmt.setString(3, category);
            stmt.setDouble(4, amount);
            stmt.setDate(5, date);
            stmt.setString(6, type);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Transaction> getTransactionsByUser(int userId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String title = rs.getString("title");
                    String category = rs.getString("category");
                    double amount = rs.getDouble("amount");
                    java.sql.Date sqlDate = rs.getDate("date");
                    LocalDate date = (sqlDate != null) ? sqlDate.toLocalDate() : null;
                    String type = rs.getString("type");

                    transactions.add(new Transaction(id, userId, title, category, amount, date, type));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public void updateTransaction(int id, int userId, String title, String category, double amount, Date date, String type) {
        String sql = "UPDATE transactions SET title = ?, category = ?, amount = ?, date = ?, type = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);
            stmt.setString(2, category);
            stmt.setDouble(3, amount);
            stmt.setDate(4, date);
            stmt.setString(5, type);
            stmt.setInt(6, id);
            stmt.setInt(7, userId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTransaction(int id, int userId) {
        String sql = "DELETE FROM transactions WHERE id = ? AND user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setInt(2, userId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
