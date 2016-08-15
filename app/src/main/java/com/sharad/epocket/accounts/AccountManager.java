package com.sharad.epocket.accounts;

import android.content.Context;

import com.sharad.epocket.database.TransactionTable;
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

    public void service(Context context, IAccount iAccount, long fromTimeInMillis) {
        DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(context);
        String where = TransactionTable.COLUMN_ACCOUNT + "=" + iAccount.getId();
        ArrayList<ITransaction> iTransactionArrayList = new ArrayList<>();
        dataSourceTransaction.getTransactions(iTransactionArrayList, where);

        Collections.sort(iTransactionArrayList, new ITransaction.iComparator());
        if(iTransactionArrayList.size() > 0) {
            long oldest = Math.max(
                    iTransactionArrayList.get(iTransactionArrayList.size() - 1).getDate(),
                    fromTimeInMillis);
            long newest = Math.max(
                    iTransactionArrayList.get(0).getDate(),
                    Calendar.getInstance().getTimeInMillis() );

            long timeInMillis = oldest;
            float balanceCash = 0;
            float balanceCard = 0;
            ArrayList<ITransaction> monthList = new ArrayList<>();

            /**
             * Update the initial balance same as the selected month
             */
            long start_ms = Utils.getMonthStart_ms(timeInMillis);
            long end_ms = Utils.getMonthEnd_ms(timeInMillis);
            String monthWhere = TransactionTable.COLUMN_ACCOUNT + "=" + iAccount.getId()
                    + " AND " + TransactionTable.COLUMN_DATE + ">=" + start_ms
                    + " AND " + TransactionTable.COLUMN_DATE + "<=" + end_ms
                    + " AND " + TransactionTable.COLUMN_TYPE + ">" + ITransaction.META_DATA_START;
            dataSourceTransaction.getTransactions(monthList, monthWhere);
            for(ITransaction iTransaction : monthList) {
                if(iTransaction.getType() == ITransaction.META_DATA_MONTH_OPENING_BALANCE_CARD) {
                    balanceCard += iTransaction.getAmount();
                }
                if(iTransaction.getType() == ITransaction.META_DATA_MONTH_OPENING_BALANCE_CASH) {
                    balanceCash += iTransaction.getAmount();
                }
            }

            boolean accountOpened = false;
            do {
                float income = 0;
                float expense = 0;
                float transfer = 0;
                float openingCard = balanceCard;
                float openingCash = balanceCash;
                start_ms = Utils.getMonthStart_ms(timeInMillis);
                end_ms = Utils.getMonthEnd_ms(timeInMillis);
                monthWhere = TransactionTable.COLUMN_ACCOUNT + "=" + iAccount.getId()
                        + " AND " + TransactionTable.COLUMN_DATE + ">=" + start_ms
                        + " AND " + TransactionTable.COLUMN_DATE + "<=" + end_ms
                        + " AND " + TransactionTable.COLUMN_TYPE + "<" + ITransaction.META_DATA_START;
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

                monthWhere = TransactionTable.COLUMN_ACCOUNT + "=" + iAccount.getId()
                        + " AND " + TransactionTable.COLUMN_DATE + ">=" + start_ms
                        + " AND " + TransactionTable.COLUMN_DATE + "<=" + end_ms
                        + " AND " + TransactionTable.COLUMN_TYPE + ">" + ITransaction.META_DATA_START;
                dataSourceTransaction.getTransactions(monthList, monthWhere);

                if(accountOpened) {
                    boolean openingCardAdded = false;
                    boolean openingCashAdded = false;
                    boolean incomeAdded = false;
                    boolean expenseAdded = false;
                    boolean transferAdded = false;
                    for (ITransaction iTransaction : monthList) {
                        switch (iTransaction.getType()) {
                            case ITransaction.META_DATA_MONTH_OPENING_BALANCE_CARD:
                                openingCardAdded = true;
                                iTransaction.setAmount(openingCard);
                                dataSourceTransaction.updateTransaction(iTransaction);
                                break;
                            case ITransaction.META_DATA_MONTH_OPENING_BALANCE_CASH:
                                openingCashAdded = true;
                                iTransaction.setAmount(openingCash);
                                dataSourceTransaction.updateTransaction(iTransaction);
                                break;
                            case ITransaction.META_DATA_MONTH_INCOME:
                                incomeAdded = true;
                                iTransaction.setAmount(income);
                                dataSourceTransaction.updateTransaction(iTransaction);
                                break;
                            case ITransaction.META_DATA_MONTH_EXPENSE:
                                expenseAdded = true;
                                iTransaction.setAmount(expense);
                                dataSourceTransaction.updateTransaction(iTransaction);
                                break;
                            case ITransaction.META_DATA_MONTH_TRANSFER:
                                transferAdded = true;
                                iTransaction.setAmount(transfer);
                                dataSourceTransaction.updateTransaction(iTransaction);
                                break;
                        }
                    }

                    if(openingCardAdded == false) {
                        ITransaction iOpeningCard = new ITransaction();
                        iOpeningCard.setAccount(iAccount.getId());
                        iOpeningCard.setDate(start_ms + 1);
                        iOpeningCard.setAmount(openingCard);
                        iOpeningCard.setType(ITransaction.META_DATA_MONTH_OPENING_BALANCE_CARD);
                        dataSourceTransaction.insertTransaction(iOpeningCard);
                    }

                    if(openingCashAdded == false) {
                        ITransaction iOpeningCash = new ITransaction();
                        iOpeningCash.setAccount(iAccount.getId());
                        iOpeningCash.setDate(start_ms + 1);
                        iOpeningCash.setAmount(openingCash);
                        iOpeningCash.setType(ITransaction.META_DATA_MONTH_OPENING_BALANCE_CASH);
                        dataSourceTransaction.insertTransaction(iOpeningCash);
                    }

                    if(incomeAdded == false) {
                        ITransaction iIncome = new ITransaction();
                        iIncome.setAccount(iAccount.getId());
                        iIncome.setDate(start_ms + 1);
                        iIncome.setAmount(income);
                        iIncome.setType(ITransaction.META_DATA_MONTH_INCOME);
                        dataSourceTransaction.insertTransaction(iIncome);
                    }

                    if(expenseAdded == false) {
                        ITransaction iExpense = new ITransaction();
                        iExpense.setAccount(iAccount.getId());
                        iExpense.setDate(start_ms + 1);
                        iExpense.setAmount(expense);
                        iExpense.setType(ITransaction.META_DATA_MONTH_EXPENSE);
                        dataSourceTransaction.insertTransaction(iExpense);
                    }

                    if(transferAdded == false) {
                        ITransaction iTransfer = new ITransaction();
                        iTransfer.setAccount(iAccount.getId());
                        iTransfer.setDate(start_ms + 1);
                        iTransfer.setAmount(transfer);
                        iTransfer.setType(ITransaction.META_DATA_MONTH_TRANSFER);
                        dataSourceTransaction.insertTransaction(iTransfer);
                    }
                } else {
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
        String where = TransactionTable.COLUMN_ACCOUNT + "=" + iAccount.getId()
                + " AND " + TransactionTable.COLUMN_DATE + ">=" + start_ms
                + " AND " + TransactionTable.COLUMN_DATE + "<=" + end_ms;
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
        String where = TransactionTable.COLUMN_ACCOUNT + "=" + iAccount.getId()
                + " AND " + TransactionTable.COLUMN_DATE + ">=" + start_ms
                + " AND " + TransactionTable.COLUMN_DATE + "<=" + end_ms;
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
        String where = TransactionTable.COLUMN_ACCOUNT + "=" + iAccount.getId()
                + " AND ( " + TransactionTable.COLUMN_SUB_TYPE + "=" + ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_CASH
                + " OR " + TransactionTable.COLUMN_SUB_TYPE + "=" + ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_BOTH + " )";
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
        String where = TransactionTable.COLUMN_ACCOUNT + "=" + iAccount.getId()
                + " AND ( " + TransactionTable.COLUMN_SUB_TYPE + "=" + ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_CARD
                + " OR " + TransactionTable.COLUMN_SUB_TYPE + "=" + ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_BOTH + " )";
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
        String where = TransactionTable.COLUMN_ACCOUNT + "=" + id;
        sourceTransaction.deleteTransactions(where);
        DataSourceAccount source = new DataSourceAccount(context);
        source.deleteAccount(id);
    }

    public void deleteTransaction(Context context, ITransaction iTransaction) {
        DataSourceAccount dataSourceAccount = new DataSourceAccount(context);
        DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(context);
        dataSourceTransaction.deleteTransaction(iTransaction);

        IAccount iAccount = dataSourceAccount.getAccount(iTransaction.getAccount());
        service(context, iAccount, iTransaction.getDate());
    }

    public long insertTransaction(Context context, ITransaction iTransaction) {
        DataSourceAccount dataSourceAccount = new DataSourceAccount(context);
        DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(context);
        long id = dataSourceTransaction.insertTransaction(iTransaction);

        IAccount iAccount = dataSourceAccount.getAccount(iTransaction.getAccount());
        service(context, iAccount, iTransaction.getDate());
        return id;
    }

    public void updateTransaction(Context context, ITransaction iTransaction) {
        DataSourceAccount dataSourceAccount = new DataSourceAccount(context);
        DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(context);

        ITransaction oldTransaction = dataSourceTransaction.getTransaction(iTransaction.getId());
        long oldTimeInMillis = oldTransaction.getDate();
        long newTimeInMillis = iTransaction.getDate();

        dataSourceTransaction.updateTransaction(iTransaction);

        IAccount iAccount = dataSourceAccount.getAccount(iTransaction.getAccount());
        service(context, iAccount, Math.min(oldTimeInMillis, newTimeInMillis));
    }

    public boolean hasAnyTransactionThisMonth(Context context, IAccount iAccount, long timeInMillis) {
        long start_ms = Utils.getMonthStart_ms(timeInMillis);
        long end_ms = Utils.getMonthEnd_ms(timeInMillis);

        DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(context);
        String where = TransactionTable.COLUMN_ACCOUNT + "=" + iAccount.getId()
                + " AND " + TransactionTable.COLUMN_DATE + ">=" + start_ms
                + " AND " + TransactionTable.COLUMN_DATE + "<=" + end_ms;
        ArrayList<ITransaction> iTransactionArrayList = new ArrayList<>();
        dataSourceTransaction.getTransactions(iTransactionArrayList, where);

        return (iTransactionArrayList.size() > 0);
    }

    public boolean hasAnyTransactionBeforeMonth(Context context, IAccount iAccount, long timeInMillis) {
        long end_ms = Utils.getMonthStart_ms(timeInMillis);

        DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(context);
        String where = TransactionTable.COLUMN_ACCOUNT + "=" + iAccount.getId()
                + " AND " + TransactionTable.COLUMN_DATE + "<=" + end_ms;
        ArrayList<ITransaction> iTransactionArrayList = new ArrayList<>();
        dataSourceTransaction.getTransactions(iTransactionArrayList, where);

        return (iTransactionArrayList.size() > 0);
    }

    public boolean hasAnyTransactionAfterMonth(Context context, IAccount iAccount, long timeInMillis) {
        long start_ms = Utils.getMonthEnd_ms(timeInMillis);

        DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(context);
        String where = TransactionTable.COLUMN_ACCOUNT + "=" + iAccount.getId()
                + " AND " + TransactionTable.COLUMN_DATE + ">=" + start_ms;
        ArrayList<ITransaction> iTransactionArrayList = new ArrayList<>();
        dataSourceTransaction.getTransactions(iTransactionArrayList, where);

        return (iTransactionArrayList.size() > 0);
    }

    public boolean hasAnyTransactionBeforeYear(Context context, IAccount iAccount, long timeInMillis) {
        long end_ms = Utils.getYearStart_ms(timeInMillis);

        DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(context);
        String where = TransactionTable.COLUMN_ACCOUNT + "=" + iAccount.getId()
                + " AND " + TransactionTable.COLUMN_DATE + "<=" + end_ms;
        ArrayList<ITransaction> iTransactionArrayList = new ArrayList<>();
        dataSourceTransaction.getTransactions(iTransactionArrayList, where);

        return (iTransactionArrayList.size() > 0);
    }

    public boolean hasAnyTransactionAfterYear(Context context, IAccount iAccount, long timeInMillis) {
        long start_ms = Utils.getYearEnd_ms(timeInMillis);

        DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(context);
        String where = TransactionTable.COLUMN_ACCOUNT + "=" + iAccount.getId()
                + " AND " + TransactionTable.COLUMN_DATE + ">=" + start_ms;
        ArrayList<ITransaction> iTransactionArrayList = new ArrayList<>();
        dataSourceTransaction.getTransactions(iTransactionArrayList, where);

        return (iTransactionArrayList.size() > 0);
    }
}
