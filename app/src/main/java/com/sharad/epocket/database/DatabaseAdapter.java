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

    public static final int DATABASE_VERSION = 2;

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
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ACCOUNT);
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_BILL);

            // Recreate new database:
            onCreate(_db);
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
}
