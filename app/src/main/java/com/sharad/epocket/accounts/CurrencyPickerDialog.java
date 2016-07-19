package com.sharad.epocket.accounts;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.sharad.epocket.R;
import com.sharad.epocket.widget.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Sharad on 18-Jul-16.
 */

public class CurrencyPickerDialog extends DialogFragment {
    private Set<Currency> currencies;
    private List<CurrencyItem> selectedCurrencies;
    private CurrencyRecyclerAdapter recyclerAdapter;
    private OnCurrencySelectedListener listener;

    public interface OnCurrencySelectedListener {
        void onCurrencySelected(Currency currency);
    }

    public void setOnCurrencySelectedListener(OnCurrencySelectedListener listener) {
        this.listener = listener;
    }

    public CurrencyPickerDialog() {
        super();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddBillDialogFragment.
     */
    public static CurrencyPickerDialog newInstance() {
        CurrencyPickerDialog fragment = new CurrencyPickerDialog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_MinWidth);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.dialog_currency_picker, container, false);

        selectedCurrencies = new ArrayList<>();
        currencies = Currency.getAvailableCurrencies();
        for (Iterator<Currency> it = currencies.iterator(); it.hasNext();) {
            Currency currency = it.next();
            selectedCurrencies.add(new CurrencyItem(currency.getCurrencyCode(),
                    currency.getDisplayName(), currency.getSymbol()));
        }

        EditText searchEditText = (EditText) rootView.findViewById(R.id.currency_picker_search);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.currency_picker_recycler_view);
        recyclerAdapter = new CurrencyRecyclerAdapter(selectedCurrencies);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(listener != null) {
                    listener.onCurrencySelected(Currency.getInstance(selectedCurrencies.get(position).isoCode));
                }
                dismiss();
            }
        }));

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString());
            }
        });
        return rootView;
    }

    private void search(String s) {
        selectedCurrencies.clear();
        for (Iterator<Currency> it = currencies.iterator(); it.hasNext();) {
            Currency currency = it.next();
            if((currency.getDisplayName().toLowerCase(Locale.ENGLISH).contains(s.toLowerCase()))
            || (currency.getSymbol().toLowerCase(Locale.ENGLISH).contains(s.toLowerCase()))){
                selectedCurrencies.add(new CurrencyItem(currency.getCurrencyCode(),
                        currency.getDisplayName(), currency.getSymbol()));
            }
        }
        recyclerAdapter.notifyDataSetChanged();
    }

    private class CurrencyItem {
        public String isoCode;
        public String name;
        public String symbol;

        public CurrencyItem(String isoCode, String name, String symbol) {
            this.isoCode = isoCode;
            this.name = name;
            this.symbol = symbol;
        }
    }

    private class CurrencyRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<CurrencyItem> itemList;
        public CurrencyRecyclerAdapter(List<CurrencyItem> itemList) {
            this.itemList = itemList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.item_currency_picker, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
            ViewHolder holder = (ViewHolder) viewHolder;
            final CurrencyItem currency = itemList.get(position);
            holder.currency_title.setText(currency.name);
            holder.currency_symbol.setText(currency.symbol);
        }

        @Override
        public int getItemCount() {
            return itemList == null ? 0 : itemList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView currency_symbol;
            public TextView currency_title;

            public ViewHolder(final View parent) {
                super(parent);
                currency_symbol = (TextView) parent.findViewById(R.id.currency_symbol);
                currency_title = (TextView) parent.findViewById(R.id.currency_title);
            }
        }
    }
}
