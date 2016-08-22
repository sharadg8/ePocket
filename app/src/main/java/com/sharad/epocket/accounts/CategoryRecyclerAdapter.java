package com.sharad.epocket.accounts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.Item;
import com.sharad.epocket.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Sharad on 06-Jul-16.
 */

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<Item> itemList;
    private OnItemClickListener itemClickListener;
    private int iconTint = -1;

    private static final int VIEW_TYPE_CATEGORY = R.layout.item_category_grid;
    private static final int VIEW_TYPE_TRANSFER = R.layout.item_transfer_account_grid;

    public CategoryRecyclerAdapter(ArrayList<ICategory> itemList) {
        this.itemList = new ArrayList<>();
        this.itemList.addAll(itemList);
    }

    public CategoryRecyclerAdapter(ArrayList<IAccount> itemList, boolean isTransfer) {
        this.itemList = new ArrayList<>();
        this.itemList.addAll(itemList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        final View v = LayoutInflater.from(context).inflate(viewType, parent, false /* attachToRoot */);
        if (viewType == VIEW_TYPE_CATEGORY) {
            return new CategoryViewHolder(v);
        } else {
            return new TransferViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case VIEW_TYPE_CATEGORY:
                CategoryViewHolder categoryViewHolder = (CategoryViewHolder) viewHolder;
                ICategory iCategory = (ICategory)itemList.get(position);
                categoryViewHolder.button.setImageResource(iCategory.getImageResource());
                if(iconTint != -1) {
                    categoryViewHolder.button.setColorFilter(iconTint);
                }
                break;
            case VIEW_TYPE_TRANSFER:
                TransferViewHolder transferViewHolder = (TransferViewHolder) viewHolder;
                IAccount iAccount = (IAccount) itemList.get(position);
                transferViewHolder.icon.setImageResource(iAccount.getImageResource());
                transferViewHolder.title.setText(iAccount.getTitle());
                transferViewHolder.currency.setText(Utils.getCurrencySymbol(iAccount.getIsoCurrency()));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(itemList.get(position) instanceof IAccount) {
            return VIEW_TYPE_TRANSFER;
        } else {
            return VIEW_TYPE_CATEGORY;
        }
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    public void setIconTint(int iconTint) {
        this.iconTint = iconTint;
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        public ImageButton button;
        public CategoryViewHolder(final View parent) {
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

    class TransferViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView currency;
        public TextView title;

        public TransferViewHolder(final View parent) {
            super(parent);
            icon = (ImageView) parent.findViewById(R.id.icon);
            title = (TextView) parent.findViewById(R.id.title);
            currency = (TextView) parent.findViewById(R.id.currency);
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
