package com.example.diploma_work;

import static androidx.core.content.ContentProviderCompat.requireContext;

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

        // Проверяем ссылку перед выполнением операций для текущего уровня
        boolean referencesExist = getData.checkCategoriesReferences(level.id);
        if (!referencesExist) {
            // Если ссылка существует, делаем кнопку активной и возвращаем ее изначальный цвет
            holder.button.setEnabled(true);
            holder.button.setBackgroundResource(R.drawable.button_registration);
        } else {
            // Если ссылки не существует, делаем кнопку неактивной и меняем ее цвет
            holder.button.setEnabled(false);
            holder.button.setBackgroundColor(Color.GRAY);
        }

        final boolean finalReferencesExist = referencesExist;
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!finalReferencesExist) {
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