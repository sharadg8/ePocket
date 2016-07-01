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
    ArrayList<BillItem> itemList;

    public BillsRecyclerAdapter(ArrayList<BillItem> itemList) {
        this.itemList = itemList;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.bills_item_card, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.amount.setText(itemList.get(position).getAmountString());
        holder.title.setText(itemList.get(position).getTitle());
        holder.date.setText(itemList.get(position).getNextDateString());
        holder.days.setText(itemList.get(position).getDaysString());
    }

    public int getItemCount() { return itemList.size(); }

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
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return false;
                }
            });

            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menu.show();
                }
            });
        }
    }
}
