package com.sharad.epocket;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sharad.epocket.accounts.DataSourceAccount;
import com.sharad.epocket.accounts.DataSourceCategory;
import com.sharad.epocket.accounts.IAccount;
import com.sharad.epocket.accounts.ICategory;
import com.sharad.epocket.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private class PrefetchData extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            /**
             * Preload currency locale
             */
            final ArrayList<IAccount> iAccountArrayList = new ArrayList<>();
            final DataSourceAccount dataSourceAccount = new DataSourceAccount(SplashActivity.this);
            dataSourceAccount.getAccounts(iAccountArrayList);

            StringBuilder request = new StringBuilder();
            StringBuilder result = new StringBuilder();
            request.append("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(");

            String to = "INR";

            ArrayList<String> isoCurrencies = new ArrayList<>();
            for(IAccount iAccount : iAccountArrayList) {
                if(!isoCurrencies.contains(iAccount.getIsoCurrency())) {
                    isoCurrencies.add(iAccount.getIsoCurrency());

                    request.append("%22");
                    request.append(iAccount.getIsoCurrency());
                    request.append(to);
                    request.append("%22,");
                }
            }
            Utils.loadLocaleMap(isoCurrencies);

            request.deleteCharAt(request.lastIndexOf(","));
            request.append(")&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");

            try {
                URL url = new URL(request.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(1000);
                urlConnection.setConnectTimeout(1000);
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            }catch( Exception e) {
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }

            /**
             * Add default categories
             */
            final ArrayList<ICategory> iCategories = new ArrayList<>();
            final DataSourceCategory dataSourceCategory = new DataSourceCategory(SplashActivity.this);
            dataSourceCategory.getCategories(iCategories);
            if(iCategories.size() == 0) {
                ICategory.getDefaultExpenseCategories(SplashActivity.this, iCategories);
                for(ICategory c : iCategories) {
                    c.setId(dataSourceCategory.insertCategory(c));
                }
                ICategory.getDefaultIncomeCategories(SplashActivity.this, iCategories);
                for(ICategory c : iCategories) {
                    c.setId(dataSourceCategory.insertCategory(c));
                }
            }
            ICategory.loadCategoryMap(iCategories);

            return result.toString();
        }

        @Override
        protected void onPostExecute(String exchange_rates) {
            super.onPostExecute(exchange_rates);

            try {
                JSONObject jsonRootObject = new JSONObject(exchange_rates);

                //Get the instance of JSONArray that contains JSONObjects
                jsonRootObject = jsonRootObject.getJSONObject("query");
                jsonRootObject = jsonRootObject.getJSONObject("results");
                JSONArray jsonArray  = jsonRootObject.optJSONArray("rate");

                //Iterate the jsonArray and print the info of JSONObjects
                if(jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String currency = jsonObject.optString("Name").toString();
                        currency = currency.substring(0, 3);
                        float rate = Float.parseFloat(jsonObject.optString("Rate").toString());
                        Utils.updateExchangeRate(currency, rate);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // will close this activity and launch main activity
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);

            finish();
        }
    }
}
