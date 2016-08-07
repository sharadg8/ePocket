package com.sharad.epocket.accounts;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.Constant;
import com.sharad.epocket.utils.ScrollHandler;
import com.sharad.epocket.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.Locale;

public class OverviewRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ITransaction> itemList;
    private SparseArray<Section> mSections = new SparseArray<>();
    private OnItemClickListener itemClickListener = null;

    private static final int VIEW_TYPE_SUMMARY = R.layout.item_account_transaction_list_summary;
    private static final int VIEW_TYPE_PIE_CHART = R.layout.item_account_transaction_list_pie_chart;
    private static final int VIEW_TYPE_HEADER = R.layout.item_account_transaction_list_header;
    private static final int VIEW_TYPE_TRANSACTION_COLLAPSED = R.layout.item_account_transaction_list;
    private static final int VIEW_TYPE_TRANSACTION_EXPANDED = R.layout.item_account_transaction_list_expanded;

    private int summaryItemCount = 0;
    private int mExpandedPosition = -1;
    private long mExpandedId = Constant.INVALID_ID;
    private final ScrollHandler mScrollHandler;
    private String mIsoCurrency;
    private Context mContext = null;
    private LayoutInflater mInflater = null;

    public static class Section {
        int firstPosition;
        int sectionedPosition;
        int sectionSize;
        long date;

        public Section(int firstPosition, long date) {
            this.firstPosition = firstPosition;
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

    public OverviewRecyclerAdapter(Context context, ScrollHandler smoothScrollController) {
        this.itemList = new ArrayList<>();
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mIsoCurrency = Currency.getInstance(Locale.getDefault()).getCurrencyCode();
        mScrollHandler = smoothScrollController;

        setHasStableIds(true);
    }

    public ArrayList<ITransaction> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<ITransaction> itemList) {
        this.itemList.clear();

        Collections.sort(itemList, new ITransaction.iComparator());

        this.itemList.addAll(itemList);

        updateSections();
        notifyDataSetChanged();
    }

    public void updateSections() {
        int position = 0;
        int size = 0;
        long date = 0;
        mSections.clear();
        for (ITransaction transaction : this.itemList) {
            if(!Utils.isSameDay(date, transaction.getDate())) {
                /**
                 * Found another day, lets close the previous section and insert new one
                 */
                if(mSections.size() > 0) {
                    mSections.valueAt(mSections.size()-1).sectionSize = size;
                    size = 0;
                }
                date = transaction.getDate();
                Section section = new Section(position, date);
                section.sectionedPosition = mSections.size() + position;
                mSections.append(section.sectionedPosition, section);
            }
            size++;
            position++;
        }
        if(mSections.size() > 0) {
            mSections.valueAt(mSections.size()-1).sectionSize = size;
        }
    }

    public void setIsoCurrency(String isoCurrency) {
        this.mIsoCurrency = isoCurrency;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final View v = mInflater.inflate(viewType, viewGroup, false /* attachToRoot */);
        switch(viewType) {
            case VIEW_TYPE_HEADER:
                return new HeaderHolder(v);
            case VIEW_TYPE_TRANSACTION_COLLAPSED:
                return new ItemHolder(v);
            case VIEW_TYPE_TRANSACTION_EXPANDED:
                return new ExpandedItemHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder.getItemViewType() == VIEW_TYPE_HEADER) {
            ((HeaderHolder) viewHolder).bind(mSections.get(position));
        } else {
            ((ItemHolder)viewHolder).bind(this.itemList.get(sectionedPositionToPosition(position)));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(isSectionHeaderPosition(position)) {
            return VIEW_TYPE_HEADER;
        }
        long stableId = this.itemList.get(sectionedPositionToPosition(position)).getId();
        return (stableId == mExpandedId) ? VIEW_TYPE_TRANSACTION_EXPANDED : VIEW_TYPE_TRANSACTION_COLLAPSED;
    }

    @Override
    public long getItemId(int position) {
        if (itemList == null) {
            return RecyclerView.NO_ID;
        }
        return position;
    }

    @Override
    public int getItemCount() {
        return (itemList.size() + mSections.size());
    }

    public boolean isSectionHeaderPosition(int position) {
        return mSections.get(position) != null;
    }

    public int positionToSectionedPosition(int position) {
        int offset = 0;
        for (int i = 0; i < mSections.size(); i++) {
            if (mSections.valueAt(i).firstPosition >= position) {
                break;
            }
            ++offset;
        }
        return position + offset;
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

    public void removeAt(int position) {
        itemList.remove(position);
        updateSections();
        int sectionedPosition = positionToSectionedPosition(position);
        notifyItemRemoved(sectionedPosition);
        notifyItemRangeChanged(sectionedPosition, getItemCount());
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onEditClicked(int position, ITransaction iTransaction);
        void onDeleteClicked(int position, ITransaction iTransaction);
    }

    /**
     * Request the UI to expand the alarm at selected position and scroll it into view.
     */
    public void expand(int position) {
        final long stableId = this.itemList.get(sectionedPositionToPosition(position)).getId();;
        if (mExpandedId == stableId) {
            return;
        }
        mExpandedId = stableId;
        mScrollHandler.smoothScrollTo(position);
        if (mExpandedPosition >= 0) {
            notifyItemChanged(mExpandedPosition);
        }
        mExpandedPosition = position;
        notifyItemChanged(position);
    }

    public void collapse(int position) {
        mExpandedId = Constant.INVALID_ID;
        mExpandedPosition = -1;
        notifyItemChanged(position);
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        public ItemHolder(View itemView) {
            super(itemView);

            handleClick();
        }

        private void handleClick() {
            // Expand handler
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expand(getAdapterPosition());
                }
            });
        }

        public void bind(ITransaction iTransaction) {
            DataSourceCategory dataSourceCategory = new DataSourceCategory(mContext);
            ICategory iCategory = dataSourceCategory.getCategory(iTransaction.getCategory());

            TextView category = (TextView) itemView.findViewById(R.id.category);
            TextView amount = (TextView) itemView.findViewById(R.id.amount);
            TextView source = (TextView) itemView.findViewById(R.id.source);
            TextView comment = (TextView) itemView.findViewById(R.id.comment);
            ImageView categoryIcon = (ImageView) itemView.findViewById(R.id.category_icon);
            View categoryColor = itemView.findViewById(R.id.category_color);

            amount.setText(Utils.formatCurrencyDec(mIsoCurrency, iTransaction.getAmount()));
            String type = "";
            switch (iTransaction.getType()) {
                case ITransaction.TRANSACTION_TYPE_ACCOUNT_EXPENSE:
                    category.setText(iCategory.getTitle());
                    categoryIcon.setImageResource(iCategory.getImageResource());
                    categoryColor.setBackgroundColor(iCategory.getColor());
                    amount.setTextColor(ContextCompat.getColor(mContext, R.color.transaction_expense));
                    type = "Expense";
                    break;
                case ITransaction.TRANSACTION_TYPE_ACCOUNT_INCOME:
                    category.setText(iCategory.getTitle());
                    categoryIcon.setImageResource(iCategory.getImageResource());
                    categoryColor.setBackgroundColor(iCategory.getColor());
                    amount.setTextColor(ContextCompat.getColor(mContext, R.color.transaction_income));
                    type = "Income";
                    break;
                case ITransaction.TRANSACTION_TYPE_ACCOUNT_TRANSFER:
                    amount.setTextColor(ContextCompat.getColor(mContext, R.color.transaction_transfer));
                    type = "Transfer";
                    break;
                case ITransaction.TRANSACTION_TYPE_ACCOUNT_WITHDRAW:
                    category.setText("Withdraw");
                    categoryIcon.setImageResource(R.drawable.ic_local_atm_black_24px);
                    categoryColor.setBackgroundColor(ContextCompat.getColor(mContext, R.color.primary));
                    amount.setTextColor(ContextCompat.getColor(mContext, R.color.transaction_transfer));
                    type = "";
                    break;
                case ITransaction.TRANSACTION_TYPE_ACCOUNT_DEPOSIT:
                    category.setText("Deposit");
                    categoryIcon.setImageResource(R.drawable.ic_local_atm_black_24px);
                    categoryColor.setBackgroundColor(ContextCompat.getColor(mContext, R.color.primary));
                    amount.setTextColor(ContextCompat.getColor(mContext, R.color.transaction_transfer));
                    type = "";
                    break;
            }

            comment.setText((iTransaction.getComment().length() > 0) ? iTransaction.getComment() : type);

            switch (iTransaction.getSubType()) {
                case ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_CARD:
                    source.setText("Card");
                    break;
                case ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_CASH:
                    source.setText("Cash");
                    break;
            }
        }
    }

    class ExpandedItemHolder extends ItemHolder {
        private ITransaction mTransaction;
        public ExpandedItemHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            handleClick();

            ImageButton edit = (ImageButton) itemView.findViewById(R.id.edit);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener != null) {
                        itemClickListener.onEditClicked(
                                sectionedPositionToPosition(getAdapterPosition()), mTransaction);
                    }
                }
            });
            ImageButton delete = (ImageButton) itemView.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener != null) {
                        itemClickListener.onDeleteClicked(
                                sectionedPositionToPosition(getAdapterPosition()), mTransaction);
                    }
                }
            });
        }

        private void handleClick() {
            // Collapse handler
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    collapse(getAdapterPosition());
                }
            });
        }

        public void bind(ITransaction iTransaction) {
            super.bind(iTransaction);
            mTransaction = iTransaction;
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
}
