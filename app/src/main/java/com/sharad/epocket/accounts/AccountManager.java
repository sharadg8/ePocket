package com.sharad.epocket.accounts;

import android.content.Context;

import com.sharad.epocket.database.ContentConstant;
import com.sharad.epocket.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;

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

    public void updateAccountListOrder(Context context, ArrayList<IAccount> iAccounts) {
        DataSourceAccount dataSourceAccount = new DataSourceAccount(context);
        int index = 0;
        for(IAccount iAccount : iAccounts) {
            IAccount account_db = dataSourceAccount.getAccount(iAccount.getId());
            account_db.setListIndex(index);
            dataSourceAccount.updateAccount(iAccount);
            index++;
        }
    }

    public float getAccountInflow(Context context, IAccount iAccount) {
        float inflow = 0;
        Calendar now = Calendar.getInstance();
        long start_ms = Utils.getMonthStart_ms(now.getTimeInMillis());
        long end_ms = now.getTimeInMillis();
        DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(context);
        String where = ContentConstant.KEY_TRANSACTION_ACCOUNT + "=" + iAccount.getId()
                + " AND " + ContentConstant.KEY_TRANSACTION_DATE + ">=" + start_ms
                + " AND " + ContentConstant.KEY_TRANSACTION_DATE + "<=" + end_ms;
        ArrayList<ITransaction> iTransactionArrayList = new ArrayList<>();
        dataSourceTransaction.getTransactions(iTransactionArrayList, where);
        for (ITransaction iTransaction : iTransactionArrayList) {
            switch (iTransaction.getType()) {
                case ITransaction.TRANSACTION_TYPE_ACCOUNT_INCOME:
                    inflow += iTransaction.getAmount();
                    break;
            }
        }
        return inflow;
    }

    public float getAccountOutflow(Context context, IAccount iAccount) {
        float outflow = 0;
        Calendar now = Calendar.getInstance();
        long start_ms = Utils.getMonthStart_ms(now.getTimeInMillis());
        long end_ms = now.getTimeInMillis();
        DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(context);
        String where = ContentConstant.KEY_TRANSACTION_ACCOUNT + "=" + iAccount.getId()
                + " AND " + ContentConstant.KEY_TRANSACTION_DATE + ">=" + start_ms
                + " AND " + ContentConstant.KEY_TRANSACTION_DATE + "<=" + end_ms;
        ArrayList<ITransaction> iTransactionArrayList = new ArrayList<>();
        dataSourceTransaction.getTransactions(iTransactionArrayList, where);
        for (ITransaction iTransaction : iTransactionArrayList) {
            switch (iTransaction.getType()) {
                case ITransaction.TRANSACTION_TYPE_ACCOUNT_EXPENSE:
                case ITransaction.TRANSACTION_TYPE_ACCOUNT_TRANSFER:
                    outflow += iTransaction.getAmount();
                    break;
            }
        }
        return outflow;
    }

    public float getAccountBalanceCash(Context context, IAccount iAccount) {
        float balCash = 0;
        DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(context);
        String where = ContentConstant.KEY_TRANSACTION_ACCOUNT + "=" + iAccount.getId()
                + " AND ( " + ContentConstant.KEY_TRANSACTION_SUB_TYPE + "=" + ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_CASH
                + " OR " + ContentConstant.KEY_TRANSACTION_SUB_TYPE + "=" + ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_BOTH + " )";
        ArrayList<ITransaction> iTransactionArrayList = new ArrayList<>();
        dataSourceTransaction.getTransactions(iTransactionArrayList, where);
        for (ITransaction iTransaction : iTransactionArrayList) {
            switch (iTransaction.getType()) {
                case ITransaction.TRANSACTION_TYPE_ACCOUNT_EXPENSE:
                case ITransaction.TRANSACTION_TYPE_ACCOUNT_DEPOSIT:
                    balCash -= iTransaction.getAmount();
                    break;
                case ITransaction.TRANSACTION_TYPE_ACCOUNT_INCOME:
                case ITransaction.TRANSACTION_TYPE_ACCOUNT_WITHDRAW:
                    balCash += iTransaction.getAmount();
                    break;
            }
        }
        return balCash;
    }

    public float getAccountBalanceCard(Context context, IAccount iAccount) {
        float balCard = 0;
        DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(context);
        String where = ContentConstant.KEY_TRANSACTION_ACCOUNT + "=" + iAccount.getId()
                + " AND ( " + ContentConstant.KEY_TRANSACTION_SUB_TYPE + "=" + ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_CARD
                + " OR " + ContentConstant.KEY_TRANSACTION_SUB_TYPE + "=" + ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_BOTH + " )";
        ArrayList<ITransaction> iTransactionArrayList = new ArrayList<>();
        dataSourceTransaction.getTransactions(iTransactionArrayList, where);
        for (ITransaction iTransaction : iTransactionArrayList) {
            switch (iTransaction.getType()) {
                case ITransaction.TRANSACTION_TYPE_ACCOUNT_EXPENSE:
                case ITransaction.TRANSACTION_TYPE_ACCOUNT_WITHDRAW:
                    balCard -= iTransaction.getAmount();
                    break;
                case ITransaction.TRANSACTION_TYPE_ACCOUNT_INCOME:
                case ITransaction.TRANSACTION_TYPE_ACCOUNT_DEPOSIT:
                    balCard += iTransaction.getAmount();
                    break;
            }
        }
        return balCard;
    }
}
