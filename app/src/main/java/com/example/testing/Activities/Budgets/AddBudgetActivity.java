package com.example.testing.Activities.Budgets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testing.Activities.MenuActivity;
import com.example.testing.R;
import com.example.testing.Repositories.BudgetRepository;

public class AddBudgetActivity extends AppCompatActivity {
    EditText edtNameBudget, edtMoney, edtDescription;
    Button btnSave, btnBack;
    BudgetRepository budgetRepository;
    private int userId = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);
        edtNameBudget = findViewById(R.id.edtBudgetName);
        edtMoney = findViewById(R.id.edtMoney);
        edtDescription = findViewById(R.id.edtDescription);
        btnSave = findViewById(R.id.btnSaveBudget);
        btnBack = findViewById(R.id.btnBackBudget);
        budgetRepository = new BudgetRepository(AddBudgetActivity.this);
        SharedPreferences spf = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        userId = spf.getInt("USER_ID", 0);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddBudgetActivity.this, MenuActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("MENU_TAB", "BUDGET_TAB");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String money = edtMoney.getText().toString().trim();
                String budgetName = edtNameBudget.getText().toString().trim();
                String description = edtDescription.getText().toString().trim();
                if (TextUtils.isEmpty(money)) {
                    edtMoney.setError("Money's budget is required");
                    return;
                }
                int moneyBudget = Integer.parseInt(money);
                if (moneyBudget <= 0) {
                    edtMoney.setError("Money's budget is numeric and more than 0");
                    return;
                }
                if (TextUtils.isEmpty(budgetName)) {
                    edtNameBudget.setError("Name's budget is required");
                    return;
                }
                // xu ly luu data toi database
                long insert = budgetRepository.AddNewBudget(budgetName, moneyBudget, description, userId);
                if (insert == -1) {
                    Toast.makeText(AddBudgetActivity.this, "create budget fail", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(AddBudgetActivity.this, "create budget successful", Toast.LENGTH_SHORT).show();
                    // quay lai danh sach list
                    Intent intent = new Intent(AddBudgetActivity.this, MenuActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("MENU_TAB", "BUDGET_TAB");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }
}

