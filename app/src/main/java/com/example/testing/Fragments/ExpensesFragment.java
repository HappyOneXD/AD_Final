package com.example.testing.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testing.Activities.Expenses.AddExpenseActivity;
import com.example.testing.Activities.Expenses.EditExpenseActivity;
import com.example.testing.Adapters.ExpenseAdapter;
import com.example.testing.Models.ExpenseModel;
import com.example.testing.R;
import com.example.testing.Repositories.ExpenseRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

// Fragment hien thi danh sach cac chi phi (expenses) with optional date filtering
public class ExpensesFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // Cac bien de quan ly danh sach expense
    private ArrayList<ExpenseModel> expenseModelArrayList;
    private ExpenseAdapter expenseAdapter;
    private ExpenseRepository expenseRepository;
    private RecyclerView expenseRcv;
    private int userId = 0; // ADDED: Store current user ID

    // Date filter views
    private LinearLayout layoutDateFilter;
    private TextView tvFilterMonth, tvFilterYear, tvClearFilter;
    private ImageButton btnPreviousMonth, btnNextMonth;

    // Calendar for date filtering (null = show all)
    private Calendar selectedCalendar;
    private boolean isFilterActive = false;

    public ExpensesFragment() {
        // Required empty public constructor
    }

    public static ExpensesFragment newInstance(String param1, String param2) {
        ExpensesFragment fragment = new ExpensesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout cho fragment
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);

        // Anh xa cac view
        Button btnCreateExpense = view.findViewById(R.id.btnAddExpense);
        expenseRcv = view.findViewById(R.id.rvExpense);

        // ADDED: Get current user ID from SharedPreferences
        SharedPreferences spf = getActivity().getSharedPreferences("USER_INFO", MODE_PRIVATE);
        userId = spf.getInt("USER_ID", 0);

        // Date filter views (will be added to layout)
        layoutDateFilter = view.findViewById(R.id.layoutDateFilter);
        if (layoutDateFilter != null) {
            tvFilterMonth = view.findViewById(R.id.tvFilterMonth);
            tvFilterYear = view.findViewById(R.id.tvFilterYear);
            btnPreviousMonth = view.findViewById(R.id.btnPreviousMonth);
            btnNextMonth = view.findViewById(R.id.btnNextMonth);
            tvClearFilter = view.findViewById(R.id.tvClearFilter);

            setupDateFilter();
        }

        // Xu ly nut them expense moi
        btnCreateExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyen sang man hinh them expense
                Intent intent = new Intent(getActivity(), AddExpenseActivity.class);
                startActivity(intent);
            }
        });

        // Khoi tao danh sach expense
        expenseModelArrayList = new ArrayList<>();
        expenseRepository = new ExpenseRepository(getActivity());

        // Load all expenses initially
        loadExpenses();

        return view;
    }

    private void setupDateFilter() {
        // Initialize to current month
        selectedCalendar = Calendar.getInstance();

        // Previous month button
        btnPreviousMonth.setOnClickListener(v -> {
            if (!isFilterActive) {
                isFilterActive = true;
            }
            selectedCalendar.add(Calendar.MONTH, -1);
            updateDateFilterDisplay();
            loadExpenses();
        });

        // Next month button
        btnNextMonth.setOnClickListener(v -> {
            if (!isFilterActive) {
                isFilterActive = true;
            }
            selectedCalendar.add(Calendar.MONTH, 1);
            updateDateFilterDisplay();
            loadExpenses();
        });

        // Click on date to open picker
        layoutDateFilter.setOnClickListener(v -> {
            if (!isFilterActive) {
                isFilterActive = true;
                updateDateFilterDisplay();
                loadExpenses();
            } else {
                showDatePickerDialog();
            }
        });

        // Clear filter button
        if (tvClearFilter != null) {
            tvClearFilter.setOnClickListener(v -> {
                isFilterActive = false;
                selectedCalendar = Calendar.getInstance();
                updateDateFilterDisplay();
                loadExpenses();
            });
        }

        updateDateFilterDisplay();
    }

    private void showDatePickerDialog() {
        int year = selectedCalendar.get(Calendar.YEAR);
        int month = selectedCalendar.get(Calendar.MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, dayOfMonth) -> {
                    selectedCalendar.set(Calendar.YEAR, selectedYear);
                    selectedCalendar.set(Calendar.MONTH, selectedMonth);
                    isFilterActive = true;
                    updateDateFilterDisplay();
                    loadExpenses();
                },
                year, month, 1
        );
        datePickerDialog.show();
    }

    private void updateDateFilterDisplay() {
        if (layoutDateFilter == null) return;

        if (isFilterActive) {
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.getDefault());
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
            tvFilterMonth.setText(monthFormat.format(selectedCalendar.getTime()));
            tvFilterYear.setText(yearFormat.format(selectedCalendar.getTime()));

            if (tvClearFilter != null) {
                tvClearFilter.setVisibility(View.VISIBLE);
            }
        } else {
            tvFilterMonth.setText("All");
            tvFilterYear.setText("Time");
            if (tvClearFilter != null) {
                tvClearFilter.setVisibility(View.GONE);
            }
        }
    }

    private void loadExpenses() {
        if (isFilterActive && layoutDateFilter != null) {
            // Load expenses for selected month AND current user
            Calendar startDate = (Calendar) selectedCalendar.clone();
            startDate.set(Calendar.DAY_OF_MONTH, 1);
            startDate.set(Calendar.HOUR_OF_DAY, 0);
            startDate.set(Calendar.MINUTE, 0);
            startDate.set(Calendar.SECOND, 0);

            Calendar endDate = (Calendar) selectedCalendar.clone();
            endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH));
            endDate.set(Calendar.HOUR_OF_DAY, 23);
            endDate.set(Calendar.MINUTE, 59);
            endDate.set(Calendar.SECOND, 59);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String startDateStr = sdf.format(startDate.getTime());
            String endDateStr = sdf.format(endDate.getTime());

            // Get expenses by date range
            ArrayList<ExpenseModel> allExpenses = expenseRepository.getExpensesByDateRange(startDateStr, endDateStr);

            // ADDED: Filter by current user
            expenseModelArrayList = new ArrayList<>();
            for (ExpenseModel expense : allExpenses) {
                if (expense.getUserId() == userId) {
                    expenseModelArrayList.add(expense);
                }
            }
        } else {
            // FIXED: Load expenses by user ID instead of all expenses
            expenseModelArrayList = expenseRepository.getExpensesByUserId(userId);
        }

        // Tao adapter va gan vao RecyclerView
        expenseAdapter = new ExpenseAdapter(expenseModelArrayList, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        expenseRcv.setLayoutManager(linearLayoutManager);
        expenseRcv.setAdapter(expenseAdapter);

        // Xu ly su kien click vao item expense de chinh sua
        expenseAdapter.setOnClickListener(new ExpenseAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                // Lay expense duoc click
                ExpenseModel selectedExpense = expenseModelArrayList.get(position);
                // Chuyen sang man hinh chinh sua expense
                Intent intent = new Intent(getActivity(), EditExpenseActivity.class);
                // Gui expense ID sang EditExpenseActivity
                intent.putExtra("EXPENSE_ID", selectedExpense.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload data when returning to fragment
        loadExpenses();
    }
}