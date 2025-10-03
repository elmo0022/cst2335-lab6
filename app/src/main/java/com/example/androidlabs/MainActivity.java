package com.example.androidlabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int REQ_NAME = 1;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_USERNAME = "username";

    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        Button btnNext = findViewById(R.id.btnNext);

        // Load saved name if present
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String saved = prefs.getString(KEY_USERNAME, "");
        if (!saved.isEmpty()) {
            etName.setText(saved);
        }

        // Launch NameActivity and expect a result
        btnNext.setOnClickListener(v -> {
            String name = etName.getText().toString();
            Intent intent = new Intent(MainActivity.this, NameActivity.class);
            intent.putExtra("USER_NAME", name);
            startActivityForResult(intent, REQ_NAME);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putString(KEY_USERNAME, etName.getText().toString()).apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_NAME) {
            if (resultCode == 1) {
                // user happy -> close app
                finish();
            } else if (resultCode == 0) {
                // user wants to change their name -> keep focus here
                etName.requestFocus();
                etName.selectAll();
                // Optional: Toast.makeText(this, "Please change your name", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
