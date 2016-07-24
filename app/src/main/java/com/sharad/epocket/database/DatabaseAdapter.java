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

    public static final int DATABASE_VERSION = 4;

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
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            switch (oldVersion) {
                case 1:
                case 2:
                    _db.execSQL(DATABASE_CREATE_SQL_CATEGORY);
                case 3:
                    _db.execSQL(DATABASE_CREATE_SQL_TRANSACTION);
            }
        }
    }

    /**
     * Data table for bills
     */
    public static final String KEY_BILL_ROWID      = "_id";
    public static final String KEY_BILL_TITLE      = "title";
    public static final String KEY_BILL_ACCOUNT    = "account";
    public static final String KEY_BILL_CURRENCY   = "currency";
    public static final String KEY_BILL_AMOUNT     = "amount";
	public static final String KEY_BILL_DATE       = "date";
    public static final String KEY_BILL_END_DATE   = "end_date";
    public static final String KEY_BILL_REPEAT     = "repeat";

    public static final String[] ALL_KEYS_BILL = new String[] {KEY_BILL_ROWID, KEY_BILL_TITLE,
            KEY_BILL_ACCOUNT, KEY_BILL_CURRENCY, KEY_BILL_AMOUNT, KEY_BILL_DATE, KEY_BILL_END_DATE,
            KEY_BILL_REPEAT};

    public static final String DATABASE_TABLE_BILL = "bill_table";

    protected static final String DATABASE_CREATE_SQL_BILL = "create table " + DATABASE_TABLE_BILL
            + " ("
            + KEY_BILL_ROWID     + " integer primary key autoincrement, "
            + KEY_BILL_TITLE     + " text not null, "
            + KEY_BILL_ACCOUNT   + " text not null, "
            + KEY_BILL_CURRENCY  + " text not null, "
            + KEY_BILL_AMOUNT    + " float not null, "
			+ KEY_BILL_DATE      + " integer not null, "
            + KEY_BILL_END_DATE  + " integer not null, "
            + KEY_BILL_REPEAT    + " integer not null"
            + ");";

    /**
     * Data table for accounts
     */
    public static final String KEY_ACCOUNT_ROWID      = "_id";
    public static final String KEY_ACCOUNT_TITLE      = "title";
    public static final String KEY_ACCOUNT_CURRENCY   = "currency";
    public static final String KEY_ACCOUNT_NOTE       = "note";
    public static final String KEY_ACCOUNT_NUMBER     = "acc_num";
    public static final String KEY_ACCOUNT_LOGIN      = "login";
    public static final String KEY_ACCOUNT_PASSWORD   = "password";
    public static final String KEY_ACCOUNT_CONTACT    = "contact";
    public static final String KEY_ACCOUNT_BAL_CARD   = "bal_card";
    public static final String KEY_ACCOUNT_BAL_CASH   = "bal_cash";
    public static final String KEY_ACCOUNT_INFLOW     = "inflow";
    public static final String KEY_ACCOUNT_OUTFLOW    = "outflow";
    public static final String KEY_ACCOUNT_TYPE       = "type";
    public static final String KEY_ACCOUNT_LAST_UPDATE= "last_update";

    public static final String[] ALL_KEYS_ACCOUNT = new String[] {KEY_ACCOUNT_ROWID,
            KEY_ACCOUNT_TITLE, KEY_ACCOUNT_CURRENCY, KEY_ACCOUNT_NOTE, KEY_ACCOUNT_NUMBER,
            KEY_ACCOUNT_LOGIN, KEY_ACCOUNT_PASSWORD, KEY_ACCOUNT_CONTACT, KEY_ACCOUNT_BAL_CARD,
            KEY_ACCOUNT_BAL_CASH, KEY_ACCOUNT_INFLOW, KEY_ACCOUNT_OUTFLOW, KEY_ACCOUNT_TYPE,
            KEY_ACCOUNT_LAST_UPDATE };

    public static final String DATABASE_TABLE_ACCOUNT = "account_table";

    protected static final String DATABASE_CREATE_SQL_ACCOUNT = "create table " + DATABASE_TABLE_ACCOUNT
            + " ("
            + KEY_ACCOUNT_ROWID       + " integer primary key autoincrement, "
            + KEY_ACCOUNT_TITLE       + " text not null, "
            + KEY_ACCOUNT_CURRENCY    + " text not null, "
            + KEY_ACCOUNT_NOTE        + " text not null, "
            + KEY_ACCOUNT_NUMBER      + " text not null, "
            + KEY_ACCOUNT_LOGIN       + " text not null, "
            + KEY_ACCOUNT_PASSWORD    + " text not null, "
            + KEY_ACCOUNT_CONTACT     + " text not null, "
            + KEY_ACCOUNT_BAL_CARD    + " float not null, "
            + KEY_ACCOUNT_BAL_CASH    + " float not null, "
            + KEY_ACCOUNT_INFLOW      + " float not null, "
            + KEY_ACCOUNT_OUTFLOW     + " float not null, "
            + KEY_ACCOUNT_TYPE        + " integer not null, "
            + KEY_ACCOUNT_LAST_UPDATE + " integer not null"
            + ");";

    /**
     * Data table for categories
     */
    public static final String KEY_CATEGORY_ROWID      = "_id";
    public static final String KEY_CATEGORY_TITLE      = "title";
    public static final String KEY_CATEGORY_COUNT      = "count";
    public static final String KEY_CATEGORY_IMAGE_IDX  = "image_idx";
    public static final String KEY_CATEGORY_COLOR      = "color";
    public static final String KEY_CATEGORY_TYPE       = "type";

    public static final String[] ALL_KEYS_CATEGORY = new String[] {KEY_CATEGORY_ROWID, KEY_CATEGORY_TITLE,
            KEY_CATEGORY_COUNT, KEY_CATEGORY_IMAGE_IDX, KEY_CATEGORY_COLOR, KEY_CATEGORY_TYPE };

    public static final String DATABASE_TABLE_CATEGORY = "category_table";

    protected static final String DATABASE_CREATE_SQL_CATEGORY = "create table " + DATABASE_TABLE_CATEGORY
            + " ("
            + KEY_CATEGORY_ROWID     + " integer primary key autoincrement, "
            + KEY_CATEGORY_TITLE     + " text not null, "
            + KEY_CATEGORY_COUNT     + " integer not null, "
            + KEY_CATEGORY_IMAGE_IDX + " integer not null, "
            + KEY_CATEGORY_COLOR     + " integer not null, "
            + KEY_CATEGORY_TYPE      + " integer not null"
            + ");";

    /**
     * Data table for transactions
     */
    public static final String KEY_TRANSACTION_ROWID        = "_id";
    public static final String KEY_TRANSACTION_DATE         = "date";
    public static final String KEY_TRANSACTION_COMMENT      = "comment";
    public static final String KEY_TRANSACTION_REPEAT       = "repeat";
    public static final String KEY_TRANSACTION_TYPE         = "type";
    public static final String KEY_TRANSACTION_SUB_TYPE     = "sub_type";
    public static final String KEY_TRANSACTION_ACCOUNT      = "account";
    public static final String KEY_TRANSACTION_CATEGORY     = "category";
    public static final String KEY_TRANSACTION_AMOUNT       = "amount";

    public static final String[] ALL_KEYS_TRANSACTION = new String[] {KEY_TRANSACTION_ROWID,
            KEY_TRANSACTION_DATE, KEY_TRANSACTION_COMMENT, KEY_TRANSACTION_REPEAT,
            KEY_TRANSACTION_TYPE, KEY_TRANSACTION_SUB_TYPE, KEY_TRANSACTION_ACCOUNT,
            KEY_TRANSACTION_CATEGORY, KEY_TRANSACTION_AMOUNT  };

    public static final String DATABASE_TABLE_TRANSACTION = "transaction_table";

    protected static final String DATABASE_CREATE_SQL_TRANSACTION = "create table " + DATABASE_TABLE_TRANSACTION
            + " ("
            + KEY_TRANSACTION_ROWID     + " integer primary key autoincrement, "
            + KEY_TRANSACTION_DATE      + " integer not null, "
            + KEY_TRANSACTION_COMMENT   + " text not null, "
            + KEY_TRANSACTION_REPEAT    + " text not null, "
            + KEY_TRANSACTION_TYPE      + " integer not null, "
            + KEY_TRANSACTION_SUB_TYPE  + " integer not null, "
            + KEY_TRANSACTION_ACCOUNT   + " integer not null, "
            + KEY_TRANSACTION_CATEGORY  + " integer not null, "
            + KEY_TRANSACTION_AMOUNT    + " float not null, "

            + "FOREIGN KEY (" + KEY_TRANSACTION_ACCOUNT + ") REFERENCES " + DATABASE_TABLE_ACCOUNT + " (" + KEY_ACCOUNT_ROWID + "), "
            + "FOREIGN KEY (" + KEY_TRANSACTION_CATEGORY + ") REFERENCES " + DATABASE_TABLE_CATEGORY + " (" + KEY_CATEGORY_ROWID + ")"
            + ");";
}
