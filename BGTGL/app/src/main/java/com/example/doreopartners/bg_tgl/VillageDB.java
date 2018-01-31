package com.example.doreopartners.bg_tgl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Doreo Partners on 12/01/2018.
 */

public class VillageDB  extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "villages.sqlite";
    private static final int DATABASE_VERSION = 1;

    public VillageDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // you can use an alternate constructor to specify a database location
        // (such as a folder on the sd card)
        // you must ensure that this folder is available and you have permission
        // to write to it
        //super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);

    }

    public Cursor getVillages() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"0 _id", "Villages", "AMIK"};
        String sqlTables = "villages";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        c.moveToFirst();
        return c;
    }

    public Cursor getVillages(String village) {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"0 _id", "Villages", "AMIK"};
        String sqlTables = "villages";
        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, "Villages = '"+village+"'", null,
                null, null, null);

        c.moveToFirst();
        return c;
    }
}
