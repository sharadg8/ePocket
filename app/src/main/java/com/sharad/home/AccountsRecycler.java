package com.sharad.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharad.budget.DetailItem;
import com.sharad.epocket.R;
import com.sharad.widgets.ProgressView;

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
        View view = LayoutInflater.from(context).inflate(R.layout.budget_list_card, parent, false);
        view.setMinimumWidth((int)(parent.getMeasuredWidth() * 0.9));
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        DetailViewHolder holder = (DetailViewHolder) viewHolder;
        holder.setText(mItemList.get(position).get_text());
        holder.setInfo(mItemList.get(position).get_info());
        holder.setImage(mItemList.get(position).get_imgId());
        holder.setColor(mItemList.get(position).get_clrId());
        holder.setProgress(mItemList.get(position).get_progress());
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        private TextView _text;
        private TextView _info;
        private ImageView _img;
        private ProgressView _progress;

        public DetailViewHolder(final View parent) {
            super(parent);
            _progress = (ProgressView) parent.findViewById(R.id.dt_progress);
            _text = (TextView) parent.findViewById(R.id.dt_text);
            _info = (TextView) parent.findViewById(R.id.dt_info);
            _img = (ImageView) parent.findViewById(R.id.dt_image);
        }

        public void setText(CharSequence text) {   _text.setText(text);   }
        public void setInfo(CharSequence text) {   _info.setText(text);   }
        public void setColor(int color) {
            _progress.setColor(color);
            _img.setColorFilter(color);
        }
        public void setProgress(int value) { _progress.setValue(value); }
        public void setImage(int id) {
            if(id > 0) {
                _img.setImageResource(id);
                _img.setVisibility(View.VISIBLE);
            } else {
                _img.setVisibility(View.INVISIBLE);
            }
        }
    }
}
