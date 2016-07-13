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

package com.sharad.epocket.accounts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.CurrencyUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * This fragment inflates a layout with two Floating Action Buttons and acts as a listener to
 * changes on them.
 */
public class AccountsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AccountsFragment.OnFragmentInteractionListener mListener;

    private final static String TAG = "AccountsFragment";
    public final static String ITEM_ID_KEY = "AccountsFragment$idKey";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BudgetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountsFragment newInstance(String param1, String param2) {
        AccountsFragment fragment = new AccountsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_accounts, container, false);
        setHasOptionsMenu(true);

        setupRecyclerView(rootView);

        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_new_account).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_account:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(View rootView) {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        final AccountsRecyclerAdapter recyclerAdapter = new AccountsRecyclerAdapter(createItemList());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerAdapter);

        recyclerAdapter.setOnItemClickListener(new AccountsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onAddTransactionClicked(int position, AccountItem account) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AddTransactionActivity.class);
                intent.putExtra("KEY_ACCOUNT_ID", account.getId());
                getActivity().startActivityForResult(intent, 0);
            }

            @Override
            public void onEditAccountClicked(int position, AccountItem account) {

            }

            @Override
            public void onDeleteAccountClicked(final int position, final AccountItem account) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete - " + account.getTitle() + "?")
                        .setMessage("This will clear all the transactions of this account, can't be undone!!")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //source.deleteBill(account.getId());
                                recyclerAdapter.removeAt(position);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }

            @Override
            public void onViewTransactionClicked(int position, AccountItem account) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AccountTransactionsActivity.class);
                intent.putExtra("KEY_ACCOUNT_ID", account.getId());
                getActivity().startActivityForResult(intent, 0);
            }

            @Override
            public void onViewTrendsClicked(int position, AccountItem account) {

            }

            @Override
            public void onViewInfoClicked(int position, AccountItem account) {

            }
        });
    }

    private List<AccountItem> createItemList() {
        List<AccountItem> itemList = new ArrayList<>();

        itemList.add(new AccountItem(0, CurrencyUtils.CURRENCY_INDIAN_RUPEE, "Travel Card", "This is note", "009010101842643", "009207859",
                "Don'tKnow", 2084, 80.86f, 24.83f, 74.73f, AccountItem.ACCOUNT_TYPE_CASH_CARD, Calendar.getInstance()));

        itemList.add(new AccountItem(0, CurrencyUtils.CURRENCY_EURO, "Axis bank", "This is note", "009010101842643", "009207859",
                "Don'tKnow", 20894, 180.86f, 8724.83f, 89274.73f, AccountItem.ACCOUNT_TYPE_CASH_CARD, Calendar.getInstance()));

        itemList.add(new AccountItem(0, CurrencyUtils.CURRENCY_INDIAN_RUPEE, "SBI", "This is note", "009010101842643", "009207859",
                "Don'tKnow", 237894, 1804.86f, 87324.83f, 89274.73f, AccountItem.ACCOUNT_TYPE_CARD_ONLY, Calendar.getInstance()));

        itemList.add(new AccountItem(0, CurrencyUtils.CURRENCY_INDIAN_RUPEE, "PPF", "This is note", "009010101842643", "009207859",
                "Don'tKnow", 2087694, 0f, 80000, 0f, AccountItem.ACCOUNT_TYPE_CARD_ONLY, Calendar.getInstance()));

        itemList.add(new AccountItem(0, CurrencyUtils.CURRENCY_EURO, "AIB", "This is note", "009010101842643", "009207859",
                "Don'tKnow", 20894, 0f, 0f, 0f, AccountItem.ACCOUNT_TYPE_CARD_ONLY, Calendar.getInstance()));

        return itemList;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
