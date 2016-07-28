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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.BaseFragment;
import com.sharad.epocket.utils.ScrollHandler;

import java.util.ArrayList;



/**
 * This fragment inflates a layout with two Floating Action Buttons and acts as a listener to
 * changes on them.
 */
public class AccountsFragment extends BaseFragment implements ScrollHandler {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    AccountsRecyclerAdapter recyclerAdapter;
    private LinearLayoutManager mLayoutManager;
    private long mScrollToAccountId = IAccount.INVALID_ID;
    private ItemTouchHelper mItemTouchHelper;

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
    public void onFabClick(View view) {
        Intent intent = new Intent(getContext(), AddTransactionActivity.class);
        intent.putExtra("KEY_ACCOUNT_ID", 0);
        startActivityForResult(intent, 0);
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
                Intent intent = new Intent(getActivity().getApplicationContext(), AddAccountActivity.class);
                intent.putExtra("AddAccountActivityKeyAccountId", -1);
                startActivityForResult(intent, 210);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(View rootView) {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerAdapter = new AccountsRecyclerAdapter(getContext(), createItemList(), this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);

        recyclerAdapter.setOnItemClickListener(new AccountsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onEditAccountClicked(int position, IAccount account) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AddAccountActivity.class);
                intent.putExtra("AddAccountActivityKeyAccountId", account.getId());
                startActivityForResult(intent, 210);
            }

            @Override
            public void onDeleteAccountClicked(final int position, final IAccount account) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete - " + account.getTitle() + "?")
                        .setMessage("This will clear all the transactions of this account, can't be undone!!")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DataSourceAccount source = new DataSourceAccount(getActivity());
                                source.deleteAccount(account.getId());
                                recyclerAdapter.removeAt(position);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }

            @Override
            public void onViewTransactionClicked(int position, IAccount account) {
                /*Intent intent = new Intent(getActivity().getApplicationContext(), Activity.class);
                intent.putExtra("KEY_ACCOUNT_ID", account.getId());
                startActivityForResult(intent, 220);*/
            }

            @Override
            public void onViewTrendsClicked(int position, IAccount account) {

            }

            @Override
            public void onViewInfoClicked(int position, IAccount account) {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == 210 ) {
            long accountId = data.getExtras().getLong("AddAccountActivityKeyAccountId", -1);
            if(accountId != -1) {
                DataSourceAccount source = new DataSourceAccount(getActivity());
                source.getAccounts(recyclerAdapter.getItemList());
                recyclerAdapter.notifyDataSetChanged();
            }
        }
    }

    private ArrayList<IAccount> createItemList() {
        ArrayList<IAccount> itemList = new ArrayList<>();

        DataSourceAccount source = new DataSourceAccount(getActivity());
        source.getAccounts(itemList);

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

    @Override
    public void setSmoothScrollStableId(long stableId) {
        mScrollToAccountId = stableId;
    }

    @Override
    public void smoothScrollTo(int position) {
        mLayoutManager.scrollToPositionWithOffset(position, 20);
    }
}
