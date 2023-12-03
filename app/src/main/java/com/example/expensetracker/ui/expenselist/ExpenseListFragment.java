package com.example.expensetracker.ui.expenselist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.expensetracker.DatabaseHelper;
import com.example.expensetracker.Expense;
import com.example.expensetracker.R;

import java.util.ArrayList;
import java.util.List;

public class ExpenseListFragment extends Fragment {

    private List<Expense> expenseList;
    private ListView listView;
    private ArrayAdapter<Expense> adapter;

    public static com.example.expensetracker.ui.expenselist.ExpenseListFragment newInstance() {
        return new com.example.expensetracker.ui.expenselist.ExpenseListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_list, container, false);

        Button btnGoBackHome = view.findViewById(R.id.btnGoBackHome);
        btnGoBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBackToHome(v);
            }
        });

        listView = view.findViewById(R.id.expenseListView);

        // Fetching expense from database using a DatabaseHelper
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        expenseList = databaseHelper.getAllExpenses();

        if (expenseList.isEmpty()) {
            listView.setAdapter(null);
        } else {
            // Creating an adapter for the list
            adapter = new ArrayAdapter<Expense>(getContext(), R.layout.expense_item, expenseList) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    if (convertView == null) {
                        convertView = getLayoutInflater().inflate(R.layout.expense_item, null);
                    }

                    Expense currentItem = getItem(position);

                    TextView itemText = convertView.findViewById(R.id.itemTextView);
                    TextView categoryText = convertView.findViewById(R.id.categoryTextView);
                    TextView amountText = convertView.findViewById(R.id.amountTextView);
                    TextView dateText = convertView.findViewById(R.id.dateTextView);

                    itemText.setText(currentItem.getItem());
                    categoryText.setText(currentItem.getCategory());
                    amountText.setText(String.valueOf(currentItem.getAmount()));
                    dateText.setText(currentItem.getDate());

                    return convertView;
                }

                @Override
                public android.widget.Filter getFilter() {
                    return new android.widget.Filter() {
                        @Override
                        protected FilterResults performFiltering(CharSequence constraint) {
                            FilterResults results = new FilterResults();

                            List<Expense> filteredList = new ArrayList<>();
                            if (constraint == null || constraint.length() == 0) {
                                // If the search string is empty, show the entire list
                                filteredList.addAll(expenseList);
                            } else {
                                // Filter the list based on the constraint (search string)
                                for (Expense expense : expenseList) {
                                    if (expense.getItem().toLowerCase().contains(constraint.toString().toLowerCase())) {
                                        filteredList.add(expense);
                                    }
                                }
                            }

                            results.values = filteredList;
                            results.count = filteredList.size();

                            return results;
                        }

                        @Override
                        protected void publishResults(CharSequence constraint, FilterResults results) {
                            clear();
                            addAll((List<Expense>) results.values);
                            notifyDataSetChanged();
                        }
                    };
                }



            };

            listView.setAdapter(adapter);

            // Setting up the SearchView for filtering the list
            SearchView searchView = view.findViewById(R.id.searchView);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return false;
                }
            });
        }


        ImageButton btnRefresh = view.findViewById(R.id.btnRefresh);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList();
            }
        });

        return view;
    }

    private void navigateBackToHome(View view) {
        Navigation.findNavController(view).navigateUp();
    }

    private void refreshList() {
        // Fetching expense from the database using a DatabaseHelper
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        expenseList.clear();
        expenseList.addAll(databaseHelper.getAllExpenses());

        // Notifying the adapter that the data set has changed
        adapter.notifyDataSetChanged();

        SearchView searchView = requireView().findViewById(R.id.searchView);
        searchView.setQuery("", false);
    }
}
