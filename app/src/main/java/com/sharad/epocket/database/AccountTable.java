package com.sharad.epocket.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Sharad on 15-Aug-16.
 */

public class AccountTable {

    // Database table
    public static final String COLUMN_ID           = "_id";
    public static final String COLUMN_TITLE        = "title";
    public static final String COLUMN_CURRENCY     = "currency";
    public static final String COLUMN_NOTE         = "note";
    public static final String COLUMN_NUMBER       = "acc_num";
    public static final String COLUMN_LOGIN        = "login";
    public static final String COLUMN_PASSWORD     = "password";
    public static final String COLUMN_CONTACT      = "contact";
    public static final String COLUMN_LOGO         = "logo";
    public static final String COLUMN_TYPE         = "type";
    public static final String COLUMN_LIST_INDEX   = "list_index";
    public static final String COLUMN_BAL_CARD     = "bal_card";
    public static final String COLUMN_BAL_CASH     = "bal_cash";
    public static final String COLUMN_LAST_UPDATE  = "last_update";

    // Database creation SQL statement
    public static final String TABLE_ACCOUNT = "account_table";
    private static final String DATABASE_CREATE = "create table " + TABLE_ACCOUNT
            + " ("
            + COLUMN_ID           + " integer primary key autoincrement, "
            + COLUMN_TITLE        + " text not null, "
            + COLUMN_CURRENCY     + " text not null, "
            + COLUMN_NOTE         + " text not null, "
            + COLUMN_NUMBER       + " text not null, "
            + COLUMN_LOGIN        + " text not null, "
            + COLUMN_PASSWORD     + " text not null, "
            + COLUMN_CONTACT      + " text not null, "
            + COLUMN_LOGO         + " integer not null, "
            + COLUMN_TYPE         + " integer not null, "
            + COLUMN_LIST_INDEX   + " integer not null, "
            + COLUMN_BAL_CARD     + " float not null, "
            + COLUMN_BAL_CASH     + " float not null, "
            + COLUMN_LAST_UPDATE  + " integer not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(AccountTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", updating data table");

        switch (oldVersion) {
            case 1:
        }
    }
}
