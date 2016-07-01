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

    public static final int DATABASE_VERSION = 1;

    // DB Item FD table fields
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

    public static final String DATABASE_NAME = "finance";
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

    /////////////////////////////////////////////////////////////////////
    //	Private Helper Classes:
    /////////////////////////////////////////////////////////////////////

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
            _db.execSQL(DATABASE_CREATE_SQL_BILL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_BILL);

           // Recreate new database:
            onCreate(_db);
        }
    }
}
