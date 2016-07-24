package com.sharad.epocket.accounts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sharad.epocket.database.DatabaseAdapter;

import java.util.ArrayList;

/**
 * Created by Sharad on 24-Jul-16.
 */

public class DataSourceTransaction extends DatabaseAdapter {
    public DataSourceTransaction(Context context) {
        super(context);
    }

    private ContentValues getContentValues(ITransaction transaction) {
        // Create row's data:
        ContentValues content = new ContentValues();
        content.put(KEY_TRANSACTION_DATE,           transaction.getDate());
        content.put(KEY_TRANSACTION_COMMENT,        transaction.getComment());
        content.put(KEY_TRANSACTION_REPEAT,         transaction.getRepeat());
        content.put(KEY_TRANSACTION_TYPE,           transaction.getType());
        content.put(KEY_TRANSACTION_SUB_TYPE,       transaction.getSubType());
        content.put(KEY_TRANSACTION_ACCOUNT,        transaction.getAccount());
        content.put(KEY_TRANSACTION_CATEGORY,       transaction.getCategory());
        content.put(KEY_TRANSACTION_AMOUNT,         transaction.getAmount());

        return content;
    }

    public long insertTransaction(ITransaction transaction) {
        SQLiteDatabase db = openDb();

        ContentValues content = getContentValues(transaction);

        // Insert it into the database.
        long id = db.insert(DATABASE_TABLE_TRANSACTION, null, content);
        closeDb();
        return id;
    }

    public boolean updateTransaction(long rowId, ITransaction transaction) {
        SQLiteDatabase db = openDb();
        String where = KEY_TRANSACTION_ROWID + "=" + rowId;

        // Create row's data:
        ContentValues content = getContentValues(transaction);

        // Update it into the database.
        boolean status = db.update(DATABASE_TABLE_TRANSACTION, content, where, null) != 0;
        closeDb();
        return status;
    }

    public boolean deleteTransaction(long rowId) {
        SQLiteDatabase db = openDb();
        String where = KEY_TRANSACTION_ROWID + "=" + rowId;
        boolean status = db.delete(DATABASE_TABLE_TRANSACTION, where, null) != 0;
        closeDb();
        return status;
    }

    public void deleteAllTransactions() {
        SQLiteDatabase db = openDb();
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_TRANSACTION);
        db.execSQL(DATABASE_CREATE_SQL_TRANSACTION);
        closeDb();
    }

    private ITransaction parseTransaction(Cursor c) {
        long id 	    = c.getLong(c.getColumnIndex(KEY_TRANSACTION_ROWID));
        long date       = c.getLong(c.getColumnIndex(KEY_TRANSACTION_DATE));
        String comment  = c.getString(c.getColumnIndex(KEY_TRANSACTION_COMMENT));
        String repeat   = c.getString(c.getColumnIndex(KEY_TRANSACTION_REPEAT));
        int type        = c.getInt(c.getColumnIndex(KEY_TRANSACTION_TYPE));
        int subType     = c.getInt(c.getColumnIndex(KEY_TRANSACTION_SUB_TYPE));
        long account    = c.getLong(c.getColumnIndex(KEY_TRANSACTION_ACCOUNT));
        long category   = c.getLong(c.getColumnIndex(KEY_TRANSACTION_CATEGORY));
        float amount    = c.getFloat(c.getColumnIndex(KEY_TRANSACTION_AMOUNT));

        ITransaction transaction = new ITransaction(id, date, comment, repeat,
                type, subType, account, category, amount);
        return transaction;
    }

    public ITransaction getTransaction(long rowId) {
        SQLiteDatabase db = openDb();
        ITransaction transaction = null;
        String where = KEY_TRANSACTION_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE_TRANSACTION, ALL_KEYS_TRANSACTION,
                where, null, null, null, null, null);
        if(c.moveToFirst()) {
            transaction = parseTransaction(c);
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
        Cursor c = 	db.query(true, DATABASE_TABLE_TRANSACTION, ALL_KEYS_TRANSACTION,
                where, null, null, null, null, null);
        if (c != null) {
            if(c.moveToFirst()) {
                do {
                    transactions.add(parseTransaction(c));
                } while (c.moveToNext());
            }
        }
        closeDb();
    }
}
