package com.example.androidlabs;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    private TextView nameText, heightText, massText, hairText, skinText, birthText, genderText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Link TextViews
        nameText = findViewById(R.id.nameTextView);
        heightText = findViewById(R.id.heightTextView);
        massText = findViewById(R.id.massTextView);
        hairText = findViewById(R.id.hairTextView);
        skinText = findViewById(R.id.skinTextView);
        birthText = findViewById(R.id.birthTextView);
        genderText = findViewById(R.id.genderTextView);

        // Get data from Intent
        Bundle data = getIntent().getExtras();
        if (data != null) {
            nameText.setText("Name: " + data.getString("name", "N/A"));
            heightText.setText("Height: " + data.getString("height", "N/A"));
            massText.setText("Mass: " + data.getString("mass", "N/A"));
            hairText.setText("Hair: " + data.getString("hair_color", "N/A"));
            skinText.setText("Skin: " + data.getString("skin_color", "N/A"));
            birthText.setText("Birth Year: " + data.getString("birth_year", "N/A"));
            genderText.setText("Gender: " + data.getString("gender", "N/A"));
        }
    }
}
