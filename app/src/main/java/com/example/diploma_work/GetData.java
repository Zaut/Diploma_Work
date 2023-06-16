package com.example.diploma_work;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
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

    public List<Levels> getGroups() {

        List<Levels> data = null;
        data = new ArrayList<Levels>();
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

                String query = "select * from Levels  ORDER BY Id ASC";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()){
                    Levels datanum=new Levels();
                    datanum.id = rs.getInt("Id");
                    datanum.name =rs.getString("LevelName");
                    data.add(datanum);
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

        return data;
    }


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



    public  List<Categories> getData(int selectedLevelId) {
        List<Categories> categories = null;
        categories = new ArrayList<Categories>();
        try {
            ConnectionHelper conStr=new ConnectionHelper();
            connect =conStr.connectionclass();
            if (connect == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                Log.e("selectedLevelId", selectedLevelId + ": ");
               String query = "SELECT * FROM Categories  WHERE LevelsId = " + selectedLevelId;
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    Categories category = new Categories();
                    category.CategoriesName = rs.getString("CategoriesName");
                    categories.add(category);
                }
                connect.close();
            }
        } catch (Exception ex) {
            Log.e("GetData", "Error getting categories: " + ex.getMessage());
            isSuccess = false;
            ConnectionResult = ex.getMessage();
            categories = null;
        }
        return categories;
    }

    public  List<Words> getWords(String selectedCategories) {
        List<Words> words = null;
        words = new ArrayList<Words>();
        try {
            ConnectionHelper conStr=new ConnectionHelper();
            connect =conStr.connectionclass();
            if (connect == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                Log.e("selectedCategories", selectedCategories + ": ");
               //String query = "SELECT * FROM Categories  WHERE CategoryName = " + selectedCategories;
               String query = "SELECT * FROM " + selectedCategories + " WHERE Is_completed = " + 0;
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    Words word = new Words();
                    word.CategoryName = rs.getString("CategoryName");
                    word.Words = rs.getString("Words");
                    word.TranslateWords = rs.getString("TranslateWords");
                    word.Sentence = rs.getString("Sentence");
                    word.Transcriptions = rs.getString("Transcriptions");
                    word.TransSentence = rs.getString("TransSentence");
                    word.Completed = rs.getInt("Is_completed");
                    byte[] imageBytes = rs.getBytes("Picture");
                    word.Picture = imageBytes;

                    Log.e("Words",  word.Words );
                    Log.e("TranslateWords",  word.TranslateWords );
                    Log.e("TranslateWords", Arrays.toString(word.Picture));
                    Log.e("Completed", String.valueOf(word.Completed));
                    words.add(word);
                }
                connect.close();
            }
        } catch (Exception ex) {
            Log.e("GetData", "Error getting categories: " + ex.getMessage());
            isSuccess = false;
            ConnectionResult = ex.getMessage();
            words = null;
        }
        return words;
    }

    public  List<Words> createNullWords(String selectedCategories) {
        List<Words> words = null;
        words = new ArrayList<Words>();
        try {
            ConnectionHelper conStr=new ConnectionHelper();
            connect =conStr.connectionclass();
            if (connect == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                Log.e("selectedCategories", selectedCategories + ": ");
//                String query = "SELECT * FROM Categories  WHERE CategoryName = " + selectedCategories;
                String query = "SELECT * FROM " + selectedCategories;
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    Words word = new Words();
                    word.CategoryName = rs.getString("CategoryName");
                    word.Words = rs.getString("Words");
                    word.TranslateWords = rs.getString("TranslateWords");
                    word.Sentence = rs.getString("Sentence");
                    word.Transcriptions = rs.getString("Transcriptions");
                    word.TransSentence = rs.getString("TransSentence");
                    word.Completed = rs.getInt("Is_completed");
                       byte[] imageBytes = rs.getBytes("Picture");
                       word.Picture = imageBytes;

                    Log.e("Words",  word.Words );
                    Log.e("TranslateWords",  word.TranslateWords );
                    Log.e("TranslateWords", Arrays.toString(word.Picture));
                    Log.e("Completed", String.valueOf(word.Completed));
                    words.add(word);
                }
                connect.close();
            }
        } catch (Exception ex) {
            Log.e("GetData", "Error getting categories: " + ex.getMessage());
            isSuccess = false;
            ConnectionResult = ex.getMessage();
            words = null;
        }
        return words;
    }
}
