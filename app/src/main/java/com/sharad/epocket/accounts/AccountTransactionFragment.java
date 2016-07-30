package com.sharad.epocket.accounts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharad.epocket.R;
import com.sharad.epocket.database.ContentConstant;
import com.sharad.epocket.utils.Constant;
import com.sharad.epocket.utils.Utils;
import com.sharad.epocket.widget.recyclerview.StickyRecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Sharad on 28-Jul-16.
 */

public class AccountTransactionFragment extends Fragment {
    private StickyRecyclerView mRecyclerView;

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
        args.putInt(Constant.ARG_TAB_NUMBER_INT, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_account_transaction, container, false);

        Bundle args = getArguments();
        accountId = args.getLong(Constant.ARG_ACCOUNT_NUMBER_LONG, -1);
        int tabNum = args.getInt(Constant.ARG_TAB_NUMBER_INT, 0);

        DataSourceAccount dataSourceAccount = new DataSourceAccount(getContext());
        IAccount account = dataSourceAccount.getAccount(accountId);

        if(account != null) {
            ArrayList<ITransaction> itemList = new ArrayList<>();

            DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(getContext());

            Calendar now = Calendar.getInstance();
            now.add(Calendar.MONTH, -tabNum);
            long start_ms = Utils.getMonthStart_ms(now.getTimeInMillis());
            long end_ms = Utils.getMonthEnd_ms(now.getTimeInMillis());
            if(tabNum == 0) {
                end_ms = now.getTimeInMillis();
            }

            String where =      ContentConstant.KEY_TRANSACTION_ACCOUNT + "=" + account.getId()
                    + " AND " + ContentConstant.KEY_TRANSACTION_DATE    + ">=" + start_ms
                    + " AND " + ContentConstant.KEY_TRANSACTION_DATE    + "<=" + end_ms;

            dataSourceTransaction.getTransactions(itemList, where);
            final TransactionRecyclerAdapter adapter = new TransactionRecyclerAdapter(this.getActivity(), itemList);
            mRecyclerView = (StickyRecyclerView) view.findViewById(R.id.recyclerView);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setIndexer(adapter);
        }

        return view;
    }
}
