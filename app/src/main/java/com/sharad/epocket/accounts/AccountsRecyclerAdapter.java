package com.sharad.epocket.accounts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.ScrollHandler;

import java.util.ArrayList;

/**
 * Created by Sharad on 12-Sep-15.
 */
public class AccountsRecyclerAdapter extends RecyclerView.Adapter<ViewHolderAccount> {
    private ArrayList<IAccount> itemList;
    public OnItemClickListener itemClickListener;

    private static final int VIEW_TYPE_ACCOUNT_COLLAPSED = R.layout.item_account_list_collapsed;
    private static final int VIEW_TYPE_ACCOUNT_EXPANDED = R.layout.item_account_list_expanded;

    private final Context mContext;
    private final LayoutInflater mInflater;
    private final ScrollHandler mScrollHandler;
    private int mExpandedPosition = -1;
    private long mExpandedId = IAccount.INVALID_ID;

    public AccountsRecyclerAdapter(Context context, ArrayList<IAccount> itemList,
                                   ScrollHandler smoothScrollController) {
        this.itemList = itemList;

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mScrollHandler = smoothScrollController;

        setHasStableIds(true);
    }

    @Override
    public void onViewRecycled(ViewHolderAccount viewHolder) {
        super.onViewRecycled(viewHolder);
        viewHolder.clearData();
    }

    public ArrayList<IAccount> getItemList() { return itemList; }

    @Override
    public ViewHolderAccount onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = mInflater.inflate(viewType, parent, false /* attachToRoot */);
        if (viewType == VIEW_TYPE_ACCOUNT_COLLAPSED) {
            return new ViewHolderCollapsedAccount(v, this);
        } else {
            return new ViewHolderExpandedAccount(v, this);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolderAccount viewHolder, final int position) {
        final IAccount account = itemList.get(position);
        viewHolder.bindAccount(mContext, account);
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    @Override
    public long getItemId(int position) {
        if (itemList == null) {
            return RecyclerView.NO_ID;
        }
        return itemList.get(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        final long stableId = getItemId(position);
        return stableId != RecyclerView.NO_ID && stableId == mExpandedId
                ? VIEW_TYPE_ACCOUNT_EXPANDED : VIEW_TYPE_ACCOUNT_COLLAPSED;
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
        void onEditAccountClicked(int position, IAccount account);
        void onDeleteAccountClicked(int position, IAccount account);
        void onViewTransactionClicked(int position, IAccount account);
        void onViewTrendsClicked(int position, IAccount account);
        void onViewInfoClicked(int position, IAccount account);
    }

    /**
     * Request the UI to expand the alarm at selected position and scroll it into view.
     */
    public void expand(int position) {
        final long stableId = getItemId(position);
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
        mExpandedId = IAccount.INVALID_ID;
        mExpandedPosition = -1;
        notifyItemChanged(position);
    }
}
