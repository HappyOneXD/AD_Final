package com.example.testing.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testing.Activities.LoginActivity;
import com.example.testing.R;

import static android.content.Context.MODE_PRIVATE;

// Fragment hien thi man hinh cai dat
public class SettingFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // Cac view trong settings
    private TextView tvUsername, tvEmail;
    private LinearLayout layoutProfile, layoutChangePassword, layoutAbout, layoutLogout;
    private String username = "";
    private String email = "";

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout cho fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Anh xa cac view
        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        layoutProfile = view.findViewById(R.id.layoutProfile);
        layoutChangePassword = view.findViewById(R.id.layoutChangePassword);
        layoutAbout = view.findViewById(R.id.layoutAbout);
        layoutLogout = view.findViewById(R.id.layoutLogout);

        // Lay thong tin user tu SharedPreferences
        SharedPreferences spf = getActivity().getSharedPreferences("USER_INFO", MODE_PRIVATE);
        username = spf.getString("USER_USERNAME", "");
        email = spf.getString("USER_EMAIL", "");

        // Hien thi thong tin user
        tvUsername.setText(username);
        tvEmail.setText(email);

        // Xu ly click vao Profile
        layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileDialog();
            }
        });

        // Xu ly click vao Change Password
        layoutChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePasswordDialog();
            }
        });

        // Xu ly click vao About
        layoutAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutDialog();
            }
        });

        // Xu ly click vao Logout
        layoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });

        return view;
    }

    // Hien thi dialog thong tin profile
    private void showProfileDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("User Profile");
        builder.setMessage("Username: " + username + "\nEmail: " + email);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    // Hien thi dialog doi mat khau
    private void showChangePasswordDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Change Password");
        builder.setMessage("This feature isn't being worked on");
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    // Hien thi dialog thong tin ung dung
    private void showAboutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("About");
        builder.setMessage("Campus Expenses Management\nThis help you manage your money");
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    // Hien thi dialog xac nhan dang xuat
    private void showLogoutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");

        // Nut Yes - dang xuat
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Xoa thong tin user trong SharedPreferences
            SharedPreferences spf = getActivity().getSharedPreferences("USER_INFO", MODE_PRIVATE);
            SharedPreferences.Editor editor = spf.edit();
            editor.clear();
            editor.apply();

            // Chuyen ve man hinh login
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        // Nut No - huy bo
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        builder.show();
    }
}