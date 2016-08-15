package com.sharad.epocket.budget;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.sharad.epocket.R;
import com.sharad.epocket.accounts.CategoryRecyclerAdapter;
import com.sharad.epocket.accounts.DataSourceCategory;
import com.sharad.epocket.accounts.ICategory;
import com.sharad.epocket.database.CategoryTable;
import com.sharad.epocket.utils.Utils;
import com.sharad.epocket.widget.AutofitRecyclerView;
import com.sharad.epocket.widget.FlowLayout;

import java.util.ArrayList;

public class AddBudgetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);
        Utils.setTaskDescription(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final FlowLayout flowLayout = (FlowLayout) findViewById(R.id.categories);

        String where = CategoryTable.COLUMN_TYPE + "=" + ICategory.CATEGORY_TYPE_EXPENSE;
        final ArrayList<ICategory> itemList = new ArrayList<>();
        DataSourceCategory dataSourceCategory = new DataSourceCategory(this);
        dataSourceCategory.getCategories(itemList, where);

        final AutofitRecyclerView recyclerView = (AutofitRecyclerView) findViewById(R.id.recyclerView);
        CategoryRecyclerAdapter rcAdapter = new CategoryRecyclerAdapter(itemList);
        rcAdapter.setIconTint(ContextCompat.getColor(this, R.color.secondary_text));
        recyclerView.setAdapter(rcAdapter);
        rcAdapter.setOnItemClickListener(new CategoryRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ImageView imageView = new ImageView(getBaseContext());
                imageView.setImageResource(itemList.get(position).getImageResource());
                imageView.setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.primary));
                flowLayout.addView(imageView);
            }

            @Override
            public void onItemLongClick(View view, int position) { }
        });
    }
}
