package com.sharad.epocket.bills;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sharad.epocket.database.BillTable;
import com.sharad.epocket.database.DatabaseHelper;

import java.util.ArrayList;

/**
 * Created by Sharad on 01-Jul-16.
 */

public class DataSourceBill {
    DatabaseHelper helper;

    public DataSourceBill(Context context) {
        helper = new DatabaseHelper(context);
    }

    public long insertBill(IBill bill) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues content = bill.getContentValues();

        // Insert it into the database.
        long id = db.insert(BillTable.TABLE_BILL, null, content);
        helper.close();
        return id;
    }

    public boolean updateBill(long rowId, IBill bill) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String where = BillTable.COLUMN_ID + "=" + rowId;

        ContentValues content = bill.getContentValues();

        // Update it into the database.
        boolean status = db.update(BillTable.TABLE_BILL, content, where, null) != 0;
        helper.close();
        return status;
    }

    public boolean deleteBill(long rowId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String where = BillTable.COLUMN_ID + "=" + rowId;
        boolean status = db.delete(BillTable.TABLE_BILL, where, null) != 0;
        helper.close();
        return status;
    }

    public IBill getBill(long rowId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        IBill bill = null;
        String where = BillTable.COLUMN_ID + "=" + rowId;
        Cursor c = 	db.query(true, BillTable.TABLE_BILL, null, where, null, null, null, null, null);
        if(c.moveToFirst()) {
            bill = new IBill(c);
        }

        helper.close();
        return bill;
    }

    public void getBills(ArrayList<IBill> bills) {
        getBills(bills, null);
    }

    public void getBills(ArrayList<IBill> bills, String where) {
        SQLiteDatabase db = helper.getReadableDatabase();
        bills.clear();
        Cursor c = 	db.query(true, BillTable.TABLE_BILL, null, where, null, null, null, null, null);
        if (c != null) {
            if(c.moveToFirst()) {
                do {
                    bills.add(new IBill(c));
                } while (c.moveToNext());
            }
        }
        helper.close();
    }
}
