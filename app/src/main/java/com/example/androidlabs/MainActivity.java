package com.example.androidlabs;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

//Start Lab 7
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

        // Fetch data from SWAPI
        new FetchPeopleTask().execute();

        // Click handling
        listView.setOnItemClickListener((parent, view, position, id) -> {
            try {
                JSONObject person = people.get(position);
                Bundle data = new Bundle();
                data.putString("name", person.getString("name"));
                data.putString("height", person.getString("height"));
                data.putString("mass", person.getString("mass"));
                data.putString("hair_color", person.getString("hair_color"));
                data.putString("skin_color", person.getString("skin_color"));
                data.putString("birth_year", person.getString("birth_year"));
                data.putString("gender", person.getString("gender"));

                View fragmentContainer = findViewById(R.id.detailContainer);
                if (fragmentContainer == null) {
                    // Phone → open new activity
                    Intent intent = new Intent(MainActivity.this, EmptyActivity.class);
                    intent.putExtras(data);
                    startActivity(intent);
                } else {
                    // Tablet → show fragment on same screen
                    DetailsFragment fragment = new DetailsFragment();
                    fragment.setArguments(data);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.detailContainer, fragment)
                            .commit();
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
                try {
                    people.add(jsonArray.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            listAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Custom adapter to show character names.
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
            try {
                JSONObject person = people.get(position);
                ((TextView) convertView.findViewById(android.R.id.text1))
                        .setText(person.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }
}
