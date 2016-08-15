package com.sharad.epocket.accounts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sharad.epocket.database.DatabaseHelper;
import com.sharad.epocket.database.TransactionTable;

import java.util.ArrayList;

/**
 * Created by Sharad on 24-Jul-16.
 */

public class DataSourceTransaction {
    DatabaseHelper helper;

    public DataSourceTransaction(Context context) {
        helper = new DatabaseHelper(context);
    }

    public long insertTransaction(ITransaction transaction) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues content = transaction.getContentValues();

        // Insert it into the database.
        long id = db.insert(TransactionTable.TABLE_TRANSACTION, null, content);
        helper.close();
        return id;
    }

    public boolean updateTransaction(ITransaction transaction) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String where = TransactionTable.COLUMN_ID + "=" + transaction.getId();

        // Create row's data:
        ContentValues content = transaction.getContentValues();

        // Update it into the database.
        boolean status = db.update(TransactionTable.TABLE_TRANSACTION, content, where, null) != 0;
        helper.close();
        return status;
    }

    public boolean deleteTransaction(ITransaction iTransaction) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String where = TransactionTable.COLUMN_ID + "=" + iTransaction.getId();
        boolean status = db.delete(TransactionTable.TABLE_TRANSACTION, where, null) != 0;
        helper.close();
        return status;
    }

    public ITransaction getTransaction(long rowId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        ITransaction transaction = null;
        String where = TransactionTable.COLUMN_ID + "=" + rowId;
        Cursor c = 	db.query(true, TransactionTable.TABLE_TRANSACTION, null, where, null, null, null, null, null);
        if(c.moveToFirst()) {
            transaction = new ITransaction(c);
        }

        helper.close();
        return transaction;
    }

    public void getTransactions(ArrayList<ITransaction> transactions) {
        getTransactions(transactions, null);
    }

    public void getTransactions(ArrayList<ITransaction> transactions, String where) {
        SQLiteDatabase db = helper.getReadableDatabase();
        transactions.clear();
        Cursor c = 	db.query(true, TransactionTable.TABLE_TRANSACTION, null, where, null, null, null, null, null);
        if (c != null) {
            if(c.moveToFirst()) {
                do {
                    transactions.add(new ITransaction(c));
                } while (c.moveToNext());
            }
        }
        helper.close();
    }

    public boolean deleteTransactions(String where) {
        if(where != "") {
            SQLiteDatabase db = helper.getWritableDatabase();
            boolean status = db.delete(TransactionTable.TABLE_TRANSACTION, where, null) != 0;
            helper.close();
            return status;
        }
        return false;
    }
}
