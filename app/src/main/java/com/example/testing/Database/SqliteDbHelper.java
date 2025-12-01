package com.example.testing.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqliteDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "campus_expenses";
    private static final int DB_VERSION = 3; // Updated version

    // Table USERS
    protected static final String TABLE_USERS = "users";
    protected static final String USER_ID = "id";
    protected static final String USER_USERNAME = "username";
    protected static final String USER_PASSWORD = "password";
    protected static final String USER_EMAIL = "email";
    protected static final String USER_PHONE = "phone";
    protected static final String USER_ROLE = "role";

    // Table BUDGETS
    protected static final String TABLE_BUDGET = "budgets";
    protected static final String ID_BUDGET = "id";
    protected static final String NAME_BUDGET = "name";
    protected static final String MONEY_BUDGET = "money";
    protected static final String DESC_BUDGET = "description";
    protected static final String STATUS_BUDGET = "status";
    protected static final String CREATOR_BUDGET = "user_id";

    // Table EXPENSES (NEW)
    protected static final String TABLE_EXPENSES = "expenses";
    protected static final String ID_EXPENSE = "id";
    protected static final String NAME_EXPENSE = "name";
    protected static final String AMOUNT_EXPENSE = "amount";
    protected static final String DESC_EXPENSE = "description";
    protected static final String BUDGET_ID_EXPENSE = "budget_id";
    protected static final String USER_ID_EXPENSE = "user_id";
    protected static final String STATUS_EXPENSE = "status";

    // Common columns
    protected static final String CREATED_AT = "created_at";
    protected static final String UPDATED_AT = "updated_at";
    protected static final String DELETED_AT = "deleted_at";

    public SqliteDbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create USERS table
        String tableUser = " CREATE TABLE " + TABLE_USERS + " ( "
                + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_USERNAME + " VARCHAR(30) NOT NULL, "
                + USER_PASSWORD + " VARCHAR(200) NOT NULL, "
                + USER_EMAIL + " VARCHAR(60) NOT NULL, "
                + USER_PHONE + " VARCHAR(20), "
                + USER_ROLE + " TINYINT DEFAULT(1), "
                + CREATED_AT + " DATETIME, "
                + UPDATED_AT + " DATETIME, "
                + DELETED_AT + " DATETIME ) ";

        // Create BUDGETS table
        String tableBudget = " CREATE TABLE " + TABLE_BUDGET + " ( "
                + ID_BUDGET + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_BUDGET + " VARCHAR(50) NOT NULL, "
                + MONEY_BUDGET + " INTEGER NOT NULL, "
                + DESC_BUDGET + " VARCHAR(200), "
                + STATUS_BUDGET + " TINYINT DEFAULT(1), "
                + CREATOR_BUDGET + " INTEGER NOT NULL, "
                + CREATED_AT + " DATETIME, "
                + UPDATED_AT + " DATETIME, "
                + DELETED_AT + " DATETIME ) ";

        // Create EXPENSES table (NEW)
        String tableExpenses = " CREATE TABLE " + TABLE_EXPENSES + " ( "
                + ID_EXPENSE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_EXPENSE + " VARCHAR(100) NOT NULL, "
                + AMOUNT_EXPENSE + " INTEGER NOT NULL, "
                + DESC_EXPENSE + " VARCHAR(200), "
                + BUDGET_ID_EXPENSE + " INTEGER NOT NULL, "
                + USER_ID_EXPENSE + " INTEGER NOT NULL, "
                + STATUS_EXPENSE + " TINYINT DEFAULT(1), "
                + CREATED_AT + " DATETIME, "
                + UPDATED_AT + " DATETIME, "
                + DELETED_AT + " DATETIME, "
                + " FOREIGN KEY(" + BUDGET_ID_EXPENSE + ") REFERENCES " + TABLE_BUDGET + "(" + ID_BUDGET + "), "
                + " FOREIGN KEY(" + USER_ID_EXPENSE + ") REFERENCES " + TABLE_USERS + "(" + USER_ID + ") ) ";

        db.execSQL(tableUser);
        db.execSQL(tableBudget);
        db.execSQL(tableExpenses);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
            onCreate(db);
        }
    }
}
