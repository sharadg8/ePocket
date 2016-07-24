package com.sharad.epocket.accounts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.sharad.epocket.R;

import java.util.ArrayList;

/**
 * Created by Sharad on 06-Jul-16.
 */

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<ICategory> itemList;
    private OnItemClickListener itemClickListener;

    public CategoryRecyclerAdapter(ArrayList<ICategory> itemList) {
        this.itemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.button.setImageResource(itemList.get(position).getImageResource());
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton button;
        public ViewHolder(final View parent) {
            super(parent);
            button = (ImageButton) parent.findViewById(R.id.image_button);

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(itemClickListener != null) {
                        itemClickListener.onItemLongClick(v, getAdapterPosition());
                        return true;
                    }
                    return false;
                }
            });

            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener != null) {
                        itemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
}
