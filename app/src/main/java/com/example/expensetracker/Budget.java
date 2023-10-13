package com.example.expensetracker;

public class Budget {
    private double amount;
    private String note;

    // Constructor
    public Budget(double amount, String note) {
        this.amount = amount;
        this.note = note;
    }

    // Getter and Setter for Amount
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    // Getter and Setter for Note
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

