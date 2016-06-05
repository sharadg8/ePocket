package com.sharad.budget;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharad.epocket.R;

import java.util.List;

/**
 * Created by Sharad on 12-Sep-15.
 */
public class DetailsRecycler extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DetailItem> mItemList;

    public DetailsRecycler(List<DetailItem> itemList) {
        mItemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.budget_list_card, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        DetailViewHolder holder = (DetailViewHolder) viewHolder;
        holder.setText(mItemList.get(position).get_text());
        holder.setInfo(mItemList.get(position).get_info());
        holder.setImage(mItemList.get(position).get_imgId());
        holder.setColor(mItemList.get(position).get_clrId());
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        private TextView _text;
        private TextView _info;
        private ImageView _img;
        private View _background;

        public DetailViewHolder(final View parent) {
            super(parent);
            _background =  parent.findViewById(R.id.dt_background);
            _text = (TextView) parent.findViewById(R.id.dt_text);
            _info = (TextView) parent.findViewById(R.id.dt_info);
            _img = (ImageView) parent.findViewById(R.id.dt_image);
            //_img.setColorFilter(Color.WHITE);
        }

        public void setText(CharSequence text) {   _text.setText(text);   }
        public void setInfo(CharSequence text) {   _info.setText(text);   }
        public void setColor(int color) { /*_background.setBackgroundColor(color);*/ }
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
