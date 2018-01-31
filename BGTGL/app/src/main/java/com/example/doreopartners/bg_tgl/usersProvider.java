package com.example.doreopartners.bg_tgl;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class usersProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.example.provider.bg_tgl";
    static final String URL = "content://" + PROVIDER_NAME + "/tgl";
    static final Uri CONTENT_URI = Uri.parse(URL);
    static final String _ID = "_id";
    static final String ikName = "tgl_name";
    static final String ikNumber = "ik_number";

    static final String qus1 = "qus1";
    static final String qus2 = "qus2";
    static final String qus3 = "qus3";
    static final String qus4 = "qus4";
    static final String qus5 = "qus5";
    static final String qus6 = "qus6";
    static final String qus7 = "qus7";
    static final String qus8 = "qus8";
    static final String qus9 = "qus9";
    static final String qus10 = "qus10";
    static final String qus11 = "qus11";
    static final String qus12 = "qus12";
    static final String score = "score";
    static final String status = "status";
    static final String interviewer = "interviewer";
    static final String date = "date";

    static final String village = "village";
    static final String amik = "amik";
    static final String tfm_date = "tfm_date";
    static final String tfm_time = "tfm_time";

    private static HashMap<String, String> USERS_PROJECTION_MAP;

    static final int USER = 1;
    static final int USER_ID = 2;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "tgl", USER);
        uriMatcher.addURI(PROVIDER_NAME, "tgl/#", USER_ID);
    }

    /**
     * Database specific constant declarations
     */
    private SQLiteDatabase db;
    static final String DATABASE_NAME = "bg_tgl";
    static final String TABLE_NAME = "tgl";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " ik_number TEXT NOT NULL, " +
                    " tgl_name TEXT NOT NULL, " +
                    " qus1 TEXT, " +
                    " qus2 TEXT, " +
                    " qus3 TEXT, " +
                    " qus4 TEXT, " +
                    " qus5 TEXT, " +
                    " qus6 TEXT, " +
                    " qus7 TEXT, " +
                    " qus8 TEXT, " +
                    " qus9 TEXT, " +
                    " qus10 TEXT, " +
                    " qus11 TEXT, " +
                    " qus12 TEXT, " +
                    " score TEXT, " +
                    " interviewer TEXT, " +
                    " date TEXT, " +
                    " status TEXT, " +
                    " village TEXT, " +
                    " amik TEXT, " +
                    " tfm_date TEXT, " +
                    " tfm_time TEXT);";

    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */
        db = dbHelper.getWritableDatabase();
        return (db == null) ? false : true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /**
         * Add a new student record
         */
        long rowID = db.insert(TABLE_NAME, "", values);
        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case USER:
                qb.setProjectionMap(USERS_PROJECTION_MAP);
                break;
            case USER_ID:
                qb.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder == "") {
            /**
             * By default sort on student names
             */
            sortOrder = ikNumber;
        }
        Cursor c = qb.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case USER:
                count = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case USER_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(TABLE_NAME, _ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case USER:
                count = db.update(TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case USER_ID:
                count = db.update(TABLE_NAME, values, _ID +
                        " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            /**
             * Get all student records
             */
            case USER:
                return "vnd.android.cursor.dir/vnd.example.users";
            /**
             * Get a particular student
             */
            case USER_ID:
                return "vnd.android.cursor.item/vnd.example.users";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    public void close() {
        System.exit(0);
    }

}

