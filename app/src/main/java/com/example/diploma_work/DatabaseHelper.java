package com.example.diploma_work;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        JsonBuilder jsonBuilder = new JsonBuilder();
        String jsonStr = jsonBuilder.buildJsonFromDatabase();
        createTables(db, jsonStr);
    }

    public void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    public void createTables(SQLiteDatabase db, String jsonStr) {
        // Создание таблицы Levels
        db.execSQL("CREATE TABLE IF NOT EXISTS Levels (Id INTEGER, LevelName TEXT)");


        // Создание таблицы Categories
        db.execSQL("CREATE TABLE IF NOT EXISTS Categories (Id INTEGER, LevelsId INTEGER, CategoriesName TEXT)");

        // Создание других необходимых таблиц

        try {
            JSONObject databaseJson = new JSONObject(jsonStr);

            // Заполнение таблицы Levels
            JSONArray levelsArray = databaseJson.getJSONArray("Levels");
            for (int i = 0; i < levelsArray.length(); i++) {
                JSONObject levelJson = levelsArray.getJSONObject(i);
                int id = levelJson.getInt("Id");
                String levelName = levelJson.getString("LevelName");
                db.execSQL("INSERT INTO Levels (Id, LevelName) VALUES (" + id + ", '" + levelName + "')");
            }

            // Заполнение таблицы Categories и связанных таблиц
            JSONArray categoriesArray = databaseJson.getJSONArray("Categories");
            for (int i = 0; i < categoriesArray.length(); i++) {
                JSONObject categoryJson = categoriesArray.getJSONObject(i);
                int categoryId = categoryJson.getInt("Id");
                int levelsId = categoryJson.getInt("LevelsId");
                String categoryName = categoryJson.getString("CategoriesName");

                // Проверка наличия дублирования CategoriesName
                Cursor cursor = db.rawQuery("SELECT * FROM Categories WHERE CategoriesName = '" + categoryName + "'", null);
                if (cursor.getCount() == 0) {
                    // Если запись с таким именем не найдена, выполняется вставка
                    db.execSQL("INSERT INTO Categories (Id, LevelsId, CategoriesName) VALUES (" + categoryId + ", " + levelsId + ", '" + categoryName + "')");
                }

                // Заполнение связанных таблиц для каждой категории
                JSONArray recordsArray = categoryJson.getJSONArray("Records");
                for (int j = 0; j < recordsArray.length(); j++) {
                    JSONObject recordJson = recordsArray.getJSONObject(j);
                    int recordId = recordJson.getInt("Id");


                    String recordCategoryName = recordJson.getString("CategoryName").replace("'", "''");
                    String words = recordJson.getString("Words").replace("'", "''");
                    String transcriptions = recordJson.getString("Transcriptions").replace("'", "''");
                    String sentence = recordJson.getString("Sentence").replace("'", "''");
                    String translateWords = recordJson.getString("TranslateWords").replace("'", "''");
                    String transSentence = recordJson.getString("TransSentence").replace("'", "''");
                    String picture = recordJson.getString("Picture").replace("'", "''");


                    int isCompleted = recordJson.getInt("Is_completed");

                    // Создание таблицы для текущей записи
                    String createTableQuery = "CREATE TABLE IF NOT EXISTS " + categoryName + " (" +
                            "Id INTEGER PRIMARY KEY, " +
                            "CategoryName TEXT, " +
                            "Words TEXT, " +
                            "Transcriptions TEXT, " +
                            "Sentence TEXT, " +
                            "TranslateWords TEXT, " +
                            "TransSentence TEXT, " +
                            "Picture TEXT, " +
                            "Is_completed INTEGER)";
                    db.execSQL(createTableQuery);

                    // Вставка данных в таблицу текущей записи
                    String insertRecordQuery = "REPLACE INTO " + categoryName + " (Id, CategoryName, Words, Transcriptions, Sentence, " +
                            "TranslateWords, TransSentence, Picture, Is_completed) VALUES (" +
                            recordId + ", '" + recordCategoryName + "', '" + words + "', '" + transcriptions + "', '" + sentence + "', '" +
                            translateWords + "', '" + transSentence + "', '" + picture + "', " + isCompleted + ")";
                    db.execSQL(insertRecordQuery);
                }
            }

            // Заполнение остальных таблиц

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public boolean checkDatabaseExists(Context context) {
        SQLiteDatabase db = null;
        try {
            String path = context.getDatabasePath(DATABASE_NAME).getPath();
            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            // База данных не существует
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return db != null;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Обновление базы данных при необходимости
    }



    public List<Categories> getData(int selectedLevelId) {
        Log.e("GetData", "+1 TTTTTTTTTTTTTTTTTTT ");
        List<Categories> categories = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        if (db == null) {
            // Handle database connection error
            return categories;
        }

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM Categories WHERE LevelsId = " + selectedLevelId;
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                int categoryNameIndex = cursor.getColumnIndex("CategoriesName");

                do {
                    if (categoryNameIndex != -1) {
                        Categories category = new Categories();
                        category.CategoriesName = cursor.getString(categoryNameIndex);
                        categories.add(category);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Log.e("GetData", "Error getting categories: " + ex.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        Log.d("Categories", "Categories size: " + categories.size());
        for (Categories category : categories) {
            Log.d("Categories", "Category Name: " + category.CategoriesName);
        }
        return categories;
    }
}