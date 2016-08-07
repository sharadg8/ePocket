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

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.BaseFragment;
import com.sharad.epocket.utils.Constant;
import com.sharad.epocket.utils.ScrollHandler;

import java.util.ArrayList;
import java.util.Collections;


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
    private long mSelectedAccountId = Constant.INVALID_ID;
    private long mDefaultAccountId = Constant.INVALID_ID;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AccountsFragment.OnFragmentInteractionListener mListener;

    private final static String TAG = "AccountsFragment";

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
        intent.putExtra(Constant.ARG_ACCOUNT_NUMBER_LONG, mSelectedAccountId);
        intent.putExtra(Constant.ARG_TRANSACTION_NUMBER_LONG, Constant.INVALID_ID);
        startActivityForResult(intent, Constant.REQ_ADD_TRANSACTION);
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
                intent.putExtra(Constant.ARG_ACCOUNT_NUMBER_LONG, Constant.INVALID_ID);
                startActivityForResult(intent, Constant.REQ_ADD_ACCOUNT);
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
                intent.putExtra(Constant.ARG_ACCOUNT_NUMBER_LONG, account.getId());
                startActivityForResult(intent, Constant.REQ_EDIT_ACCOUNT);
            }

            @Override
            public void onDeleteAccountClicked(final int position, final IAccount account) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete - " + account.getTitle() + "?")
                        .setMessage("This will clear all the transactions of this account, can't be undone!!")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                AccountManager manager = AccountManager.getInstance();
                                manager.deleteAccount(getContext(), account.getId());
                                recyclerAdapter.removeAt(position);
                                AccountManager.getInstance().updateAccountListOrder(getContext(),
                                        recyclerAdapter.getItemList());
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }

            @Override
            public void onViewTransactionClicked(int position, IAccount account) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AccountTransactionActivity.class);
                intent.putExtra(Constant.ARG_ACCOUNT_NUMBER_LONG, account.getId());
                startActivityForResult(intent, Constant.REQ_LIST_TRANSACTION);
            }

            @Override
            public void onAccountClicked(long accountId) {
                mSelectedAccountId = (accountId != Constant.INVALID_ID) ? accountId : mDefaultAccountId;
            }

            @Override
            public void onViewInfoClicked(int position, IAccount account) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AccountOverviewActivity.class);
                intent.putExtra(Constant.ARG_ACCOUNT_NUMBER_LONG, account.getId());
                startActivityForResult(intent, Constant.REQ_LIST_TRANSACTION);
            }

            @Override
            public void onWithdrawClicked(int position, IAccount account) {
                showWithdrawDialog(account);
            }
        });
    }

    private void showWithdrawDialog(IAccount account) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(Constant.DLG_ACCOUNT_WITHDRAW);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        WithdrawDialogFragment newFragment = WithdrawDialogFragment.newInstance(account.getId(), Constant.INVALID_ID);
        newFragment.setOnWithdrawDialogListener(new WithdrawDialogFragment.OnWithdrawDialogListener() {
            @Override
            public void onTransactionUpdated(ITransaction iTransaction) {
                onActivityResult(Constant.REQ_ADD_TRANSACTION, Activity.RESULT_OK, null);
            }
        });
        newFragment.show(ft, Constant.DLG_ACCOUNT_WITHDRAW);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_CANCELED) {
            return;
        }

        long accountId;
        switch (requestCode) {
            case Constant.REQ_ADD_ACCOUNT:
                accountId = data.getExtras().getLong(Constant.ARG_ACCOUNT_NUMBER_LONG, Constant.INVALID_ID);
                if (accountId != Constant.INVALID_ID) {
                    DataSourceAccount source = new DataSourceAccount(getContext());
                    IAccount iAccount = source.getAccount(accountId);
                    int position = recyclerAdapter.getItemCount();
                    recyclerAdapter.getItemList().add(iAccount);
                    recyclerAdapter.notifyItemInserted(position);
                    smoothScrollTo(position);
                    AccountManager.getInstance().updateAccountListOrder(getContext(), recyclerAdapter.getItemList());
                }
                break;
            case Constant.REQ_EDIT_ACCOUNT:
                accountId = data.getExtras().getLong(Constant.ARG_ACCOUNT_NUMBER_LONG, Constant.INVALID_ID);
                if (accountId != Constant.INVALID_ID) {
                    DataSourceAccount source = new DataSourceAccount(getContext());
                    IAccount iAccount = source.getAccount(accountId);
                    int position = 0;
                    for(IAccount account : recyclerAdapter.getItemList()) {
                        if(account.getId() == iAccount.getId()) {
                            recyclerAdapter.getItemList().set(position, iAccount);
                            recyclerAdapter.notifyItemChanged(position);
                            smoothScrollTo(position);
                            break;
                        }
                        position++;
                    }
                }
                break;
            case Constant.REQ_ADD_TRANSACTION:
            case Constant.REQ_LIST_TRANSACTION:
                DataSourceAccount source = new DataSourceAccount(getContext());
                IAccount iAccount = source.getAccount(mSelectedAccountId);
                if(iAccount != null) {
                    int position = 0;
                    for (IAccount account : recyclerAdapter.getItemList()) {
                        if (account.getId() == iAccount.getId()) {
                            AccountManager manager = AccountManager.getInstance();
                            iAccount.setBalanceCash(manager.getAccountBalanceCash(getContext(), iAccount));
                            iAccount.setBalanceCard(manager.getAccountBalanceCard(getContext(), iAccount));
                            iAccount.setInflow(manager.getAccountInflow(getContext(), iAccount));
                            iAccount.setOutflow(manager.getAccountOutflow(getContext(), iAccount));
                            recyclerAdapter.getItemList().set(position, iAccount);
                            recyclerAdapter.notifyItemChanged(position);
                            smoothScrollTo(position);
                            break;
                        }
                        position++;
                    }
                }
                break;
        }
    }

    private ArrayList<IAccount> createItemList() {
        ArrayList<IAccount> iAccountArrayList = new ArrayList<>();

        DataSourceAccount dataSourceAccount = new DataSourceAccount(getContext());
        dataSourceAccount.getAccounts(iAccountArrayList);

        Collections.sort(iAccountArrayList, new IAccount.iComparator());

        for (IAccount iAccount : iAccountArrayList) {
            AccountManager manager = AccountManager.getInstance();
            iAccount.setBalanceCash(manager.getAccountBalanceCash(getContext(), iAccount));
            iAccount.setBalanceCard(manager.getAccountBalanceCard(getContext(), iAccount));
            iAccount.setInflow(manager.getAccountInflow(getContext(), iAccount));
            iAccount.setOutflow(manager.getAccountOutflow(getContext(), iAccount));
        }

        if(iAccountArrayList.size() > 0) {
            mDefaultAccountId = iAccountArrayList.get(0).getId();
            mSelectedAccountId = mDefaultAccountId;
        }
        return iAccountArrayList;
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

    }

    @Override
    public void smoothScrollTo(int position) {
        mLayoutManager.scrollToPositionWithOffset(position, 12);
    }
}
