package com.sharad.epocket.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sharad on 15-Aug-16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "finance";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        AccountTable.onCreate(database);
        CategoryTable.onCreate(database);
        TransactionTable.onCreate(database);
        BillTable.onCreate(database);
    }

    @Override
    public void onConfigure(SQLiteDatabase database){
        database.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        AccountTable.onUpgrade(database, oldVersion, newVersion);
        CategoryTable.onUpgrade(database, oldVersion, newVersion);
        TransactionTable.onUpgrade(database, oldVersion, newVersion);
        BillTable.onUpgrade(database, oldVersion, newVersion);
    }
}
