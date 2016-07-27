package com.sharad.epocket.accounts;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.Utils;

/**
 * Created by Sharad on 27-Jul-16.
 */

public class ViewHolderCollapsedAccount extends ViewHolderAccount {
    public final ImageView image;

    public ViewHolderCollapsedAccount(View itemView, final AccountsRecyclerAdapter adapter) {
        super(itemView);

        image = (ImageView) itemView.findViewById(R.id.image);

        // Expand handler
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.expand(getAdapterPosition());
            }
        });
    }

    @Override
    public void bindAccount(Context context, IAccount account) {
        setData(account);
        title.setText(mAccount.getTitle());
        balance.setText(Utils.formatCurrencyDec(mAccount.getIsoCurrency(), mAccount.getBalance()));
    }
}
