package com.example.diploma_work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.List;

public class LevelsAdapter extends BaseAdapter {
    private List<Levels> data;
    private LayoutInflater inflater;

    public LevelsAdapter(Context context, List<Levels> data) {
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
            convertView = inflater.inflate(R.layout.level_list, parent, false);
            holder = new ViewHolder();
            holder.button = (Button) convertView.findViewById(R.id.btn_list_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Levels level = data.get(position);
        holder.button.setText(level.name);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Обработка нажатия на кнопку
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        Button button;
    }
}