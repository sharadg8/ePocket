package com.sharad.epocket.budget;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.CircleButton;

import java.util.List;

/**
 * Created by Sharad on 12-Sep-15.
 */
public class CategoryRecycler extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CategoryItem> mItemList;

    public CategoryRecycler(List<CategoryItem> itemList) {
        mItemList = itemList;
    }

    public List<CategoryItem> getmItemList() {
        return mItemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.category_select_card, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        DetailViewHolder holder = (DetailViewHolder) viewHolder;
        holder.setImage(mItemList.get(position).get_imgId());
        holder.setColor(mItemList.get(position).get_clrId());
        holder.setChecked(mItemList.get(position).is_checked());
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        private CircleButton _button;


        public DetailViewHolder(final View parent) {
            super(parent);
            _button = (CircleButton) parent.findViewById(R.id.at_category_item);
            _button.setChecked(false);
            _button.setColorFilter(Color.DKGRAY, Color.WHITE);
            _button.setButtonType(CircleButton.CIRCLE_BUTTON_TYPE_ICON);
        }

        public void setImage(int imageId) {   _button.setImageResource(imageId);   }
        public void setColor(int colorId) {   _button.setColor(colorId);   }
        public void setChecked(boolean isChecked) {   _button.setChecked(isChecked);   }
    }
}
