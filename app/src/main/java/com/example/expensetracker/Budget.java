package com.example.expensetracker;

public class Budget {
    private double amount;
    private String category;
    private String note;

    // Constructor
    public Budget(double amount, String category, String note) {
        this.amount = amount;
        this.category = category;
        this.note = note;
    }

    // Getter and Setter for Amount
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Getter and Setter for Note
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

