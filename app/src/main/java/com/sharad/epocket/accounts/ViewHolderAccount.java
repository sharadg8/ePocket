package com.sharad.epocket.accounts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sharad.epocket.R;

/**
 * Created by Sharad on 27-Jul-16.
 */

public abstract class ViewHolderAccount extends RecyclerView.ViewHolder{
    public final View arrow;
    public final TextView title;
    public final TextView balance;
    public final View bookmark;

    protected IAccount mAccount;

    public ViewHolderAccount(View itemView) {
        super(itemView);
        arrow = itemView.findViewById(R.id.arrow);
        bookmark = itemView.findViewById(R.id.bookmark);
        title = (TextView) itemView.findViewById(R.id.title);
        balance = (TextView) itemView.findViewById(R.id.balance);
    }

    public void setData(IAccount account) {
        mAccount = account;
    }

    public void clearData() {
        mAccount = null;
    }

    /**
     * Binds the view with {@link IAccount} data.
     */
    public abstract void bindAccount(Context context, IAccount account);
}
