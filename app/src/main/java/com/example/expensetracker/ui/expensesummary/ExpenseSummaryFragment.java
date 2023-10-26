package com.example.expensetracker.ui.expensesummary;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.expensetracker.CategoryAxisValueFormatter;
import com.example.expensetracker.CategorySum;
import com.example.expensetracker.DatabaseHelper;
import com.example.expensetracker.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.expensetracker.R;

public class ExpenseSummaryFragment extends Fragment {

//    private ExpenseSummaryViewModel mViewModel;
//
//    public static ExpenseSummaryFragment newInstance() {
//        return new ExpenseSummaryFragment();
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_expense_summary, container, false);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(ExpenseSummaryViewModel.class);
//        // TODO: Use the ViewModel
//    }







    private BarChart barChart;
    private DatabaseHelper databaseHelper;
    private List<String> predefinedCategories;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_expense_summary, container, false);
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

        // Fetch expenses from the database and calculate sums by category
        List<CategorySum> categorySums = databaseHelper.getExpensesByCategorySum();

        // Prepare data for the bar chart
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        Map<String, Float> categorySumMap = new HashMap<>();
        for (CategorySum categorySum : categorySums) {
            categorySumMap.put(categorySum.getCategory(), (float) categorySum.getSum());
        }

        for (String category : predefinedCategories) {
            float sum = categorySumMap.getOrDefault(category, 0.0f);
            entries.add(new BarEntry(entries.size(), sum));
            labels.add(category);
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Category Sums");
        barDataSet.setColor(Color.RED);
        BarData barData = new BarData(barDataSet);

        barChart.setData(barData);
        barChart.getXAxis().setValueFormatter(new CategoryAxisValueFormatter(labels));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setText("Expense Categories");

        // Disable the right Y-axis
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        barChart.notifyDataSetChanged();
        barChart.invalidate();

        return root;
    }

}