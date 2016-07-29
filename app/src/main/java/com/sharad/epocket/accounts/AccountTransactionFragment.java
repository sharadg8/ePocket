package com.sharad.epocket.accounts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharad.epocket.R;
import com.sharad.epocket.widget.recyclerview.StickyRecyclerView;

/**
 * Created by Sharad on 28-Jul-16.
 */

public class AccountTransactionFragment extends Fragment {
    private StickyRecyclerView mRecyclerView;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public AccountTransactionFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AccountTransactionFragment newInstance(int sectionNumber) {
        AccountTransactionFragment fragment = new AccountTransactionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_account_transaction, container, false);

        final TransactionRecyclerAdapter adapter = new TransactionRecyclerAdapter(this.getActivity());
        mRecyclerView = (StickyRecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setIndexer(adapter);

        return view;
    }
}
