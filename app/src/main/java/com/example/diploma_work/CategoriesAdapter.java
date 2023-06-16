package com.example.diploma_work;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

    private String name;

    private Context context;

    public CategoriesAdapter(Context context, FragmentManager fragmentManager, List<Categories> data) {
        this.context = context;
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

        if (GlobalVariables.listSucces.contains(category.CategoriesName)) {

            holder.button.setBackgroundResource(R.drawable.button_succes);

        } else {

            holder.button.setBackgroundResource(R.drawable.button_level);

        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String selectedCategory = category.CategoriesName;
                if (GlobalVariables.listSucces.contains(selectedCategory)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Начать сначала");
                    builder.setMessage("Хотите начать сначала?");
                    builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ConnectionHelper connectionHelper = new ConnectionHelper();
                            GetData getData = new GetData();
                            List<Words> words = getData.createNullWords(category.CategoriesName);

                            for (Words word : words) {
                                Log.e("word.CategoryName", word.CategoryName + ": ");
                                word.setCompleted(0);
                                connectionHelper.updateWordCompletionStatus(word.Words, word.getCompleted(), selectedCategory);
                                GlobalVariables.listSucces.remove(selectedCategory);

                            }
                            Categories category = data.get(position);
                            Fragment wordsFragment = new WordsFragment();
                            Bundle args = new Bundle();
                            args.putString("selectedCategories", category.CategoriesName);
                            wordsFragment.setArguments(args);

                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.Frame_Layout, wordsFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();


                        }
                    });
                    builder.setNegativeButton("Нет", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else{
                    Categories category = data.get(position);


                Fragment wordsFragment = new WordsFragment();
                Bundle args = new Bundle();
                args.putString("selectedCategories", category.CategoriesName);
                wordsFragment.setArguments(args);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Frame_Layout, wordsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                }
            }

        });
//                Categories category = data.get(position);
//
//                // Создаем новый фрагмент для отображения категорий
//                Fragment wordsFragment = new WordsFragment();
//
//                // Передаем информацию о выбранном уровне во фрагмент
//                Bundle args = new Bundle();
//                args.putString("selectedCategories", category.CategoriesName);
//                wordsFragment.setArguments(args);
//
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.Frame_Layout, wordsFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//            }
//        });
        return convertView;
    }

    private static class ViewHolder {
        Button button;
    }

    private void iterateList() {
        for (String element : GlobalVariables.listSucces)
        {

        }
    }

}