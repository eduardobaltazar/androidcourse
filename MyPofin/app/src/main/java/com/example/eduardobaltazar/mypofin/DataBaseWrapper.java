package com.example.eduardobaltazar.mypofin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseWrapper extends SQLiteOpenHelper {

    public static final String MYPLOGIN = "MypofinLogin";
    public static final String MYPLOGIN_EMAIL = "_email";
    public static final String MYPLOGIN_PSW = "_password";

    private static final String DATABASE_NAME = "MypofinLogin.db";
    private static final int DATABASE_VERSION = 1;

    // creation SQLite statement
    private static final String DATABASE_CREATE = "create table " + MYPLOGIN
            + "(" + MYPLOGIN_EMAIL + " text not null, "
            + MYPLOGIN_PSW + " text not null);";

    public DataBaseWrapper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you should do some logging in here
        // ..

        db.execSQL("DROP TABLE IF EXISTS " + MYPLOGIN);
        onCreate(db);
    }

}