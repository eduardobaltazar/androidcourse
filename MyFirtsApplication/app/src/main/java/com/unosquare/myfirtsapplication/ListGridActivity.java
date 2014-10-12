package com.unosquare.myfirtsapplication;

/**
 * Created by admin on 11/10/2014.
 */
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListGridActivity extends Activity {
    Button btn;
    ListView list;
    ListGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_grid);

        initUI();
        setAdapter();
        setClick();
    }

    private List<Model> getDummyData() {
        List<Model> list = new ArrayList<Model>();

        Model model = new Model();
        model.setUsername("Ana Maria");
        model.setAddress("La misma 332");
        model.setPhone("2341324");
        list.add(model);
        list.add(model);
        list.add(model);
        list.add(model);
        list.add(model);
        list.add(model);
        list.add(model);
        list.add(model);
        list.add(model);

        return list;
    }

    private List<Model> getDummyDataUpdate() {
        List<Model> list = new ArrayList<Model>();

        Model model = new Model();
        model.setUsername("Pancha");
        model.setAddress("La Otra 74");
        model.setPhone("879373467");
        list.add(model);
        list.add(model);
        list.add(model);
        list.add(model);
        list.add(model);
        list.add(model);
        list.add(model);
        list.add(model);
        list.add(model);

        return list;
    }

    private void setClick() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.setData(getDummyDataUpdate());
                adapter.notifyDataSetChanged();

            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ListGridActivity.this, "item Click:" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter() {
        adapter = new ListGridAdapter(this, getDummyData());
        list.setAdapter(adapter);
    }

    private void initUI() {
        list = (ListView) findViewById(R.id.list);
        btn = (Button) findViewById(R.id.btn_update);
    }

    private class ListGridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<Model> data;

        public void setData(List<Model> data) {
            this.data = data;
        }

        public ListGridAdapter(Context ctx, List<Model> data) {
            inflater = LayoutInflater.from(ctx);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
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
            Model item = (Model) getItem(pos);

            holder.phone.setText(item.getPhone());
            holder.address.setText(item.getAddress());
            holder.userName.setText(item.getUsername());
        }

        private void initUIAdapter(ViewHolder holder, View view) {
            holder.userName = (TextView) view.findViewById(R.id.text_username);
            holder.address = (TextView) view.findViewById(R.id.text_address);
            holder.phone = (TextView) view.findViewById(R.id.text_phone);
        }

        private class ViewHolder {
            TextView userName;
            TextView address;
            TextView phone;
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }
    }
}