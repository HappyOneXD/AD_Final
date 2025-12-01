package com.example.testing.Activities.Expenses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testing.Activities.MenuActivity;
import com.example.testing.Models.BudgetModel;
import com.example.testing.R;
import com.example.testing.Repositories.BudgetRepository;
import com.example.testing.Repositories.ExpenseRepository;

import java.util.ArrayList;

// Activity de them moi mot chi phi (expense)
public class AddExpenseActivity extends AppCompatActivity {
    EditText edtNameExpense, edtAmount, edtDescription;
    Spinner spinnerBudget;
    Button btnSave, btnBack;
    ExpenseRepository expenseRepository;
    BudgetRepository budgetRepository;
    private int userId = 0;
    private ArrayList<BudgetModel> budgetList;
    private ArrayList<String> budgetNames;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // Anh xa cac view tu layout
        edtNameExpense = findViewById(R.id.edtExpenseName);
        edtAmount = findViewById(R.id.edtAmount);
        edtDescription = findViewById(R.id.edtDescription);
        spinnerBudget = findViewById(R.id.spinnerBudget);
        btnSave = findViewById(R.id.btnSaveExpense);
        btnBack = findViewById(R.id.btnBackExpense);

        // Khoi tao repository de lam viec voi database
        expenseRepository = new ExpenseRepository(AddExpenseActivity.this);
        budgetRepository = new BudgetRepository(AddExpenseActivity.this);

        // Lay thong tin user tu SharedPreferences
        SharedPreferences spf = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        userId = spf.getInt("USER_ID", 0);

        // Load danh sach budget vao spinner
        loadBudgetsToSpinner();

        // Xu ly nut Back - quay lai man hinh MenuActivity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddExpenseActivity.this, MenuActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("MENU_TAB", "EXPENSES_TAB");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        // Xu ly nut Save - luu expense moi vao database
        btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                // Lay du lieu tu cac EditText
                String name = edtNameExpense.getText().toString().trim();
                String amountStr = edtAmount.getText().toString().trim();
                String description = edtDescription.getText().toString().trim();

                // Kiem tra ten expense khong duoc de trong
                if (TextUtils.isEmpty(name)){
                    edtNameExpense.setError("Expense name is required");
                    return;
                }

                // Kiem tra so tien khong duoc de trong
                if (TextUtils.isEmpty(amountStr)){
                    edtAmount.setError("Amount is required");
                    return;
                }

                // Chuyen doi so tien sang kieu int
                int amount = Integer.parseInt(amountStr);
                if (amount <= 0){
                    edtAmount.setError("Amount must be greater than 0");
                    return;
                }

                // Kiem tra phai chon budget
                if (spinnerBudget.getSelectedItemPosition() == 0){
                    Toast.makeText(AddExpenseActivity.this, "Please select a budget", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lay budget duoc chon tu spinner (tru di 1 vi position 0 la "Select Budget")
                int selectedBudgetPosition = spinnerBudget.getSelectedItemPosition() - 1;
                int budgetId = budgetList.get(selectedBudgetPosition).getId();

                // Luu expense vao database
                long insert = expenseRepository.addNewExpense(name, amount, description, budgetId, userId);
                if (insert == -1){
                    // Loi khi tao expense
                    Toast.makeText(AddExpenseActivity.this, "Create expense fail", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // Thanh cong
                    Toast.makeText(AddExpenseActivity.this, "Create expense successful", Toast.LENGTH_SHORT).show();
                    // Quay lai danh sach expense
                    Intent intent = new Intent(AddExpenseActivity.this, MenuActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("MENU_TAB", "EXPENSES_TAB");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    // Load danh sach budget vao spinner de user chon
    private void loadBudgetsToSpinner(){
        budgetList = budgetRepository.getListBudgets();
        budgetNames = new ArrayList<>();

        // Them option "Select Budget" o dau danh sach
        budgetNames.add("Select Budget");

        // Them ten cac budget vao danh sach
        for (BudgetModel budget : budgetList){
            budgetNames.add(budget.getName());
        }

        // Tao adapter cho spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, budgetNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBudget.setAdapter(adapter);
    }
}