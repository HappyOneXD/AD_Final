package com.example.testing.Activities.Expenses;

import android.content.Intent;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testing.Activities.MenuActivity;
import com.example.testing.Models.BudgetModel;
import com.example.testing.Models.ExpenseModel;
import com.example.testing.R;
import com.example.testing.Repositories.BudgetRepository;
import com.example.testing.Repositories.ExpenseRepository;

import java.util.ArrayList;

// Activity de chinh sua va xoa expense
public class EditExpenseActivity extends AppCompatActivity {
    EditText edtNameExpense, edtAmount, edtDescription;
    Spinner spinnerBudget;
    Button btnUpdate, btnDelete, btnBack;
    ExpenseRepository expenseRepository;
    BudgetRepository budgetRepository;
    private int expenseId = 0;
    private ArrayList<BudgetModel> budgetList;
    private ArrayList<String> budgetNames;
    private ExpenseModel currentExpense;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        // Anh xa cac view tu layout
        edtNameExpense = findViewById(R.id.edtExpenseName);
        edtAmount = findViewById(R.id.edtAmount);
        edtDescription = findViewById(R.id.edtDescription);
        spinnerBudget = findViewById(R.id.spinnerBudget);
        btnUpdate = findViewById(R.id.btnUpdateExpense);
        btnDelete = findViewById(R.id.btnDeleteExpense);
        btnBack = findViewById(R.id.btnBackExpense);

        // Khoi tao repository
        expenseRepository = new ExpenseRepository(EditExpenseActivity.this);
        budgetRepository = new BudgetRepository(EditExpenseActivity.this);

        // Lay expense ID tu Intent
        Intent intent = getIntent();
        expenseId = intent.getIntExtra("EXPENSE_ID", 0);

        // Load danh sach budget vao spinner
        loadBudgetsToSpinner();

        // Load thong tin expense hien tai
        loadExpenseData();

        // Xu ly nut Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditExpenseActivity.this, MenuActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("MENU_TAB", "EXPENSES_TAB");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        // Xu ly nut Update - cap nhat thong tin expense
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                // Lay du lieu tu cac EditText
                String name = edtNameExpense.getText().toString().trim();
                String amountStr = edtAmount.getText().toString().trim();
                String description = edtDescription.getText().toString().trim();

                // Kiem tra validation
                if (TextUtils.isEmpty(name)){
                    edtNameExpense.setError("Expense name is required");
                    return;
                }

                if (TextUtils.isEmpty(amountStr)){
                    edtAmount.setError("Amount is required");
                    return;
                }

                int amount = Integer.parseInt(amountStr);
                if (amount <= 0){
                    edtAmount.setError("Amount must be greater than 0");
                    return;
                }

                if (spinnerBudget.getSelectedItemPosition() == 0){
                    Toast.makeText(EditExpenseActivity.this, "Please select a budget", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lay budget duoc chon
                int selectedBudgetPosition = spinnerBudget.getSelectedItemPosition() - 1;
                int budgetId = budgetList.get(selectedBudgetPosition).getId();

                // Cap nhat expense trong database
                int update = expenseRepository.updateExpense(expenseId, name, amount, description, budgetId);
                if (update > 0){
                    Toast.makeText(EditExpenseActivity.this, "Update expense successful", Toast.LENGTH_SHORT).show();
                    // Quay lai danh sach expense
                    Intent intent = new Intent(EditExpenseActivity.this, MenuActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("MENU_TAB", "EXPENSES_TAB");
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(EditExpenseActivity.this, "Update expense fail", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xu ly nut Delete - xoa expense
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hien thi dialog xac nhan truoc khi xoa
                showDeleteConfirmDialog();
            }
        });
    }

    // Load danh sach budget vao spinner
    private void loadBudgetsToSpinner(){
        budgetList = budgetRepository.getListBudgets();
        budgetNames = new ArrayList<>();

        budgetNames.add("Select Budget");

        for (BudgetModel budget : budgetList){
            budgetNames.add(budget.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, budgetNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBudget.setAdapter(adapter);
    }

    // Load thong tin expense hien tai de hien thi len form
    private void loadExpenseData(){
        currentExpense = expenseRepository.getExpenseById(expenseId);
        if (currentExpense != null){
            // Hien thi thong tin expense len cac view
            edtNameExpense.setText(currentExpense.getName());
            edtAmount.setText(String.valueOf(currentExpense.getAmount()));
            edtDescription.setText(currentExpense.getDescription());

            // Chon budget tuong ung trong spinner
            for (int i = 0; i < budgetList.size(); i++){
                if (budgetList.get(i).getId() == currentExpense.getBudgetId()){
                    // Cong them 1 vi position 0 la "Select Budget"
                    spinnerBudget.setSelection(i + 1);
                    break;
                }
            }
        }
    }

    // Hien thi dialog xac nhan xoa expense
    private void showDeleteConfirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Expense");
        builder.setMessage("Are you sure you want to delete this expense?");

        // Nut Yes - xoa expense
        builder.setPositiveButton("Yes", (dialog, which) -> {
            int delete = expenseRepository.deleteExpense(expenseId);
            if (delete > 0){
                Toast.makeText(EditExpenseActivity.this, "Delete expense successful", Toast.LENGTH_SHORT).show();
                // Quay lai danh sach expense
                Intent intent = new Intent(EditExpenseActivity.this, MenuActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("MENU_TAB", "EXPENSES_TAB");
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                Toast.makeText(EditExpenseActivity.this, "Delete expense fail", Toast.LENGTH_SHORT).show();
            }
        });

        // Nut No - huy bo
        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });

        builder.show();
    }
}
