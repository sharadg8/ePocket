package com.sharad.epocket.accounts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Sharad on 12-Aug-16.
 */

public class OverviewYearRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_MONTH = R.layout.item_account_transaction_list_year;
    ArrayList<String> itemList;
    ArrayList<Integer> itemType;
    Context mContext;
    LayoutInflater mInflater;
    String mIsoCurrency;

    public OverviewYearRecyclerAdapter(Context context, String isoCurrency) {
        this.itemList = new ArrayList<>();
        this.itemType = new ArrayList<>();
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mIsoCurrency = isoCurrency;

        setHasStableIds(true);
    }

    public void setItemList(ArrayList<String> itemList, long selectedYear) {
        this.itemList.clear();
        this.itemType.clear();

        this.itemList.addAll(itemList);

        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = mInflater.inflate(viewType, parent, false /* attachToRoot */);
        int height = parent.getMeasuredHeight()/4;
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, height);
        int margin = Utils.dp2pix(4);
        params.setMargins(margin, margin, margin, margin);
        v.setLayoutParams(params);

        return new ViewHolderYear(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final String string = itemList.get(position);
        ((ViewHolderYear)viewHolder).bind(string);
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    @Override
    public long getItemId(int position) {
        if (itemList == null) {
            return RecyclerView.NO_ID;
        }
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_MONTH;
    }

    private class ViewHolderYear extends RecyclerView.ViewHolder {
        public ViewHolderYear(View v) {
            super(v);
        }

        public void bind(String string) {
            TextView title = (TextView)itemView.findViewById(R.id.title);
            title.setText(string);

            TextView income = (TextView)itemView.findViewById(R.id.income);
            income.setText(Utils.formatCurrencyDec(mIsoCurrency, 2600.24f));

            TextView expense = (TextView)itemView.findViewById(R.id.expense);
            expense.setText(Utils.formatCurrencyDec(mIsoCurrency, 260.24f));

            TextView transfer = (TextView)itemView.findViewById(R.id.transfer);
            transfer.setText(Utils.formatCurrencyDec(mIsoCurrency, 1260.24f));

            if(getAdapterPosition() == 1 || getAdapterPosition() == 5
                    || getAdapterPosition() == 3 || getAdapterPosition() == 10) {
                transfer.setVisibility(View.GONE);
            }

            if(getAdapterPosition() == 0 || getAdapterPosition() == 1 || getAdapterPosition() == 2
                    || getAdapterPosition() == 3 || getAdapterPosition() == 11) {
                int[] attrs = new int[] { android.R.attr.colorBackground };
                TypedArray ta = mContext.obtainStyledAttributes(attrs);
                int color = ta.getColor(0, Color.WHITE);
                ta.recycle();
                ((CardView)itemView).setCardBackgroundColor(color);
                ((CardView)itemView).setCardElevation(0);
                itemView.setClickable(false);
                income.setVisibility(View.GONE);
                expense.setVisibility(View.GONE);
                transfer.setVisibility(View.GONE);
            }
        }
    }
}
