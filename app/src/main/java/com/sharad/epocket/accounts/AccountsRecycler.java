package com.sharad.epocket.accounts;

import android.content.Context;
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
public class AccountsRecycler extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AccountItem> mItemList;

    public AccountsRecycler(List<AccountItem> itemList) {
        mItemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_account_list, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        DetailViewHolder holder = (DetailViewHolder) viewHolder;
        //holder.setText(mItemList.get(position).get_text());
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        //private TextView _text;


        public DetailViewHolder(final View parent) {
            super(parent);
            //_text = (TextView) parent.findViewById(R.id.dt_text);
            final ZigZagCardView expandedItems = (ZigZagCardView) parent.findViewById(R.id.expanded_items);
            ImageButton expandMore = (ImageButton) parent.findViewById(R.id.expand_more);
            expandMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(expandedItems.getVisibility() == View.GONE) {
                        expandedItems.setVisibility(View.VISIBLE);
                    } else {
                        expandedItems.setVisibility(View.GONE);
                    }
                }
            });
        }

        //public void setText(CharSequence text) {   _text.setText(text);   }
    }
}
