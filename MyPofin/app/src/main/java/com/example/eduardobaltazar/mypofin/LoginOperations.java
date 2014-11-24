package com.example.eduardobaltazar.mypofin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LoginOperations {

    // Database fields
    private DataBaseWrapper dbHelper;
    private String[] STUDENT_TABLE_COLUMNS = { DataBaseWrapper.MYPLOGIN_EMAIL, DataBaseWrapper.MYPLOGIN_PSW };
    private SQLiteDatabase database;

    public LoginOperations(Context context) {
        dbHelper = new DataBaseWrapper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insertUser(String email, String password) {
        ContentValues values = new ContentValues();
        values.put(DataBaseWrapper.MYPLOGIN_EMAIL, email);
        values.put(DataBaseWrapper.MYPLOGIN_PSW, password);

        long idrecord = database.insert(DataBaseWrapper.MYPLOGIN, null, values);

    }

    public void deleteUser() {
        database.delete(DataBaseWrapper.MYPLOGIN, null, null);
    }

    public UserModel getUser() {
        UserModel usmod = new UserModel();
        Cursor cursor = database.query(DataBaseWrapper.MYPLOGIN,
                STUDENT_TABLE_COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            usmod = parseRecord(cursor);
        }

        cursor.close();
        return usmod;

    }

    private UserModel parseRecord(Cursor cursor) {
        UserModel record = new UserModel();
        record.setEmail((cursor.getString(0)));
        record.setPassword(cursor.getString(1));
        return record;
    }

}
