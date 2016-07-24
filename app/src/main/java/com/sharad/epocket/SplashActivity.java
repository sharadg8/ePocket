package com.sharad.epocket;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sharad.epocket.accounts.DataSourceAccount;
import com.sharad.epocket.accounts.IAccount;
import com.sharad.epocket.accounts.ICategory;
import com.sharad.epocket.accounts.DataSourceCategory;
import com.sharad.epocket.utils.Utils;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new PrefetchData().execute();
    }

    /**
     * Async Task to make http call
     */
    private class PrefetchData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            /**
             * Preload currency locale
             */
            final ArrayList<IAccount> accountItems = new ArrayList<>();
            final DataSourceAccount dataSourceAccount = new DataSourceAccount(SplashActivity.this);
            dataSourceAccount.getAccounts(accountItems);
            for(IAccount a : accountItems) {
                Utils.formatCurrency(a.getIsoCurrency(), a.getBalance());
            }

            /**
             * Add default categories
             */
            final ArrayList<ICategory> categories = new ArrayList<>();
            final DataSourceCategory dataSourceCategory = new DataSourceCategory(SplashActivity.this);
            dataSourceCategory.getCategories(categories);
            if(categories.size() == 0) {
                ICategory.getDefaultExpenseCategories(SplashActivity.this, categories);
                for(ICategory c : categories) {
                    c.setId(dataSourceCategory.insertCategory(c));
                }
                ICategory.getDefaultIncomeCategories(SplashActivity.this, categories);
                for(ICategory c : categories) {
                    c.setId(dataSourceCategory.insertCategory(c));
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // After completing http call
            // will close this activity and launch main activity
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);

            finish();
        }
    }
}
