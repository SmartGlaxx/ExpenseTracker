package com.example.expensetracker.ui.expensesummary;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.AsyncTask;
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

public class ExpenseSummaryFragment extends Fragment {

    private BarChart barChart;
    private DatabaseHelper databaseHelper;
    private List<String> predefinedCategories;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_expense_summary, container, false);
        barChart = root.findViewById(R.id.barChart);
        databaseHelper = new DatabaseHelper(requireContext());

        // Defining predefined categories
        predefinedCategories = new ArrayList<>();
        predefinedCategories.add("Food");
        predefinedCategories.add("Rent");
        predefinedCategories.add("Clothes");
        predefinedCategories.add("Travel");
        predefinedCategories.add("Utilities");
        predefinedCategories.add("Transportation");

        // Execute AsyncTask to fetch and prepare data
        new FetchDataAndDisplayChartTask().execute();

        return root;
    }

    private class FetchDataAndDisplayChartTask extends AsyncTask<Void, Void, Void> {
        private List<CategorySum> categorySums;
        private ArrayList<BarEntry> entries;
        private ArrayList<String> labels;

        @Override
        protected Void doInBackground(Void... voids) {
            // Fetching expenses from the database and calculate sums by category
            categorySums = databaseHelper.getExpensesByCategorySum();

            entries = new ArrayList<>();
            labels = new ArrayList<>();

            Map<String, Float> categorySumMap = new HashMap<>();
            for (CategorySum categorySum : categorySums) {
                categorySumMap.put(categorySum.getCategory(), (float) categorySum.getSum());
            }

            for (String category : predefinedCategories) {
                float sum = categorySumMap.getOrDefault(category, 0.0f);
                entries.add(new BarEntry(entries.size(), sum));
                labels.add(category);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Updating the UI thread with the fetched and prepared data
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BarDataSet barDataSet = new BarDataSet(entries, "Category Sums");
                    barDataSet.setColor(Color.RED);
                    BarData barData = new BarData(barDataSet);

                    barChart.setData(barData);
                    barChart.getXAxis().setValueFormatter(new CategoryAxisValueFormatter(labels));
                    barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    barChart.setDrawValueAboveBar(true);
                    barChart.getDescription().setText("Expense Categories");

                    // Disabling the right Y-axis
                    YAxis rightAxis = barChart.getAxisRight();
                    rightAxis.setEnabled(false);

                    barChart.notifyDataSetChanged();
                    barChart.invalidate();
                }
            });
        }
    }
}
