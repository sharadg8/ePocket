package com.sharad.epocket.accounts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.sharad.epocket.R;

import java.util.List;

/**
 * Created by Sharad on 06-Jul-16.
 */

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Integer> mItemList;

    public CategoryRecyclerAdapter(List<Integer> itemList) {
        mItemList = itemList;
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
        holder.button.setImageResource(mItemList.get(position).intValue());
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton button;
        public ViewHolder(final View parent) {
            super(parent);
            button = (ImageButton) parent.findViewById(R.id.image_button);
        }
    }
}
