package com.example.expensetracker;

public class BudgetCategorySum {
    private String category;
    private double sum;

    public BudgetCategorySum(String category, double sum) {
        this.category = category;
        this.sum = sum;
    }

    public String getCategory() {
        return category;
    }

    public double getSum() {
        return sum;
    }
}

