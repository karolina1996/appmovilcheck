package com.karolina.check.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.karolina.check.models.Vaccine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RicardoAndrés on 07/04/2017.
 */

public class VaccineDao {

    static final String TABLE = "vaccine";
    static final String C_ID = "_id";
    static final String C_USER_ID = "user_id";
    static final String C_NAME = "name";
    static final String C_DESC = "desc";
    static final String C_DATE = "date";
    static final String C_IMAGE = "image";

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    SQLiteDatabase sqLiteDatabase;

    public VaccineDao(Context context){
        DataBaseHelper helper = new DataBaseHelper(context);
        sqLiteDatabase = helper.getWritableDatabase();
    }

    public void insert(Vaccine vaccine){
        ContentValues contentValues = new ContentValues();
        contentValues.put(C_USER_ID, vaccine.getUserId());
        contentValues.put(C_NAME, vaccine.getName());
        contentValues.put(C_DESC, vaccine.getDesc());
        contentValues.put(C_DATE, dateFormat.format(vaccine.getDate()));
        contentValues.put(C_IMAGE, vaccine.getImage());
        long id = sqLiteDatabase.insert(TABLE, null,contentValues);
        vaccine.setId(id);
    }

    public void update(Vaccine vaccine){
        ContentValues contentValues = new ContentValues();
        contentValues.put(C_USER_ID, vaccine.getUserId());
        contentValues.put(C_NAME, vaccine.getName());
        contentValues.put(C_DESC, vaccine.getDesc());
        contentValues.put(C_DATE, vaccine.getSDate());
        contentValues.put(C_IMAGE, vaccine.getImage());
        sqLiteDatabase.update(TABLE, contentValues, "_id = ?", new String[]{Long.toString(vaccine.getId())});
    }

    public void delete(long id){
        sqLiteDatabase.delete(TABLE, C_ID+" = ?", new String[]{Long.toString(id)});
    }

    public Vaccine getVaccineById(long id){
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM vaccine WHERE _id = ?", new String[]{Long.toString(id)});
        return cursorToVaccine(cursor);
    }

    public List<Vaccine> getAllVaccines(){
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM vaccine ORDER BY date ASC", null);
        return cursorToList(cursor);
    }

    private Vaccine cursorToVaccine(Cursor cursor){
        Vaccine vaccine = null;

        if(cursor.moveToNext()){
            vaccine = new Vaccine();
            vaccine.setId(cursor.getLong(0));
            vaccine.setUserId(cursor.getString(1));
            vaccine.setName(cursor.getString(2));
            vaccine.setDesc(cursor.getString(3));
            vaccine.setDate(cursor.getString(4));
            vaccine.setImage(cursor.getString(5));
        }

        return vaccine;
    }

    private List<Vaccine> cursorToList(Cursor cursor){
        List<Vaccine> data = new ArrayList<>();

        for(int i=0; i<cursor.getCount(); i++){
            Vaccine vaccine = cursorToVaccine(cursor);
            data.add(vaccine);
        }

        return data;
    }

}
