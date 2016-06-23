package com.sharad.epocket.bills;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharad.epocket.R;

import java.util.List;

/**
 * Created by Sharad on 23-Jun-16.
 */

public class BillsRecyclerAdapter extends RecyclerView.Adapter {
    List<String> itemList;

    public BillsRecyclerAdapter(List<String> itemList) {
        this.itemList = itemList;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.bills_item_card, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

    }

    public int getItemCount() { return itemList.size(); }

    private class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }
}
