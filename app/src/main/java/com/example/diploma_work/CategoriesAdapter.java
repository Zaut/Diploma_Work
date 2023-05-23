package com.example.diploma_work;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class CategoriesAdapter extends BaseAdapter {
    private List<Categories> data;
    private LayoutInflater inflater;

    private FragmentManager fragmentManager;

    public CategoriesAdapter(Context context, FragmentManager fragmentManager, List<Categories> data) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        this.fragmentManager = fragmentManager;
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
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Categories category = data.get(position);

                // Создаем новый фрагмент для отображения категорий
                Fragment wordsFragment = new WordsFragment();

                // Передаем информацию о выбранном уровне во фрагмент
                Bundle args = new Bundle();
                args.putString("selectedCategories", category.CategoriesName);
                wordsFragment.setArguments(args);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Frame_Layout, wordsFragment);
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