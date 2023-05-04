package com.example.diploma_work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class CategoriesAdapter extends BaseAdapter {
    private List<Categories> data;
    private LayoutInflater inflater;

    public CategoriesAdapter(Context context, List<Categories> data) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.categories_list, parent, false);
            holder = new ViewHolder();
            holder.button = convertView.findViewById(R.id.btn_categories_list_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Categories category = data.get(position);
        holder.button.setText(category.CategoriesName);

        return convertView;
    }

    private static class ViewHolder {
        Button button;
    }
}