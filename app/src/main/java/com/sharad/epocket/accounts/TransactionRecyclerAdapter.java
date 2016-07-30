package com.sharad.epocket.accounts;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.Item;
import com.sharad.epocket.utils.Utils;
import com.sharad.epocket.widget.recyclerview.StickyRecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TransactionRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements StickyRecyclerView.HeaderIndexer {
    ArrayList<ITransaction> itemList;

    private static final int TYPE_HEADER = 1;
    private static final int TYPE_TRANSACTION = 2;

    private LayoutInflater mInflater = null;
    private View mHeader = null;

    public TransactionRecyclerAdapter(Context context, ArrayList<ITransaction> itemList) {
        this.itemList = itemList;
        mInflater = LayoutInflater.from(context);
        mHeader = mInflater.inflate(R.layout.item_account_transaction_header_list, null, false);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch(viewType) {
            case TYPE_HEADER:
                View header = mInflater.inflate(R.layout.item_account_transaction_header_list, viewGroup, false);
                return new HeaderHolder(header);
            case TYPE_TRANSACTION:
                View item = mInflater.inflate(R.layout.item_account_transaction_list, viewGroup, false);
                return new ItemHolder(item);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch(viewHolder.getItemViewType()) {
            case TYPE_HEADER:
                ((HeaderHolder)viewHolder).bind(this.itemList.get(position));
                break;
            case TYPE_TRANSACTION:
                ((ItemHolder)viewHolder).bind(this.itemList.get(position));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(itemList.get(position) instanceof HeaderItem) {
            return TYPE_HEADER;
        }
        return TYPE_TRANSACTION;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getHeaderPositionFromItemPosition(int position) {
        return -1;
    }

    @Override
    public int getHeaderItemsNumber(int headerPosition) {
        return itemList.size();
    }

    @Override
    public View getHeaderView(int headerPosition) {
        TextView title = (TextView) mHeader.findViewById(R.id.header_title);
        title.setText("Header " + (headerPosition / 10));
        return mHeader;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        public ItemHolder(View itemView) {
            super(itemView);
        }

        public void bind(Item item) {
            if(item instanceof ITransaction) {
                ITransaction transaction = (ITransaction) item;

                TextView category = (TextView) itemView.findViewById(R.id.category);
                TextView amount = (TextView) itemView.findViewById(R.id.amount);
                TextView source = (TextView) itemView.findViewById(R.id.source);
                TextView comment = (TextView) itemView.findViewById(R.id.comment);
                ImageView categoryIcon = (ImageView) itemView.findViewById(R.id.category_icon);
                View categoryColor = itemView.findViewById(R.id.category_color);

                category.setText(transaction.getComment());
                amount.setText(Utils.formatCurrencyDec("INR", transaction.getAmount()));
                comment.setText(transaction.getComment());
                if(transaction.getSubType() == ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_CARD) {
                    source.setText("Card");
                } else if(transaction.getSubType() == ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_CASH) {
                    source.setText("Cash");
                } else {
                    source.setText("");
                }
            }
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        public HeaderHolder(View itemView) {
            super(itemView);
        }
        public void bind(Item item) {
            if(item instanceof HeaderItem) {
                HeaderItem headerItem = (HeaderItem) item;
                TextView title = (TextView) this.itemView.findViewById(R.id.header_title);
                title.setText(headerItem.getDateString());
            }
        }
    }

    public class HeaderItem extends ITransaction {
        public HeaderItem(long date) {
            super();
            this.id = date;
        }
        public String getDateString() {
            SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy");
            return df.format(this.id);
        }
        @Override
        public ContentValues getContentValues() { return null; }
    }
}
