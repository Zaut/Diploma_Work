package com.example.diploma_work;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Activity;
import android.content.Context;
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

public class LevelsAdapter extends BaseAdapter {
    private List<Levels> data;
    private LayoutInflater inflater;
    private FragmentManager fragmentManager;

    public LevelsAdapter(FragmentActivity activity, List<Levels> data) {
        this.fragmentManager = activity.getSupportFragmentManager();
        this.data = data;
        this.inflater = LayoutInflater.from(activity);
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
        // holder.button.setText(level.name);
        holder.button.setText(String.valueOf(level.name));
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Levels level = data.get(position);

                // Создаем новый фрагмент для отображения категорий
                Fragment categoriesFragment = new CategoriesFragment();

                // Передаем информацию о выбранном уровне во фрагмент
                Bundle args = new Bundle();
                args.putInt("selectedLevel", level.id);
                categoriesFragment.setArguments(args);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Frame_Layout, categoriesFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        Button button;
    }
}