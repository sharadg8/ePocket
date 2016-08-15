package com.sharad.epocket.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Sharad on 15-Aug-16.
 */

public class CategoryTable {

    // Database table
    public static final String COLUMN_ID         = "_id";
    public static final String COLUMN_TITLE      = "title";
    public static final String COLUMN_COUNT      = "count";
    public static final String COLUMN_IMAGE_IDX  = "image_idx";
    public static final String COLUMN_COLOR      = "color";
    public static final String COLUMN_TYPE       = "type";

    // Database creation SQL statement
    public static final String TABLE_CATEGORY = "category_table";
    private static final String DATABASE_CREATE = "create table " + TABLE_CATEGORY
            + " ("
            + COLUMN_ID        + " integer primary key autoincrement, "
            + COLUMN_TITLE     + " text not null, "
            + COLUMN_COUNT     + " integer not null, "
            + COLUMN_IMAGE_IDX + " integer not null, "
            + COLUMN_COLOR     + " integer not null, "
            + COLUMN_TYPE      + " integer not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(CategoryTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", updating data table");

        switch (oldVersion) {
            case 1:
        }
    }
}
