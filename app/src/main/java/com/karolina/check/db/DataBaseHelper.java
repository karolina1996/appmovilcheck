package com.karolina.check.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by RicardoAndrés on 07/04/2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "check.db";
    public static int VERSION = 1;

    public DataBaseHelper(Context context){
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query;

        query = "CREATE TABLE vaccine(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id VARCHAR, " +
                "name VARCHAR, " +
                "desc VARCHAR, " +
                "date DATE, " +
                "image VARCHAR)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE vaccine");
        onCreate(sqLiteDatabase);
    }

}
