package com.example.expensetracker.ui.income;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.expensetracker.DatabaseHelper;
import com.example.expensetracker.Income;
import com.example.expensetracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class IncomeFragment extends Fragment {

    private Spinner spinnerIncomeSource;
    private EditText editTextIncomeAmount;
    private Button buttonDate, buttonSave;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    public IncomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_income, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerIncomeSource = view.findViewById(R.id.spinnerCategory);
        editTextIncomeAmount = view.findViewById(R.id.editTextAmount);
        buttonDate = view.findViewById(R.id.buttonDate);
        buttonSave = view.findViewById(R.id.buttonSave);

        // Get the array of categories from resources
        String[] categoriesArray = getResources().getStringArray(R.array.income_categories);

        // Add the title as the first item in the array
        String[] categoriesWithTitle = new String[categoriesArray.length + 1];
        categoriesWithTitle[0] = "Income source"; // Title
        System.arraycopy(categoriesArray, 0, categoriesWithTitle, 1, categoriesArray.length);

        // Create the ArrayAdapter with the modified array
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categoriesWithTitle
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the spinner
        spinnerIncomeSource.setAdapter(adapter);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        // Set up the date picker dialog
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveIncome();
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateButtonText();
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateDateButtonText() {
        String formattedDate = dateFormat.format(calendar.getTime());
        buttonDate.setText(formattedDate);
    }

    private void saveIncome() {
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());

        String source = spinnerIncomeSource.getSelectedItem().toString();
        String amountStr = editTextIncomeAmount.getText().toString();

        if (source.equals("Income source") || amountStr.isEmpty()) {
            Toast.makeText(requireContext(), "Please select a valid source and fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse the amount as a double
        double amount = Double.parseDouble(amountStr);
        String date = dateFormat.format(calendar.getTime());

        // TODO: Save the income to a database or perform further actions
        Income newIncome = new Income(source, amount, date);
        databaseHelper.insertNewIncome(newIncome);

        // Clear the input fields
        spinnerIncomeSource.setSelection(0);
        editTextIncomeAmount.setText("");
        calendar = Calendar.getInstance(); // Reset the date to the current date
        updateDateButtonText();

        Toast.makeText(requireContext(), "Income saved", Toast.LENGTH_LONG).show();
    }
}
