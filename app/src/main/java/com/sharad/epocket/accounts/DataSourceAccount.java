package com.sharad.epocket.accounts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sharad.epocket.database.AccountTable;
import com.sharad.epocket.database.DatabaseHelper;

import java.util.ArrayList;

/**
 * Created by Sharad on 17-Jul-16.
 */

public class DataSourceAccount {
    DatabaseHelper helper;

    public DataSourceAccount(Context context) {
        helper = new DatabaseHelper(context);
    }

    public long insertAccount(IAccount account) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues content = account.getContentValues();

        // Insert it into the database.
        long id = db.insert(AccountTable.TABLE_ACCOUNT, null, content);
        helper.close();
        return id;
    }

    public boolean updateAccount(IAccount iAccount) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String where = AccountTable.COLUMN_ID + "=" + iAccount.getId();

        // Create row's data:
        ContentValues content = iAccount.getContentValues();

        // Update it into the database.
        boolean status = db.update(AccountTable.TABLE_ACCOUNT, content, where, null) != 0;
        helper.close();
        return status;
    }

    public boolean deleteAccount(long rowId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String where = AccountTable.COLUMN_ID + "=" + rowId;
        boolean status = db.delete(AccountTable.TABLE_ACCOUNT, where, null) != 0;
        helper.close();
        return status;
    }

    public IAccount getAccount(long rowId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        IAccount account = null;
        String where = AccountTable.COLUMN_ID + "=" + rowId;
        Cursor c = 	db.query(true, AccountTable.TABLE_ACCOUNT, null, where, null, null, null, null, null);
        if(c.moveToFirst()) {
            account = new IAccount(c);
        }

        helper.close();
        return account;
    }

    public void getAccounts(ArrayList<IAccount> accounts) {
        getAccounts(accounts, null);
    }

    public void getAccounts(ArrayList<IAccount> accounts, String where) {
        SQLiteDatabase db = helper.getReadableDatabase();
        accounts.clear();
        Cursor c = 	db.query(true, AccountTable.TABLE_ACCOUNT, null, where, null, null, null, null, null);
        if (c != null) {
            if(c.moveToFirst()) {
                do {
                    accounts.add(new IAccount(c));
                } while (c.moveToNext());
            }
        }
        helper.close();
    }
}
