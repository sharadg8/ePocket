package com.sharad.epocket.accounts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sharad.epocket.database.ContentConstant;
import com.sharad.epocket.database.DatabaseAdapter;

import java.util.ArrayList;

/**
 * Created by Sharad on 17-Jul-16.
 */

public class DataSourceAccount extends DatabaseAdapter {
    public DataSourceAccount(Context context) {
        super(context);
    }

    public long insertAccount(IAccount account) {
        SQLiteDatabase db = openDb();

        ContentValues content = account.getContentValues();

        // Insert it into the database.
        long id = db.insert(DATABASE_TABLE_ACCOUNT, null, content);
        closeDb();
        return id;
    }

    public boolean updateAccount(long rowId, IAccount account) {
        SQLiteDatabase db = openDb();
        String where = ContentConstant.KEY_ACCOUNT_ROWID + "=" + rowId;

        // Create row's data:
        ContentValues content = account.getContentValues();

        // Update it into the database.
        boolean status = db.update(DATABASE_TABLE_ACCOUNT, content, where, null) != 0;
        closeDb();
        return status;
    }

    public boolean deleteAccount(long rowId) {
        SQLiteDatabase db = openDb();
        String where = ContentConstant.KEY_ACCOUNT_ROWID + "=" + rowId;
        boolean status = db.delete(DATABASE_TABLE_ACCOUNT, where, null) != 0;
        closeDb();
        return status;
    }

    public void deleteAllAccounts() {
        SQLiteDatabase db = openDb();
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ACCOUNT);
        db.execSQL(DATABASE_CREATE_SQL_ACCOUNT);
        closeDb();
    }

    public IAccount getAccount(long rowId) {
        SQLiteDatabase db = openDb();
        IAccount account = null;
        String where = ContentConstant.KEY_ACCOUNT_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE_ACCOUNT, ContentConstant.ALL_KEYS_ACCOUNT,
                where, null, null, null, null, null);
        if(c.moveToFirst()) {
            account = new IAccount(c);
        }

        closeDb();
        return account;
    }

    public void getAccounts(ArrayList<IAccount> accounts) {
        getAccounts(accounts, null);
    }

    public void getAccounts(ArrayList<IAccount> accounts, String where) {
        SQLiteDatabase db = openDb();
        accounts.clear();
        Cursor c = 	db.query(true, DATABASE_TABLE_ACCOUNT, ContentConstant.ALL_KEYS_ACCOUNT,
                where, null, null, null, null, null);
        if (c != null) {
            if(c.moveToFirst()) {
                do {
                    accounts.add(new IAccount(c));
                } while (c.moveToNext());
            }
        }
        closeDb();
    }
}
