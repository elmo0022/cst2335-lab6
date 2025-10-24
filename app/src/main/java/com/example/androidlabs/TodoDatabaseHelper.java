package com.example.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.ContentValues;

// Database manager for todo items - handles table creation and version control
public class TodoDatabaseHelper extends SQLiteOpenHelper {

    // Database identification constants
    private static final String DATABASE_FILE = "todo_database.db";
    private static final int SCHEMA_VERSION = 1;

    // Table and column definitions
    public static final String TODO_TABLE = "todo_entries";
    public static final String KEY_ID = "_id";
    public static final String KEY_TASK = "task_description";
    public static final String KEY_PRIORITY = "is_urgent";

    private static final String INITIALIZE_DB =
            "CREATE TABLE " + TODO_TABLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_TASK + " TEXT NOT NULL, " +
                    KEY_PRIORITY + " INTEGER DEFAULT 0)";

    public TodoDatabaseHelper(Context context) {
        super(context, DATABASE_FILE, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(INITIALIZE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);
    }

    // === ADD THESE MISSING METHODS ===

    /**
     * Retrieve all todo items from the database
     * @return Cursor containing all todo records
     */
    public Cursor queryAllTodos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TODO_TABLE, null, null, null, null, null, null);
    }

    /**
     * Insert a new todo item into the database
     * @param task The todo description text
     * @param isUrgent Whether the todo is urgent
     * @return row ID of newly inserted item, or -1 if error
     */
    public long insertTodo(String task, boolean isUrgent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TASK, task);
        values.put(KEY_PRIORITY, isUrgent ? 1 : 0);
        return db.insert(TODO_TABLE, null, values);
    }

    /**
     * Delete a todo item from the database by ID
     * @param id The unique ID of the todo to delete
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteTodo(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TODO_TABLE, KEY_ID + " = ?", new String[]{String.valueOf(id)}) > 0;
    }

    /**
     * Get the current database version for debugging
     * @return database version number
     */
    public int getDbVersion() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.getVersion();
    }
}