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

import com.example.testing.Activities.Budgets.AddBudgetActivity;
import com.example.testing.Activities.Budgets.EditBudgetActivity;
import com.example.testing.Adapters.BudgetAdapter;
import com.example.testing.Models.BudgetModel;
import com.example.testing.R;
import com.example.testing.Repositories.BudgetRepository;

import java.util.ArrayList;

// Fragment hien thi danh sach cac budget
public class BudgetFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // Cac bien de quan ly danh sach budget
    private ArrayList<BudgetModel> budgetModelArrayList;
    private BudgetAdapter budgetAdapter;
    private BudgetModel budgetModel;
    private BudgetRepository budgetRepository;
    private RecyclerView budgetRcv;

    public BudgetFragment() {
        // Required empty public constructor
    }

    public static BudgetFragment newInstance(String param1, String param2) {
        BudgetFragment fragment = new BudgetFragment();
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
        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        // Anh xa cac view
        Button btnCreateBudget = view.findViewById(R.id.btnAddBudget);
        budgetRcv = view.findViewById(R.id.rvBudget);

        // Xu ly nut them budget moi
        btnCreateBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddBudgetActivity.class);
                startActivity(intent);
            }
        });

        // Khoi tao danh sach budget
        budgetModelArrayList = new ArrayList<>();
        budgetRepository = new BudgetRepository(getActivity());

        // Lay danh sach budget tu database
        budgetModelArrayList = budgetRepository.getListBudgets();

        // Tao adapter va gan vao RecyclerView
        budgetAdapter = new BudgetAdapter(budgetModelArrayList, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        budgetRcv.setLayoutManager(linearLayoutManager);
        budgetRcv.setAdapter(budgetAdapter);

        // Xu ly su kien click vao item budget de chinh sua
        budgetAdapter.setOnClickListener(new BudgetAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                // Lay budget duoc click
                BudgetModel selectedBudget = budgetModelArrayList.get(position);
                // Chuyen sang man hinh chinh sua budget
                Intent intent = new Intent(getActivity(), EditBudgetActivity.class);
                // Gui budget ID sang EditBudgetActivity
                intent.putExtra("BUDGET_ID", selectedBudget.getId());
                startActivity(intent);
            }
        });

        return view;
    }
}