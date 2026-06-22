package com.example.trial;


import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class ProcessingModule {

    public static double calculateTotal(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase("expense"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public static double calculateTotalIncome(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase("income"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public static Map<String, Double> calculateCategoryBreakdown(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase("expense"))
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    public static Map<String, Double> calculateMonthlyTotals(List<Transaction> transactions) {
        Map<String, Double> monthlyTotals = new LinkedHashMap<>();
        for (Transaction t : transactions) {
            if (t.getType().equalsIgnoreCase("expense") && t.getDate() != null) {
                String month = t.getDate().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
                monthlyTotals.put(month, monthlyTotals.getOrDefault(month, 0.0) + t.getAmount());
            }
        }
        return monthlyTotals;
    }

    public static List<Transaction> filterByType(List<Transaction> transactions, String type) {
        return transactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase(type))
                .toList();
    }
}
