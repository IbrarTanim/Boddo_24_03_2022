package net.boddo.btm.Utills;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthPreference {


    private static Context context;
    public static final String name = AuthPreference.class.getSimpleName();

    public AuthPreference(Context context) {
        this.context = context;
    }

    public static void setEmail(String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(name, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setPassword(String key,String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String getEmail(String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,0);
        return  sharedPreferences.getString(key,"");
    }

    public static String getPassword(String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,0);
        return sharedPreferences.getString(key,"");
    }
}
