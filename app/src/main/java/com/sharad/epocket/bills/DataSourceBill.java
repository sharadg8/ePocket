package com.sharad.epocket.bills;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sharad.epocket.database.DatabaseAdapter;

import java.util.ArrayList;

/**
 * Created by Sharad on 01-Jul-16.
 */

public class DataSourceBill extends DatabaseAdapter {

    public DataSourceBill(Context context) {
        super(context);
    }

    private ContentValues getContentValues(IBill bill) {
        // Create row's data:
        ContentValues content = new ContentValues();
        content.put(KEY_BILL_TITLE     , bill.getTitle());
        content.put(KEY_BILL_ACCOUNT   , bill.getAccount());
        content.put(KEY_BILL_CURRENCY  , bill.getCurrency());
        content.put(KEY_BILL_AMOUNT    , bill.getAmount());
        content.put(KEY_BILL_DATE      , bill.getStartDateLong());
        content.put(KEY_BILL_END_DATE  , bill.getEndDateLong());
        content.put(KEY_BILL_REPEAT    , bill.getRepeat());

        return content;
    }

    public long insertBill(IBill bill) {
        SQLiteDatabase db = openDb();

        ContentValues content = getContentValues(bill);

        // Insert it into the database.
        long id = db.insert(DATABASE_TABLE_BILL, null, content);
        closeDb();
        return id;
    }

    public boolean updateBill(long rowId, IBill bill) {
        SQLiteDatabase db = openDb();
        String where = KEY_BILL_ROWID + "=" + rowId;

        ContentValues content = getContentValues(bill);

        // Update it into the database.
        boolean status = db.update(DATABASE_TABLE_BILL, content, where, null) != 0;
        closeDb();
        return status;
    }

    public boolean deleteBill(long rowId) {
        SQLiteDatabase db = openDb();
        String where = KEY_BILL_ROWID + "=" + rowId;
        boolean status = db.delete(DATABASE_TABLE_BILL, where, null) != 0;
        closeDb();
        return status;
    }

    public void deleteAllBills() {
        SQLiteDatabase db = openDb();
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_BILL);
        db.execSQL(DATABASE_CREATE_SQL_BILL);
        closeDb();
    }

    private IBill parseBill(Cursor c) {
        long id 		= c.getLong(c.getColumnIndex(KEY_BILL_ROWID));
        String title 	= c.getString(c.getColumnIndex(KEY_BILL_TITLE));
        String account  = c.getString(c.getColumnIndex(KEY_BILL_ACCOUNT));
        String currency	= c.getString(c.getColumnIndex(KEY_BILL_CURRENCY));
        float amount	= c.getFloat(c.getColumnIndex(KEY_BILL_AMOUNT));
        long stDate		= c.getLong(c.getColumnIndex(KEY_BILL_DATE));
        long endDate	= c.getLong(c.getColumnIndex(KEY_BILL_END_DATE));
        int repeat		= c.getInt(c.getColumnIndex(KEY_BILL_REPEAT));
        IBill bill = new IBill(id, title, account, currency, amount, stDate, endDate, repeat);
        return bill;
    }

    public IBill getBill(long rowId) {
        SQLiteDatabase db = openDb();
        IBill bill = null;
        String where = KEY_BILL_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE_BILL, ALL_KEYS_BILL,
                where, null, null, null, null, null);
        if(c.moveToFirst()) {
            bill = parseBill(c);
        }

        closeDb();
        return bill;
    }

    public void getBills(ArrayList<IBill> bills) {
        getBills(bills, null);
    }

    public void getBills(ArrayList<IBill> bills, String where) {
        SQLiteDatabase db = openDb();
        bills.clear();
        Cursor c = 	db.query(true, DATABASE_TABLE_BILL, ALL_KEYS_BILL,
                where, null, null, null, null, null);
        if (c != null) {
            if(c.moveToFirst()) {
                do {
                    bills.add(parseBill(c));
                } while (c.moveToNext());
            }
        }
        closeDb();
    }
}
