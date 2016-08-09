package com.sharad.epocket.accounts;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.Constant;
import com.sharad.epocket.utils.Item;
import com.sharad.epocket.utils.ScrollHandler;
import com.sharad.epocket.utils.Utils;
import com.sharad.epocket.widget.chart.LineChartView;
import com.sharad.epocket.widget.recyclerview.StickyRecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.Locale;

public class OverviewRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements StickyRecyclerView.HeaderIndexer {
    private ArrayList<Item> itemList = null;
    private ArrayList<Integer> itemType = null;
    private OnItemClickListener itemClickListener = null;

    private final int MAX_NUM_DAYS = 31;
    private static final int VIEW_TYPE_SUMMARY = R.layout.item_account_transaction_list_summary;
    private static final int VIEW_TYPE_LINE_CHART = R.layout.item_account_transaction_list_line_chart;
    private static final int VIEW_TYPE_PIE_CHART = R.layout.item_account_transaction_list_pie_chart;
    private static final int VIEW_TYPE_HEADER = R.layout.item_account_transaction_list_header;
    private static final int VIEW_TYPE_TRANSACTION = R.layout.item_account_transaction_list;
    private static final int VIEW_TYPE_TRANSACTION_EXPANDED = R.layout.item_account_transaction_list_expanded;

    private int mExpandedPosition = RecyclerView.NO_POSITION;
    private long mExpandedId = Constant.INVALID_ID;
    private long selectedMonth = 0;
    private final ScrollHandler mScrollHandler;
    private String mIsoCurrency;
    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private View mHeader = null;

    private float[] lineChartData = new float[MAX_NUM_DAYS];

    public OverviewRecyclerAdapter(Context context, ScrollHandler smoothScrollController) {
        this.itemList = new ArrayList<>();
        this.itemType = new ArrayList<>();
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mHeader = mInflater.inflate(R.layout.item_account_transaction_list_header, null, false);
        mIsoCurrency = Currency.getInstance(Locale.getDefault()).getCurrencyCode();
        mScrollHandler = smoothScrollController;

        setHasStableIds(true);
    }

    public void setItemList(ArrayList<ITransaction> transactionArrayList, long selectedMonth) {
        this.selectedMonth = selectedMonth;
        this.itemList.clear();
        this.itemType.clear();

        Collections.sort(transactionArrayList, new ITransaction.iComparator());

        this.itemList.add(new Item(0));
        this.itemType.add(VIEW_TYPE_SUMMARY);

        this.itemList.add(new Item(0));
        this.itemType.add(VIEW_TYPE_LINE_CHART);

        this.itemList.add(new Item(0));
        this.itemType.add(VIEW_TYPE_PIE_CHART);

        this.itemList.add(new Item(0));
        this.itemType.add(VIEW_TYPE_PIE_CHART);

        long date = 0;
        for (ITransaction transaction : transactionArrayList) {
            if(!Utils.isSameDay(date, transaction.getDate())) {
                date = transaction.getDate();
                this.itemList.add(new Item(date));
                this.itemType.add(VIEW_TYPE_HEADER);
            }
            this.itemList.add(transaction);
            this.itemType.add(VIEW_TYPE_TRANSACTION);
        }

        computeLineChartData();
        notifyDataSetChanged();
    }

