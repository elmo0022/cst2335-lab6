package com.example.androidlabs;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Color;

public class MainActivity extends AppCompatActivity {
    private List<TodoItem> todoList;
    private ListView listView;
    private EditText editText;
    private Switch urgentSwitch;
    private Button addButton;
    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize components
        todoList = new ArrayList<>();
        listView = findViewById(R.id.todoListView);
        editText = findViewById(R.id.todoEditText);
        urgentSwitch = findViewById(R.id.urgentSwitch);
        addButton = findViewById(R.id.addButton);

        // Setup adapter
        adapter = new TodoAdapter();
        listView.setAdapter(adapter);

        // Add button click listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTodoItem();
            }
        });
    }

    private void addTodoItem() {
        String text = editText.getText().toString().trim();
        if (!text.isEmpty()) {
            boolean isUrgent = urgentSwitch.isChecked();
            todoList.add(new TodoItem(text, isUrgent));
            adapter.notifyDataSetChanged();
            editText.setText(""); // Clear input
            urgentSwitch.setChecked(false); // Reset switch
        } else {
            Toast.makeText(this, "Please enter some text", Toast.LENGTH_SHORT).show();
        }
    }

    // Custom Adapter
    private class TodoAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return todoList.size();
        }

        @Override
        public Object getItem(int position) {
            return todoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.todo_item, parent, false);
            }

            TextView textView = convertView.findViewById(R.id.todoTextView);
            TodoItem item = todoList.get(position);

            // Set the text
            textView.setText(item.getText());

            // Set colors for urgent items
            if (item.isUrgent()) {
                convertView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                textView.setTextColor(getResources().getColor(android.R.color.white));
            } else {
                convertView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                textView.setTextColor(getResources().getColor(android.R.color.black));
            }

            return convertView;
        }
    }
}