package com.example.testing.Repositories;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.testing.Database.SqliteDbHelper;
import com.example.testing.Models.ExpenseModel;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ExpenseRepository extends SqliteDbHelper {
    public ExpenseRepository(@Nullable Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZonedDateTime zone = ZonedDateTime.now();
        return dtf.format(zone);
    }

    // Add new expense
    @RequiresApi(api = Build.VERSION_CODES.O)
    public long addNewExpense(String name, int amount, String description, int budgetId, int userId) {
        String currentDate = getCurrentDate();
        ContentValues values = new ContentValues();
        values.put(NAME_EXPENSE, name);
        values.put(AMOUNT_EXPENSE, amount);
        values.put(DESC_EXPENSE, description);
        values.put(BUDGET_ID_EXPENSE, budgetId);
        values.put(USER_ID_EXPENSE, userId);
        values.put(STATUS_EXPENSE, 1);
        values.put(CREATED_AT, currentDate);

        SQLiteDatabase db = this.getWritableDatabase();
        long insert = db.insert(TABLE_EXPENSES, null, values);
        db.close();
        return insert;
    }

    // Get all expenses
    @SuppressLint("Range")
    public ArrayList<ExpenseModel> getListExpenses() {
        ArrayList<ExpenseModel> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT e.*, b." + NAME_BUDGET + " as budget_name FROM " + TABLE_EXPENSES + " e " +
                "LEFT JOIN " + TABLE_BUDGET + " b ON e." + BUDGET_ID_EXPENSE + " = b." + ID_BUDGET +
                " ORDER BY e." + CREATED_AT + " DESC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    ExpenseModel expense = new ExpenseModel(
                            cursor.getInt(cursor.getColumnIndex(ID_EXPENSE)),
                            cursor.getString(cursor.getColumnIndex(NAME_EXPENSE)),
                            cursor.getInt(cursor.getColumnIndex(AMOUNT_EXPENSE)),
                            cursor.getString(cursor.getColumnIndex(DESC_EXPENSE)),
                            cursor.getInt(cursor.getColumnIndex(BUDGET_ID_EXPENSE)),
                            cursor.getInt(cursor.getColumnIndex(USER_ID_EXPENSE)),
                            cursor.getInt(cursor.getColumnIndex(STATUS_EXPENSE)),
                            cursor.getString(cursor.getColumnIndex(CREATED_AT)),
                            cursor.getString(cursor.getColumnIndex(UPDATED_AT)),
                            cursor.getString(cursor.getColumnIndex(DELETED_AT))
                    );
                    expense.setBudgetName(cursor.getString(cursor.getColumnIndex("budget_name")));
                    expenses.add(expense);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return expenses;
    }

    // Get expenses by date range (NEW METHOD)
    @SuppressLint("Range")
    public ArrayList<ExpenseModel> getExpensesByDateRange(String startDate, String endDate) {
        ArrayList<ExpenseModel> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT e.*, b." + NAME_BUDGET + " as budget_name FROM " + TABLE_EXPENSES + " e " +
                "LEFT JOIN " + TABLE_BUDGET + " b ON e." + BUDGET_ID_EXPENSE + " = b." + ID_BUDGET +
                " WHERE e." + CREATED_AT + " >= ? AND e." + CREATED_AT + " <= ?" +
                " ORDER BY e." + CREATED_AT + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{startDate, endDate});

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    ExpenseModel expense = new ExpenseModel(
                            cursor.getInt(cursor.getColumnIndex(ID_EXPENSE)),
                            cursor.getString(cursor.getColumnIndex(NAME_EXPENSE)),
                            cursor.getInt(cursor.getColumnIndex(AMOUNT_EXPENSE)),
                            cursor.getString(cursor.getColumnIndex(DESC_EXPENSE)),
                            cursor.getInt(cursor.getColumnIndex(BUDGET_ID_EXPENSE)),
                            cursor.getInt(cursor.getColumnIndex(USER_ID_EXPENSE)),
                            cursor.getInt(cursor.getColumnIndex(STATUS_EXPENSE)),
                            cursor.getString(cursor.getColumnIndex(CREATED_AT)),
                            cursor.getString(cursor.getColumnIndex(UPDATED_AT)),
                            cursor.getString(cursor.getColumnIndex(DELETED_AT))
                    );
                    expense.setBudgetName(cursor.getString(cursor.getColumnIndex("budget_name")));
                    expenses.add(expense);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return expenses;
    }

    // Get expenses by user ID
    @SuppressLint("Range")
    public ArrayList<ExpenseModel> getExpensesByUserId(int userId) {
        ArrayList<ExpenseModel> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT e.*, b." + NAME_BUDGET + " as budget_name FROM " + TABLE_EXPENSES + " e " +
                "LEFT JOIN " + TABLE_BUDGET + " b ON e." + BUDGET_ID_EXPENSE + " = b." + ID_BUDGET +
                " WHERE e." + USER_ID_EXPENSE + " = ? ORDER BY e." + CREATED_AT + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    ExpenseModel expense = new ExpenseModel(
                            cursor.getInt(cursor.getColumnIndex(ID_EXPENSE)),
                            cursor.getString(cursor.getColumnIndex(NAME_EXPENSE)),
                            cursor.getInt(cursor.getColumnIndex(AMOUNT_EXPENSE)),
                            cursor.getString(cursor.getColumnIndex(DESC_EXPENSE)),
                            cursor.getInt(cursor.getColumnIndex(BUDGET_ID_EXPENSE)),
                            cursor.getInt(cursor.getColumnIndex(USER_ID_EXPENSE)),
                            cursor.getInt(cursor.getColumnIndex(STATUS_EXPENSE)),
                            cursor.getString(cursor.getColumnIndex(CREATED_AT)),
                            cursor.getString(cursor.getColumnIndex(UPDATED_AT)),
                            cursor.getString(cursor.getColumnIndex(DELETED_AT))
                    );
                    expense.setBudgetName(cursor.getString(cursor.getColumnIndex("budget_name")));
                    expenses.add(expense);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return expenses;
    }

    // Get expense by ID
    @SuppressLint("Range")
    public ExpenseModel getExpenseById(int expenseId) {
        ExpenseModel expense = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT e.*, b." + NAME_BUDGET + " as budget_name FROM " + TABLE_EXPENSES + " e " +
                "LEFT JOIN " + TABLE_BUDGET + " b ON e." + BUDGET_ID_EXPENSE + " = b." + ID_BUDGET +
                " WHERE e." + ID_EXPENSE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(expenseId)});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            expense = new ExpenseModel(
                    cursor.getInt(cursor.getColumnIndex(ID_EXPENSE)),
                    cursor.getString(cursor.getColumnIndex(NAME_EXPENSE)),
                    cursor.getInt(cursor.getColumnIndex(AMOUNT_EXPENSE)),
                    cursor.getString(cursor.getColumnIndex(DESC_EXPENSE)),
                    cursor.getInt(cursor.getColumnIndex(BUDGET_ID_EXPENSE)),
                    cursor.getInt(cursor.getColumnIndex(USER_ID_EXPENSE)),
                    cursor.getInt(cursor.getColumnIndex(STATUS_EXPENSE)),
                    cursor.getString(cursor.getColumnIndex(CREATED_AT)),
                    cursor.getString(cursor.getColumnIndex(UPDATED_AT)),
                    cursor.getString(cursor.getColumnIndex(DELETED_AT))
            );
            expense.setBudgetName(cursor.getString(cursor.getColumnIndex("budget_name")));
        }
        cursor.close();
        db.close();
        return expense;
    }

    // Update expense
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int updateExpense(int expenseId, String name, int amount, String description, int budgetId) {
        String currentDate = getCurrentDate();
        ContentValues values = new ContentValues();
        values.put(NAME_EXPENSE, name);
        values.put(AMOUNT_EXPENSE, amount);
        values.put(DESC_EXPENSE, description);
        values.put(BUDGET_ID_EXPENSE, budgetId);
        values.put(UPDATED_AT, currentDate);

        SQLiteDatabase db = this.getWritableDatabase();
        int update = db.update(TABLE_EXPENSES, values, ID_EXPENSE + " = ?", new String[]{String.valueOf(expenseId)});
        db.close();
        return update;
    }

    // Delete expense
    public int deleteExpense(int expenseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int delete = db.delete(TABLE_EXPENSES, ID_EXPENSE + " = ?", new String[]{String.valueOf(expenseId)});
        db.close();
        return delete;
    }

    // Get total expenses for a budget
    @SuppressLint("Range")
    public int getTotalExpensesByBudget(int budgetId) {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + AMOUNT_EXPENSE + ") as total FROM " + TABLE_EXPENSES +
                " WHERE " + BUDGET_ID_EXPENSE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(budgetId)});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            total = cursor.getInt(cursor.getColumnIndex("total"));
        }
        cursor.close();
        db.close();
        return total;
    }

    // Get recent expenses (limited)
    @SuppressLint("Range")
    public ArrayList<ExpenseModel> getRecentExpenses(int limit) {
        ArrayList<ExpenseModel> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT e.*, b." + NAME_BUDGET + " as budget_name FROM " + TABLE_EXPENSES + " e " +
                "LEFT JOIN " + TABLE_BUDGET + " b ON e." + BUDGET_ID_EXPENSE + " = b." + ID_BUDGET +
                " ORDER BY e." + CREATED_AT + " DESC LIMIT " + limit;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    ExpenseModel expense = new ExpenseModel(
                            cursor.getInt(cursor.getColumnIndex(ID_EXPENSE)),
                            cursor.getString(cursor.getColumnIndex(NAME_EXPENSE)),
                            cursor.getInt(cursor.getColumnIndex(AMOUNT_EXPENSE)),
                            cursor.getString(cursor.getColumnIndex(DESC_EXPENSE)),
                            cursor.getInt(cursor.getColumnIndex(BUDGET_ID_EXPENSE)),
                            cursor.getInt(cursor.getColumnIndex(USER_ID_EXPENSE)),
                            cursor.getInt(cursor.getColumnIndex(STATUS_EXPENSE)),
                            cursor.getString(cursor.getColumnIndex(CREATED_AT)),
                            cursor.getString(cursor.getColumnIndex(UPDATED_AT)),
                            cursor.getString(cursor.getColumnIndex(DELETED_AT))
                    );
                    expense.setBudgetName(cursor.getString(cursor.getColumnIndex("budget_name")));
                    expenses.add(expense);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return expenses;
    }
    // Get expenses by date range for a specific user
    @SuppressLint("Range")
    public ArrayList<ExpenseModel> getExpensesByDateRangeAndUser(String startDate, String endDate, int userId) {
        ArrayList<ExpenseModel> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT e.*, b." + NAME_BUDGET + " as budget_name FROM " + TABLE_EXPENSES + " e " +
                "LEFT JOIN " + TABLE_BUDGET + " b ON e." + BUDGET_ID_EXPENSE + " = b." + ID_BUDGET +
                " WHERE e." + CREATED_AT + " >= ? AND e." + CREATED_AT + " <= ? AND e." + USER_ID_EXPENSE + " = ?" +
                " ORDER BY e." + CREATED_AT + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{startDate, endDate, String.valueOf(userId)});

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    ExpenseModel expense = new ExpenseModel(
                            cursor.getInt(cursor.getColumnIndex(ID_EXPENSE)),
                            cursor.getString(cursor.getColumnIndex(NAME_EXPENSE)),
                            cursor.getInt(cursor.getColumnIndex(AMOUNT_EXPENSE)),
                            cursor.getString(cursor.getColumnIndex(DESC_EXPENSE)),
                            cursor.getInt(cursor.getColumnIndex(BUDGET_ID_EXPENSE)),
                            cursor.getInt(cursor.getColumnIndex(USER_ID_EXPENSE)),
                            cursor.getInt(cursor.getColumnIndex(STATUS_EXPENSE)),
                            cursor.getString(cursor.getColumnIndex(CREATED_AT)),
                            cursor.getString(cursor.getColumnIndex(UPDATED_AT)),
                            cursor.getString(cursor.getColumnIndex(DELETED_AT))
                    );
                    expense.setBudgetName(cursor.getString(cursor.getColumnIndex("budget_name")));
                    expenses.add(expense);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return expenses;
    }

    // Get total expenses for a specific user
    @SuppressLint("Range")
    public int getTotalExpensesByUser(int userId) {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + AMOUNT_EXPENSE + ") as total FROM " + TABLE_EXPENSES +
                " WHERE " + USER_ID_EXPENSE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            total = cursor.getInt(cursor.getColumnIndex("total"));
        }
        cursor.close();
        db.close();
        return total;
    }
}