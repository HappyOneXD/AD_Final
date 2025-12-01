package com.example.testing.Activities.Budgets;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testing.Activities.MenuActivity;
import com.example.testing.Models.BudgetModel;
import com.example.testing.R;
import com.example.testing.Repositories.BudgetRepository;

// Activity de chinh sua va xoa budget
public class EditBudgetActivity extends AppCompatActivity {
    EditText edtNameBudget, edtMoney, edtDescription;
    Button btnUpdate, btnDelete, btnBack;
    BudgetRepository budgetRepository;
    private int budgetId = 0;
    private BudgetModel currentBudget;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_budget);

        // Anh xa cac view tu layout
        edtNameBudget = findViewById(R.id.edtBudgetName);
        edtMoney = findViewById(R.id.edtMoney);
        edtDescription = findViewById(R.id.edtDescription);
        btnUpdate = findViewById(R.id.btnUpdateBudget);
        btnDelete = findViewById(R.id.btnDeleteBudget);
        btnBack = findViewById(R.id.btnBackBudget);

        // Khoi tao repository
        budgetRepository = new BudgetRepository(EditBudgetActivity.this);

        // Lay budget ID tu Intent
        Intent intent = getIntent();
        budgetId = intent.getIntExtra("BUDGET_ID", 0);

        // Load thong tin budget hien tai
        loadBudgetData();

        // Xu ly nut Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditBudgetActivity.this, MenuActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("MENU_TAB", "BUDGET_TAB");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        // Xu ly nut Update - cap nhat thong tin budget
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                // Lay du lieu tu cac EditText
                String name = edtNameBudget.getText().toString().trim();
                String moneyStr = edtMoney.getText().toString().trim();
                String description = edtDescription.getText().toString().trim();

                // Kiem tra validation
                if (TextUtils.isEmpty(moneyStr)){
                    edtMoney.setError("Money's budget is required");
                    return;
                }

                int money = Integer.parseInt(moneyStr);
                if (money <= 0){
                    edtMoney.setError("Money's budget must be greater than 0");
                    return;
                }

                if (TextUtils.isEmpty(name)){
                    edtNameBudget.setError("Name's budget is required");
                    return;
                }

                // Cap nhat budget trong database
                int update = budgetRepository.updateBudget(budgetId, name, money, description);
                if (update > 0){
                    Toast.makeText(EditBudgetActivity.this, "Update budget successful", Toast.LENGTH_SHORT).show();
                    // Quay lai danh sach budget
                    Intent intent = new Intent(EditBudgetActivity.this, MenuActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("MENU_TAB", "BUDGET_TAB");
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(EditBudgetActivity.this, "Update budget fail", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xu ly nut Delete - xoa budget
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hien thi dialog xac nhan truoc khi xoa
                showDeleteConfirmDialog();
            }
        });
    }

    // Load thong tin budget hien tai de hien thi len form
    private void loadBudgetData(){
        currentBudget = budgetRepository.getBudgetById(budgetId);
        if (currentBudget != null){
            // Hien thi thong tin budget len cac view
            edtNameBudget.setText(currentBudget.getName());
            edtMoney.setText(String.valueOf(currentBudget.getMoney()));
            edtDescription.setText(currentBudget.getDescription());
        }
    }

    // Hien thi dialog xac nhan xoa budget
    private void showDeleteConfirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Budget");
        builder.setMessage("Are you sure you want to delete this budget? All expenses related to this budget will remain but will be orphaned.");

        // Nut Yes - xoa budget
        builder.setPositiveButton("Yes", (dialog, which) -> {
            int delete = budgetRepository.deleteBudget(budgetId);
            if (delete > 0){
                Toast.makeText(EditBudgetActivity.this, "Delete budget successful", Toast.LENGTH_SHORT).show();
                // Quay lai danh sach budget
                Intent intent = new Intent(EditBudgetActivity.this, MenuActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("MENU_TAB", "BUDGET_TAB");
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                Toast.makeText(EditBudgetActivity.this, "Delete budget fail", Toast.LENGTH_SHORT).show();
            }
        });

        // Nut No - huy bo
        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });

        builder.show();
    }
}
