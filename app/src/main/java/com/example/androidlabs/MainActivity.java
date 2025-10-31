package com.example.androidlabs;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<JSONObject> people = new ArrayList<>();
    private PeopleAdapter listAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.peopleListView);
        listAdapter = new PeopleAdapter();
        listView.setAdapter(listAdapter);

        // Fetch SWAPI data
        new FetchPeopleTask().execute();

        // Handle clicks
        listView.setOnItemClickListener((parent, view, position, id) -> {
            JSONObject person = people.get(position);

            // Bundle data
            Bundle data = new Bundle();
            data.putString("name", person.optString("name"));
            data.putString("height", person.optString("height"));
            data.putString("mass", person.optString("mass"));
            data.putString("hair_color", person.optString("hair_color"));
            data.putString("skin_color", person.optString("skin_color"));
            data.putString("birth_year", person.optString("birth_year"));
            data.putString("gender", person.optString("gender"));

            View fragmentContainer = findViewById(R.id.detailContainer);
            if (fragmentContainer != null) {
                // Tablet → show fragment
                DetailsFragment fragment = new DetailsFragment();
                fragment.setArguments(data);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detailContainer, fragment)
                        .commit();
            } else {
                // Phone → start DetailsActivity
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtras(data);
                startActivity(intent);
            }
        });
    }

    /**
     * AsyncTask to fetch Star Wars people from SWAPI.
     */
    private class FetchPeopleTask extends AsyncTask<Void, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(Void... voids) {
            try {
                URL url = new URL("https://swapi.dev/api/people/?format=json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                JSONObject json = new JSONObject(result.toString());
                return json.getJSONArray("results");

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if (jsonArray == null) return;

            people.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                people.add(jsonArray.optJSONObject(i));
            }
            listAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Adapter for showing character names.
     */
    private class PeopleAdapter extends BaseAdapter {
        @Override
        public int getCount() { return people.size(); }

        @Override
        public Object getItem(int position) { return people.get(position); }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, android.view.ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater()
                        .inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            JSONObject person = people.get(position);
            ((TextView) convertView.findViewById(android.R.id.text1))
                    .setText(person.optString("name"));
            return convertView;
        }
    }
}
