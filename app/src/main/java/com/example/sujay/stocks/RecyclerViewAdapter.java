package com.example.sujay.stocks;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Sujay on 3/11/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private static final String TAG = "EmployeesAdapter";
    private List<Stock> stocksList;
    private MainActivity mainAct;

    public RecyclerViewAdapter(List<Stock> empList, MainActivity ma) {
        this.stocksList = empList;
        mainAct = ma;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        //Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_list_row, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Stock stock = stocksList.get(position);

        if(Double.toString(stock.getstock_change()).charAt(0)=='-') {
            holder.stock_percentage.setTextColor(Color.parseColor("#ffcc0000"));
            holder.stock_name.setTextColor(Color.parseColor("#ffcc0000"));
            holder.stock_symbol.setTextColor(Color.parseColor("#ffcc0000"));
            holder.stock_value.setTextColor(Color.parseColor("#ffcc0000"));
            holder.stock_percentage.setText("\u25BC"+stock.getstock_change() + "(" + stock.getstock_percentage() + "%)");
        }
        else
        {
            holder.stock_percentage.setTextColor(Color.parseColor("#ff669900"));
            holder.stock_name.setTextColor(Color.parseColor("#ff669900"));
            holder.stock_symbol.setTextColor(Color.parseColor("#ff669900"));
            holder.stock_value.setTextColor(Color.parseColor("#ff669900"));
            holder.stock_percentage.setText("\u25B2"+stock.getstock_change() + "(" + stock.getstock_percentage() + "%)");
        }

        holder.stock_name.setText(stock.getstock_name());
        holder.stock_symbol.setText(stock.getstock_symbol());
        holder.stock_value.setText(Double.toString(stock.getstock_value()));
    }
    @Override
    public int getItemCount() {
        return stocksList.size();
    }

    {
    }
}

