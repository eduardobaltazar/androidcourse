package com.example.eduardobaltazar.mypofin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PointsListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<PointModel> pointsList;

    public PointsListAdapter(Context ctx, ArrayList<PointModel> pointsList) {
        inflater = LayoutInflater.from(ctx);
        this.pointsList = pointsList;
    }

    @Override
    public int getCount() {
        return pointsList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.row_list_points, null);
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
        PointModel item = (PointModel) getItem(pos);

        holder.text_title.setText(item.getTitle());
    }

    private void initUIAdapter(ViewHolder holder, View view) {
        holder.text_title = (TextView) view.findViewById(R.id.text_title);
    }

    private class ViewHolder {
        TextView text_title;
    }

    @Override
    public Object getItem(int i) {
        return pointsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

}
