package com.example.androidlabs;

// Model class representing a single todo item with text and urgency status
public class TodoItem {
    private long id;           // Database primary key
    private String text;       // Todo task description
    private boolean isUrgent;  // Urgency flag (true=urgent, false=normal)

    // Constructor for new items without ID (ID will be assigned by database)
    public TodoItem(String text, boolean isUrgent) {
        this.id = -1;  // Temporary ID until saved to database
        this.text = text;
        this.isUrgent = isUrgent;
    }

    // Constructor for items loaded from database with existing ID
    public TodoItem(long id, String text, boolean isUrgent) {
        this.id = id;
        this.text = text;
        this.isUrgent = isUrgent;
    }

    // Getter methods
    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public boolean isUrgent() {
        return isUrgent;
    }

    // Setter methods (if needed for updates)
    public void setId(long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUrgent(boolean urgent) {
        isUrgent = urgent;
    }
}