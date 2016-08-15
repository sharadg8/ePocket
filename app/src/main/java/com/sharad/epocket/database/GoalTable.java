package com.sharad.epocket.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Sharad on 15-Aug-16.
 */

public class GoalTable {

    // Database table
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_TARGET_DATE = "end_date";
    public static final String COLUMN_AMOUNT = "amount";

    // Database creation SQL statement
    public static final String TABLE_GOAL = "goal_table";
    private static final String DATABASE_CREATE = "create table " + TABLE_GOAL
            + " ("
            + COLUMN_ID             + " integer primary key autoincrement, "
            + COLUMN_START_DATE     + " integer not null, "
            + COLUMN_TARGET_DATE    + " integer not null, "
            + COLUMN_AMOUNT         + " flaot not null, "
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(GoalTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", updating data table");
        switch (oldVersion) {
            case 1:
                database.execSQL("DROP TABLE IF EXISTS " + TABLE_GOAL);
                onCreate(database);
        }
    }
}
