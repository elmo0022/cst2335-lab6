package com.example.androidlabs;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty); // Must be this layout

        // Only add fragment if this is the first creation
        if (savedInstanceState == null) {
            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(getIntent().getExtras()); // Pass the bundle

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detailContainer, fragment)
                    .commit();
        }
    }
}
