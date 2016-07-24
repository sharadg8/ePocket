package com.sharad.epocket.accounts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharad.epocket.R;
import com.sharad.epocket.transaction.CategoryImageList;
import com.sharad.epocket.widget.AutofitRecyclerView;
import com.sharad.epocket.widget.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

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

        final List<Integer> itemList = new ArrayList<>();
        for(int i=0; i< CategoryImageList.imageResource.length; i++) {
            itemList.add(CategoryImageList.imageResource[i]);
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
        void onItemSelected(int tabNum, long categoryId);
    }
}
