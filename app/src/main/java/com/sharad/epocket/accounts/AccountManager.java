package com.sharad.epocket.accounts;

import android.content.Context;

import com.sharad.epocket.database.ContentConstant;
import com.sharad.epocket.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * Created by Sharad on 31-Jul-16.
 */
public class AccountManager {
    private static AccountManager ourInstance = new AccountManager();
    private AccountManager() { }

    public static AccountManager getInstance() {
        return ourInstance;
    }

    public void service(Context context, IAccount iAccount) {
        DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(context);
        String where = ContentConstant.KEY_TRANSACTION_ACCOUNT + "=" + iAccount.getId();
        ArrayList<ITransaction> iTransactionArrayList = new ArrayList<>();
        dataSourceTransaction.getTransactions(iTransactionArrayList, where);

        Collections.sort(iTransactionArrayList, new ITransaction.iComparator());
        if(iTransactionArrayList.size() > 0) {
            long oldest = iTransactionArrayList.get(iTransactionArrayList.size() - 1).getDate();
            long newest = Math.max(
                    iTransactionArrayList.get(0).getDate(),
                    Calendar.getInstance().getTimeInMillis() );

            long timeInMillis = oldest;
            float balanceCash = 0;
            float balanceCard = 0;
            ArrayList<ITransaction> monthList = new ArrayList<>();
            boolean accountOpened = false;
            do {
                float income = 0;
                float expense = 0;
                float transfer = 0;
                float openingCard = balanceCard;
                float openingCash = balanceCash;
                long start_ms = Utils.getMonthStart_ms(timeInMillis);
                long end_ms = Utils.getMonthEnd_ms(timeInMillis);
                String monthWhere = ContentConstant.KEY_TRANSACTION_ACCOUNT + "=" + iAccount.getId()
                        + " AND " + ContentConstant.KEY_TRANSACTION_DATE + ">=" + start_ms
                        + " AND " + ContentConstant.KEY_TRANSACTION_DATE + "<=" + end_ms
                        + " AND " + ContentConstant.KEY_TRANSACTION_TYPE + "<" + ITransaction.META_DATA_START;
                dataSourceTransaction.getTransactions(monthList, monthWhere);
                for (ITransaction iTransaction : monthList) {
                    accountOpened = true;
                    switch (iTransaction.getType()) {
                        case ITransaction.TRANSACTION_TYPE_ACCOUNT_EXPENSE:
                            expense += iTransaction.getAmount();
                            switch (iTransaction.getSubType()) {
                                case ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_CARD:
                                    balanceCard -= iTransaction.getAmount();
                                    break;
                                case ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_CASH:
                                    balanceCash -= iTransaction.getAmount();
                                    break;
                            }
                            break;
                        case ITransaction.TRANSACTION_TYPE_ACCOUNT_INCOME:
                            income += iTransaction.getAmount();
                            switch (iTransaction.getSubType()) {
                                case ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_CARD:
                                    balanceCard += iTransaction.getAmount();
                                    break;
                                case ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_CASH:
                                    balanceCash += iTransaction.getAmount();
                                    break;
                            }
                            break;
                        case ITransaction.TRANSACTION_TYPE_ACCOUNT_TRANSFER:
                            transfer += iTransaction.getAmount();
                            switch (iTransaction.getSubType()) {
                                case ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_CARD:
                                    balanceCard -= iTransaction.getAmount();
                                    break;
                                case ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_CASH:
                                    balanceCash -= iTransaction.getAmount();
                                    break;
                            }
                            break;
                        case ITransaction.TRANSACTION_TYPE_ACCOUNT_WITHDRAW:
                            balanceCash += iTransaction.getAmount();
                            balanceCard -= iTransaction.getAmount();
                            break;
                        case ITransaction.TRANSACTION_TYPE_ACCOUNT_DEPOSIT:
                            balanceCard += iTransaction.getAmount();
                            balanceCash -= iTransaction.getAmount();
                            break;
                    }
                }

                if(accountOpened) {
                    /* Opening balance card */
                    monthWhere = ContentConstant.KEY_TRANSACTION_ACCOUNT + "=" + iAccount.getId()
                            + " AND " + ContentConstant.KEY_TRANSACTION_DATE + ">=" + start_ms
                            + " AND " + ContentConstant.KEY_TRANSACTION_DATE + "<=" + end_ms
                            + " AND " + ContentConstant.KEY_TRANSACTION_TYPE + "=" + ITransaction.META_DATA_MONTH_OPENING_BALANCE_CARD;
                    dataSourceTransaction.getTransactions(monthList, monthWhere);
                    if(monthList.size() > 0) {
                        monthList.get(0).setAmount(openingCard);
                        dataSourceTransaction.updateTransaction(monthList.get(0));
                    } else {
                        ITransaction iOpeningCard = new ITransaction();
                        iOpeningCard.setAccount(iAccount.getId());
                        iOpeningCard.setDate(start_ms + 1);
                        iOpeningCard.setAmount(openingCard);
                        iOpeningCard.setType(ITransaction.META_DATA_MONTH_OPENING_BALANCE_CARD);
                        dataSourceTransaction.insertTransaction(iOpeningCard);
                    }

                    /* Opening balance cash */
                    monthWhere = ContentConstant.KEY_TRANSACTION_ACCOUNT + "=" + iAccount.getId()
                            + " AND " + ContentConstant.KEY_TRANSACTION_DATE + ">=" + start_ms
                            + " AND " + ContentConstant.KEY_TRANSACTION_DATE + "<=" + end_ms
                            + " AND " + ContentConstant.KEY_TRANSACTION_TYPE + "=" + ITransaction.META_DATA_MONTH_OPENING_BALANCE_CASH;
                    dataSourceTransaction.getTransactions(monthList, monthWhere);
                    if(monthList.size() > 0) {
                        monthList.get(0).setAmount(openingCash);
                        dataSourceTransaction.updateTransaction(monthList.get(0));
                    } else {
                        ITransaction iOpeningCash = new ITransaction();
                        iOpeningCash.setAccount(iAccount.getId());
                        iOpeningCash.setDate(start_ms + 1);
                        iOpeningCash.setAmount(openingCash);
                        iOpeningCash.setType(ITransaction.META_DATA_MONTH_OPENING_BALANCE_CASH);
                        dataSourceTransaction.insertTransaction(iOpeningCash);
                    }

                    /* Income */
                    monthWhere = ContentConstant.KEY_TRANSACTION_ACCOUNT + "=" + iAccount.getId()
                            + " AND " + ContentConstant.KEY_TRANSACTION_DATE + ">=" + start_ms
                            + " AND " + ContentConstant.KEY_TRANSACTION_DATE + "<=" + end_ms
                            + " AND " + ContentConstant.KEY_TRANSACTION_TYPE + "=" + ITransaction.META_DATA_MONTH_INCOME;
                    dataSourceTransaction.getTransactions(monthList, monthWhere);
                    if(monthList.size() > 0) {
                        monthList.get(0).setAmount(income);
                        dataSourceTransaction.updateTransaction(monthList.get(0));
                    } else {
                        ITransaction iIncome = new ITransaction();
                        iIncome.setAccount(iAccount.getId());
                        iIncome.setDate(start_ms + 1);
                        iIncome.setAmount(income);
                        iIncome.setType(ITransaction.META_DATA_MONTH_INCOME);
                        dataSourceTransaction.insertTransaction(iIncome);
                    }

                    /* Expense */
                    monthWhere = ContentConstant.KEY_TRANSACTION_ACCOUNT + "=" + iAccount.getId()
                            + " AND " + ContentConstant.KEY_TRANSACTION_DATE + ">=" + start_ms
                            + " AND " + ContentConstant.KEY_TRANSACTION_DATE + "<=" + end_ms
                            + " AND " + ContentConstant.KEY_TRANSACTION_TYPE + "=" + ITransaction.META_DATA_MONTH_EXPENSE;
                    dataSourceTransaction.getTransactions(monthList, monthWhere);
                    if(monthList.size() > 0) {
                        monthList.get(0).setAmount(expense);
                        dataSourceTransaction.updateTransaction(monthList.get(0));
                    } else {
                        ITransaction iExpense = new ITransaction();
                        iExpense.setAccount(iAccount.getId());
                        iExpense.setDate(start_ms + 1);
                        iExpense.setAmount(expense);
                        iExpense.setType(ITransaction.META_DATA_MONTH_EXPENSE);
                        dataSourceTransaction.insertTransaction(iExpense);
                    }

                    /* Transfer */
                    monthWhere = ContentConstant.KEY_TRANSACTION_ACCOUNT + "=" + iAccount.getId()
                            + " AND " + ContentConstant.KEY_TRANSACTION_DATE + ">=" + start_ms
                            + " AND " + ContentConstant.KEY_TRANSACTION_DATE + "<=" + end_ms
                            + " AND " + ContentConstant.KEY_TRANSACTION_TYPE + "=" + ITransaction.META_DATA_MONTH_TRANSFER;
                    dataSourceTransaction.getTransactions(monthList, monthWhere);
                    if(monthList.size() > 0) {
                        monthList.get(0).setAmount(transfer);
                        dataSourceTransaction.updateTransaction(monthList.get(0));
                    } else {
                        ITransaction iTransfer = new ITransaction();
                        iTransfer.setAccount(iAccount.getId());
                        iTransfer.setDate(start_ms + 1);
                        iTransfer.setAmount(transfer);
                        iTransfer.setType(ITransaction.META_DATA_MONTH_TRANSFER);
                        dataSourceTransaction.insertTransaction(iTransfer);
                    }
                } else {
                    monthWhere = ContentConstant.KEY_TRANSACTION_ACCOUNT + "=" + iAccount.getId()
                            + " AND " + ContentConstant.KEY_TRANSACTION_DATE + ">=" + start_ms
                            + " AND " + ContentConstant.KEY_TRANSACTION_DATE + "<=" + end_ms
                            + " AND " + ContentConstant.KEY_TRANSACTION_TYPE + ">=" + ITransaction.META_DATA_START;
                    dataSourceTransaction.getTransactions(monthList, monthWhere);
                    for (ITransaction delete : monthList) {
                        dataSourceTransaction.deleteTransaction(delete);
                    }
                }

                Calendar month = Calendar.getInstance();
                month.setTimeInMillis(timeInMillis);
                month.add(Calendar.MONTH, +1);
                timeInMillis = month.getTimeInMillis();
            }while (timeInMillis <= newest);
        }
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

    public void deleteAccount(Context context, long id) {
        DataSourceTransaction sourceTransaction = new DataSourceTransaction(context);
        String where = ContentConstant.KEY_TRANSACTION_ACCOUNT + "=" + id;
        sourceTransaction.deleteTransactions(where);
        DataSourceAccount source = new DataSourceAccount(context);
        source.deleteAccount(id);
    }

    public boolean hasAnyTransactionThisMonth(Context context, IAccount iAccount, long timeInMillis) {
        long start_ms = Utils.getMonthStart_ms(timeInMillis);
        long end_ms = Utils.getMonthEnd_ms(timeInMillis);

        DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(context);
        String where = ContentConstant.KEY_TRANSACTION_ACCOUNT + "=" + iAccount.getId()
                + " AND " + ContentConstant.KEY_TRANSACTION_DATE + ">=" + start_ms
                + " AND " + ContentConstant.KEY_TRANSACTION_DATE + "<=" + end_ms;
        ArrayList<ITransaction> iTransactionArrayList = new ArrayList<>();
        dataSourceTransaction.getTransactions(iTransactionArrayList, where);

        return (iTransactionArrayList.size() > 0);
    }

    public boolean hasAnyTransactionBeforeMonth(Context context, IAccount iAccount, long timeInMillis) {
        long end_ms = Utils.getMonthStart_ms(timeInMillis);

        DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(context);
        String where = ContentConstant.KEY_TRANSACTION_ACCOUNT + "=" + iAccount.getId()
                + " AND " + ContentConstant.KEY_TRANSACTION_DATE + "<=" + end_ms;
        ArrayList<ITransaction> iTransactionArrayList = new ArrayList<>();
        dataSourceTransaction.getTransactions(iTransactionArrayList, where);

        return (iTransactionArrayList.size() > 0);
    }

    public boolean hasAnyTransactionAfterMonth(Context context, IAccount iAccount, long timeInMillis) {
        long start_ms = Utils.getMonthEnd_ms(timeInMillis);

        DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(context);
        String where = ContentConstant.KEY_TRANSACTION_ACCOUNT + "=" + iAccount.getId()
                + " AND " + ContentConstant.KEY_TRANSACTION_DATE + ">=" + start_ms;
        ArrayList<ITransaction> iTransactionArrayList = new ArrayList<>();
        dataSourceTransaction.getTransactions(iTransactionArrayList, where);

        return (iTransactionArrayList.size() > 0);
    }

    public boolean hasAnyTransactionBeforeYear(Context context, IAccount iAccount, long timeInMillis) {
        long end_ms = Utils.getYearStart_ms(timeInMillis);

        DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(context);
        String where = ContentConstant.KEY_TRANSACTION_ACCOUNT + "=" + iAccount.getId()
                + " AND " + ContentConstant.KEY_TRANSACTION_DATE + "<=" + end_ms;
        ArrayList<ITransaction> iTransactionArrayList = new ArrayList<>();
        dataSourceTransaction.getTransactions(iTransactionArrayList, where);

        return (iTransactionArrayList.size() > 0);
    }

    public boolean hasAnyTransactionAfterYear(Context context, IAccount iAccount, long timeInMillis) {
        long start_ms = Utils.getYearEnd_ms(timeInMillis);

        DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(context);
        String where = ContentConstant.KEY_TRANSACTION_ACCOUNT + "=" + iAccount.getId()
                + " AND " + ContentConstant.KEY_TRANSACTION_DATE + ">=" + start_ms;
        ArrayList<ITransaction> iTransactionArrayList = new ArrayList<>();
        dataSourceTransaction.getTransactions(iTransactionArrayList, where);

        return (iTransactionArrayList.size() > 0);
    }
}
