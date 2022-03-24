package net.boddo.btm.Utills;

import android.content.Context;
import android.content.SharedPreferences;

public class LikeUnlikePicturePrefs {

    private static Context context;
    public final static String PREFS_NAME = "LikeUnlikePicturePrefs";

    public LikeUnlikePicturePrefs(Context context) {

        this.context = context;

    }

    public static void setFlagStatus(String key, Boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }


    public static boolean getFlagStatus(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        return  sharedPreferences.getBoolean(key,false);
    }


}
