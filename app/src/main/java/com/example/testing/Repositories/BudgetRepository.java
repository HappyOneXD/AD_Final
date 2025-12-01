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
    private String getCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZonedDateTime zone = ZonedDateTime.now();
        return dtf.format(zone);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public long AddNewBudget(String name, int money, String description, int userId) {
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

    @SuppressLint("Range")
    public ArrayList<BudgetModel> getListBudgets() {
        ArrayList<BudgetModel> budgets = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BUDGET, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
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
}

