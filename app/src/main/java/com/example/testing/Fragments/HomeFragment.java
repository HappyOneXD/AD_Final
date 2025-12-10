package com.example.testing.Fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testing.Adapters.ExpenseAdapter;
import com.example.testing.Models.BudgetModel;
import com.example.testing.Models.ExpenseModel;
import com.example.testing.R;
import com.example.testing.Repositories.BudgetRepository;
import com.example.testing.Repositories.ExpenseRepository;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // Cac bien de hien thi thong tin tong quan
    private TextView tvTotalBudgets, tvTotalExpenses, tvRecentTitle, tvMonthYear;
    private PieChart pieChartExpenses;
    private RecyclerView rvRecentExpenses;
    private BudgetRepository budgetRepository;
    private ExpenseRepository expenseRepository;
    private ExpenseAdapter expenseAdapter;
    private int userId = 0;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // hien cac view
        tvTotalBudgets = view.findViewById(R.id.tvTotalBudgets);
        tvTotalExpenses = view.findViewById(R.id.tvTotalExpenses);
        tvMonthYear = view.findViewById(R.id.tvMonthYear);
        pieChartExpenses = view.findViewById(R.id.pieChartExpenses);
        tvRecentTitle = view.findViewById(R.id.tvRecentTitle);
        rvRecentExpenses = view.findViewById(R.id.rvRecentExpenses);

        // Khoi tao repository
        budgetRepository = new BudgetRepository(getActivity());
        expenseRepository = new ExpenseRepository(getActivity());

        // Lay thong tin user
        SharedPreferences spf = getActivity().getSharedPreferences("USER_INFO", MODE_PRIVATE);
        userId = spf.getInt("USER_ID", 0);

        // Hien thi thong tin tong quan
        loadDashboardData();

        // Hien thi pie chart expense distribution
        loadExpenseDistributionChart();

        // Hien thi danh sach expense gan day
        loadRecentExpenses();

        return view;
    }

    // Load thong tin tong quan ve budget va expense
    private void loadDashboardData(){
        // Tinh tong so tien cua tat ca budget
        int totalBudgetMoney = budgetRepository.getTotalBudgetMoney();
        tvTotalBudgets.setText("Total Budgets: " + formatCurrency(totalBudgetMoney) + " VND");

        // Lay danh sach tat ca expense de tinh tong
        ArrayList<ExpenseModel> allExpenses = expenseRepository.getListExpenses();
        int totalExpenses = 0;
        for (ExpenseModel expense : allExpenses){
            totalExpenses += expense.getAmount();
        }
        tvTotalExpenses.setText("Total Expenses: " + formatCurrency(totalExpenses) + " VND");
    }

    // Load pie chart showing expense distribution by budget/category
    private void loadExpenseDistributionChart(){
        // Get current month and year
        java.text.SimpleDateFormat monthFormat = new java.text.SimpleDateFormat("MMMM yyyy", java.util.Locale.getDefault());
        String currentMonthYear = monthFormat.format(new java.util.Date());
        tvMonthYear.setText("Expenses for " + currentMonthYear);

        // Get all expenses
        ArrayList<ExpenseModel> allExpenses = expenseRepository.getListExpenses();

        // Group expenses by budget name and calculate totals
        HashMap<String, Integer> expensesByBudget = new HashMap<>();
        int totalAmount = 0;

        for (ExpenseModel expense : allExpenses){
            String budgetName = expense.getBudgetName();
            if (budgetName == null || budgetName.isEmpty()){
                budgetName = "Uncategorized";
            }

            int currentAmount = expensesByBudget.getOrDefault(budgetName, 0);
            expensesByBudget.put(budgetName, currentAmount + expense.getAmount());
            totalAmount += expense.getAmount();
        }

        // Create pie entries
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        // Define custom colors for the pie chart
        int[] customColors = {
                Color.rgb(76, 175, 80),   // Green
                Color.rgb(255, 193, 7),   // Amber
                Color.rgb(33, 150, 243),  // Blue
                Color.rgb(255, 87, 34),   // Deep Orange
                Color.rgb(156, 39, 176),  // Purple
                Color.rgb(0, 188, 212),   // Cyan
                Color.rgb(255, 152, 0),   // Orange
                Color.rgb(121, 85, 72)    // Brown
        };

        int colorIndex = 0;
        for (Map.Entry<String, Integer> entry : expensesByBudget.entrySet()){
            float percentage = (float) entry.getValue() / totalAmount * 100;
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
            colors.add(customColors[colorIndex % customColors.length]);
            colorIndex++;
        }

        // If no expenses, show "No data"
        if (entries.isEmpty()){
            entries.add(new PieEntry(1, "No expenses yet"));
            colors.add(Color.GRAY);
        }

        // Create dataset
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // Create pie data
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChartExpenses));

        // Configure pie chart
        pieChartExpenses.setData(data);
        pieChartExpenses.setUsePercentValues(true);
        pieChartExpenses.getDescription().setEnabled(false);
        pieChartExpenses.setExtraOffsets(5, 10, 5, 5);
        pieChartExpenses.setDragDecelerationFrictionCoef(0.95f);
        pieChartExpenses.setDrawHoleEnabled(true);
        pieChartExpenses.setHoleColor(Color.WHITE);
        pieChartExpenses.setTransparentCircleRadius(61f);
        pieChartExpenses.setDrawCenterText(true);
        pieChartExpenses.setCenterText("Expense\nDistribution");
        pieChartExpenses.setCenterTextSize(14f);
        pieChartExpenses.setRotationAngle(0);
        pieChartExpenses.setRotationEnabled(true);
        pieChartExpenses.setHighlightPerTapEnabled(true);

        // Legend configuration
        pieChartExpenses.getLegend().setEnabled(true);
        pieChartExpenses.getLegend().setTextSize(12f);
        pieChartExpenses.getLegend().setFormSize(12f);
        pieChartExpenses.getLegend().setWordWrapEnabled(true);

        // Animate chart
        pieChartExpenses.animateY(1000);
        pieChartExpenses.invalidate();
    }

    // Dinh dang so tien theo format currency (1,000,000)
    private String formatCurrency(int amount){
        return String.format("%,d", amount);
    }

    // Load danh sach 5 expense gan day nhat
    private void loadRecentExpenses(){
        ArrayList<ExpenseModel> recentExpenses = expenseRepository.getRecentExpenses(5);

        if (recentExpenses.size() > 0){
            tvRecentTitle.setText("Recent Expenses:");

            // Hien thi danh sach expense gan day trong RecyclerView
            expenseAdapter = new ExpenseAdapter(recentExpenses, getContext());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
            rvRecentExpenses.setLayoutManager(linearLayoutManager);
            rvRecentExpenses.setAdapter(expenseAdapter);
        } else {
            tvRecentTitle.setText("No recent expenses");
        }
    }
}