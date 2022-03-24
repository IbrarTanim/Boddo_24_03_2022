package net.boddo.btm.Utills;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private static Context context;


    public SharedPref(Context context) {
        this.context = context;
    }

    public final static String PREFS_NAME = SharedPref.class.getSimpleName();

    public static void setString(Context context, String country, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(country,value);
        editor.apply();
    }

    public static String getString(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME,0);
        return  sharedPreferences.getString(key,"");
    }

    public void setHasClickedSkipOrLetsGoButton(String key, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getHasClickedSkipOrLetsGoButton(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getBoolean(key, false);
    }

    public static void setUserName(String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getUserName(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(key, "");
    }

    public static void setLikeCount(String key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getLikeCount(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(key, 0);
    }

    public static void setFavoriteCount(String key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getFavoriteCount(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(key, 0);
    }

    public static void setVisitorCount(String key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getVisitorCount(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(key, 0);
    }

    public static void setIsLoggedIn(String key, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getIsLoggedIn(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getBoolean(key, false);
    }

    public static void setUserAccessToken(String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getUserAccessToken(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(key, "");
    }

    public static void setFCMToken(String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getFCMToken(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(key, "");
    }

    public static void setUserId(String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getUserId(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(key, "");
    }

    public static void setUserEmail(String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }


    public static String getUserEmail(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(key, "");
    }

    public static void setUserPassword(String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getUserPassword(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(key, "");
    }


    public static void setLastChatRoom(String key, String lastChatRoom) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, lastChatRoom);
        editor.apply();
    }

    public static String getLastChatRoom(String key) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_NAME, 0);
        return pref.getString(key, "");
    }

    public static void setNotificationEnabled(String key, String isEnabled) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, isEnabled);
        editor.apply();
    }

    public static String getNotificationEnabled(String key) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_NAME, 0);
        return pref.getString(key, "");
    }

    public void setEmpty() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, 0);
        sharedPreferences.edit().clear().apply();
    }


    public static void setCheckBox(String key, boolean isSetCheck) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, isSetCheck);
        editor.apply();
    }

    public static Boolean getCheckBoxResult(String key) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_NAME, 0);
        return pref.getBoolean(key, false);
    }

    public static void clearIsChecked(String key) {

        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        sharedPref.edit().remove(key).commit();

    }



}