package com.example.androidlabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetailsFragment extends Fragment {

    private TextView nameText, heightText, massText, hairText, skinText, birthText, genderText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        // Link TextViews
        nameText = view.findViewById(R.id.nameTextView);
        heightText = view.findViewById(R.id.heightTextView);
        massText = view.findViewById(R.id.massTextView);
        hairText = view.findViewById(R.id.hairTextView);
        skinText = view.findViewById(R.id.skinTextView);
        birthText = view.findViewById(R.id.birthTextView);
        genderText = view.findViewById(R.id.genderTextView);

        // Get arguments passed from MainActivity or EmptyActivity
        Bundle data = getArguments();
        if (data != null) {
            nameText.setText("Name: " + data.getString("name", "N/A"));
            heightText.setText("Height: " + data.getString("height", "N/A"));
            massText.setText("Mass: " + data.getString("mass", "N/A"));
            hairText.setText("Hair: " + data.getString("hair_color", "N/A"));
            skinText.setText("Skin: " + data.getString("skin_color", "N/A"));
            birthText.setText("Birth Year: " + data.getString("birth_year", "N/A"));
            genderText.setText("Gender: " + data.getString("gender", "N/A"));
        }

        return view;
    }
}
