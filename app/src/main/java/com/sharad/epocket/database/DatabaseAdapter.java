package com.sharad.epocket.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Sharad on 15-Jun-14.
 */
public class DatabaseAdapter {
    // For logging
    private static final String TAG = "DBAdapter";

    public static final String DATABASE_NAME = "finance";

    public static final int DATABASE_VERSION = 1;

    private final Context mContext;

    private DatabaseHelper mDbHelper;

    public DatabaseAdapter(Context ctx) {
        this.mContext = ctx.getApplicationContext();
    }

    // Open the database connection.
    protected SQLiteDatabase openDb() {
        if (mDbHelper == null) {
            mDbHelper = new DatabaseHelper(mContext);
        }
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Enable foreign key constraints
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys = ON;");
        }

        return db;
    }

    // Close the database connection.
    protected void closeDb() {
        mDbHelper.close();
    }

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL_ACCOUNT);
            _db.execSQL(DATABASE_CREATE_SQL_BILL);
            _db.execSQL(DATABASE_CREATE_SQL_CATEGORY);
            _db.execSQL(DATABASE_CREATE_SQL_TRANSACTION);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            /*
            switch (oldVersion) {
                case 1:
                case 2:
                    _db.execSQL(DATABASE_CREATE_SQL_CATEGORY);
                case 3:
                    _db.execSQL(DATABASE_CREATE_SQL_TRANSACTION);
            }
            */
        }
    }

    /**
     * Data table for bills
     */
    public static final String DATABASE_TABLE_BILL = "bill_table";

    protected static final String DATABASE_CREATE_SQL_BILL = "create table " + DATABASE_TABLE_BILL
            + " ("
            + ContentConstant.KEY_BILL_ROWID     + " integer primary key autoincrement, "
            + ContentConstant.KEY_BILL_TITLE     + " text not null, "
            + ContentConstant.KEY_BILL_ACCOUNT   + " text not null, "
            + ContentConstant.KEY_BILL_CURRENCY  + " text not null, "
            + ContentConstant.KEY_BILL_AMOUNT    + " float not null, "
			+ ContentConstant.KEY_BILL_DATE      + " integer not null, "
            + ContentConstant.KEY_BILL_END_DATE  + " integer not null, "
            + ContentConstant.KEY_BILL_REPEAT    + " integer not null"
            + ");";

    /**
     * Data table for accounts
     */
    public static final String DATABASE_TABLE_ACCOUNT = "account_table";

    protected static final String DATABASE_CREATE_SQL_ACCOUNT = "create table " + DATABASE_TABLE_ACCOUNT
            + " ("
            + ContentConstant.KEY_ACCOUNT_ROWID        + " integer primary key autoincrement, "
            + ContentConstant.KEY_ACCOUNT_TITLE        + " text not null, "
            + ContentConstant.KEY_ACCOUNT_CURRENCY     + " text not null, "
            + ContentConstant.KEY_ACCOUNT_NOTE         + " text not null, "
            + ContentConstant.KEY_ACCOUNT_NUMBER       + " text not null, "
            + ContentConstant.KEY_ACCOUNT_LOGIN        + " text not null, "
            + ContentConstant.KEY_ACCOUNT_PASSWORD     + " text not null, "
            + ContentConstant.KEY_ACCOUNT_CONTACT      + " text not null, "
            + ContentConstant.KEY_ACCOUNT_LOGO         + " integer not null, "
            + ContentConstant.KEY_ACCOUNT_TYPE         + " integer not null, "
            + ContentConstant.KEY_ACCOUNT_LIST_INDEX   + " integer not null, "
            + ContentConstant.KEY_ACCOUNT_BAL_CARD     + " float not null, "
            + ContentConstant.KEY_ACCOUNT_BAL_CASH     + " float not null, "
            + ContentConstant.KEY_ACCOUNT_LAST_UPDATE  + " integer not null"
            + ");";

    /**
     * Data table for categories
     */
    public static final String DATABASE_TABLE_CATEGORY = "category_table";

    protected static final String DATABASE_CREATE_SQL_CATEGORY = "create table " + DATABASE_TABLE_CATEGORY
            + " ("
            + ContentConstant.KEY_CATEGORY_ROWID     + " integer primary key autoincrement, "
            + ContentConstant.KEY_CATEGORY_TITLE     + " text not null, "
            + ContentConstant.KEY_CATEGORY_COUNT     + " integer not null, "
            + ContentConstant.KEY_CATEGORY_IMAGE_IDX + " integer not null, "
            + ContentConstant.KEY_CATEGORY_COLOR     + " integer not null, "
            + ContentConstant.KEY_CATEGORY_TYPE      + " integer not null"
            + ");";

    /**
     * Data table for transactions
     */
    public static final String DATABASE_TABLE_TRANSACTION = "transaction_table";

    protected static final String DATABASE_CREATE_SQL_TRANSACTION = "create table " + DATABASE_TABLE_TRANSACTION
            + " ("
            + ContentConstant.KEY_TRANSACTION_ROWID     + " integer primary key autoincrement, "
            + ContentConstant.KEY_TRANSACTION_DATE      + " integer not null, "
            + ContentConstant.KEY_TRANSACTION_COMMENT   + " text not null, "
            + ContentConstant.KEY_TRANSACTION_REPEAT    + " text not null, "
            + ContentConstant.KEY_TRANSACTION_TYPE      + " integer not null, "
            + ContentConstant.KEY_TRANSACTION_SUB_TYPE  + " integer not null, "
            + ContentConstant.KEY_TRANSACTION_ACCOUNT   + " integer not null, "
            + ContentConstant.KEY_TRANSACTION_CATEGORY  + " integer not null, "
            + ContentConstant.KEY_TRANSACTION_AMOUNT    + " float not null, "

            + "FOREIGN KEY (" + ContentConstant.KEY_TRANSACTION_ACCOUNT + ") REFERENCES " + DATABASE_TABLE_ACCOUNT + " (" + ContentConstant.KEY_ACCOUNT_ROWID + "), "
            + "FOREIGN KEY (" + ContentConstant.KEY_TRANSACTION_CATEGORY + ") REFERENCES " + DATABASE_TABLE_CATEGORY + " (" + ContentConstant.KEY_CATEGORY_ROWID + ")"
            + ");";
}
