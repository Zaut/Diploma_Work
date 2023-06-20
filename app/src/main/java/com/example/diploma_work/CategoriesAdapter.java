package com.example.diploma_work;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class CategoriesAdapter extends BaseAdapter {
    private List<Categories> data;
    private LayoutInflater inflater;

    private ProgressDialog progressDialog;
    private FragmentManager fragmentManager;

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
        boolean isUnique = true;
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedCategory = category.CategoriesName;
                if (GlobalVariables.listSucces.contains(selectedCategory)) {
                    showResetConfirmationDialog(selectedCategory);
                } else {
                    startWordsFragment(category.CategoriesName);
                }
            }
        });

        return convertView;
    }

    private void startWordsFragment(String selectedCategory) {
        Fragment wordsFragment = new WordsFragment();
        Bundle args = new Bundle();
        args.putString("selectedCategories", selectedCategory);
        wordsFragment.setArguments(args);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Frame_Layout, wordsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void showResetConfirmationDialog(String selectedCategory) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Начать сначала");
        builder.setMessage("Хотите начать сначала?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setTitle("Сброс данных");
                progressDialog.setMessage("Выполняется сброс данных...");
                progressDialog.setIndeterminate(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

               // new ResetDataTask().execute(selectedCategory);
            }
        });
        builder.setNegativeButton("Нет", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

//    private class ResetDataTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String  doInBackground(String... params) {
//            String selectedCategory = params[0];
//            ConnectionHelper connectionHelper = new ConnectionHelper();
//            DatabaseHelper getData = new GetData();
//            List<Words> words = getData.createNullWords(selectedCategory);
//
//            for (Words word : words) {
//                Log.e("word.CategoryName", word.CategoryName + ": ");
//                word.setCompleted(0);
//                connectionHelper.updateWordCompletionStatus(word.Words, word.getCompleted(), selectedCategory);
//                GlobalVariables.listSucces.remove(selectedCategory);
//            }
//
//            return selectedCategory;
//        }

//        @Override
//        protected void onPostExecute(String selectedCategory) {
//            progressDialog.dismiss();
//            Toast.makeText(context, "Данные успешно сброшены", Toast.LENGTH_SHORT).show();
//            startWordsFragment(selectedCategory);
//
//        }
//    }

    private static class ViewHolder {
        Button button;
    }
}