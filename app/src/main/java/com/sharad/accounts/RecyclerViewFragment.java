package com.sharad.accounts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharad.epocket.R;
import com.sharad.utils.viewpager.HollyViewPagerBus;

/**
 * Created by Sharad on 13-Jun-16.
 */

public class RecyclerViewFragment extends Fragment {
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transactions_recyclerview, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new TransactionsRecycler());

        HollyViewPagerBus.registerRecyclerView(getActivity(), recyclerView);
    }
}
