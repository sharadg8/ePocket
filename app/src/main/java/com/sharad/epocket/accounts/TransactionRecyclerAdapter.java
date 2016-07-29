package com.sharad.epocket.accounts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharad.epocket.R;
import com.sharad.epocket.widget.recyclerview.StickyRecyclerView;

public class TransactionRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements StickyRecyclerView.HeaderIndexer {

    private LayoutInflater mInflater = null;
    private View mHeader = null;

    public TransactionRecyclerAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mHeader = mInflater.inflate(R.layout.item_account_transaction_header_list, null, false);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == 0) {
            View view = mInflater.inflate(R.layout.item_account_transaction_header_list, viewGroup, false);
            return new HeaderHolder(view);
        } else {
            View view = mInflater.inflate(R.layout.item_account_transaction_list, viewGroup, false);
            return new ItemHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if((position % 10) == 0) {
            HeaderHolder holder = (HeaderHolder)viewHolder;
            holder.header.setText("Header " + (position / 10));
        } else {
            ItemHolder holder = (ItemHolder)viewHolder;
            holder.item.setText("Item " + position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return ((position % 10) == 0) ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return 50;
    }

    @Override
    public int getHeaderPositionFromItemPosition(int position) {
        return (position / 10) * 10;
    }

    @Override
    public int getHeaderItemsNumber(int headerPosition) {
        return 9;
    }

    @Override
    public View getHeaderView(int headerPosition) {
        TextView title = (TextView) mHeader.findViewById(R.id.header_title);
        title.setText("Header " + (headerPosition / 10));
        return mHeader;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        public TextView item;
        public ItemHolder(View itemView) {
            super(itemView);
            item = (TextView) itemView.findViewById(R.id.text_item);
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView header;
        public HeaderHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.header_title);
        }
    }
}
