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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.RecyclerItemClickListener;
import com.sharad.epocket.utils.SnappyRecyclerView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        setupRecyclerView(rootView);

        FloatingActionButton fabExpense = (FloatingActionButton) rootView.findViewById(R.id.fab_expense);
        fabExpense.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(AddTransactionDialogFragment.TRANSACTION_TYPE_EXPENSE);
            }
        });

        FloatingActionButton fabIncome = (FloatingActionButton) rootView.findViewById(R.id.fab_income);
        fabIncome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(AddTransactionDialogFragment.TRANSACTION_TYPE_INCOME);
            }
        });

        FloatingActionButton fabTransfer = (FloatingActionButton) rootView.findViewById(R.id.fab_transfer);
        fabTransfer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(AddTransactionDialogFragment.TRANSACTION_TYPE_TRANSFER);
            }
        });

        return rootView;
    }

    private void showDialog(int type) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = AddTransactionDialogFragment.newInstance(type);
        newFragment.show(ft, "dialog");
    }

    private void setupRecyclerView(View rootView) {
        SnappyRecyclerView recyclerView = (SnappyRecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setSnapEnabled(true, true);
        recyclerView.showIndicator(true);
        AccountsRecycler recyclerAdapter = new AccountsRecycler(createItemList());
        recyclerView.setAdapter(recyclerAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Toast.makeText(getActivity().getApplicationContext(), "Item clicked at " + position, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), AccountTransactionsActivity.class);
                        intent.putExtra("key", 1); //Optional parameters
                        getActivity().startActivity(intent);
                    }
                })
        );
    }

    private List<AccountItem> createItemList() {
        List<AccountItem> itemList = new ArrayList<>();
        DecimalFormat nf = new DecimalFormat("##,##,###");
        SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy");

        itemList.add(new AccountItem("Title01", "Title", R.drawable.ic_home_black_24dp, getResources().getColor(R.color.dark_palette00)));
        itemList.add(new AccountItem("Title02", "Bank Name", R.drawable.ic_home_black_24dp, getResources().getColor(R.color.dark_palette04)));
        itemList.add(new AccountItem("Title03", "Account Number", R.drawable.ic_home_black_24dp, getResources().getColor(R.color.dark_palette08)));
        itemList.add(new AccountItem("Title04", "Title", R.drawable.ic_home_black_24dp, getResources().getColor(R.color.dark_palette01)));
        itemList.add(new AccountItem("Title05", "Bank Name", R.drawable.ic_home_black_24dp, getResources().getColor(R.color.dark_palette09)));

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