    public void setIsoCurrency(String isoCurrency) {
        this.mIsoCurrency = isoCurrency;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final View v = mInflater.inflate(viewType, viewGroup, false /* attachToRoot */);
        switch(viewType) {
            case VIEW_TYPE_SUMMARY:
            case VIEW_TYPE_PIE_CHART:
                return  new SummaryHolder(v);
            case VIEW_TYPE_LINE_CHART:
                return  new LineChartHolder(v);
            case VIEW_TYPE_HEADER:
                return new HeaderHolder(v);
            case VIEW_TYPE_TRANSACTION:
                return new ItemHolder(v);
            case VIEW_TYPE_TRANSACTION_EXPANDED:
                return new ExpandedItemHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case VIEW_TYPE_SUMMARY:
            case VIEW_TYPE_PIE_CHART:
                ((SummaryHolder)viewHolder).bind();
                break;
            case VIEW_TYPE_LINE_CHART:
                ((LineChartHolder)viewHolder).bind();
                break;
            case VIEW_TYPE_HEADER:
                ((HeaderHolder)viewHolder).bind(itemList.get(position));
                break;
            case VIEW_TYPE_TRANSACTION:
            case VIEW_TYPE_TRANSACTION_EXPANDED:
                ((ItemHolder)viewHolder).bind(this.itemList.get(position));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = itemType.get(position);
        if(type == VIEW_TYPE_TRANSACTION) {
            long stableId = this.itemList.get(position).getId();
            type = (stableId == mExpandedId) ? VIEW_TYPE_TRANSACTION_EXPANDED
                    : VIEW_TYPE_TRANSACTION;
        }
        return type;
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
        return itemList.size();
    }

    @Override
    public int getHeaderPositionFromItemPosition(int position) {
        for(int i = position; i>=0; i--) {
            if(itemType.get(i) == VIEW_TYPE_HEADER){
                return i;
            }
        }
        return RecyclerView.NO_POSITION;
    }

    @Override
    public int getHeaderItemsNumber(int headerPosition) {
        int count = 0;
        for(int i = headerPosition+1; i<itemType.size(); i++) {
            if(itemType.get(i) == VIEW_TYPE_HEADER){
                return count;
            }
            count++;
        }
        return 1;
    }

    @Override
    public View getHeaderView(int headerPosition) {
        TextView title = (TextView) mHeader.findViewById(R.id.header_title);
        title.setText(Utils.getLongDateString(itemList.get(headerPosition).getId()));
        return mHeader;
    }

    public void removeAt(int position) {
        itemList.remove(position);
        itemType.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());

        /**
         * Remove extra headers if any left by position
         */
        if((position == itemType.size()-2)
                || (isHeaderPosition(position-1) && isHeaderPosition(position))) {
            itemList.remove(position-1);
            itemType.remove(position-1);
            notifyItemRemoved(position-1);
            notifyItemRangeChanged(position-1, getItemCount());
        }

        computeLineChartData();
    }

    private boolean isHeaderPosition(int position) {
        if((position >= 0) && (position < itemType.size())) {
            return (itemType.get(position) == VIEW_TYPE_HEADER);
        }
        return false;
    }

    private void computeLineChartData() {
        float balance = 0;
        float transaction[] = new float[lineChartData.length];
        Calendar calendar = Calendar.getInstance();
        for(int i=0; i<itemType.size(); i++) {
            if(itemType.get(i) == VIEW_TYPE_TRANSACTION) {
                ITransaction iTransaction = (ITransaction)itemList.get(i);
                int index = Utils.getDayOfMonth(iTransaction.getDate()) - 1;
                switch (iTransaction.getType()) {
                    case ITransaction.TRANSACTION_TYPE_MONTH_OPENING_BALANCE:
                        balance = iTransaction.getAccount();
                        break;
                    case ITransaction.TRANSACTION_TYPE_ACCOUNT_EXPENSE:
                    case ITransaction.TRANSACTION_TYPE_ACCOUNT_DEPOSIT:
                        transaction[index] -= iTransaction.getAmount();
                        break;
                    case ITransaction.TRANSACTION_TYPE_ACCOUNT_INCOME:
                    case ITransaction.TRANSACTION_TYPE_ACCOUNT_WITHDRAW:
                        transaction[index] += iTransaction.getAmount();
                        break;
                }
            }
        }

        for(int i=0; i<lineChartData.length; i++) {
            balance += transaction[i];
            lineChartData[i] = balance;
        }
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
        final long stableId = this.itemList.get(position).getId();;
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

        public void bind(Item item) {
            if(item instanceof ITransaction) {
                ITransaction iTransaction = (ITransaction)item;
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
                        itemClickListener.onEditClicked(getAdapterPosition(), mTransaction);
                    }
                }
            });
            ImageButton delete = (ImageButton) itemView.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener != null) {
                        itemClickListener.onDeleteClicked(getAdapterPosition(), mTransaction);
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

        public void bind(Item item) {
            super.bind(item);
            if(item instanceof ITransaction) {
                ITransaction iTransaction = (ITransaction) item;
                mTransaction = iTransaction;
            }
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        public HeaderHolder(View itemView) {
            super(itemView);
        }
        public void bind(Item item) {
            TextView title = (TextView) this.itemView.findViewById(R.id.header_title);
            title.setText(Utils.getLongDateString(item.getId()));
        }
    }

    class SummaryHolder extends RecyclerView.ViewHolder {
        public SummaryHolder(View itemView) {
            super(itemView);
        }
        public void bind() {
        }
    }

    class LineChartHolder extends RecyclerView.ViewHolder {
        public LineChartHolder(View itemView) {
            super(itemView);
        }
        public void bind() {
            LineChartView chart = (LineChartView) itemView.findViewById(R.id.chart);
            int numPoints = Utils.isThisMonth(selectedMonth) ? Utils.getDayOfMonth(selectedMonth) : MAX_NUM_DAYS;
            chart.setChartData(lineChartData, numPoints);
        }
    }
}
