package com.sharad.epocket.accounts;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.Utils;

/**
 * Created by Sharad on 27-Jul-16.
 */

public class ViewHolderExpandedAccount extends ViewHolderAccount {
    public final TextView cashBalance;
    public final TextView cardBalance;
    public final TextView inflow;
    public final TextView outflow;
    public final TextView lastUpdate;

    public final ImageButton transactions;
    public final ImageButton edit;
    public final ImageButton delete;
    public final ImageButton info;
    public final ImageButton withdraw;

    public ViewHolderExpandedAccount(View itemView, final AccountsRecyclerAdapter adapter) {
        super(itemView);

        cashBalance = (TextView) itemView.findViewById(R.id.account_balance_cash);
        cardBalance = (TextView) itemView.findViewById(R.id.account_balance_card);
        inflow = (TextView) itemView.findViewById(R.id.account_inflow);
        outflow = (TextView) itemView.findViewById(R.id.account_outflow);
        lastUpdate = (TextView) itemView.findViewById(R.id.account_last_update);

        transactions = (ImageButton) itemView.findViewById(R.id.account_transactions);
        edit = (ImageButton) itemView.findViewById(R.id.account_edit);
        delete = (ImageButton) itemView.findViewById(R.id.account_delete);
        info = (ImageButton) itemView.findViewById(R.id.account_info);
        withdraw = (ImageButton) itemView.findViewById(R.id.account_withdraw);

        // Collapse handler
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.collapse(getAdapterPosition());
            }
        });

        transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.itemClickListener != null) {
                    adapter.itemClickListener.onViewTransactionClicked(getAdapterPosition(), mAccount);
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.itemClickListener != null) {
                    adapter.itemClickListener.onEditAccountClicked(getAdapterPosition(), mAccount);
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.itemClickListener != null) {
                    adapter.itemClickListener.onDeleteAccountClicked(getAdapterPosition(), mAccount);
                }
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.itemClickListener != null) {
                    adapter.itemClickListener.onViewInfoClicked(getAdapterPosition(), mAccount);
                }
            }
        });

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.itemClickListener != null) {
                    adapter.itemClickListener.onWithdrawClicked(getAdapterPosition(), mAccount);
                }
            }
        });
    }

    @Override
    public void bindAccount(Context context, IAccount account) {
        setData(account);
        title.setText(mAccount.getTitle());
        balance.setText(Utils.formatCurrencyDec(mAccount.getIsoCurrency(), mAccount.getBalance()));

        cardBalance.setText(Utils.formatCurrencyDec(mAccount.getIsoCurrency(), mAccount.getBalanceCard()));
        cashBalance.setText(Utils.formatCurrencyDec(mAccount.getIsoCurrency(), mAccount.getBalanceCash()));
        inflow.setText(Utils.formatCurrencyDec(mAccount.getIsoCurrency(), mAccount.getInflow()));
        outflow.setText(Utils.formatCurrencyDec(mAccount.getIsoCurrency(), mAccount.getOutflow()));
        lastUpdate.setText(mAccount.getLastUpdateString());

        cardBalance.setVisibility(mAccount.hasCardAccount() ? View.VISIBLE : View.GONE);
        cashBalance.setVisibility(mAccount.hasCashAccount() ? View.VISIBLE : View.GONE);
        withdraw.setVisibility((mAccount.getAccountType() == IAccount.ACCOUNT_TYPE_CASH_CARD) ? View.VISIBLE : View.GONE);
    }
}
