package com.example.diploma_work;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity  {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Context appContext = getApplicationContext();

        // Использование контекста приложения
        GlobalVariables.loadState(appContext);
        setContentView(R.layout.activity_main);


//        MyAsyncTask myAsyncTask = new MyAsyncTask();
//        myAsyncTask.execute();

//        FragmentRegistration fragmentRegistration = new FragmentRegistration();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.my_fragment, myFragment);
//        fragmentTransaction.commit();
//
        FragmentSignIn fragmentSignIn = new FragmentSignIn();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Frame_Layout, fragmentSignIn);
        ft.commit();


        JsonBuilder jsonBuilder = new JsonBuilder();
        String jsonStr = jsonBuilder.buildJsonFromDatabase();

        // Вывод JSON-строки в Logcat
        int maxLength = 4000;
        for (int i = 0; i < jsonStr.length(); i += maxLength) {
            int endIndex = Math.min(i + maxLength, jsonStr.length());
            String part = jsonStr.substring(i, endIndex);
            Log.d("JSON", part);
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this); // Передайте контекст активности
        boolean isDatabaseExists = dbHelper.checkDatabaseExists(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (isDatabaseExists) {
            Toast.makeText(this, "База данных уже существует", Toast.LENGTH_SHORT).show();
        } else {
            // Создать новую базу данных
            dbHelper.createTables(db, jsonStr);
        }




        // dbHelper.deleteDatabase(this);

    }


//    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... voids) {
//            GetData dataGetter = new GetData();
//            List<Integer> levelIds = dataGetter.getAllLevelIds();
//
//            for (int levelId : levelIds) {
//                boolean referencesExist = dataGetter.checkCategoriesReferences(levelId);
//                if (referencesExist) {
//                    Levels level = new Levels();
//                    level.id = levelId;
//                    GlobalVariables.addGroup(level);
//                    Log.d("MyTag", "Added level with id: " + levelId);
//                }
//            }
//
//            return null;
//        }
//    }
}