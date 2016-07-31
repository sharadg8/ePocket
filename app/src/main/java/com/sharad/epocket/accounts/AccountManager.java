package com.sharad.epocket.accounts;

import android.content.Context;

/**
 * Created by Sharad on 31-Jul-16.
 */
public class AccountManager {
    private static AccountManager ourInstance = new AccountManager();

    public static AccountManager getInstance() {
        return ourInstance;
    }

    private AccountManager() { }

    public void updateAccount(Context context, long id) {

    }
}
