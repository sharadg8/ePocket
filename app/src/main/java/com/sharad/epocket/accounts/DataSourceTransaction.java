package com.sharad.epocket.accounts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sharad.epocket.database.ContentConstant;
import com.sharad.epocket.database.DatabaseAdapter;

import java.util.ArrayList;

/**
 * Created by Sharad on 24-Jul-16.
 */

public class DataSourceTransaction extends DatabaseAdapter {
    public DataSourceTransaction(Context context) {
        super(context);
    }

    public long insertTransaction(ITransaction transaction) {
        SQLiteDatabase db = openDb();

        ContentValues content = transaction.getContentValues();

        // Insert it into the database.
        long id = db.insert(DATABASE_TABLE_TRANSACTION, null, content);
        closeDb();
        return id;
    }

    public boolean updateTransaction(ITransaction transaction) {
        SQLiteDatabase db = openDb();
        String where = ContentConstant.KEY_TRANSACTION_ROWID + "=" + transaction.getId();

        // Create row's data:
        ContentValues content = transaction.getContentValues();

        // Update it into the database.
        boolean status = db.update(DATABASE_TABLE_TRANSACTION, content, where, null) != 0;
        closeDb();
        return status;
    }

    public boolean deleteTransaction(ITransaction iTransaction) {
        SQLiteDatabase db = openDb();
        String where = ContentConstant.KEY_TRANSACTION_ROWID + "=" + iTransaction.getId();
        boolean status = db.delete(DATABASE_TABLE_TRANSACTION, where, null) != 0;
        closeDb();
        return status;
    }

    public ITransaction getTransaction(long rowId) {
        SQLiteDatabase db = openDb();
        ITransaction transaction = null;
        String where = ContentConstant.KEY_TRANSACTION_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE_TRANSACTION, ContentConstant.ALL_KEYS_TRANSACTION,
                where, null, null, null, null, null);
        if(c.moveToFirst()) {
            transaction = new ITransaction(c);
        }

        closeDb();
        return transaction;
    }

    public void getTransactions(ArrayList<ITransaction> transactions) {
        getTransactions(transactions, null);
    }

    public void getTransactions(ArrayList<ITransaction> transactions, String where) {
        SQLiteDatabase db = openDb();
        transactions.clear();
        Cursor c = 	db.query(true, DATABASE_TABLE_TRANSACTION, ContentConstant.ALL_KEYS_TRANSACTION,
                where, null, null, null, null, null);
        if (c != null) {
            if(c.moveToFirst()) {
                do {
                    transactions.add(new ITransaction(c));
                } while (c.moveToNext());
            }
        }
        closeDb();
    }

    public boolean deleteTransactions(String where) {
        if(where != "") {
            SQLiteDatabase db = openDb();
            boolean status = db.delete(DATABASE_TABLE_TRANSACTION, where, null) != 0;
            closeDb();
            return status;
        }
        return false;
    }
}
