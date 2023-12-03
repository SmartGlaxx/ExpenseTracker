package com.example.expensetracker;

public class Expense {
    private String item;
    private String category;
    private double amount;
    private String date;

    public Expense() {
    }

    public Expense(String item, String category, double amount, String date) {
        this.item = item;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
