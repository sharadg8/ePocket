package com.sharad.epocket.accounts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Sharad on 12-Sep-15.
 */
public class AccountsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<AccountItem> itemList;
    private OnItemClickListener itemClickListener;

    public AccountsRecyclerAdapter(ArrayList<AccountItem> itemList) {
        this.itemList = itemList;
    }

    public ArrayList<AccountItem> getItemList() { return itemList; }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_account_list, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        DetailViewHolder holder = (DetailViewHolder) viewHolder;
        final AccountItem account = itemList.get(position);

        /* Set account item parameters */
        holder.title.setText(account.getTitle());
        holder.balance.setText(Utils.formatCurrency(account.getIsoCurrency(), account.getBalance()));
        holder.balanceCard.setText(Utils.formatCurrency(account.getIsoCurrency(), account.getBalanceCard()));
        holder.balanceCash.setText(Utils.formatCurrency(account.getIsoCurrency(), account.getBalanceCash()));
        holder.inflow.setText(Utils.formatCurrency(account.getIsoCurrency(), account.getInflow()));
        holder.outflow.setText(Utils.formatCurrency(account.getIsoCurrency(), account.getOutflow()));
        holder.lastUpdate.setText(account.getLastUpdateString());

        holder.balanceCard.setVisibility((account.hasCardAccount() ? View.VISIBLE : View.GONE));
        holder.balanceCash.setVisibility((account.hasCashAccount() ? View.VISIBLE : View.GONE));

        /* Create listener callbacks */
        holder.editAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null) {
                    itemClickListener.onEditAccountClicked(position, account);
                }
            }
        });
        holder.deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null) {
                    itemClickListener.onDeleteAccountClicked(position, account);
                }
            }
        });
        holder.viewTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null) {
                    itemClickListener.onViewTransactionClicked(position, account);
                }
            }
        });
        holder.viewTrends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null) {
                    itemClickListener.onViewTrendsClicked(position, account);
                }
            }
        });
        holder.viewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null) {
                    itemClickListener.onViewInfoClicked(position, account);
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
        void onEditAccountClicked(int position, AccountItem account);
        void onDeleteAccountClicked(int position, AccountItem account);
        void onViewTransactionClicked(int position, AccountItem account);
        void onViewTrendsClicked(int position, AccountItem account);
        void onViewInfoClicked(int position, AccountItem account);
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        public ImageButton viewTransaction;
        public ImageButton viewTrends;
        public ImageButton viewInfo;
        public ImageButton editAccount;
        public ImageButton deleteAccount;
        public TextView title;
        public TextView balance;
        public TextView balanceCash;
        public TextView balanceCard;
        public TextView inflow;
        public TextView outflow;
        public TextView lastUpdate;

        public DetailViewHolder(final View parent) {
            super(parent);
            final View expandedItems = parent.findViewById(R.id.account_expanded_items);
            final ImageButton expandMore = (ImageButton) parent.findViewById(R.id.account_expand_more);
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

            viewTransaction = (ImageButton) parent.findViewById(R.id.account_transactions);
            viewTrends = (ImageButton) parent.findViewById(R.id.account_trends);
            viewInfo = (ImageButton) parent.findViewById(R.id.account_info);
            editAccount = (ImageButton) parent.findViewById(R.id.account_edit);
            deleteAccount = (ImageButton) parent.findViewById(R.id.account_delete);

            title = (TextView) parent.findViewById(R.id.account_title);
            balance = (TextView) parent.findViewById(R.id.account_balance);
            balanceCash = (TextView) parent.findViewById(R.id.account_balance_cash);
            balanceCard = (TextView) parent.findViewById(R.id.account_balance_card);
            inflow = (TextView) parent.findViewById(R.id.account_inflow);
            outflow = (TextView) parent.findViewById(R.id.account_outflow);
            lastUpdate = (TextView) parent.findViewById(R.id.account_last_update);
        }
    }
}
