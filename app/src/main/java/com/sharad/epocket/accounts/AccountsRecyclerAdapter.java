package com.sharad.epocket.accounts;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.ZigZagCardView;

import java.util.List;

/**
 * Created by Sharad on 12-Sep-15.
 */
public class AccountsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AccountItem> itemList;
    private OnItemClickListener itemClickListener;

    public AccountsRecyclerAdapter(List<AccountItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_account_list, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        DetailViewHolder holder = (DetailViewHolder) viewHolder;
        holder.addTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null) {
                    itemClickListener.onAddTransactionClicked(position, itemList.get(position));
                }
            }
        });
        holder.editAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null) {
                    itemClickListener.onEditAccountClicked(position, itemList.get(position));
                }
            }
        });
        holder.deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null) {
                    itemClickListener.onDeleteAccountClicked(position, itemList.get(position));
                }
            }
        });
        holder.viewTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null) {
                    itemClickListener.onViewTransactionClicked(position, itemList.get(position));
                }
            }
        });
        holder.viewTrends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null) {
                    itemClickListener.onViewTrendsClicked(position, itemList.get(position));
                }
            }
        });
        holder.viewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null) {
                    itemClickListener.onViewInfoClicked(position, itemList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    public void removeAt(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemList.size());
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onAddTransactionClicked(int position, AccountItem account);
        void onEditAccountClicked(int position, AccountItem account);
        void onDeleteAccountClicked(int position, AccountItem account);
        void onViewTransactionClicked(int position, AccountItem account);
        void onViewTrendsClicked(int position, AccountItem account);
        void onViewInfoClicked(int position, AccountItem account);
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        public FloatingActionButton addTransaction;
        public ImageButton viewTransaction;
        public ImageButton viewTrends;
        public ImageButton viewInfo;
        public ImageButton editAccount;
        public ImageButton deleteAccount;

        public DetailViewHolder(final View parent) {
            super(parent);
            final ZigZagCardView expandedItems = (ZigZagCardView) parent.findViewById(R.id.expanded_items);
            final ImageButton expandMore = (ImageButton) parent.findViewById(R.id.expand_more);
            expandMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(expandedItems.getVisibility() == View.GONE) {
                        expandedItems.setVisibility(View.VISIBLE);
                        expandMore.setImageResource(R.drawable.ic_expand_less_black_24dp);
                    } else {
                        expandedItems.setVisibility(View.GONE);
                        expandMore.setImageResource(R.drawable.ic_expand_more_black_24dp);
                    }
                }
            });

            addTransaction = (FloatingActionButton) parent.findViewById(R.id.add_transaction);
            viewTransaction = (ImageButton) parent.findViewById(R.id.account_transactions);
            viewTrends = (ImageButton) parent.findViewById(R.id.account_trends);
            viewInfo = (ImageButton) parent.findViewById(R.id.account_info);
            editAccount = (ImageButton) parent.findViewById(R.id.account_edit);
            deleteAccount = (ImageButton) parent.findViewById(R.id.account_delete);
        }
    }
}
