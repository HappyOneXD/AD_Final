package com.example.testing.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.testing.Activities.LoginActivity;
import com.example.testing.Models.UserModel;
import com.example.testing.R;
import com.example.testing.Repositories.UserRepository;

import static android.content.Context.MODE_PRIVATE;

public class SettingFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // Views in settings
    private TextView tvUsername, tvEmail;
    private LinearLayout layoutProfile, layoutChangePassword, layoutAbout, layoutLogout;
    private String username = "";
    private String email = "";
    private int userId = 0;
    private UserRepository userRepository;

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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

        // Initialize repository
        userRepository = new UserRepository(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout for fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Map views
        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        layoutProfile = view.findViewById(R.id.layoutProfile);
        layoutChangePassword = view.findViewById(R.id.layoutChangePassword);
        layoutAbout = view.findViewById(R.id.layoutAbout);
        layoutLogout = view.findViewById(R.id.layoutLogout);

        // Get user info from SharedPreferences
        SharedPreferences spf = getActivity().getSharedPreferences("USER_INFO", MODE_PRIVATE);
        userId = spf.getInt("USER_ID", 0);
        username = spf.getString("USER_USERNAME", "");
        email = spf.getString("USER_EMAIL", "");

        // Display user info
        tvUsername.setText(username);
        tvEmail.setText(email);

        // Handle click on Profile
        layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileDialog();
            }
        });

        // Handle click on Change Password
        layoutChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePasswordDialog();
            }
        });

        // Handle click on About
        layoutAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutDialog();
            }
        });

        // Handle click on Logout
        layoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });

        return view;
    }

    // Show profile information dialog
    private void showProfileDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("User Profile");
        builder.setMessage("Username: " + username + "\nEmail: " + email);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    // Show change password dialog
    private void showChangePasswordDialog(){
        // Create custom layout for password change
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        // Current Password field
        final EditText edtCurrentPassword = new EditText(getContext());
        edtCurrentPassword.setHint("Current Password");
        edtCurrentPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(edtCurrentPassword);

        // New Password field
        final EditText edtNewPassword = new EditText(getContext());
        edtNewPassword.setHint("New Password");
        edtNewPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params1.setMargins(0, 20, 0, 0);
        edtNewPassword.setLayoutParams(params1);
        layout.addView(edtNewPassword);

        // Confirm Password field
        final EditText edtConfirmPassword = new EditText(getContext());
        edtConfirmPassword.setHint("Confirm New Password");
        edtConfirmPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params2.setMargins(0, 20, 0, 0);
        edtConfirmPassword.setLayoutParams(params2);
        layout.addView(edtConfirmPassword);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Change Password");
        builder.setView(layout);

        // Change button
        builder.setPositiveButton("Change", (dialog, which) -> {
            String currentPassword = edtCurrentPassword.getText().toString().trim();
            String newPassword = edtNewPassword.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();

            // Validation
            if (TextUtils.isEmpty(currentPassword)){
                Toast.makeText(getContext(), "Please enter current password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(newPassword)){
                Toast.makeText(getContext(), "Please enter new password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(confirmPassword)){
                Toast.makeText(getContext(), "Please confirm new password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)){
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verify current password
            UserModel user = userRepository.loginUser(username, currentPassword);

            if (user != null && user.getId() > 0){
                // Current password is correct, update to new password
                int result = userRepository.updatePasswordByUsername(username, newPassword);

                if (result > 0){
                    Toast.makeText(getContext(), "Password changed successfully!", Toast.LENGTH_SHORT).show();

                    // Show confirmation dialog
                    showPasswordChangedDialog();
                } else {
                    Toast.makeText(getContext(), "Failed to change password. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Current password is incorrect", Toast.LENGTH_SHORT).show();
            }
        });

        // Cancel button
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    // Show password changed confirmation dialog
    private void showPasswordChangedDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Success");
        builder.setMessage("Your password has been changed successfully!\n\nPlease login again with your new password.");
        builder.setPositiveButton("OK", (dialog, which) -> {
            // Clear SharedPreferences and logout
            SharedPreferences spf = getActivity().getSharedPreferences("USER_INFO", MODE_PRIVATE);
            SharedPreferences.Editor editor = spf.edit();
            editor.clear();
            editor.apply();

            // Go to login screen
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
        builder.setCancelable(false);
        builder.show();
    }

    // Show about dialog
    private void showAboutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("About");
        builder.setMessage("Campus Expenses Management\nVersion 1.0.0\n\nThis app helps you manage your daily expenses efficiently.\n\nDeveloped by BudgetWise Solutions Team");
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    // Show logout confirmation dialog
    private void showLogoutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");

        // Yes button - logout
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Clear user info in SharedPreferences
            SharedPreferences spf = getActivity().getSharedPreferences("USER_INFO", MODE_PRIVATE);
            SharedPreferences.Editor editor = spf.edit();
            editor.clear();
            editor.apply();

            // Go to login screen
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        });

        // No button - cancel
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        builder.show();
    }
}