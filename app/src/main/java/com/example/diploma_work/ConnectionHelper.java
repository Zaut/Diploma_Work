package com.example.diploma_work;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionHelper {
    String ip, db, DBUserNameStr, DBPasswordStr;

    @SuppressLint("NewApi")
    public Connection connectionclass() {


//       //SMARTER ASP
        //"Data Source=SQL5110.site4now.net;Initial Catalog=db_a9a0f7_diplomawork;User Id=db_a9a0f7_diplomawork_admin;Password=uchiha322

        ip = "SQL5110.site4now.net";
        db = "db_a9a0f7_diplomawork";
        DBUserNameStr = "db_a9a0f7_diplomawork_admin";
        DBPasswordStr = "uchiha322";


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        java.sql.Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + ip + ";databaseName=" + db + ";user=" + DBUserNameStr + ";password=" + DBPasswordStr + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException se) {
            Log.e("error here 1 : ", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("error here 2 : ", e.getMessage());
        } catch (Exception e) {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }



    public List<Levels> getGroups(Context context) {
        List<Levels> data = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

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


    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                Log.e("Error closing connection", e.getMessage());
            }
        }
    }

//    public void updateWordCompletionStatus(String word, int completed, String category) {
//        Connection connection = connectionclass();
//        try {
//            String updateQuery = "UPDATE " + category + " SET is_completed = ? WHERE Words = ?";
//            PreparedStatement statement = connection.prepareStatement(updateQuery);
//            statement.setInt(1, completed);
//            statement.setString(2, word);
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            Log.e("Error updating word", e.getMessage());
//        } finally {
//            closeConnection(connection);
//        }
//    }
}