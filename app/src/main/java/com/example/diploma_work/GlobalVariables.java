package com.example.diploma_work;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GlobalVariables {

    public static int globalSelectedLevel;

    public static  String globalLogin;

    public static List<String> listSucces = new ArrayList<>();

    public static  List<Levels> groups = new ArrayList<>();

    public static void addGroup(Levels level) {
        groups.add(level);
    }

    public static List<Levels> getGroups() {
        return groups;
    }


    private static final String PREF_SELECTED_LEVEL = "pref_selected_level";
    private static final String PREF_LOGIN = "pref_login";
    private static final String PREF_LIST_SUCCESS = "pref_list_success";

    public static void saveState(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(PREF_SELECTED_LEVEL, globalSelectedLevel);
        editor.putString(PREF_LOGIN, globalLogin);
        editor.putStringSet(PREF_LIST_SUCCESS, new HashSet<>(listSucces));

        editor.apply();
    }

    public static void loadState(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        globalSelectedLevel = sharedPreferences.getInt(PREF_SELECTED_LEVEL, 0);
        globalLogin = sharedPreferences.getString(PREF_LOGIN, "");
        Set<String> successSet = sharedPreferences.getStringSet(PREF_LIST_SUCCESS, new HashSet<>());
        listSucces = new ArrayList<>(successSet);
    }
}
