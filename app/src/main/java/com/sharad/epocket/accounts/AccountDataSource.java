package com.sharad.epocket.accounts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sharad.epocket.database.DatabaseAdapter;

import java.util.ArrayList;

/**
 * Created by Sharad on 17-Jul-16.
 */

public class AccountDataSource extends DatabaseAdapter {
    public AccountDataSource(Context context) {
        super(context);
    }

    private ContentValues getContentValues(AccountItem account) {
        // Create row's data:
        ContentValues content = new ContentValues();
        content.put(KEY_ACCOUNT_TITLE,       account.getTitle());
        content.put(KEY_ACCOUNT_CURRENCY,    account.getIsoCurrency());
        content.put(KEY_ACCOUNT_NOTE,        account.getNote());
        content.put(KEY_ACCOUNT_NUMBER,      account.getAccountNumber());
        content.put(KEY_ACCOUNT_LOGIN,       account.getLoginId());
        content.put(KEY_ACCOUNT_PASSWORD,    account.getPassword());
        content.put(KEY_ACCOUNT_CONTACT,     account.getContact());
        content.put(KEY_ACCOUNT_BAL_CARD,    account.getBalanceCard());
        content.put(KEY_ACCOUNT_BAL_CASH,    account.getBalanceCash());
        content.put(KEY_ACCOUNT_INFLOW,      account.getInflow());
        content.put(KEY_ACCOUNT_OUTFLOW,     account.getOutflow());
        content.put(KEY_ACCOUNT_TYPE,        account.getAccountType());
        content.put(KEY_ACCOUNT_LAST_UPDATE, account.getLastUpdateMSec());

        return content;
    }

    public long insertAccount(AccountItem account) {
        SQLiteDatabase db = openDb();

        ContentValues content = getContentValues(account);

        // Insert it into the database.
        long id = db.insert(DATABASE_TABLE_ACCOUNT, null, content);
        closeDb();
        return id;
    }

    public boolean updateAccount(long rowId, AccountItem account) {
        SQLiteDatabase db = openDb();
        String where = KEY_ACCOUNT_ROWID + "=" + rowId;

        // Create row's data:
        ContentValues content = getContentValues(account);

        // Update it into the database.
        boolean status = db.update(DATABASE_TABLE_ACCOUNT, content, where, null) != 0;
        closeDb();
        return status;
    }

    public boolean deleteAccount(long rowId) {
        SQLiteDatabase db = openDb();
        String where = KEY_ACCOUNT_ROWID + "=" + rowId;
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

    private AccountItem parseAccount(Cursor c) {
        long id 		    = c.getLong(c.getColumnIndex(KEY_ACCOUNT_ROWID));
        String title        = c.getString(c.getColumnIndex(KEY_ACCOUNT_TITLE));
        String isoCurrency  = c.getString(c.getColumnIndex(KEY_ACCOUNT_CURRENCY));
        String note         = c.getString(c.getColumnIndex(KEY_ACCOUNT_NOTE));
        String accountNum   = c.getString(c.getColumnIndex(KEY_ACCOUNT_NUMBER));
        String loginId      = c.getString(c.getColumnIndex(KEY_ACCOUNT_LOGIN));
        String password     = c.getString(c.getColumnIndex(KEY_ACCOUNT_PASSWORD));
        String contact      = c.getString(c.getColumnIndex(KEY_ACCOUNT_CONTACT));
        float balanceCard   = c.getFloat(c.getColumnIndex(KEY_ACCOUNT_BAL_CARD));
        float balanceCash   = c.getFloat(c.getColumnIndex(KEY_ACCOUNT_BAL_CASH));
        float inflow        = c.getFloat(c.getColumnIndex(KEY_ACCOUNT_INFLOW));
        float outflow       = c.getFloat(c.getColumnIndex(KEY_ACCOUNT_OUTFLOW));
        int accountType     = c.getInt(c.getColumnIndex(KEY_ACCOUNT_TYPE));
        long lastUpdateMSec = c.getLong(c.getColumnIndex(KEY_ACCOUNT_LAST_UPDATE));

        AccountItem account = new AccountItem(id, isoCurrency, title, note, accountNum, loginId,
                password, contact, balanceCard, balanceCash, inflow, outflow, accountType, lastUpdateMSec);
        return account;
    }

    public AccountItem getAccount(long rowId) {
        SQLiteDatabase db = openDb();
        AccountItem account = null;
        String where = KEY_ACCOUNT_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE_ACCOUNT, ALL_KEYS_ACCOUNT,
                where, null, null, null, null, null);
        if(c.moveToFirst()) {
            account = parseAccount(c);
        }

        closeDb();
        return account;
    }

    public void getAccounts(ArrayList<AccountItem> accounts) {
        getAccounts(accounts, null);
    }

    public void getAccounts(ArrayList<AccountItem> accounts, String where) {
        SQLiteDatabase db = openDb();
        accounts.clear();
        Cursor c = 	db.query(true, DATABASE_TABLE_ACCOUNT, ALL_KEYS_ACCOUNT,
                where, null, null, null, null, null);
        if (c != null) {
            if(c.moveToFirst()) {
                do {
                    accounts.add(parseAccount(c));
                } while (c.moveToNext());
            }
        }
        closeDb();
    }
}
