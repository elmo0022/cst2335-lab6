package com.example.androidlabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // a) request code for startActivityForResult
    private static final int REQ_NAME = 1;

    // SharedPreferences constants
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_USERNAME = "username";

    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName  = findViewById(R.id.etName);
        Button btnNext = findViewById(R.id.btnNext);

        // c) Load previously saved name, but only if it exists
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String saved = prefs.getString(KEY_USERNAME, "");  // empty string = not saved yet
        if (!saved.isEmpty()) {
            etName.setText(saved);
        }

        // a) Start NameActivity for a result, passing the current EditText value
        btnNext.setOnClickListener(v -> {
            String name = etName.getText().toString();
            Intent intent = new Intent(MainActivity.this, NameActivity.class);
            intent.putExtra("USER_NAME", name);
            startActivityForResult(intent, REQ_NAME); // (not startActivity)
        });
    }

    // b) Save the current value in SharedPreferences so it loads next time
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit()
                .putString(KEY_USERNAME, etName.getText().toString())
                .apply();
    }

    // (You already have this if you followed earlier steps)
    // Handle the result coming back from NameActivity:
    // resultCode == 1 => "Thank You" -> close app
    // resultCode == 0 => "Don't call me that" -> return to edit name
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_NAME) {
            if (resultCode == 1) {
                finish();
            }
        }
    }
}
