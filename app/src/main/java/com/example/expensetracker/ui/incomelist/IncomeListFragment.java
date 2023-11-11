package com.example.expensetracker.ui.incomelist;

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
import com.example.expensetracker.Income;
import com.example.expensetracker.R;

import java.util.ArrayList;
import java.util.List;

public class IncomeListFragment extends Fragment {

    private List<Income> incomeList;
    private ListView listView;
    private ArrayAdapter<Income> adapter;

    public static IncomeListFragment newInstance() {
        return new IncomeListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income_list, container, false);

        Button btnGoBackHome = view.findViewById(R.id.btnGoBackHome);
        btnGoBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBackToHome(v);
            }
        });

        listView = view.findViewById(R.id.incomeListView);

        // Fetch income from database using a DatabaseHelper
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        incomeList = databaseHelper.getAllIncome();

        if (incomeList.isEmpty()) {
            listView.setAdapter(null);
        } else {
            // Create an adapter for the list
            adapter = new ArrayAdapter<Income>(getContext(), R.layout.income_item, incomeList) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    if (convertView == null) {
                        convertView = getLayoutInflater().inflate(R.layout.income_item, null);
                    }

                    Income currentItem = getItem(position);

                    TextView sourceText = convertView.findViewById(R.id.sourceTextView);
                    TextView amountText = convertView.findViewById(R.id.amountTextView);
                    TextView dateText = convertView.findViewById(R.id.dateTextView);

                    sourceText.setText(currentItem.getSource());
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

                            List<Income> filteredList = new ArrayList<>();
                            if (constraint == null || constraint.length() == 0) {
                                // If the search string is empty, show the entire list
                                filteredList.addAll(incomeList);
                            } else {
                                // Filter the list based on the constraint (search string)
                                for (Income income : incomeList) {
                                    if (income.getSource().toLowerCase().contains(constraint.toString().toLowerCase())) {
                                        filteredList.add(income);
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
                            addAll((List<Income>) results.values);
                            notifyDataSetChanged();
                        }
                    };
                }



            };

            listView.setAdapter(adapter);

            // Set up the SearchView for filtering the list
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
        // Fetch income from the database using a DatabaseHelper
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        incomeList.clear();
        incomeList.addAll(databaseHelper.getAllIncome());

        // Notify the adapter that the data set has changed
        adapter.notifyDataSetChanged();

        SearchView searchView = requireView().findViewById(R.id.searchView);
        searchView.setQuery("", false);
    }
}
