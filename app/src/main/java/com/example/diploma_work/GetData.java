package com.example.diploma_work;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetData {
    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;




    public List<Users> getUsers() {

        List<Users> user = null;
        user = new ArrayList<Users>();
        try
        {
            ConnectionHelper conStr=new ConnectionHelper();
            connect =conStr.connectionclass();
            if (connect == null)
            {
                ConnectionResult = "Check Your Internet Access!";
            }
            else
            {

                String query = "select * from Users ";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()){
                    Users users =new Users();
                    users.id = rs.getInt("Id");
                    users.Name =rs.getString("Name");
                    users.Email =rs.getString("Email");
                    users.Password =rs.getString("Password");
                    user.add(users);
                }


                ConnectionResult = " successful";
                isSuccess=true;
                connect.close();
            }
        }
        catch (Exception ex)
        {
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }

        return user;
    }




    public List<Words> getWords(Context context, String selectedCategories) {
        List<Words> words = new ArrayList<>();

        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String query = "SELECT * FROM " + selectedCategories + " WHERE Is_completed = " + 0;
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Words word = new Words();
                int categoryNameIndex = cursor.getColumnIndex("CategoryName");
                if (categoryNameIndex != -1) {
                    word.CategoryName = cursor.getString(categoryNameIndex);
                }

                int wordsIndex = cursor.getColumnIndex("Words");
                if (wordsIndex != -1) {
                    word.Words = cursor.getString(wordsIndex);
                }

                int translateWordsIndex = cursor.getColumnIndex("TranslateWords");
                if (translateWordsIndex != -1) {
                    word.TranslateWords = cursor.getString(translateWordsIndex);
                }

                int sentenceIndex = cursor.getColumnIndex("Sentence");
                if (sentenceIndex != -1) {
                    word.Sentence = cursor.getString(sentenceIndex);
                }

                int transcriptionsIndex = cursor.getColumnIndex("Transcriptions");
                if (transcriptionsIndex != -1) {
                    word.Transcriptions = cursor.getString(transcriptionsIndex);
                }

                int transSentenceIndex = cursor.getColumnIndex("TransSentence");
                if (transSentenceIndex != -1) {
                    word.TransSentence = cursor.getString(transSentenceIndex);
                }

                int completedIndex = cursor.getColumnIndex("Is_completed");
                if (completedIndex != -1) {
                    word.Completed = cursor.getInt(completedIndex);
                }

                int pictureIndex = cursor.getColumnIndex("Picture");
                if (pictureIndex != -1) {
                    byte[] imageBytes = cursor.getBlob(pictureIndex);
                    word.Picture = imageBytes;
                }

                Log.e("Words", word.Words);
                Log.e("TranslateWords", word.TranslateWords);
                Log.e("Picture", Arrays.toString(word.Picture));
                Log.e("Completed", String.valueOf(word.Completed));
                Log.e("Picture", String.valueOf(word.Picture));
                words.add(word);
            }
            cursor.close();
            db.close();
        } catch (SQLiteException ex) {
            Log.e("GetData", "Error getting categories: " + ex.getMessage());
            isSuccess = false;
            ConnectionResult = ex.getMessage();
            words = null;
        }
        return words;
    }
}
