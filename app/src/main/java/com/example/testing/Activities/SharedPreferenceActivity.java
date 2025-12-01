package com.example.testing.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testing.R;

public class SharedPreferenceActivity extends AppCompatActivity {
    EditText edtNumber1, edtNumber2, edtResult;
    Button btnAdd, btnClear, btnSavePref;
    TextView tvHistory;
    private String historyUser = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_preference);
        edtNumber1 = findViewById(R.id.edtNumber1);
        edtNumber2 = findViewById(R.id.edtNumber2);
        edtResult = findViewById(R.id.edtResult);
        btnAdd = findViewById(R.id.btnAddNumber);
        btnClear = findViewById(R.id.btnClearData);
        btnSavePref = findViewById(R.id.btnSavePref);
        tvHistory = findViewById(R.id.tvHistory);
        edtResult.setEnabled(false); // read only

        //read data from shared preference
        // view history of user
        SharedPreferences prefs = getSharedPreferences("CalculateMath", MODE_PRIVATE);
        historyUser = prefs.getString("HistoryUserMath", "");
        tvHistory.setText(historyUser);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number1 = Integer.parseInt(edtNumber1.getText().toString().trim());
                int number2 = Integer.parseInt(edtNumber2.getText().toString().trim());
                int result = number1 + number2;
                edtResult.setText(String.valueOf(result)); // view result
                historyUser += number1 + " + " + number2 + " = " + result;
                tvHistory.setText(historyUser);
                historyUser += "\n"; // moi phep tinh se xuong dong
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                historyUser = "";
                tvHistory.setText(historyUser);
                edtNumber1.setText(historyUser);
                edtNumber2.setText(historyUser);
                edtResult.setText(historyUser);
            }
        });
        btnSavePref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(historyUser)) {
                    Toast.makeText(SharedPreferenceActivity.this, "History can be not empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences sharedPref = getSharedPreferences("CalculateMath", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("HistoryUserMath", historyUser);
                editor.apply();
            }
        });
    }
}
