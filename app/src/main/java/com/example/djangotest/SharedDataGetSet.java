package com.example.djangotest;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedDataGetSet {
    public static String getMySavedToken(Context context){

        SharedPreferences preferences = context.getSharedPreferences("myprefs",Context.MODE_PRIVATE);
        String token_in_func = preferences.getString("token","");
        String res_token = "Token "+token_in_func;
        return res_token;
    }
}
