package com.sharad.epocket.bills;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sharad.epocket.R;

import java.util.ArrayList;

/**
 * Created by Sharad on 23-Jun-16.
 */

public class BillsRecyclerAdapter extends RecyclerView.Adapter {
    private ArrayList<IBill> itemList;
    private OnMenuClickListener mMenuClickListener;

    public BillsRecyclerAdapter(ArrayList<IBill> itemList) {
        this.itemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_bill_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.amount.setText(itemList.get(position).getAmountString());
        holder.title.setText(itemList.get(position).getTitle());
        holder.date.setText(itemList.get(position).getNextDateString());
        holder.days.setText(itemList.get(position).getDaysString());
        holder.menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                boolean status = false;
                switch (item.getItemId()) {
                    case R.id.menu_item_edit:
                        if(mMenuClickListener != null) {
                            mMenuClickListener.onItemEditClicked(position);
                        }
                        status = true;
                        break;
                    case R.id.menu_item_delete:
                        if(mMenuClickListener != null) {
                            mMenuClickListener.onItemDeleteClicked(position);
                        }
                        status = true;
                        break;
                }
                return status;
            }
        });
    }

    @Override
    public int getItemCount() { return itemList.size(); }

    public void setOnMenuClickedListener(OnMenuClickListener listener) {
        mMenuClickListener = listener;
    }

    public IBill getItem(int position) {
        IBill item = null;
        if(position < itemList.size()) {
            item = itemList.get(position);
        }
        return item;
    }

    public void removeAt(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemList.size());
    }

    public interface OnMenuClickListener {
        void onItemEditClicked(int position);
        void onItemDeleteClicked(int position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private PopupMenu menu;
        public TextView amount;
        public TextView title;
        public TextView date;
        public TextView days;

        public ViewHolder(View itemView) {
            super(itemView);

            amount = (TextView) itemView.findViewById(R.id.bill_amount);
            title = (TextView) itemView.findViewById(R.id.bill_title);
            date = (TextView) itemView.findViewById(R.id.bill_date);
            days = (TextView) itemView.findViewById(R.id.bill_days_rem);

            ImageButton menuButton = (ImageButton) itemView.findViewById(R.id.contextMenu);
            menu = new PopupMenu(itemView.getContext(), menuButton, Gravity.CENTER);
            menu.inflate(R.menu.popup_menu);
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menu.show();
                }
            });
        }
    }
}
