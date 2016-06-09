/*
 * Copyright 2014, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sharad.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sharad.common.RecyclerItemClickListener;
import com.sharad.common.SnappyRecyclerView;
import com.sharad.epocket.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * This fragment inflates a layout with two Floating Action Buttons and acts as a listener to
 * changes on them.
 */
public class AccountsFragment extends Fragment {
    private final static String TAG = "AccountsFragment";
    public final static String ITEM_ID_KEY = "AccountsFragment$idKey";

    public static AccountsFragment createInstance(long id) {
        AccountsFragment fragment = new AccountsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ITEM_ID_KEY, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.feed_list, container, false);

        SnappyRecyclerView recyclerView = (SnappyRecyclerView) rootView.findViewById(R.id.recyclerView);
        setupRecyclerView(recyclerView);
        return rootView;
    }

    private void setupRecyclerView(SnappyRecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setSnapEnabled(true, true);
        AccountsRecycler recyclerAdapter = new AccountsRecycler(createItemList());
        recyclerView.setAdapter(recyclerAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getActivity().getApplicationContext(), "Item clicked at " + position, Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }

    private List<AccountItem> createItemList() {
        List<AccountItem> itemList = new ArrayList<>();
        DecimalFormat nf = new DecimalFormat("##,##,###");
        SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy");

        itemList.add(new AccountItem("Title01", "Title", R.mipmap.ic_beach_access_black_24dp, getResources().getColor(R.color.dark_palette00)));
        itemList.add(new AccountItem("Title02", "Bank Name", R.mipmap.ic_child_friendly_black_24dp, getResources().getColor(R.color.dark_palette04)));
        itemList.add(new AccountItem("Title03", "Account Number", R.mipmap.ic_flight_black_24dp, getResources().getColor(R.color.dark_palette08)));
        itemList.add(new AccountItem("Title04", "Title", R.mipmap.ic_beach_access_black_24dp, getResources().getColor(R.color.dark_palette01)));
        itemList.add(new AccountItem("Title05", "Bank Name", R.mipmap.ic_child_friendly_black_24dp, getResources().getColor(R.color.dark_palette09)));

        return itemList;
    }
}
