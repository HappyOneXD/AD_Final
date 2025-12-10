package com.example.testing.Repositories;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.testing.Database.SqliteDbHelper;
import com.example.testing.Models.UserModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserRepository extends SqliteDbHelper {
    private static final String TAG = "UserRepository";

    public UserRepository(@Nullable Context context) {
        super(context);
    }

    // Get current date - works with API 24+
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Save user account
    public long saveUserAccount(String username, String password, String email, String phone) {
        String currentDate = getCurrentDate();
        ContentValues values = new ContentValues();
        values.put(USER_USERNAME, username);
        values.put(USER_PASSWORD, password);
        values.put(USER_EMAIL, email);
        values.put(USER_PHONE, phone);
        values.put(USER_ROLE, 1);
        values.put(CREATED_AT, currentDate);

        SQLiteDatabase db = this.getWritableDatabase();
        long insert = db.insert(TABLE_USERS, null, values);
        db.close();
        return insert;
    }

    public boolean checkExistsUsername(String username) {
        // Check if username exists in database
        boolean checkingExists = false;
        String[] cols = {USER_ID, USER_USERNAME};
        String condition = USER_USERNAME + " =? ";
        String[] params = {username};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.query(TABLE_USERS, cols, condition, params, null, null, null);
        if (data.getCount() > 0) {
            checkingExists = true;
        }
        data.close();
        db.close();
        return checkingExists;
    }

    @SuppressLint("Range")
    public UserModel loginUser(String username, String password) {
        // Check if username and password exist in database
        UserModel infoUser = null;
        String[] cols = {USER_ID, USER_USERNAME, USER_EMAIL, USER_PHONE, USER_ROLE};
        String condition = USER_USERNAME + " =? AND " + USER_PASSWORD + " =? ";
        String[] params = {username, password};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.query(TABLE_USERS, cols, condition, params, null, null, null);
        if (data.getCount() > 0) {
            data.moveToFirst();
            infoUser = new UserModel();
            infoUser.setId(data.getInt(data.getColumnIndex(USER_ID)));
            infoUser.setUsername(data.getString(data.getColumnIndex(USER_USERNAME)));
            infoUser.setEmail(data.getString(data.getColumnIndex(USER_EMAIL)));
            infoUser.setPhone(data.getString(data.getColumnIndex(USER_PHONE)));
            infoUser.setRole(data.getInt(data.getColumnIndex(USER_ROLE)));
        }
        data.close();
        db.close();
        return infoUser;
    }

    // FIXED: Get user by username and email (for password reset)
    @SuppressLint("Range")
    public UserModel getUserByUsernameAndEmail(String username, String email){
        UserModel infoUser = null;
        SQLiteDatabase db = null;
        Cursor data = null;

        try {
            String[] cols = { USER_ID, USER_USERNAME, USER_EMAIL };
            String condition = USER_USERNAME + " =? AND " + USER_EMAIL + " =? ";
            String[] params = { username, email };

            db = this.getReadableDatabase();
            data = db.query(TABLE_USERS, cols, condition, params, null, null, null);

            Log.d(TAG, "Query executed. Row count: " + data.getCount());

            if (data.getCount() > 0){
                data.moveToFirst();
                infoUser = new UserModel();
                infoUser.setId(data.getInt(data.getColumnIndex(USER_ID)));
                infoUser.setUsername(data.getString(data.getColumnIndex(USER_USERNAME)));
                infoUser.setEmail(data.getString(data.getColumnIndex(USER_EMAIL)));
                Log.d(TAG, "User found: " + infoUser.getUsername());
            } else {
                Log.d(TAG, "No user found with username: " + username + " and email: " + email);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in getUserByUsernameAndEmail: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (data != null) {
                data.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return infoUser;
    }

    // FIXED: Update password by username
    public int updatePasswordByUsername(String username, String newPassword){
        SQLiteDatabase db = null;
        int update = 0;

        try {
            String currentDate = getCurrentDate();
            ContentValues values = new ContentValues();
            values.put(USER_PASSWORD, newPassword);
            values.put(UPDATED_AT, currentDate);

            db = this.getWritableDatabase();
            update = db.update(TABLE_USERS, values, USER_USERNAME + " = ?", new String[]{username});
            Log.d(TAG, "Password updated for user: " + username + ", rows affected: " + update);
        } catch (Exception e) {
            Log.e(TAG, "Error updating password: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return update;
    }
}