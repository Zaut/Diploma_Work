package com.example.diploma_work;

import static androidx.core.content.ContentProviderCompat.requireContext;

import static com.example.diploma_work.GlobalVariables.groups;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class LevelsAdapter extends BaseAdapter {
    private List<Levels> data;
    private LayoutInflater inflater;
    private FragmentManager fragmentManager;

    private GetData getData;

    public LevelsAdapter(FragmentActivity activity, List<Levels> data) {
        this.fragmentManager = activity.getSupportFragmentManager();
        this.data = data;
        this.inflater = LayoutInflater.from(activity);
        this.getData = new GetData();
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
        holder.button.setText(String.valueOf(level.name));

        final AtomicBoolean hasMatch = new AtomicBoolean(false);
        for (Levels item : groups) {
            if (item.id == level.id) {
                hasMatch.set(true);
                break;
            }
        }

        if (hasMatch.get()) {
            holder.button.setEnabled(false);
            holder.button.setBackgroundResource(R.drawable.inactive_button);
            holder.button.setTextColor(Color.WHITE);

        } else {
            holder.button.setEnabled(true);
            holder.button.setBackgroundResource(R.drawable.button_level);
            holder.button.setTextColor(Color.BLACK);
        }

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasMatch.get()) {
                    Levels level = data.get(position);
                    Fragment categoriesFragment = new CategoriesFragment();
                    GlobalVariables.globalSelectedLevel = level.id;

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.Frame_Layout, categoriesFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    // Обработка случая, когда ссылки не существуют
                    // ...
                }
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        Button button;
    }
}