package com.example.testing.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testing.R;

public class LifeCycleActivity extends AppCompatActivity {
    private String LOG_ACTIVITY = "LOG_ACTIVITY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // khi app khoi dong ham nay se chay
        // muc dich chinh ham nay la de load giao dien app
        // xu ly cac logic - tuong tac voi nguoi dung
        // ham nay chi chay duy nhat mot lan
        setContentView(R.layout.activity_life_cycle);
        Log.i(LOG_ACTIVITY, "****** onCreate is running ******");

        Button btnChangeActivity = findViewById(R.id.btnChangeActivity);
        btnChangeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LifeCycleActivity.this, LifeCycleSecondActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // ham nay se chay truoc khi man hinh giao dien xuat hien
        Log.i(LOG_ACTIVITY, "****** onStart is running ********");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // ham nay chay khi giao dien da xuat hien va nguoi dung co the tuong tac voi APP
        Log.i(LOG_ACTIVITY, "******* onResume is running *********");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // ham nay se chay khi chuan bi co mot activity moi sap xua hien
        Log.i(LOG_ACTIVITY, "******* onPause is running ******");
    }

    @Override
    protected void onStop() {
        super.onStop();
        // ham nay se chay khi activity cu bi an di va activity moi hien thi
        Log.i(LOG_ACTIVITY, "****** onStop is running *******");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // ham nay se chay khi hien thi quay tro lai activity truoc do da bi an di
        Log.i(LOG_ACTIVITY, "******* onRestart is running ********");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // ham nay se chay khi APP bi huy(giai phong khoi bo nho thiet bi) - khong phai la an APP di
        Log.i(LOG_ACTIVITY, "****** App is destroyed ********");
    }
}
