package com.example.testing.Activities;

import android.content.Intent;
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

import com.example.testing.MainActivity;
import com.example.testing.Models.UserModel;
import com.example.testing.R;
import com.example.testing.Repositories.UserRepository;

public class LoginActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    Button btnLogin, btnCancel;
    TextView tvSignUp, tvForgetPassword;
    UserRepository userRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userRepository = new UserRepository(LoginActivity.this);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin  = findViewById(R.id.btnLogin);
        btnCancel = findViewById(R.id.btnCancel);
        tvSignUp  = findViewById(R.id.tvSignUp);
        tvForgetPassword = findViewById(R.id.tvForgetPassword);

        // Click to redirect RegisterActivity
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSignUp = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intentSignUp);
            }
        });

        // Click to redirect ForgetPasswordActivity
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForget = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intentForget);
            }
        });

        // FIXED: Added Cancel button click listener
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                if (TextUtils.isEmpty(username)){
                    edtUsername.setError("Username can be not empty");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    edtPassword.setError("Password can be not empty");
                    return;
                }
                UserModel infoUser = userRepository.loginUser(username, password);
                // FIXED: Better null check
                if (infoUser != null && infoUser.getId() > 0 && infoUser.getUsername() != null){
                    // Login successful
                    // Luu thong tin nguoi dung Share preferences
                    SharedPreferences sharePf = getSharedPreferences("USER_INFO", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharePf.edit();
                    editor.putInt("USER_ID", infoUser.getId());
                    editor.putString("USER_USERNAME", infoUser.getUsername());
                    editor.putString("USER_EMAIL", infoUser.getEmail());
                    editor.putInt("USER_ROLE", infoUser.getRole());
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    // Send data to Main Activity
                    Bundle bundle = new Bundle();
                    bundle.putInt("ID_ACCOUNT", infoUser.getId());
                    bundle.putString("ACCOUNT_USER", infoUser.getUsername());
                    bundle.putString("EMAIL_USER", infoUser.getEmail());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    // Account invalid
                    Toast.makeText(LoginActivity.this, "Account invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}