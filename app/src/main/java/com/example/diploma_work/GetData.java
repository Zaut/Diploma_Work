package com.example.diploma_work;

import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
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
            connect =conStr.connectionclass();        // Connect to database
            if (connect == null)
            {
                ConnectionResult = "Check Your Internet Access!";
            }
            else
            {
                // Change below query according to your own database.
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
//                String query = "SELECT * FROM Categories  WHERE CategoryName = " + selectedCategories;
                String query = "SELECT * FROM " + selectedCategories;
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    Words word = new Words();
                    word.Words = rs.getString("Words");
                    Log.e("Words",  word.Words );
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
