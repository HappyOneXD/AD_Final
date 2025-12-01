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
import com.example.testing.Models.UserModel;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class UserRepository extends SqliteDbHelper {
    public UserRepository(@Nullable Context context) {
        super(context);
    }

    // day la noi viet thao tac truy van du lieu
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZonedDateTime zone = ZonedDateTime.now();
        return dtf.format(zone);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public long saveUserAccount(String username, String password, String email, String phone) {
        // xu ly lay ra ngay thang hien tai
        String currentDate = getCurrentDate();
        ContentValues values = new ContentValues();
        values.put(USER_USERNAME, username);
        values.put(USER_PASSWORD, password);
        values.put(USER_EMAIL, email);
        values.put(USER_PHONE, phone);
        values.put(USER_ROLE, 1);
        values.put(CREATED_AT, currentDate);
        // insert data to users table
        SQLiteDatabase db = this.getWritableDatabase();
        long insert = db.insert(TABLE_USERS, null, values);
        db.close();
        return insert;
    }

    public boolean checkExistsUsername(String username) {
        // kiem tra xem username(account) da ton tai trong csdl chua ?
        boolean checkingExists = false;
        // khai bao danh sach cac cot trong bang users can truy van
        String[] cols = {USER_ID, USER_USERNAME};
        // select id, username form users;
        String condition = USER_USERNAME + " =? ";
        // select id, username from users where username = ?
        String[] params = {username}; // tham so truyen vao cho dieu kien WHERE
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
        // kiem xem username va password cua nguoi co ton tai trong csdl hay ko?
        UserModel infoUser = new UserModel();
        String[] cols = {USER_ID, USER_USERNAME, USER_EMAIL, USER_PHONE, USER_ROLE};
        String condition = USER_USERNAME + " =? AND " + USER_PASSWORD + " =? ";
        // select id, username, email, phone, role from users where username = ? and password = ?
        String[] params = {username, password};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.query(TABLE_USERS, cols, condition, params, null, null, null);
        if (data.getCount() > 0) {
            data.moveToFirst();
            // gan du lieu vao User Model
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
}
