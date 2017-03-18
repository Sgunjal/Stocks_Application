package com.example.sujay.stocks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Sujay on 3/11/2017.
 */

public class AsyncTaskStockFinancial extends AsyncTask<String, Integer, String> {

    private MainActivity mainActivity;
    private int count;
    private String stock_cmpny_name="";

    private final String dataURL = "http://finance.google.com/finance/info?client=ig&q=";
    private static final String TAG = "AsyncTaskStockFinancial";

    public AsyncTaskStockFinancial(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected void onPreExecute() {
        //Toast.makeText(mainActivity, "Loading Country Data...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String s) {
        ArrayList<Stock> stockList = parseJSON(s);
        mainActivity.addNewStock(stockList);
    }

    @Override
    protected String doInBackground(String... params) {
        Uri dataUri = Uri.parse(dataURL+params[0]);
        stock_cmpny_name=params[1];
        String urlToUse = dataUri.toString();
        //Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            //Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            //Log.e(TAG, "doInBackground: ", e);
            return null;
        }
        //Log.d(TAG, "doInBackground: " + sb.toString());
        return sb.toString();
    }

    private ArrayList<Stock> parseJSON(String s) {

        ArrayList<Stock> stockList = new ArrayList<>();
        String stock_symbol="";
        double stock_value=0;
        double stock_change=0;
        double stock_change_percentage=0;
        try {
            s=s.replace("/","");
            //System.out.println(s);
            JSONArray jObjMain = new JSONArray(s);
            count = jObjMain.length();

            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jCountry = (JSONObject) jObjMain.get(i);
                stock_symbol = jCountry.getString("t");
                stock_value = Double.parseDouble(jCountry.getString("l"));
                stock_change = Double.parseDouble(jCountry.getString("c"));
                stock_change_percentage = Double.parseDouble(jCountry.getString("cp"));
            }
            stockList.add(0,
                    new Stock(stock_cmpny_name,stock_symbol, stock_value, stock_change_percentage, stock_change));
            return stockList;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}