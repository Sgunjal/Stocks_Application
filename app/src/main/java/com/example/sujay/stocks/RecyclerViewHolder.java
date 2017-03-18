package com.example.sujay.stocks;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Sujay on 3/11/2017.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView stock_name;
    public TextView stock_symbol;
    public TextView stock_value;
    public TextView stock_percentage;

    public RecyclerViewHolder(View view) {
        super(view);
        stock_name = (TextView) view.findViewById(R.id.stockName);
        stock_symbol = (TextView) view.findViewById(R.id.stockSymbol);
        stock_value = (TextView) view.findViewById(R.id.stockValue);
        stock_percentage = (TextView) view.findViewById(R.id.stockPercentage);
    }
}

