package com.sharad.epocket.bills;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sharad.epocket.database.ContentConstant;
import com.sharad.epocket.database.DatabaseAdapter;

import java.util.ArrayList;

/**
 * Created by Sharad on 01-Jul-16.
 */

public class DataSourceBill extends DatabaseAdapter {

    public DataSourceBill(Context context) {
        super(context);
    }

    public long insertBill(IBill bill) {
        SQLiteDatabase db = openDb();

        ContentValues content = bill.getContentValues();

        // Insert it into the database.
        long id = db.insert(DATABASE_TABLE_BILL, null, content);
        closeDb();
        return id;
    }

    public boolean updateBill(long rowId, IBill bill) {
        SQLiteDatabase db = openDb();
        String where = ContentConstant.KEY_BILL_ROWID + "=" + rowId;

        ContentValues content = bill.getContentValues();

        // Update it into the database.
        boolean status = db.update(DATABASE_TABLE_BILL, content, where, null) != 0;
        closeDb();
        return status;
    }

    public boolean deleteBill(long rowId) {
        SQLiteDatabase db = openDb();
        String where = ContentConstant.KEY_BILL_ROWID + "=" + rowId;
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

    public IBill getBill(long rowId) {
        SQLiteDatabase db = openDb();
        IBill bill = null;
        String where = ContentConstant.KEY_BILL_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE_BILL, ContentConstant.ALL_KEYS_BILL,
                where, null, null, null, null, null);
        if(c.moveToFirst()) {
            bill = new IBill(c);
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
        Cursor c = 	db.query(true, DATABASE_TABLE_BILL, ContentConstant.ALL_KEYS_BILL,
                where, null, null, null, null, null);
        if (c != null) {
            if(c.moveToFirst()) {
                do {
                    bills.add(new IBill(c));
                } while (c.moveToNext());
            }
        }
        closeDb();
    }
}
