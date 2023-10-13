package com.example.expensetracker.ui.budget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
        buttonSave = view.findViewById(R.id.buttonBudgetSave);

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

        if (amountStr.isEmpty() || note.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_LONG).show();
            return;
        }

        // Parse the amount as a double
        double amount = Double.parseDouble(amountStr);

        // TODO: Save the budget to a database or perform further actions
        Budget newBudget = new Budget(amount, note);
        long newRowId = databaseHelper.insertNewBudget(newBudget);

        if (newRowId != -1) {
            Toast.makeText(requireContext(), "Budget saved", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(requireContext(), "Failed to save budget", Toast.LENGTH_LONG).show();
        }

        // Clear the input fields
        editTextBudgetAmount.setText("");
        editTextBudgetNote.setText("");
    }
}
