package com.sharad.epocket.accounts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharad.epocket.R;
import com.sharad.epocket.database.ContentConstant;
import com.sharad.epocket.utils.Constant;
import com.sharad.epocket.widget.recyclerview.StickyRecyclerView;

import java.util.ArrayList;

/**
 * Created by Sharad on 28-Jul-16.
 */

public class AccountTransactionFragment extends Fragment {
    private StickyRecyclerView mRecyclerView;

    private static final String ARG_SECTION_NUMBER = "section_number";

    private long accountId;

    public AccountTransactionFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AccountTransactionFragment newInstance(long accountNumber, int sectionNumber) {
        AccountTransactionFragment fragment = new AccountTransactionFragment();
        Bundle args = new Bundle();
        args.putLong(Constant.ARG_ACCOUNT_NUMBER_LONG, accountNumber);
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_account_transaction, container, false);

        Bundle args = getArguments();
        accountId = args.getLong(Constant.ARG_ACCOUNT_NUMBER_LONG, -1);

        DataSourceAccount dataSourceAccount = new DataSourceAccount(getContext());
        IAccount account = dataSourceAccount.getAccount(accountId);

        if(account != null) {
            ArrayList<ITransaction> itemList = new ArrayList<>();

            DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(getContext());
            String where = ContentConstant.KEY_TRANSACTION_ACCOUNT + "=" + account.getId();
            dataSourceTransaction.getTransactions(itemList, where);

            /*itemList.add(new ITransaction(0, 0, "This is sample comment", "", ITransaction.TRANSACTION_TYPE_ACCOUNT_EXPENSE, ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_CARD, 0, 0, 1234.34f));
            itemList.add(new ITransaction(0, 0, "This is comment", "", ITransaction.TRANSACTION_TYPE_ACCOUNT_INCOME, ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_CARD, 0, 0, 134.34f));
            itemList.add(new ITransaction(0, 0, "This is sample", "", ITransaction.TRANSACTION_TYPE_ACCOUNT_EXPENSE, ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_CASH, 0, 0, 124.34f));
            itemList.add(new ITransaction(0, 0, "", "", ITransaction.TRANSACTION_TYPE_ACCOUNT_TRANSFER, ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_CASH, 0, 0, 234.34f));
            itemList.add(new ITransaction(0, 0, "This is sample comment", "", ITransaction.TRANSACTION_TYPE_ACCOUNT_EXPENSE, ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_CARD, 0, 0, 134.34f));*/
            final TransactionRecyclerAdapter adapter = new TransactionRecyclerAdapter(this.getActivity(), itemList);
            mRecyclerView = (StickyRecyclerView) view.findViewById(R.id.recyclerView);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setIndexer(adapter);
        }

        return view;
    }
}
