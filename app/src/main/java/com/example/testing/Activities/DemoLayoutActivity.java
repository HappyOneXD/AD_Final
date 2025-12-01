package com.example.testing.Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testing.R;

public class DemoLayoutActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    RadioGroup radGender;
    RadioButton radFemale, radMale;
    CheckBox cbAgree;
    Button btnSubmit, btnCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // load view ngoai thu muc layout
        setContentView(R.layout.activity_demo_constraint_layout);
        // find element
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.editPassword);
        radGender = findViewById(R.id.radGroupGender);
        radFemale = findViewById(R.id.radFemale);
        radMale = findViewById(R.id.radMale);
        cbAgree = findViewById(R.id.cbAgree);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);

        // block btn cancel
        btnCancel.setEnabled(false); // can not click button

        // event click button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get data from Edit Text username and password
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    edtUsername.setError("Enter username, please");
                    return; // dung lai va bao loi
                }
                if (TextUtils.isEmpty(password)) {
                    edtPassword.setError("Enter password, please");
                    return;
                }
                // handle choose gender
                int selectedId = radGender.getCheckedRadioButtonId(); // choose radio button
                RadioButton rad = (RadioButton) findViewById(selectedId);
                if (rad == null) {
                    Toast.makeText(DemoLayoutActivity.this, "Choose gender ,please", Toast.LENGTH_SHORT).show();
                    return;
                }
                // handle check conditions
                if (!cbAgree.isChecked()) {
                    Toast.makeText(DemoLayoutActivity.this, "Confirm conditions ,please", Toast.LENGTH_SHORT).show();
                    btnCancel.setEnabled(false); // block
                    return;
                } else {
                    btnCancel.setEnabled(true); // unblock
                }
                String gender = rad.getText().toString().trim(); // gender
                // save data to database

            }
        });
    }
}
