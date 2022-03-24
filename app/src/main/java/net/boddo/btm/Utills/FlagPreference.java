package net.boddo.btm.Utills;

import android.content.Context;
import android.content.SharedPreferences;

public class FlagPreference {

    private static Context context;
    public static final String name = FlagPreference.class.getSimpleName();

    public FlagPreference(Context context) {
        this.context = context;
    }

    public static String getFlag(String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,0);
        return  sharedPreferences.getString(key,"");
    }

    public static void setFlag(String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(name, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }


}
