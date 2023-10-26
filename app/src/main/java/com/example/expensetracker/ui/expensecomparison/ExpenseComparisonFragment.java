package com.example.expensetracker.ui.expensecomparison;

//import androidx.lifecycle.ViewModelProvider;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//
//import com.example.expensetracker.BudgetCategorySum;
//import com.example.expensetracker.DatabaseHelper;
//import com.example.expensetracker.R;
//import com.github.mikephil.charting.charts.BarChart;
//import com.github.mikephil.charting.data.BarData;
//import com.github.mikephil.charting.data.BarDataSet;
//import com.github.mikephil.charting.data.BarEntry;
//import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.components.YAxis;
//import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ExpenseComparisonFragment extends Fragment {
//
//    private BarChart barChart;
//    private DatabaseHelper databaseHelper;
//    private List<String> predefinedCategories;
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View root = inflater.inflate(R.layout.fragment_expense_comparison, container, false);
//        barChart = root.findViewById(R.id.barChart);
//        databaseHelper = new DatabaseHelper(requireContext());
//
//        // Define predefined categories
//        predefinedCategories = new ArrayList<>();
//        predefinedCategories.add("Food");
//        predefinedCategories.add("Rent");
//        predefinedCategories.add("Clothes");
//        predefinedCategories.add("Travel");
//        predefinedCategories.add("Utilities");
//        predefinedCategories.add("Transportation");
//
//        // Fetch budget data from the database
//        List<BudgetCategorySum> budgetCategorySums = databaseHelper.getBudgetByCategorySum();
//
//        // Prepare data for the bar chart
//        ArrayList<BarEntry> entries = new ArrayList<>();
//        ArrayList<String> labels = new ArrayList<>();
//
//        for (int i = 0; i < predefinedCategories.size(); i++) {
//            float budgetSum = 0.0f;
//            for (BudgetCategorySum budgetCategorySum : budgetCategorySums) {
//                if (budgetCategorySum.getCategory().equals(predefinedCategories.get(i))) {
//                    budgetSum = (float) budgetCategorySum.getSum();
//                    break;
//                }
//            }
//            entries.add(new BarEntry(i, budgetSum));
//            labels.add(predefinedCategories.get(i));
//        }
//
//        BarDataSet barDataSet = new BarDataSet(entries, "Budget Sums");
//        BarData barData = new BarData(barDataSet);
//
//        barChart.setData(barData);
//        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
//        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
//        barChart.setDrawValueAboveBar(true);
//        barChart.getDescription().setText("Budget Categories");
//
//        // Disable the right Y-axis
//        YAxis rightAxis = barChart.getAxisRight();
//        rightAxis.setEnabled(false);
//
//        barChart.notifyDataSetChanged();
//        barChart.invalidate();
//
//        return root;
//    }
//}


import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.expensetracker.BudgetCategorySum;
import com.example.expensetracker.CategorySum;
import com.example.expensetracker.DatabaseHelper;
import com.example.expensetracker.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
//import com.github.mikephil.charting.data.BarEntryGroup;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

public class ExpenseComparisonFragment extends Fragment {

    private BarChart barChart;
    private DatabaseHelper databaseHelper;
    private List<String> predefinedCategories;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_expense_comparison, container, false);
        barChart = root.findViewById(R.id.barChart);
        databaseHelper = new DatabaseHelper(requireContext());

        // Define predefined categories
        predefinedCategories = new ArrayList<>();
        predefinedCategories.add("Food");
        predefinedCategories.add("Rent");
        predefinedCategories.add("Clothes");
        predefinedCategories.add("Travel");
        predefinedCategories.add("Utilities");
        predefinedCategories.add("Transportation");

        // Fetch expense data from the database
        List<CategorySum> expenseCategorySums = databaseHelper.getExpensesByCategorySum();
        // Fetch budget data from the database
        List<BudgetCategorySum> budgetCategorySums = databaseHelper.getBudgetByCategorySum();

        // Prepare data for the bar chart
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        ArrayList<String> labels = new ArrayList();

        // Define different colors for expenses and budget
        int[] colors = new int[]{Color.RED, getResources().getColor(R.color.gray_400)};

        for (int i = 0; i < predefinedCategories.size(); i++) {
            String category = predefinedCategories.get(i);
            float expenseSum = 0.0f;
            float budgetSum = 0.0f;

            // Find the expense sum for the category
            for (CategorySum categorySum : expenseCategorySums) {
                if (categorySum.getCategory().equals(category)) {
                    expenseSum = (float) categorySum.getSum();
                    break;
                }
            }

            // Find the budget sum for the category
            for (BudgetCategorySum budgetCategorySum : budgetCategorySums) {
                if (budgetCategorySum.getCategory().equals(category)) {
                    budgetSum = (float) budgetCategorySum.getSum();
                    break;
                }
            }

            ArrayList<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(i, expenseSum));
            entries.add(new BarEntry(i, budgetSum));
            labels.add(category);

            BarDataSet barDataSet = new BarDataSet(entries, "");
            barDataSet.setColors(colors);
            barDataSet.setStackLabels(new String[]{"Expenses", "Budget"});
            dataSets.add(barDataSet);
        }

        BarData barData = new BarData(dataSets);
        barChart.setData(barData);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setText("Expense vs. Budget by Category");

        // Disable the right Y-axis
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        barChart.notifyDataSetChanged();
        barChart.invalidate();

        return root;
    }
}
