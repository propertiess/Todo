package com.example.todolist;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.todolist.model.Category;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PreConfig {
    private static final String CATEGORYLIST_KEY = "categorylist_key";

    public static void writeListInPref(Context context, List<Category> categoryList){
        Gson gson = new Gson();
        String jsonString = gson.toJson(categoryList);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(CATEGORYLIST_KEY, jsonString);
        editor.apply();
    }

    public static List<Category> readListFromPref(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = pref.getString(CATEGORYLIST_KEY, "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Category>>() {}.getType();
        List<Category> categoryList = gson.fromJson(jsonString, type);

        return categoryList;
    }

}
