package com.sharad.epocket.accounts;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharad.epocket.R;
import com.sharad.epocket.database.ContentConstant;
import com.sharad.epocket.utils.Constant;
import com.sharad.epocket.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;

public class AccountOverviewYearFragment extends Fragment {
    IAccount iAccount = null;
    long timeInMillis;
    ArrayList<ITransaction> iTransactionArrayList = null;
    RecyclerView recyclerView = null;
    OverviewYearRecyclerAdapter recyclerAdapter = null;

    public AccountOverviewYearFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param accountId Account database id.
     * @param timeMs    Time in msec for the selected month.
     * @return A new instance of fragment AccountOverviewYearFragment.
     */
    public static AccountOverviewYearFragment newInstance(long accountId, long timeMs) {
        AccountOverviewYearFragment fragment = new AccountOverviewYearFragment();
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
                long start_ms = Utils.getYearStart_ms(timeInMillis);
                long end_ms = Utils.getYearEnd_ms(timeInMillis);
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
        View view = inflater.inflate(R.layout.fragment_account_overview_year, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new OverviewYearRecyclerAdapter(getContext(), iAccount.getIsoCurrency());
        recyclerView.setAdapter(recyclerAdapter);

        ArrayList<String> strings = new ArrayList<>();
        strings.add("JAN");
        strings.add("FEB");
        strings.add("MAR");
        strings.add("APR");
        strings.add("MAY");
        strings.add("JUN");
        strings.add("JUL");
        strings.add("AUG");
        strings.add("SEP");
        strings.add("OCT");
        strings.add("NOV");
        strings.add("DEC");

        recyclerAdapter.setItemList(strings, Calendar.getInstance().getTimeInMillis());

        return view;
    }

}
