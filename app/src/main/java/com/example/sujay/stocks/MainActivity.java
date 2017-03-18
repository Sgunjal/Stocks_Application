package com.example.sujay.stocks;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private List<Stock> stockList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swiper;
    private RecyclerViewAdapter mAdapter;
    private StockDatabaseHandler stockDatabaseHandler;
    private static String stockURL = "http://www.marketwatch.com/investing/stock";
    private String stock_name_to_search;
    private String stock_name_to_search2;
    private ConnectivityManager connectivityManager;
    private String stk_name_to_serach="";
    String [] stockArr;
    int is_refresh=0;
    int pos;
    EditText et;
    String arr[];
    private int flag=0;
    String st_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setScrollbarFadingEnabled(true);
        recyclerView.setVerticalScrollBarEnabled(true);

        mAdapter = new RecyclerViewAdapter(stockList, this);

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        stockDatabaseHandler = new StockDatabaseHandler(this);

        stockDatabaseHandler.dumpLog();
        ArrayList<Stock> list = stockDatabaseHandler.loadCountries();
        stockList.addAll(list);

        swiper = (SwipeRefreshLayout) findViewById(R.id.swiper);
        doRefresh();
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });
    }
    public void addNewStock(ArrayList<Stock> stock_symbol) {
        Stock s1=stock_symbol.get(0);

        flag=0;
        for(int j=0;j<stockList.size();j++)
        {
            if(s1.getstock_symbol().equals(stockList.get(j).getstock_symbol().toString()))
            {
                flag=1;
                break;
            }
        }
        if(flag==1)
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
            builder1.setIcon(R.drawable.alert_icon);
            builder1.setMessage("Stock Symbol "+ s1.getstock_symbol() +" is already Displayed");
            builder1.setTitle("Duplicate Stock");
            AlertDialog dialog1 = builder1.create();
            dialog1.show();
        }
        else
        {
            stockList.add(s1);
            //Collections.sort(stockList);
            Collections.sort(stockList, new Comparator<Stock>() {
                @Override
                public int compare(Stock object1, Stock object2) {
                    return object1.getstock_symbol().compareTo(object2.getstock_symbol());
                }
            });
            stockDatabaseHandler.deleteStock(s1.getstock_name());
            stockDatabaseHandler.addStock(s1);
            mAdapter.notifyDataSetChanged();
        }
    }
    public void stock_Data_to_add( ArrayList<String> stock_symbol) {

        stock_name_to_search= stock_symbol.get(0);
        arr = stock_name_to_search.split(" ");
        if(stock_symbol.size()==1 && stk_name_to_serach.toUpperCase().equals(arr[0]))
        {
            stock_name_to_search2= arr[0];
            //System.out.println("IF"+stock_name_to_search2);
            Log.d("MainActivity","If: " + stock_name_to_search2+ " "+ stock_name_to_search.substring(stock_name_to_search.indexOf(" ", stock_name_to_search.indexOf(" ") + 1)+1));
            new AsyncTaskStockFinancial(MainActivity.this).execute(stock_name_to_search2,stock_name_to_search.substring(stock_name_to_search.indexOf(" ", stock_name_to_search.indexOf(" ") + 1)+1));
        }
        else
        {
            stockArr = stock_symbol.toArray(new String[stock_symbol.size()]);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Make a selection");
            builder.setItems(stockArr, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    stock_name_to_search=stockArr[which];
                    arr = stock_name_to_search.split(" ");
                    stock_name_to_search2= arr[0];
                    //System.out.println("ELSE"+stock_name_to_search2);
                    //System.out.println("IF"+stock_name_to_search2);
                    Log.d("MainActivity","If: " + stock_name_to_search2+ " "+ stock_name_to_search.substring(stock_name_to_search.indexOf(" ", stock_name_to_search.indexOf(" ") + 1)+1));
                    new AsyncTaskStockFinancial(MainActivity.this).execute(stock_name_to_search2,stock_name_to_search.substring(stock_name_to_search.indexOf(" ", stock_name_to_search.indexOf(" ") + 1)+1));
                }
            });

            builder.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    public void no_Data_Found()
    {
        AlertDialog.Builder buildera = new AlertDialog.Builder(MainActivity.this);
        buildera.setMessage("Data for stock symbol");
        buildera.setTitle("Symbol Not Found: "+ stk_name_to_serach);
        AlertDialog dialoga = buildera.create();
        dialoga.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stock_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuB:
                swiper.setRefreshing(false);
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                boolean isWifiConn = networkInfo.isConnected();
                networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                boolean isMobileConn = networkInfo.isConnected();
                if(isWifiConn==true || isMobileConn==true ) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    et = new EditText(this);
                    et.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
                    et.setAllCaps(true);
                    et.setInputType(InputType.TYPE_CLASS_TEXT);
                    et.setGravity(Gravity.CENTER_HORIZONTAL);

                    builder.setView(et);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            stk_name_to_serach=et.getText().toString();
                            flag=0;
                            for(int j=0;j<stockList.size();j++)
                            {
                                if(stk_name_to_serach.toUpperCase().equals(stockList.get(j).getstock_symbol().toString()))
                                {

                                    flag=1;
                                    break;
                                }
                            }
                            if(flag==1)
                            {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                                builder1.setIcon(R.drawable.alert_icon);
                                builder1.setMessage("Stock Symbol "+ stk_name_to_serach.toUpperCase() +" is already Displayed");
                                builder1.setTitle("Duplicate Stock");
                                AlertDialog dialog1 = builder1.create();
                                dialog1.show();
                            }
                            else
                            {
                                new AsyncTaskStockSymbol(MainActivity.this).execute(et.getText().toString());
                            }
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

                    builder.setMessage("Please enter a Stock Symbol:");
                    builder.setTitle("Stock Selection");

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Stocks cannot be Updated Without A Network Connection");
                    builder.setTitle("No Network Connection");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                //Toast.makeText(this,"Add Stock Pressed !!!",Toast.LENGTH_SHORT).show();
                //swiper.setRefreshing(true);
                return true;
            default:
                //swiper.setRefreshing(true);
                return super.onOptionsItemSelected(item);

        }
    }
    private void doRefresh() {
        is_refresh=1;
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfo.isConnected();
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfo.isConnected();
        if(isWifiConn==true || isMobileConn==true ) {
            for (int i = 0; i < stockList.size(); i++) {
                Stock temp = stockList.get(i);
                stockList.remove(i);
                new AsyncTaskStockFinancial(MainActivity.this).execute(temp.getstock_symbol(), temp.getstock_name());
            }
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Stocks cannot be Updated Without A Network Connection");
            builder.setTitle("No Network Connection");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        is_refresh=0;
        swiper.setRefreshing(false);
    }
    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks
        if(is_refresh!=1) {
            int pos = recyclerView.getChildLayoutPosition(v);
            Stock m = stockList.get(pos);
            String url = stockURL + "/" + m.getstock_symbol();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
        else
        {
            Toast.makeText(v.getContext(), "Wait Refreshing" , Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(v.getContext(), "SHORT " + m.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks

        pos = recyclerView.getChildLayoutPosition(v);
        Stock m = stockList.get(pos);
        st_name=m.getstock_symbol();

        //Toast.makeText(v.getContext(), "LONG " + m.toString(), Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.delete_icon);

        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Stock m = stockList.get(pos);
                st_name=m.getstock_name();
                stockList.remove(pos);
                stockDatabaseHandler.deleteStock(m.getstock_name());
                mAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setMessage("Delete Stock Symbol "+st_name+" ?");
        builder.setTitle("Delete Stock");
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }
}
