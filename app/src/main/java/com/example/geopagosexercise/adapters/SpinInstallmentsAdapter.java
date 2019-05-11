package com.example.geopagosexercise.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.geopagosexercise.bo.Installments;

import java.util.List;

public class SpinInstallmentsAdapter extends ArrayAdapter<Installments> {

    private Context context;
    private List<Installments> values;
    private String symbol;

    public SpinInstallmentsAdapter(Context context, int textViewResourceId,
                            List<Installments> values, String symbol) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
        this.symbol = symbol;
    }

    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public Installments getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);

        if(values.get(position).getiCount() == 1) {
            label.setText(values.get(position).getiCount() + " cuota de " + symbol + values.get(position).getiAmount() + " (" + symbol + values.get(position).getiTotalAmount() + ")");
        }else{
            label.setText(values.get(position).getiCount() + " cuotas de " + symbol + values.get(position).getiAmount() + " (" + symbol + values.get(position).getiTotalAmount() + ")");
        }
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);

        if(values.get(position).getiCount() == 1) {
            label.setText(values.get(position).getiCount() + " cuota de " + symbol + values.get(position).getiAmount() + " (" + symbol + values.get(position).getiTotalAmount() + ")");
        }else{
            label.setText(values.get(position).getiCount() + " cuotas de " + symbol + values.get(position).getiAmount() + " (" + symbol + values.get(position).getiTotalAmount() + ")");
        }

        return label;
    }
}
