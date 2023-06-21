package com.example.diploma_work;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
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

                // Проверка наличия дублирования LevelName
                Cursor cursor = db.rawQuery("SELECT * FROM Levels WHERE LevelName = '" + levelName + "'", null);
                if (cursor.getCount() == 0) {
                    // Если запись с таким именем не найдена, выполняется вставка
                    db.execSQL("INSERT INTO Levels (Id, LevelName) VALUES (" + id + ", '" + levelName + "')");
                }
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

                    // Get the picture string from the JSON object
                    String pictureString = recordJson.getString("Picture").replace("'", "''");
                    // Decode the picture string from Base64 to byte array
                    byte[] pictureBytes = Base64.decode(pictureString, Base64.DEFAULT);
                    Log.d("Debug", "pictureBytesDBHelper: " + Arrays.toString(pictureBytes));

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
                            translateWords + "', '" + transSentence + "', ?, " + isCompleted + ")";
                    db.execSQL(insertRecordQuery, new Object[]{pictureBytes});
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

    public List<Levels> getGroups(Context context) {
        List<Levels> data = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        if (db == null) {
            // Handle database connection error
            return data;
        }

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM Levels ORDER BY Id ASC";
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("Id");
                int nameIndex = cursor.getColumnIndex("LevelName");

                do {
                    if (idIndex != -1 && nameIndex != -1) {
                        Levels level = new Levels();
                        level.id = cursor.getInt(idIndex);
                        level.name = cursor.getString(nameIndex);
                        data.add(level);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Log.e("getGroups", "Error getting groups: " + ex.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return data;
    }



    public List<Words> createNullWords(String selectedCategories) {
        List<Words> words = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        if (db == null) {
            // Обработка ошибки подключения к базе данных
            return words;
        }

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + selectedCategories;
            cursor = db.rawQuery(query, null);

            int categoryNameIndex = cursor.getColumnIndex("CategoryName");
            int wordsIndex = cursor.getColumnIndex("Words");
            int translateWordsIndex = cursor.getColumnIndex("TranslateWords");
            int sentenceIndex = cursor.getColumnIndex("Sentence");
            int transcriptionsIndex = cursor.getColumnIndex("Transcriptions");
            int transSentenceIndex = cursor.getColumnIndex("TransSentence");
            int completedIndex = cursor.getColumnIndex("Is_completed");
            int pictureIndex = cursor.getColumnIndex("Picture");

            while (cursor.moveToNext()) {
                Words word = new Words();
                if (categoryNameIndex >= 0) {
                    word.CategoryName = cursor.getString(categoryNameIndex);
                }
                if (wordsIndex >= 0) {
                    word.Words = cursor.getString(wordsIndex);
                }
                if (translateWordsIndex >= 0) {
                    word.TranslateWords = cursor.getString(translateWordsIndex);
                }
                if (sentenceIndex >= 0) {
                    word.Sentence = cursor.getString(sentenceIndex);
                }
                if (transcriptionsIndex >= 0) {
                    word.Transcriptions = cursor.getString(transcriptionsIndex);
                }
                if (transSentenceIndex >= 0) {
                    word.TransSentence = cursor.getString(transSentenceIndex);
                }
                if (completedIndex >= 0) {
                    word.Completed = cursor.getInt(completedIndex);
                }
                if (pictureIndex >= 0) {
                    byte[] imageBytes = cursor.getBlob(pictureIndex);
                    word.Picture = imageBytes;
                }

                Log.e("Words",  word.Words );
                Log.e("TranslateWords",  word.TranslateWords );
                Log.e("TranslateWords", Arrays.toString(word.Picture));
                Log.e("Completed", String.valueOf(word.Completed));
                words.add(word);
            }
        } catch (Exception ex) {
            Log.e("createNullWords", "Error getting words: " + ex.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return words;
    }


    public void updateWordCompletionStatus(String word, int completed, String category) {
        SQLiteDatabase db = getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put("Is_completed", completed);

            String whereClause = "Words = ?";
            String[] whereArgs = {word};

            int rowsAffected = db.update(category, values, whereClause, whereArgs);
            if (rowsAffected > 0) {
                Log.d("Update Success", "Updated word completion status");
            } else {
                Log.d("Update Failed", "No matching word found");
            }
        } catch (Exception e) {
            Log.e("Error updating word", e.getMessage());
        } finally {
            db.close();
        }
    }


    public List<Integer> getAllLevelIds() {
        List<Integer> levelIds = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        if (db == null) {
            // Обработка ошибки подключения к базе данных
            return levelIds;
        }

        Cursor cursor = null;
        try {
            String query = "SELECT Id FROM Levels";
            cursor = db.rawQuery(query, null);

            int columnIndex = cursor.getColumnIndex("Id");
            if (columnIndex != -1) {
                if (cursor.moveToFirst()) {
                    do {
                        int levelId = cursor.getInt(columnIndex);
                        levelIds.add(levelId);
                    } while (cursor.moveToNext());
                }
            } else {
                Log.e("getAllLevelIds", "Column 'Id' not found in the Cursor");
            }
        } catch (Exception ex) {
            Log.e("getAllLevelIds", "Error getting level IDs: " + ex.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return levelIds;
    }

    public boolean checkCategoriesReferences(int levelId) {
        boolean allReferencesExist = true;
        SQLiteDatabase db = getReadableDatabase();

        if (db == null) {
            // Обработка ошибки подключения к базе данных
            return false;
        }

        Cursor cursor = null;
        try {
            String query = "SELECT Categories.LevelsId FROM Categories LEFT JOIN Levels ON Categories.LevelsId = Levels.Id WHERE Levels.Id = ?";
            String[] selectionArgs = {String.valueOf(levelId)};
            cursor = db.rawQuery(query, selectionArgs);

            if (cursor.moveToFirst()) {
                allReferencesExist = false;
            }
        } catch (Exception ex) {
            Log.e("checkCategoriesReferences", "Error checking category references: " + ex.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return allReferencesExist;
    }



}