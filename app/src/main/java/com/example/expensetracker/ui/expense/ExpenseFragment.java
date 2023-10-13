package com.example.expensetracker.ui.expense;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.example.expensetracker.databinding.FragmentExpenseBinding;
//
//public class ExpenseFragment extends Fragment {
//
//    private FragmentExpenseBinding binding;
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        ExpenseViewModel slideshowViewModel =
//                new ViewModelProvider(this).get(ExpenseViewModel.class);
//
//            binding = FragmentExpenseBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        final TextView textView = binding.textSlideshow;
//        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        return root;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//}

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
import com.example.expensetracker.Expense;
import com.example.expensetracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ExpenseFragment extends Fragment {

    private EditText editTextItem;
    private Spinner spinnerCategory;
    private EditText editTextAmount;
    private Button buttonDate, buttonSave;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    public ExpenseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expense, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextItem = view.findViewById(R.id.editTextItem);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        editTextAmount = view.findViewById(R.id.editTextAmount);
        buttonDate = view.findViewById(R.id.buttonDate);
        buttonSave = view.findViewById(R.id.buttonSave);

        // Get the array of categories from resources
        String[] categoriesArray = getResources().getStringArray(R.array.expense_categories);

        // Add the title as the first item in the array
        String[] categoriesWithTitle = new String[categoriesArray.length + 1];
        categoriesWithTitle[0] = "Select a category"; // Title
        System.arraycopy(categoriesArray, 0, categoriesWithTitle, 1, categoriesArray.length);

        // Create the ArrayAdapter with the modified array
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categoriesWithTitle
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the spinner
        spinnerCategory.setAdapter(adapter);

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
                saveExpense();
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

    private void saveExpense() {
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        String item = editTextItem.getText().toString();
        String category = spinnerCategory.getSelectedItem().toString();
        String amountStr = editTextAmount.getText().toString();

        if (category.equals("Select a category") || amountStr.isEmpty()) {
            Toast.makeText(requireContext(), "Please select a valid category and fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse the amount as a double
        double amount = Double.parseDouble(amountStr);
        String date = dateFormat.format(calendar.getTime());

        // TODO: Save the expense to a database or perform further actions
        Expense newExpense = new Expense(item, category, amount, date);
        databaseHelper.insertNewExpense(newExpense);

        // Clear the input fields
        spinnerCategory.setSelection(0);
        editTextAmount.setText("");
        calendar = Calendar.getInstance(); // Reset the date to the current date
        updateDateButtonText();

        Toast.makeText(requireContext(), "Expense saved", Toast.LENGTH_LONG).show();
    }
}
