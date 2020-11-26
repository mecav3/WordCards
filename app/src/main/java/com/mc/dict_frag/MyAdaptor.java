package com.mc.dict_frag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdaptor extends BaseAdapter {
    private final ArrayList<String> items;
    private final LayoutInflater layoutInflater;

    public MyAdaptor(Context context, ArrayList<String> itemss) {
        this.items = itemss;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public String getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder holder;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.list_row, null);
            holder = new ViewHolder();
            holder.kelime_ = v.findViewById(R.id.listview1);
            holder.anlam_ = v.findViewById(R.id.listview2);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        String[] ay = getItem(position).split("£", 2);
        holder.kelime_.setText(ay[0].toUpperCase());
        holder.anlam_.setText("--> ".concat(ay[1]));
        return v;
    }

    private static class ViewHolder {
        TextView kelime_;
        TextView anlam_;
    }

}
