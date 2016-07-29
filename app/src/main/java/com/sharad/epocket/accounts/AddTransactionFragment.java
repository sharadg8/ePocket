package com.sharad.epocket.accounts;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharad.epocket.R;
import com.sharad.epocket.database.ContentConstant;
import com.sharad.epocket.widget.AutofitRecyclerView;

import java.util.ArrayList;

/**
 * Created by Sharad on 09-Jul-16.
 */

public class AddTransactionFragment extends Fragment {
    private int transactionType;
    private OnItemSelectedListener itemSelectedListener;
    private AutofitRecyclerView autofitRecyclerView;
    private CategoryRecyclerAdapter categoryRecyclerAdapter;

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
        autofitRecyclerView = (AutofitRecyclerView)rootView.findViewById(R.id.recyclerView);

        Bundle args = getArguments();
        transactionType = args.getInt(ARG_TRANSACTION_TYPE, 0);

        final ArrayList<ICategory> itemList = new ArrayList<>();
        final DataSourceCategory source = new DataSourceCategory(getActivity());

        switch (transactionType) {
            case ICategory.CATEGORY_TYPE_EXPENSE:
                String where = ContentConstant.KEY_CATEGORY_TYPE + "=" + ICategory.CATEGORY_TYPE_EXPENSE;

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
                itemList.add(new ICategory(CategoryImageList.imageResource.length-1));
                break;
            case ICategory.CATEGORY_TYPE_INCOME:
                where = ContentConstant.KEY_CATEGORY_TYPE + "=" + ICategory.CATEGORY_TYPE_INCOME;

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
                itemList.add(new ICategory(CategoryImageList.imageResource.length-1));
                break;
            case ICategory.CATEGORY_TYPE_TRANSFER:
                break;
        }

        categoryRecyclerAdapter = new CategoryRecyclerAdapter(itemList);
        categoryRecyclerAdapter.setOnItemClickListener(new CategoryRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(position == (itemList.size() - 1)) {
                    showCategoryDialog(AddCategoryDialogFragment.INVALID_ID);
                } else {
                    if (itemSelectedListener != null) {
                        itemSelectedListener.onItemSelected(transactionType, itemList.get(position));
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                showCategoryDialog(itemList.get(position).getId());
            }
        });
        autofitRecyclerView.setAdapter(categoryRecyclerAdapter);
        return rootView;
    }

    void showCategoryDialog(long id) {
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
        AddCategoryDialogFragment newFragment = AddCategoryDialogFragment.newInstance(id, transactionType);
        newFragment.setOnCategoryDialogListener(new AddCategoryDialogFragment.OnCategoryDialogListener() {
            @Override
            public void onCategoryDialogFinish(ICategory category) {
                boolean found = false;
                for(int i=0; i<categoryRecyclerAdapter.itemList.size(); i++) {
                    if(categoryRecyclerAdapter.itemList.get(i).getId() == category.getId()) {
                        categoryRecyclerAdapter.itemList.get(i).setTitle(category.getTitle());
                        categoryRecyclerAdapter.itemList.get(i).setImageIndex(category.getImageIndex());
                        categoryRecyclerAdapter.itemList.get(i).setColor(category.getColor());
                        found = true;
                        break;
                    }
                }
                if(!found) {
                    categoryRecyclerAdapter.itemList.add(categoryRecyclerAdapter.itemList.size()-1, category);
                }
                categoryRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCategoryDialogRemove(long id) {
                for(int i=0; i<categoryRecyclerAdapter.itemList.size(); i++) {
                    if(categoryRecyclerAdapter.itemList.get(i).getId() == id) {
                        categoryRecyclerAdapter.itemList.remove(i);
                        categoryRecyclerAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        });
        newFragment.show(ft, "dialog");
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        itemSelectedListener = listener;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int tabNum, ICategory category);
    }
}
