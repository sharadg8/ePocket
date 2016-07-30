package com.sharad.epocket.accounts;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.SparseArray;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Locale;

public class TransactionRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements StickyRecyclerView.HeaderIndexer {

    private ArrayList<ITransaction> itemList;
    private SparseArray<Section> mSections = new SparseArray<>();

    private static final int TYPE_HEADER = 1;
    private static final int TYPE_TRANSACTION = 2;

    private String mIsoCurrency;
    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private View mHeader = null;

    public static class Section {
        int sectionedPosition;
        int sectionSize;
        long date;

        public Section(long date) {
            this.date = date;
        }

        public CharSequence getTitle() {
            if(DateUtils.isToday(date)) {
                return "Today";
            } else if(Utils.isYesterday(date)) {
                return "Yesterday";
            }
            SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy");
            return df.format(date);
        }
    }

    public TransactionRecyclerAdapter(Context context) {
        this.itemList = new ArrayList<>();
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mHeader = mInflater.inflate(R.layout.item_account_transaction_header_list, null, false);
        mIsoCurrency = Currency.getInstance(Locale.getDefault()).getCurrencyCode();
    }

    public void setItemList(ArrayList<ITransaction> itemList) {
        mSections.clear();
        this.itemList.clear();

        Collections.sort(itemList, new TransactionComparator());

        int position = 0;
        int size = 0;
        long date = 0;
        for (ITransaction transaction : itemList) {
            this.itemList.add(transaction);
            if(!Utils.isSameDay(date, transaction.getDate())) {
                /**
                 * Found another day, lets close the previous section and insert new one
                 */
                if(mSections.size() > 0) {
                    mSections.valueAt(mSections.size()-1).sectionSize = size;
                    size = 0;
                }
                date = transaction.getDate();
                Section section = new Section(date);
                section.sectionedPosition = mSections.size() + position;
                mSections.append(section.sectionedPosition, section);
            }
            size++;
            position++;
        }
        if(mSections.size() > 0) {
            mSections.valueAt(mSections.size()-1).sectionSize = size;
        }

        notifyDataSetChanged();
    }

    public void setIsoCurrency(String isoCurrency) {
        this.mIsoCurrency = isoCurrency;
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
                ((HeaderHolder)viewHolder).bind(mSections.get(position));
                break;
            case TYPE_TRANSACTION:
                ((ItemHolder)viewHolder).bind(this.itemList.get(sectionedPositionToPosition(position)));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return isSectionHeaderPosition(position) ? TYPE_HEADER : TYPE_TRANSACTION;
    }

    @Override
    public int getItemCount() {
        return (itemList.size() + mSections.size());
    }

    @Override
    public int getHeaderPositionFromItemPosition(int position) {
        int headerPosition = 0;
        for (int i = 0; i < mSections.size(); i++) {
            if (mSections.valueAt(i).sectionedPosition > position) {
                break;
            }
            headerPosition = mSections.valueAt(i).sectionedPosition;
        }
        return headerPosition;
    }

    @Override
    public int getHeaderItemsNumber(int headerPosition) {
        return mSections.get(headerPosition).sectionSize;
    }

    @Override
    public View getHeaderView(int headerPosition) {
        TextView title = (TextView) mHeader.findViewById(R.id.header_title);
        title.setText(mSections.get(headerPosition).getTitle());
        return mHeader;
    }

    public boolean isSectionHeaderPosition(int position) {
        return mSections.get(position) != null;
    }

    public int sectionedPositionToPosition(int sectionedPosition) {
        if (isSectionHeaderPosition(sectionedPosition)) {
            return RecyclerView.NO_POSITION;
        }

        int offset = 0;
        for (int i = 0; i < mSections.size(); i++) {
            if (mSections.valueAt(i).sectionedPosition > sectionedPosition) {
                break;
            }
            --offset;
        }
        return sectionedPosition + offset;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        public ItemHolder(View itemView) {
            super(itemView);
        }

        public void bind(Item item) {
            if(item instanceof ITransaction) {
                ITransaction transaction = (ITransaction) item;

                DataSourceCategory dataSourceCategory = new DataSourceCategory(mContext);
                ICategory iCategory = dataSourceCategory.getCategory(transaction.getCategory());

                TextView category = (TextView) itemView.findViewById(R.id.category);
                TextView amount = (TextView) itemView.findViewById(R.id.amount);
                TextView source = (TextView) itemView.findViewById(R.id.source);
                TextView comment = (TextView) itemView.findViewById(R.id.comment);
                ImageView categoryIcon = (ImageView) itemView.findViewById(R.id.category_icon);
                View categoryColor = itemView.findViewById(R.id.category_color);

                category.setText(iCategory.getTitle());
                categoryIcon.setImageResource(iCategory.getImageResource());
                categoryColor.setBackgroundColor(iCategory.getColor());

                amount.setText(Utils.formatCurrencyDec(mIsoCurrency, transaction.getAmount()));
                String type = "";
                switch (transaction.getType()) {
                    case ITransaction.TRANSACTION_TYPE_ACCOUNT_EXPENSE:
                        amount.setTextColor(ContextCompat.getColor(mContext, R.color.transaction_expense));
                        type = "Expense";
                        break;
                    case ITransaction.TRANSACTION_TYPE_ACCOUNT_INCOME:
                        amount.setTextColor(ContextCompat.getColor(mContext, R.color.transaction_income));
                        type = "Income";
                        break;
                    case ITransaction.TRANSACTION_TYPE_ACCOUNT_TRANSFER:
                        amount.setTextColor(ContextCompat.getColor(mContext, R.color.transaction_transfer));
                        type = "Transfer";
                        break;
                }

                comment.setText((transaction.getComment().length() > 0) ? transaction.getComment() : type);

                switch (transaction.getSubType()) {
                    case ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_CARD:
                        source.setText("Card");
                        break;
                    case ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_CASH:
                        source.setText("Cash");
                        break;
                }
            }
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        public HeaderHolder(View itemView) {
            super(itemView);
        }
        public void bind(Section item) {
            TextView title = (TextView) this.itemView.findViewById(R.id.header_title);
            title.setText(item.getTitle());
        }
    }

    private class TransactionComparator implements Comparator<ITransaction> {
        @Override
        public int compare(ITransaction o, ITransaction o1) {
            return (o.getDate() == o1.getDate())
                    ? 0
                    : ((o.getDate() < o1.getDate()) ? 1 : -1);
        }
    }
}
