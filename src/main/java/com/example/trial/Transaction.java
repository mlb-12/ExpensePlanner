package com.example.trial;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Transaction {
    private final IntegerProperty id;
    private final IntegerProperty user_id;  // NEW
    private final StringProperty title;
    private final StringProperty category;
    private final DoubleProperty amount;
    private final ObjectProperty<LocalDate> date;
    private final StringProperty type;

    public Transaction(int id, int userId, String title, String category, double amount, LocalDate date, String type) {
        this.id = new SimpleIntegerProperty(id);
        this.user_id = new SimpleIntegerProperty(userId);
        this.title = new SimpleStringProperty(title);
        this.category = new SimpleStringProperty(category);
        this.amount = new SimpleDoubleProperty(amount);
        this.date = new SimpleObjectProperty<>(date);
        this.type = new SimpleStringProperty(type);
    }

    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    public int getUserId() { return user_id.get(); }
    public void setUserId(int value) { user_id.set(value); }
    public IntegerProperty userIdProperty() { return user_id; }

    public String getTitle() { return title.get(); }
    public void setTitle(String value) { title.set(value); }
    public StringProperty titleProperty() { return title; }

    public String getCategory() { return category.get(); }
    public void setCategory(String value) { category.set(value); }
    public StringProperty categoryProperty() { return category; }

    public double getAmount() { return amount.get(); }
    public void setAmount(double value) { amount.set(value); }
    public DoubleProperty amountProperty() { return amount; }

    public LocalDate getDate() { return date.get(); }
    public void setDate(LocalDate value) { date.set(value); }
    public ObjectProperty<LocalDate> dateProperty() { return date; }

    public String getType() { return type.get(); }
    public void setType(String value) { type.set(value); }
    public StringProperty typeProperty() { return type; }
}
