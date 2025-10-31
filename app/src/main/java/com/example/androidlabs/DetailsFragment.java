package com.example.androidlabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.androidlabs.R;
//start Lab 7

public class DetailsFragment extends Fragment {
    public DetailsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details, container, false);
        Bundle args = getArguments();
        if (args != null) {
            ((TextView)v.findViewById(R.id.fill_name)).setText(args.getString("name",""));
            ((TextView)v.findViewById(R.id.fill_height)).setText(args.getString("height",""));
            // set other fields similarly
        }
        return v;
    }
}
