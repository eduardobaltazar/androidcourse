package com.unosquare.finalapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by admin on 18/10/2014.
 */
public class ForecastAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ModelGet.Query.Results.Channel.Item.Forecast[] data;

    public ForecastAdapter(Context ctx, ModelGet.Query.Results.Channel.Item.Forecast[] data) {
        inflater = LayoutInflater.from(ctx);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.list_row, null);
            holder = new ViewHolder();
            initUIAdapter(holder, view);
            view.setTag(holder);
            Log.d("tag", "creando layout:" + position);
        } else {
            Log.d("tag", "reciclando");
        }
        holder = (ViewHolder) view.getTag();
        setText(holder, position);

        return view;
    }

    private void setText(ViewHolder holder, int pos) {
        ModelGet.Query.Results.Channel.Item.Forecast item = (ModelGet.Query.Results.Channel.Item.Forecast) getItem(pos);

        holder.text_code.setText(item.getCode());
        holder.text_date.setText(item.getText());
        holder.text_day.setText(item.getDay());
        holder.text_high.setText(item.getHigh());
        holder.text_low.setText(item.getLow());
        holder.text_text.setText(item.getText());
    }

    private void initUIAdapter(ViewHolder holder, View view) {
        holder.text_code = (TextView) view.findViewById(R.id.text_code);
        holder.text_date = (TextView) view.findViewById(R.id.text_date);
        holder.text_day = (TextView) view.findViewById(R.id.text_day);
        holder.text_high = (TextView) view.findViewById(R.id.text_high);
        holder.text_low = (TextView) view.findViewById(R.id.text_low);
        holder.text_text = (TextView) view.findViewById(R.id.text_text);
    }

    private class ViewHolder {
        TextView text_code;
        TextView text_date;
        TextView text_day;
        TextView text_high;
        TextView text_low;
        TextView text_text;
    }

    @Override
    public Object getItem(int i) {
        return data[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
