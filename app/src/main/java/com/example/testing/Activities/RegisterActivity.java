package com.example.testing.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testing.R;
import com.example.testing.Repositories.UserRepository;

public class RegisterActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword, edtEmail, edtPhone;
    Button btnRegister, btnCancel;
    TextView tvLoginAccount;
    UserRepository userRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userRepository = new UserRepository(RegisterActivity.this);
        tvLoginAccount = findViewById(R.id.tvLoginAccount);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        btnRegister = findViewById(R.id.btnRegister);
        btnCancel = findViewById(R.id.btnCancel);
        tvLoginAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSignup = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intentSignup);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    edtUsername.setError("Username is required");
                    return;
                }
                String password = edtPassword.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    edtPassword.setError("Password is required");
                    return;
                }
                String email = edtEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    edtEmail.setError("Email is required");
                    return;
                }
                String phone = edtPhone.getText().toString().trim();
                // kiem tra username da ton tai trong csdl hay chua?
                boolean checkingUsername = userRepository.checkExistsUsername(username);
                if (checkingUsername) {
                    // da ton tai username trong csdl
                    // khong cho dang ky tai khoan theo username do
                    edtUsername.setError("Username exists, please choose other username.");
                    return;
                }
                long insert = userRepository.saveUserAccount(username, password, email, phone);
                if (insert == -1) {
                    // loi - khong tao dc tai khoan
                    Toast.makeText(RegisterActivity.this, "Register fail, please try again", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // thanh cong
                    Toast.makeText(RegisterActivity.this, "Register account successful", Toast.LENGTH_SHORT).show();
                    Intent intentLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intentLogin); // chuyen sang trang dang nhap
                }
            }
        });
    }
}
