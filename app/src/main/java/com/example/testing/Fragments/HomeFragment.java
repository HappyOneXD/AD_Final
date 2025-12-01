package com.example.testing.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testing.Adapters.ExpenseAdapter;
import com.example.testing.Models.ExpenseModel;
import com.example.testing.R;
import com.example.testing.Repositories.BudgetRepository;
import com.example.testing.Repositories.ExpenseRepository;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

// Fragment hien thi dashboard tong quan
public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // Cac bien de hien thi thong tin tong quan
    private TextView tvTotalBudgets, tvTotalExpenses, tvRecentTitle;
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

        // Anh xa cac view
        tvTotalBudgets = view.findViewById(R.id.tvTotalBudgets);
        tvTotalExpenses = view.findViewById(R.id.tvTotalExpenses);
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

        // Hien thi danh sach expense gan day
        loadRecentExpenses();

        return view;
    }

    // Load thong tin tong quan ve budget va expense
    private void loadDashboardData(){
        // Tinh tong so tien cua tat ca budget
        int totalBudgetMoney = budgetRepository.getTotalBudgetMoney();
        tvTotalBudgets.setText("Total Budgets: " + totalBudgetMoney + " VND");

        // Lay danh sach tat ca expense de tinh tong
        ArrayList<ExpenseModel> allExpenses = expenseRepository.getListExpenses();
        int totalExpenses = 0;
        for (ExpenseModel expense : allExpenses){
            totalExpenses += expense.getAmount();
        }
        tvTotalExpenses.setText("Total Expenses: " + totalExpenses + " VND");
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