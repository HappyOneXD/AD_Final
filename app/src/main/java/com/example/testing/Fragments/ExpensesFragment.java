package com.example.testing.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.testing.Activities.Expenses.AddExpenseActivity;
import com.example.testing.Adapters.ExpenseAdapter;
import com.example.testing.Models.ExpenseModel;
import com.example.testing.R;
import com.example.testing.Repositories.ExpenseRepository;

import java.util.ArrayList;

// Fragment hien thi danh sach cac chi phi (expenses)
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

        // Lay danh sach expense tu database
        expenseModelArrayList = expenseRepository.getListExpenses();

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
                Intent intent = new Intent(getActivity(), com.example.testing.Activities.Expenses.EditExpenseActivity.class);
                // Gui expense ID sang EditExpenseActivity
                intent.putExtra("EXPENSE_ID", selectedExpense.getId());
                startActivity(intent);
            }
        });

        return view;
    }
}