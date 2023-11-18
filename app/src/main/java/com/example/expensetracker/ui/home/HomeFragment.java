package com.example.expensetracker.ui.home;

import android.annotation.SuppressLint;
import android.app.LauncherActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.expensetracker.DatabaseHelper;
import com.example.expensetracker.Expense;
import com.example.expensetracker.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.expensetracker.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

//    private HorizontalBarChart horizontalBarChart;
    private ListView listView;
    private List<Expense> expenseList;
//    private TextView detailsLabel;
    private Handler handler;
    private PieChart pieChart;
    private double incomeSum;
    private double expenseSum;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        handler = new Handler();

        pieChart = root.findViewById(R.id.pieChart);
        initializePieChart();

        // Fetch expenses from database using a DatabaseHelper
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        expenseList = databaseHelper.getAllExpenses();

        Button buttonGoToIncomeList = root.findViewById(R.id.btnGoToIncomeList);
        Button buttonGoToExpenseList = root.findViewById(R.id.btnGoToExpenseList);
        buttonGoToIncomeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToIncomeList(v);
            }
        });
        buttonGoToExpenseList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToExpenseList(v);
            }
        });


        double incomeSum = databaseHelper.getIncomeSum();
        double expenseSum = databaseHelper.getExpenseSum();

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) incomeSum, 0));
        entries.add(new PieEntry((float) expenseSum, 1));

        // Create a data set for the pie chart
        PieDataSet dataSet = new PieDataSet(entries, "Income and Expense");
        dataSet.setColors(new int[]{R.color.blue, R.color.red}, getContext());

        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12f);
        // Create a data object and set it to the chart
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setData(data);

        // Refresh the chart
        pieChart.invalidate();

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("key")) {
            String value = intent.getStringExtra("key");
        }


        return root;
    }

    private void initializePieChart() {
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(android.R.color.white);

        pieChart.setTransparentCircleRadius(0f);
        pieChart.setHoleRadius(50f);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
    }

    public void navigateToIncomeList(View view) {
        Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_incomeListFragment);
    }
    public void navigateToExpenseList(View view) {
        Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_expenseListFragment);
    }
}





