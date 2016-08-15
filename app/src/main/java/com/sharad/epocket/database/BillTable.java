package com.sharad.epocket.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Sharad on 15-Aug-16.
 */

public class BillTable {

    // Database table
    public static final String COLUMN_ID         = "_id";
    public static final String COLUMN_TITLE      = "title";
    public static final String COLUMN_ACCOUNT    = "account";
    public static final String COLUMN_CURRENCY   = "currency";
    public static final String COLUMN_AMOUNT     = "amount";
    public static final String COLUMN_DATE       = "date";
    public static final String COLUMN_END_DATE   = "end_date";
    public static final String COLUMN_REPEAT     = "repeat";

    // Database creation SQL statement
    public static final String TABLE_BILL       = "bill_table";
    private static final String DATABASE_CREATE  = "create table " + TABLE_BILL
            + " ("
            + COLUMN_ID        + " integer primary key autoincrement, "
            + COLUMN_TITLE     + " text not null, "
            + COLUMN_ACCOUNT   + " text not null, "
            + COLUMN_CURRENCY  + " text not null, "
            + COLUMN_AMOUNT    + " float not null, "
            + COLUMN_DATE      + " integer not null, "
            + COLUMN_END_DATE  + " integer not null, "
            + COLUMN_REPEAT    + " integer not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(BillTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", updating data table");

        switch (oldVersion) {
            case 1:
        }
    }
}
