package com.example.diploma_work;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class JsonBuilder {
    public String buildJsonFromDatabase() {
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connection = conStr.connectionclass();

            if (connection == null) {
                return "Check Your Internet Access!";
            }

            // Создание JSON объекта для базы данных
            JSONObject databaseJson = new JSONObject();

            // Получение данных из таблицы Levels
            JSONArray levelsArray = new JSONArray();
            String levelsQuery = "SELECT * FROM Levels";
            Statement levelsStmt = connection.createStatement();
            ResultSet levelsRs = levelsStmt.executeQuery(levelsQuery);
            while (levelsRs.next()) {
                JSONObject levelJson = new JSONObject();
                levelJson.put("Id", levelsRs.getInt("Id"));
                levelJson.put("LevelName", levelsRs.getString("LevelName"));
                levelsArray.put(levelJson);
            }
            databaseJson.put("Levels", levelsArray);

            // Получение данных из таблицы Categories
            JSONArray categoriesArray = new JSONArray();
            String categoriesQuery = "SELECT * FROM Categories";
            Statement categoriesStmt = connection.createStatement();
            ResultSet categoriesRs = categoriesStmt.executeQuery(categoriesQuery);
            while (categoriesRs.next()) {
                int categoryId = categoriesRs.getInt("Id");
                int levelsId = categoriesRs.getInt("LevelsId");
                String categoryName = categoriesRs.getString("CategoriesName");

                JSONObject categoryJson = new JSONObject();
                categoryJson.put("Id", categoryId);
                categoryJson.put("LevelsId", levelsId);
                categoryJson.put("CategoriesName", categoryName);

                // Получение данных из таблицы с именем, соответствующим CategoriesName
                JSONArray tableArray = new JSONArray();
                String tableQuery = "SELECT * FROM " + categoryName;
                Statement tableStmt = connection.createStatement();
                ResultSet tableRs = tableStmt.executeQuery(tableQuery);
                while (tableRs.next()) {
                    JSONObject tableJson = new JSONObject();
                    tableJson.put("Id", tableRs.getInt("Id"));
                    tableJson.put("CategoryName", tableRs.getString("CategoryName"));
                    tableJson.put("Words", tableRs.getString("Words"));
                    tableJson.put("Transcriptions", tableRs.getString("Transcriptions"));
                    tableJson.put("Sentence", tableRs.getString("Sentence"));
                    tableJson.put("TranslateWords", tableRs.getString("TranslateWords"));
                    tableJson.put("TransSentence", tableRs.getString("TransSentence"));
                    tableJson.put("Picture", tableRs.getString("Picture")); // Если Picture представлен в виде base64 строки
                    tableJson.put("Is_completed", tableRs.getInt("Is_completed"));

                    tableArray.put(tableJson);
                }

                // Добавление записей таблицы в категорию
                categoryJson.put("Records", tableArray);

                // Добавление категории в массив категорий
                categoriesArray.put(categoryJson);
            }
            databaseJson.put("Categories", categoriesArray);

            // Получение данных из остальных таблиц
            // Добавьте аналогичный код для каждой таблицы, используя соответствующие поля

            // Преобразование JSON объекта в строку
            String jsonStr = databaseJson.toString();

            connection.close();
            return jsonStr;
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }
}