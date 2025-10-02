package com.example.androidlabs;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        TextView tvWelcome   = findViewById(R.id.tvWelcome);
        Button btnThankYou   = findViewById(R.id.btnThankYou);
        Button btnDontCall   = findViewById(R.id.btnDontCall);

        // a) Read the name passed from MainActivity and show "Welcome <name>!"
        String name = getIntent().getStringExtra("USER_NAME");
        if (name == null) name = "";
        // Using the format string for localization friendliness:
        tvWelcome.setText(getString(R.string.welcome_name, name));

        // c) Thank You -> set result 1 and return
        btnThankYou.setOnClickListener(v -> {
            setResult(1);
            finish();
        });

        // b) Don't call me that -> set result 0 and return
        btnDontCall.setOnClickListener(v -> {
            setResult(0);
            finish();
        });
    }
}
