package com.sharad.epocket.accounts;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharad.epocket.R;
import com.sharad.epocket.widget.AutofitRecyclerView;
import com.sharad.epocket.widget.RecyclerItemClickListener;

import java.util.ArrayList;

/**
 * Created by Sharad on 09-Jul-16.
 */

public class AddTransactionFragment extends Fragment {
    private int transactionType;
    private OnItemSelectedListener itemSelectedListener;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_TRANSACTION_TYPE = "transaction_type";

    public AddTransactionFragment() { }
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AddTransactionFragment newInstance(int transactionType) {
        AddTransactionFragment fragment = new AddTransactionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TRANSACTION_TYPE, transactionType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_transaction, container, false);
        AutofitRecyclerView recyclerView = (AutofitRecyclerView)rootView.findViewById(R.id.recyclerView);

        Bundle args = getArguments();
        transactionType = args.getInt(ARG_TRANSACTION_TYPE, 0);

        final ArrayList<ICategory> itemList = new ArrayList<>();
        final DataSourceCategory source = new DataSourceCategory(getActivity());

        switch (transactionType) {
            case ICategory.CATEGORY_TYPE_EXPENSE:
                String where = DataSourceCategory.KEY_CATEGORY_TYPE + "=" + ICategory.CATEGORY_TYPE_EXPENSE;

                source.getCategories(itemList, where);
                if(itemList.size() == 0) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Add Default Expense Categories?")
                            .setMessage("No expense categories found in the database. " +
                                    "Let us add some defaults, you could edit them later")
                            .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ICategory.getDefaultExpenseCategories(getContext(), itemList);
                                    for(ICategory c : itemList) {
                                        c.setId(source.insertCategory(c));
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .show();
                }
                break;
            case ICategory.CATEGORY_TYPE_INCOME:
                where = DataSourceCategory.KEY_CATEGORY_TYPE + "=" + ICategory.CATEGORY_TYPE_INCOME;

                source.getCategories(itemList, where);
                if(itemList.size() == 0) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Add Default Income Categories?")
                            .setMessage("No income categories found in the database. " +
                                    "Let us add some defaults, you could edit them later")
                            .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ICategory.getDefaultIncomeCategories(getContext(), itemList);
                                    for(ICategory c : itemList) {
                                        c.setId(source.insertCategory(c));
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .show();
                }
                break;
            case ICategory.CATEGORY_TYPE_TRANSFER:
                break;
        }

        CategoryRecyclerAdapter rcAdapter = new CategoryRecyclerAdapter(itemList);
        recyclerView.setAdapter(rcAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if(itemSelectedListener != null) {
                            itemSelectedListener.onItemSelected(transactionType, itemList.get(position));
                        }
                    }
                })
        );

        return rootView;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        itemSelectedListener = listener;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int tabNum, ICategory category);
    }
}
