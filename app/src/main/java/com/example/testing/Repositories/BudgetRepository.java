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
import com.example.testing.Models.BudgetModel;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BudgetRepository extends SqliteDbHelper {
    public BudgetRepository(@Nullable Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getCurrentDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZonedDateTime zone = ZonedDateTime.now();
        return dtf.format(zone);
    }

    // Add new budget
    @RequiresApi(api = Build.VERSION_CODES.O)
    public long AddNewBudget(String name, int money, String description, int userId){
        String currentDate = getCurrentDate();
        ContentValues values = new ContentValues();
        values.put(NAME_BUDGET, name);
        values.put(MONEY_BUDGET, money);
        values.put(DESC_BUDGET, description);
        values.put(STATUS_BUDGET, 1);
        values.put(CREATOR_BUDGET, userId);
        values.put(CREATED_AT, currentDate);
        SQLiteDatabase db = this.getWritableDatabase();
        long insert = db.insert(TABLE_BUDGET, null, values);
        db.close();
        return insert;
    }

    // Get all budgets
    @SuppressLint("Range")
    public ArrayList<BudgetModel> getListBudgets(){
        ArrayList<BudgetModel> budgets = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BUDGET, null);
        if (cursor.getCount() > 0){
            if (cursor.moveToFirst()){
                do {
                    budgets.add(
                            new BudgetModel(
                                    cursor.getInt(cursor.getColumnIndex(ID_BUDGET)),
                                    cursor.getString(cursor.getColumnIndex(NAME_BUDGET)),
                                    cursor.getInt(cursor.getColumnIndex(MONEY_BUDGET)),
                                    cursor.getString(cursor.getColumnIndex(DESC_BUDGET)),
                                    cursor.getInt(cursor.getColumnIndex(STATUS_BUDGET)),
                                    cursor.getInt(cursor.getColumnIndex(CREATOR_BUDGET)),
                                    cursor.getString(cursor.getColumnIndex(CREATED_AT)),
                                    cursor.getString(cursor.getColumnIndex(UPDATED_AT)),
                                    cursor.getString(cursor.getColumnIndex(DELETED_AT))
                            )
                    );
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return budgets;
    }

    // Get budgets by user ID
    @SuppressLint("Range")
    public ArrayList<BudgetModel> getBudgetsByUserId(int userId){
        ArrayList<BudgetModel> budgets = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BUDGET + " WHERE " + CREATOR_BUDGET + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.getCount() > 0){
            if (cursor.moveToFirst()){
                do {
                    budgets.add(
                            new BudgetModel(
                                    cursor.getInt(cursor.getColumnIndex(ID_BUDGET)),
                                    cursor.getString(cursor.getColumnIndex(NAME_BUDGET)),
                                    cursor.getInt(cursor.getColumnIndex(MONEY_BUDGET)),
                                    cursor.getString(cursor.getColumnIndex(DESC_BUDGET)),
                                    cursor.getInt(cursor.getColumnIndex(STATUS_BUDGET)),
                                    cursor.getInt(cursor.getColumnIndex(CREATOR_BUDGET)),
                                    cursor.getString(cursor.getColumnIndex(CREATED_AT)),
                                    cursor.getString(cursor.getColumnIndex(UPDATED_AT)),
                                    cursor.getString(cursor.getColumnIndex(DELETED_AT))
                            )
                    );
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return budgets;
    }

    // Get budget by ID
    @SuppressLint("Range")
    public BudgetModel getBudgetById(int budgetId){
        BudgetModel budget = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BUDGET + " WHERE " + ID_BUDGET + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(budgetId)});
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            budget = new BudgetModel(
                    cursor.getInt(cursor.getColumnIndex(ID_BUDGET)),
                    cursor.getString(cursor.getColumnIndex(NAME_BUDGET)),
                    cursor.getInt(cursor.getColumnIndex(MONEY_BUDGET)),
                    cursor.getString(cursor.getColumnIndex(DESC_BUDGET)),
                    cursor.getInt(cursor.getColumnIndex(STATUS_BUDGET)),
                    cursor.getInt(cursor.getColumnIndex(CREATOR_BUDGET)),
                    cursor.getString(cursor.getColumnIndex(CREATED_AT)),
                    cursor.getString(cursor.getColumnIndex(UPDATED_AT)),
                    cursor.getString(cursor.getColumnIndex(DELETED_AT))
            );
        }
        cursor.close();
        db.close();
        return budget;
    }

    // Update budget
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int updateBudget(int budgetId, String name, int money, String description){
        String currentDate = getCurrentDate();
        ContentValues values = new ContentValues();
        values.put(NAME_BUDGET, name);
        values.put(MONEY_BUDGET, money);
        values.put(DESC_BUDGET, description);
        values.put(UPDATED_AT, currentDate);

        SQLiteDatabase db = this.getWritableDatabase();
        int update = db.update(TABLE_BUDGET, values, ID_BUDGET + " = ?", new String[]{String.valueOf(budgetId)});
        db.close();
        return update;
    }

    // Delete budget
    public int deleteBudget(int budgetId){
        SQLiteDatabase db = this.getWritableDatabase();
        int delete = db.delete(TABLE_BUDGET, ID_BUDGET + " = ?", new String[]{String.valueOf(budgetId)});
        db.close();
        return delete;
    }

    // Get total budget amount
    @SuppressLint("Range")
    public int getTotalBudgetMoney(){
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + MONEY_BUDGET + ") as total FROM " + TABLE_BUDGET;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            total = cursor.getInt(cursor.getColumnIndex("total"));
        }
        cursor.close();
        db.close();
        return total;
    }
}