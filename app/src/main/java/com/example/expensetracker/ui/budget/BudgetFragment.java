package com.example.expensetracker.ui.budget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.expensetracker.Budget;
import com.example.expensetracker.DatabaseHelper;
import com.example.expensetracker.R;

public class BudgetFragment extends Fragment {

    private EditText editTextBudgetAmount;
    private EditText editTextBudgetNote;
    private Spinner spinnerBudgetCategory;
    private Button buttonSave;

    public BudgetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budget, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextBudgetAmount = view.findViewById(R.id.editTextBudgetAmount);
        editTextBudgetNote = view.findViewById(R.id.editTextBudgetNote);
        spinnerBudgetCategory = view.findViewById(R.id.spinnerBudgetCategory); // Updated
        buttonSave = view.findViewById(R.id.buttonBudgetSave);

        // Getting the array of budget categories from resources
        String[] budgetCategoriesArray = getResources().getStringArray(R.array.budget_categories);

        // Adding "Select a category" as the first item in the array
        String[] budgetCategoriesWithTitle = new String[budgetCategoriesArray.length + 1];
        budgetCategoriesWithTitle[0] = "Select a category";

        // Copying the budgetCategoriesArray into budgetCategoriesWithTitle
        System.arraycopy(budgetCategoriesArray, 0, budgetCategoriesWithTitle, 1, budgetCategoriesArray.length);

        // Creating the ArrayAdapter with the modified array
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                budgetCategoriesWithTitle
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Setting the adapter to the spinner
        spinnerBudgetCategory.setAdapter(adapter);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBudget();
            }
        });
    }

    private void saveBudget() {
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());

        String amountStr = editTextBudgetAmount.getText().toString();
        String note = editTextBudgetNote.getText().toString();
        String category = spinnerBudgetCategory.getSelectedItem().toString();

        if (category.equals("Select a category") || amountStr.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields and select a valid category", Toast.LENGTH_LONG).show();
            return;
        }

        // Parsing the amount as a double
        double amount = Double.parseDouble(amountStr);

        // TODO: Save the budget to a database or perform further actions
        Budget newBudget = new Budget(amount, category, note);
        long newRowId = databaseHelper.insertNewBudget(newBudget);

        if (newRowId != -1) {
            Toast.makeText(requireContext(), "Budget saved", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(requireContext(), "Failed to save budget", Toast.LENGTH_LONG).show();
        }

        // Clearing the input fields
        editTextBudgetAmount.setText("");
        editTextBudgetNote.setText("");
        spinnerBudgetCategory.setSelection(0);
    }
}
