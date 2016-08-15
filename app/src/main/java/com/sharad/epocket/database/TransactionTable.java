package com.sharad.epocket.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Sharad on 15-Aug-16.
 */

public class TransactionTable {

    // Database table
    public static final String COLUMN_ID        = "_id";
    public static final String COLUMN_DATE         = "date";
    public static final String COLUMN_COMMENT      = "comment";
    public static final String COLUMN_REPEAT       = "repeat";
    public static final String COLUMN_TYPE         = "type";
    public static final String COLUMN_SUB_TYPE     = "sub_type";
    public static final String COLUMN_ACCOUNT      = "account";
    public static final String COLUMN_CATEGORY     = "category";
    public static final String COLUMN_AMOUNT       = "amount";

    // Database creation SQL statement
    public static final String TABLE_TRANSACTION = "transaction_table";
    protected static final String DATABASE_CREATE = "create table " + TABLE_TRANSACTION
            + " ("
            + COLUMN_ID        + " integer primary key autoincrement, "
            + COLUMN_DATE      + " integer not null, "
            + COLUMN_COMMENT   + " text not null, "
            + COLUMN_REPEAT    + " text not null, "
            + COLUMN_TYPE      + " integer not null, "
            + COLUMN_SUB_TYPE  + " integer not null, "
            + COLUMN_ACCOUNT   + " integer not null, "
            + COLUMN_CATEGORY  + " integer not null, "
            + COLUMN_AMOUNT    + " float not null, "

            + "FOREIGN KEY (" + COLUMN_ACCOUNT + ") REFERENCES " + AccountTable.TABLE_ACCOUNT + " (" + AccountTable.COLUMN_ID + ")"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(TransactionTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", updating data table");

        switch (oldVersion) {
            case 1:
        }
    }
}
