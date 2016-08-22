package com.sharad.epocket.accounts;

import android.animation.ArgbEvaluator;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Sharad on 12-Aug-16.
 */

public class OverviewYearRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    OnItemClickListener itemClickListener = null;
    private static final int VIEW_TYPE_MONTH = R.layout.item_account_transaction_list_year;
    ArrayList<MonthItem> itemList;
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

    public void setItemList(ArrayList<MonthItem> itemList) {
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
        final MonthItem monthItem = itemList.get(position);
        ((ViewHolderYear)viewHolder).bind(monthItem);
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onClick(View view, long timeInMillis);
    }

    private class ViewHolderYear extends RecyclerView.ViewHolder {
        public ViewHolderYear(View v) {
            super(v);
        }

        public void bind(final MonthItem monthItem) {
            TextView title = (TextView)itemView.findViewById(R.id.title);
            title.setText(monthItem.getDateString());

            TextView income = (TextView)itemView.findViewById(R.id.income);
            income.setText(Utils.formatCurrencyDec(mIsoCurrency, monthItem.getIncome()));

            TextView expense = (TextView)itemView.findViewById(R.id.expense);
            expense.setText(Utils.formatCurrencyDec(mIsoCurrency, monthItem.getExpense()));

            TextView transfer = (TextView)itemView.findViewById(R.id.transfer);
            if(Math.abs(monthItem.getTransfer()) > 0.01f) {
                transfer.setText(Utils.formatCurrencyDec(mIsoCurrency, monthItem.getTransfer()));
            } else {
                transfer.setVisibility(View.GONE);
            }

            if(monthItem.isActive() == false) {
                int[] attrs = new int[] { android.R.attr.colorBackground };
                TypedArray ta = mContext.obtainStyledAttributes(attrs);
                int color = ta.getColor(0, Color.WHITE);
                ta.recycle();
                color = (int) new ArgbEvaluator().evaluate(0.5f, color, Color.WHITE);
                ((CardView)itemView).setCardBackgroundColor(color);
                ((CardView)itemView).setCardElevation(0);
                ((CardView)itemView).setForeground(null);
                itemView.setClickable(false);
                itemView.setFocusable(false);
                income.setVisibility(View.GONE);
                expense.setVisibility(View.GONE);
                transfer.setVisibility(View.GONE);
                View empty = itemView.findViewById(R.id.empty);
                empty.setVisibility(View.VISIBLE);
            }

            if(Utils.isThisMonth(monthItem.getTimeInMillis())) {
                itemView.findViewById(R.id.thisMonth).setVisibility(View.VISIBLE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if((itemClickListener != null) && monthItem.isActive()) {
                        itemClickListener.onClick(itemView, monthItem.getTimeInMillis());
                    }
                }
            });
        }
    }

    public static class MonthItem {
        float expense;
        float income;
        float transfer;
        boolean active = false;
        static SimpleDateFormat df = new SimpleDateFormat("MMM");
        Calendar date = Calendar.getInstance();

        public MonthItem(long timeInMillis) {
            this.date.setTimeInMillis(timeInMillis);
        }

        public MonthItem(long timeInMillis, float income, float expense, float transfer) {
            this(timeInMillis);
            this.income = income;
            this.expense = expense;
            this.transfer = transfer;
            this.active = true;
        }

        public void setExpense(float expense) {
            this.expense = expense;
            this.active = true;
        }

        public void setIncome(float income) {
            this.income = income;
            this.active = true;
        }

        public void setTransfer(float transfer) {
            this.transfer = transfer;
            this.active = true;
        }

        public float getExpense() { return expense; }
        public float getIncome() { return income; }
        public float getTransfer() { return transfer;}
        public String getDateString() { return df.format(date.getTime()); }
        public long getTimeInMillis() { return date.getTimeInMillis(); }
        public boolean isActive() { return active; }
    }
}
