package com.unosquare.actionbarnavigationdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DrawerAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    public static final String[] menu = {"Section 1", "Section 2", "Section 3", "Help"};

    public DrawerAdapter(Context context){
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return menu.length;
    }

    @Override
    public Object getItem(int position) {
        return menu[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.row_drawer, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        setText(holder, position);
        return convertView;
    }

    private void setText(ViewHolder holder, int position) {
        holder.text.setText((String) getItem(position));
    }

    private class ViewHolder{
        TextView text;
    }
}