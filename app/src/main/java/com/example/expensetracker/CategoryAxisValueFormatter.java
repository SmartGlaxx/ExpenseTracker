package com.example.expensetracker;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.List;

public class CategoryAxisValueFormatter extends ValueFormatter {
    private final List<String> labels;

    public CategoryAxisValueFormatter(List<String> labels) {
        this.labels = labels;
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        int index = (int) value;
        if (index >= 0 && index < labels.size()) {
            return labels.get(index);
        }
        return "";
    }
}
