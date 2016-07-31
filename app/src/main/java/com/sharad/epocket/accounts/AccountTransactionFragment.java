package com.sharad.epocket.accounts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharad.epocket.R;
import com.sharad.epocket.database.ContentConstant;
import com.sharad.epocket.utils.Constant;
import com.sharad.epocket.utils.ScrollHandler;
import com.sharad.epocket.utils.Utils;
import com.sharad.epocket.widget.recyclerview.StickyRecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Sharad on 28-Jul-16.
 */

public class AccountTransactionFragment extends Fragment implements ScrollHandler {
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
            final TransactionRecyclerAdapter recyclerAdapter = new TransactionRecyclerAdapter(this.getActivity(), this);
            recyclerAdapter.setItemList(itemList);
            recyclerAdapter.setIsoCurrency(account.getIsoCurrency());
            mRecyclerView = (StickyRecyclerView) view.findViewById(R.id.recyclerView);
            mRecyclerView.setAdapter(recyclerAdapter);
            mRecyclerView.setIndexer(recyclerAdapter);

            recyclerAdapter.setOnItemClickListener(new TransactionRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onEditClicked(int position, ITransaction iTransaction) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), AddTransactionActivity.class);
                    intent.putExtra(Constant.ARG_ACCOUNT_NUMBER_LONG, iTransaction.getAccount());
                    intent.putExtra(Constant.ARG_TRANSACTION_NUMBER_LONG, iTransaction.getId());
                    startActivityForResult(intent, 230);
                }

                @Override
                public void onDeleteClicked(final int position, final ITransaction iTransaction) {
                    new AlertDialog.Builder(getContext())
                            .setMessage("Delete this transaction?")
                            .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    DataSourceTransaction source = new DataSourceTransaction(getContext());
                                    source.deleteTransaction(iTransaction.getId());
                                    recyclerAdapter.removeAt(position);
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .show();
                }
            });
        }

        return view;
    }

    @Override
    public void setSmoothScrollStableId(long stableId) {

    }

    @Override
    public void smoothScrollTo(int position) {
        mRecyclerView.getLayoutManager().scrollToPosition(position);
    }
}
