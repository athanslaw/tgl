package com.example.doreopartners.bg_tgl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.content.ContentValues;
import java.util.HashMap;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class TGLDB  extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "TGLs.sqlite";
    private static final int DATABASE_VERSION = 1;

    public TGLDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor getTGLs() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"0 _ID", "IKNumber", "IKName", "status", "date", "Age", "Sex", "Phone", "LGA", "District", "Village"};
        String sqlTables = "tgl";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        c.moveToFirst();
        return c;
    }

    public Cursor getTGLs(String ikNumber) {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"0 _ID", "IKNumber", "IKName", "status", "date", "Age", "Sex", "Phone", "LGA", "District", "Village"};
        String sqlTables = "tgl";
        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, "IKNumber = '"+ikNumber+"'", null,
                null, null, null);

        c.moveToFirst();
        return c;
    }

    public Cursor getTGLsDateDesc() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"0 _ID", "IKNumber", "IKName", "status", "date", "Age", "Sex", "Phone", "LGA", "District", "Village"};
        String sqlTables = "tgl";
        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, "date DESC");

        c.moveToFirst();
        return c;
    }

    public void insertRecord(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IKNumber", queryValues.get("IKNumber"));
        values.put("IKName", queryValues.get("IKName"));
        values.put("status", queryValues.get("status"));
        values.put("date", queryValues.get("date"));

        values.put("Age", "");
        values.put("Sex", "");
        values.put("Phone", "");
        values.put("LGA", "");
        values.put("District", "");
        values.put("Village", "");


        database.insert("tgl", null, values);
        database.close();
    }

}
