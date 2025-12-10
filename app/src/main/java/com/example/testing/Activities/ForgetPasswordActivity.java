package com.example.testing.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testing.Models.UserModel;
import com.example.testing.R;
import com.example.testing.Repositories.UserRepository;

public class ForgetPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ForgetPasswordActivity";

    EditText edtEmail, edtUsername;
    Button btnResetPassword, btnBack;
    TextView tvBackToLogin;
    UserRepository userRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_forget_password);
            Log.d(TAG, "Layout inflated successfully");

            // Initialize repository first
            userRepository = new UserRepository(this);
            Log.d(TAG, "UserRepository initialized");

            // Map views from layout
            try {
                edtEmail = findViewById(R.id.edtEmail);
                edtUsername = findViewById(R.id.edtUsername);
                btnResetPassword = findViewById(R.id.btnResetPassword);
                btnBack = findViewById(R.id.btnBack);
                tvBackToLogin = findViewById(R.id.tvBackToLogin);
                Log.d(TAG, "All views found successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error finding views: " + e.getMessage());
                Toast.makeText(this, "Error initializing views", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // Handle Back to Login
            tvBackToLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        Log.e(TAG, "Error navigating to LoginActivity: " + e.getMessage());
                    }
                }
            });

            // Handle Back button
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        finish();
                    } catch (Exception e) {
                        Log.e(TAG, "Error on back button: " + e.getMessage());
                    }
                }
            });

            // Handle Reset Password button
            btnResetPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        resetPassword();
                    } catch (Exception e) {
                        Log.e(TAG, "Error in resetPassword: " + e.getMessage());
                        e.printStackTrace();
                        Toast.makeText(ForgetPasswordActivity.this,
                                "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Failed to initialize: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void resetPassword() {
        Log.d(TAG, "resetPassword() called");

        // Get input from EditTexts
        String email = edtEmail.getText().toString().trim();
        String username = edtUsername.getText().toString().trim();

        Log.d(TAG, "Username: " + username + ", Email: " + email);

        // Validation
        if (TextUtils.isEmpty(username)){
            edtUsername.setError("Username is required");
            edtUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)){
            edtEmail.setError("Email is required");
            edtEmail.requestFocus();
            return;
        }

        try {
            // Check if username and email exist in database
            Log.d(TAG, "Checking user in database...");
            UserModel user = userRepository.getUserByUsernameAndEmail(username, email);

            if (user != null) {
                Log.d(TAG, "User found - ID: " + user.getId() + ", Username: " + user.getUsername());
            } else {
                Log.d(TAG, "User is null");
            }

            if (user != null && user.getId() > 0 && user.getUsername() != null){
                // If exists, show password reset dialog
                Log.d(TAG, "User verified, showing reset dialog");
                showPasswordResetDialog(user.getUsername());
            } else {
                // If not found
                Log.d(TAG, "User not found in database");
                Toast.makeText(ForgetPasswordActivity.this,
                        "Username or Email not found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Database error: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(ForgetPasswordActivity.this,
                    "Database error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Show password reset success dialog
    private void showPasswordResetDialog(String username){
        try {
            Log.d(TAG, "showPasswordResetDialog() called for user: " + username);

            // Create temporary password
            String tempPassword = "Temp@123";
            Log.d(TAG, "Temporary password: " + tempPassword);

            // Update temporary password in database
            Log.d(TAG, "Updating password in database...");
            int update = userRepository.updatePasswordByUsername(username, tempPassword);
            Log.d(TAG, "Update result: " + update);

            if (update > 0){
                Log.d(TAG, "Password updated successfully, showing dialog");

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Password Reset Successful");
                builder.setMessage("Your temporary password is: " + tempPassword +
                        "\n\nPlease login with this password and change it in Settings." +
                        "\n\nNote: In a real application, this password would be sent to your email.");

                // OK button - return to login screen
                builder.setPositiveButton("OK", (dialog, which) -> {
                    try {
                        Log.d(TAG, "OK button clicked, navigating to login");
                        Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        Log.e(TAG, "Error navigating to login: " + e.getMessage());
                        e.printStackTrace();
                    }
                });

                builder.setCancelable(false);

                try {
                    builder.show();
                    Log.d(TAG, "Dialog shown successfully");
                } catch (Exception e) {
                    Log.e(TAG, "Error showing dialog: " + e.getMessage());
                    e.printStackTrace();
                    Toast.makeText(this, "Error showing dialog: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            } else {
                Log.e(TAG, "Password update failed");
                Toast.makeText(this, "Password reset failed. Please try again.",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in showPasswordResetDialog: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}