package com.example.diploma_work;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GetData {
    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

//    public List<Levels> getGroups() {
//        List<Levels> data = null;
//        data = new ArrayList<Levels>();
//        try {
//            ConnectionHelper conStr = new ConnectionHelper();
//            connect = conStr.connectionclass(); // Connect to database
//            if (connect == null) {
//                ConnectionResult = "Check Your Internet Access!";
//            } else {
//                // Change below query according to your own database.
//                String query = "SELECT * FROM Levels";
//                Statement stmt = connect.createStatement();
//                ResultSet rs = stmt.executeQuery(query);
//                while (rs.next()) {
//                    Levels datanum = new Levels();
//                    datanum.setId(rs.getInt("LevelID"));
//                    datanum.setName(rs.getString("LevelName"));
//
//                    // Get categories for the level
//                    List<Categories> categories = new ArrayList<>();
//                    String categoriesQuery = "SELECT * FROM Categories WHERE LevelID = " + datanum.getId();
//                    Statement categoriesStmt = connect.createStatement();
//                    ResultSet categoriesRs = categoriesStmt.executeQuery(categoriesQuery);
//                    while (categoriesRs.next()) {
//                        Categories category = new Categories();
//                        category.setId(categoriesRs.getInt("CategoryID"));
//                        category.setName(categoriesRs.getString("CategoryName"));
//                        categories.add(category);
//                    }
//                    categoriesStmt.close();
//                    categoriesRs.close();
//
//                    datanum.setCategories(categories);
//                    data.add(datanum);
//                }
//
//                ConnectionResult = " successful";
//                isSuccess = true;
//                connect.close();
//            }
//        } catch (Exception ex) {
//            isSuccess = false;
//            ConnectionResult = ex.getMessage();
//        }
//
//        return data;
//    }

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
                String query = "select * from Levels";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()){
                    Levels datanum=new Levels();
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
    private List<Categories> getData(Levels selectedLevel) {
        // Получаем список категорий из базы данных, используя выбранный уровень
        List<Categories> categories = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclass();
            if (connect == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                // Получаем список категорий из базы данных, соответствующих выбранному уровню
                String query = "SELECT * FROM Categories WHERE LevelsId = " + selectedLevel.id;
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    Categories category = new Categories();
                    category.Id = rs.getInt("Id");
                    category.CategoriesName = rs.getString("CategoryName");
                    categories.add(category);
                }
                connect.close();
            }
        } catch (Exception ex) {
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        return categories;
    }
}
