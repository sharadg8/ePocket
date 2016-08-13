package com.sharad.epocket.accounts;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

public class AccountOverviewMonthFragment extends Fragment implements ScrollHandler{
    IAccount iAccount = null;
    long timeInMillis;
    ArrayList<ITransaction> iTransactionArrayList = null;
    StickyRecyclerView recyclerView = null;
    OverviewMonthRecyclerAdapter recyclerAdapter = null;

    public AccountOverviewMonthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param accountId Account database id.
     * @param timeMs    Time in msec for the selected month.
     * @return A new instance of fragment AccountOverviewMonthFragment.
     */
    public static AccountOverviewMonthFragment newInstance(long accountId, long timeMs) {
        AccountOverviewMonthFragment fragment = new AccountOverviewMonthFragment();
        Bundle args = new Bundle();
        args.putLong(Constant.ARG_ACCOUNT_NUMBER_LONG, accountId);
        args.putLong(Constant.ARG_TIME_IN_MS_LONG, timeMs);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long accountId = Constant.INVALID_ID;
        timeInMillis = Calendar.getInstance().getTimeInMillis();

        if (getArguments() != null) {
            accountId = getArguments().getLong(Constant.ARG_ACCOUNT_NUMBER_LONG);
            timeInMillis = getArguments().getLong(Constant.ARG_TIME_IN_MS_LONG);
        }
        iTransactionArrayList = new ArrayList<>();

        if(accountId != Constant.INVALID_ID) {
            DataSourceAccount dataSourceAccount = new DataSourceAccount(getContext());
            iAccount = dataSourceAccount.getAccount(accountId);
            if (iAccount != null) {
                long start_ms = Utils.getMonthStart_ms(timeInMillis);
                long end_ms = Utils.getMonthEnd_ms(timeInMillis);
                String where = ContentConstant.KEY_TRANSACTION_ACCOUNT + "=" + iAccount.getId()
                        + " AND " + ContentConstant.KEY_TRANSACTION_DATE + ">=" + start_ms
                        + " AND " + ContentConstant.KEY_TRANSACTION_DATE + "<=" + end_ms;
                DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(getContext());
                dataSourceTransaction.getTransactions(iTransactionArrayList, where);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_overview_month, container, false);

        recyclerAdapter = new OverviewMonthRecyclerAdapter(getContext(), this, iAccount.getIsoCurrency());
        recyclerAdapter.setItemList(iTransactionArrayList, timeInMillis);

        recyclerView = (StickyRecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setIndexer(recyclerAdapter);
        recyclerView.setAdapter(recyclerAdapter);

        recyclerAdapter.setOnItemClickListener(new OverviewMonthRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onEditClicked(int position, ITransaction iTransaction) {
                if((iTransaction.getType() == ITransaction.TRANSACTION_TYPE_ACCOUNT_WITHDRAW)
                        || (iTransaction.getType() == ITransaction.TRANSACTION_TYPE_ACCOUNT_DEPOSIT)) {
                    showWithdrawDialog(iTransaction);
                } else {
                    Intent intent = new Intent(getContext(), AddTransactionActivity.class);
                    intent.putExtra(Constant.ARG_ACCOUNT_NUMBER_LONG, iTransaction.getAccount());
                    intent.putExtra(Constant.ARG_TRANSACTION_NUMBER_LONG, iTransaction.getId());
                    startActivityForResult(intent, Constant.REQ_EDIT_TRANSACTION);
                }
            }

            @Override
            public void onDeleteClicked(final int position, final ITransaction iTransaction) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Delete this transaction?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DataSourceTransaction source = new DataSourceTransaction(
                                        getContext());
                                source.deleteTransaction(iTransaction);
                                recyclerAdapter.removeAt(position);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case Constant.REQ_EDIT_TRANSACTION:
                long transactionId = data.getExtras().getLong(Constant.ARG_TRANSACTION_NUMBER_LONG, Constant.INVALID_ID);
                if (transactionId != Constant.INVALID_ID) {
                    // TODO update this to refresh the data.
                }
                break;
        }
    }

    private void showWithdrawDialog(ITransaction iTransaction) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(Constant.DLG_ACCOUNT_WITHDRAW);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        WithdrawDialogFragment newFragment = WithdrawDialogFragment.newInstance(iTransaction.getAccount(), iTransaction.getId());
        newFragment.setOnWithdrawDialogListener(new WithdrawDialogFragment.OnWithdrawDialogListener() {
            @Override
            public void onTransactionUpdated(ITransaction iTransaction) {
                // TODO update this to refresh the data.
            }
        });
        newFragment.show(ft, Constant.DLG_ACCOUNT_WITHDRAW);
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    @Override
    public void setSmoothScrollStableId(long stableId) {

    }

    @Override
    public void smoothScrollTo(int position) {
        recyclerView.getLayoutManager().scrollToPosition(position);
    }
}
