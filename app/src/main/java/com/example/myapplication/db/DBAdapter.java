package com.example.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapplication.model.ListModel;

import java.util.ArrayList;

public class DBAdapter {

    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "sqlitetest";
    static final int DATABASE_VERSION = 2;

    static final String TBL_LOGIN_DETAIL = "tblLoginDetail";

    static final String CREATE_TABLE_LOGIN_DETAIL = "create table tblLoginDetail(_id integer primary key ," +
            "userId varchar(20),userName varchar(100),timestamp DATE DEFAULT CURRENT_TIMESTAMP);";

    static final String KEY_USER_ID = "userId";
    static final String KEY_USER_NAME = "userName";

    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_LOGIN_DETAIL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "upgrading database from version" + oldVersion + "to" + newVersion + ", which will destroy all old data");
            db.execSQL("Drop Table if exists " + TBL_LOGIN_DETAIL);
            onCreate(db);
        }
    }

    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public void addListItem(ArrayList<ListModel> listItem) {
        ContentValues values = new ContentValues();
        for (int i = 0; i < listItem.size(); i++) {

            values.put(KEY_USER_ID, listItem.get(i).userId);
            values.put(KEY_USER_NAME, listItem.get(i).userName);
            db.insert(TBL_LOGIN_DETAIL, null, values);
        }

        db.close(); // Closing database connection
    }

    public boolean insertLogs(int user_id, String username) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_USER_ID, 1);
        cv.put(KEY_USER_NAME, "USER_NAME");
        long id = db.insert(TBL_LOGIN_DETAIL, null, cv);

        return id > 0;
    }

    public boolean deleteLogs(int user_id) {
        return db.delete(TBL_LOGIN_DETAIL, KEY_USER_ID + "=" + user_id, null) > 0;
    }

    public boolean updateUsername(int user_id, String username) {
        String where = KEY_USER_ID + "='" + user_id + "'";
        ContentValues cv = new ContentValues();
        cv.put(KEY_USER_NAME, username);
        return db.update(TBL_LOGIN_DETAIL, cv, where, null) > 0;
    }

    public Cursor getUSer(String user_id) {
        String query = "select * from " + TBL_LOGIN_DETAIL + " where " + KEY_USER_ID + "='" + user_id + "'";
        return
                db.rawQuery(query, null);
    }

    public Cursor getAllUser() {
        String query = "select * from " + TBL_LOGIN_DETAIL;
        return
                db.rawQuery(query, null);
    }
    public void deleteAllData() {
        db.execSQL("delete from "+ TBL_LOGIN_DETAIL);
        db.close();
    }
}
