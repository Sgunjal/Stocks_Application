package com.example.sujay.stocks;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;

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

public class AsyncTaskStockSymbol extends AsyncTask<String, Integer, String> {

    private MainActivity mainActivity=new MainActivity();
    private int count;

    private final String dataURL = "http://stocksearchapi.com/api/?api_key=1a7ce9de6f34422194881aba3fca1abef1e95498&search_text=";
    private static final String TAG = "AsyncTaskStockSymbol";

    public AsyncTaskStockSymbol(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(mainActivity, "Loading Stocks Data...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String s) {
        if(s==null)
        {
            mainActivity.no_Data_Found();
        }
        else {
            ArrayList<String> stock_symbol = parseJSON(s);
            mainActivity.stock_Data_to_add(stock_symbol);
        }
    }
    @Override
    protected String doInBackground(String... params) {
        Uri dataUri = Uri.parse(dataURL+params[0]);
        String urlToUse = dataUri.toString();
        //Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            //Log.d(TAG, "mmmmmmmmm: " + urlToUse);
            URL url = new URL(urlToUse);
            //Log.d(TAG, "mmmmmsdsdsdsd: " + urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int HTTP_NOT_FOUND=conn.getResponseCode();
            if ( HTTP_NOT_FOUND == 404) {
                return null;
            }
            else
            {
                conn.setRequestMethod("GET");
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                //Log.d(TAG, "doInBackground: " + sb.toString());
                return sb.toString();

            }
        }catch(Exception e){
                //Log.e(TAG, "doInBackground: ", e);
                return null;
            }

    }

    private ArrayList<String> parseJSON(String s) {

        try {
            JSONArray jObjMain = new JSONArray(s);
            count = jObjMain.length();
            System.out.println("count:- "+count);
            Log.d(TAG, "count: " + count);
            String stock_symbol="";
            ArrayList<String> sArray = new ArrayList<>();

            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jCountry = (JSONObject) jObjMain.get(i);
                String stock_name = jCountry.getString("company_name");
                stock_symbol = jCountry.getString("company_symbol");
                sArray.add(i,stock_symbol+" - " +stock_name);
            }

            return sArray;
        } catch (Exception e) {
            //Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}