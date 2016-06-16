package com.sharad.epocket;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharad.utils.DrawerButton;

import java.util.List;

/**
 * Created by Sharad on 16-Jun-16.
 */

public class DrawerGridRecycler extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NavigationDrawerItem> itemList;
    private Context context;

    public DrawerGridRecycler(Context context, List<NavigationDrawerItem> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_drawer_item, null);
        DrawerGridViewHolder rcv = new DrawerGridViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        DrawerGridViewHolder holder = (DrawerGridViewHolder) viewHolder;
        holder.button.setText(itemList.get(position).getName());
        holder.button.setImageResource(itemList.get(position).getIcon());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    private class DrawerGridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public DrawerButton button;

        public DrawerGridViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            button = (DrawerButton)itemView.findViewById(R.id.navigation_button);
        }

        @Override
        public void onClick(View view) {
            //Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}
