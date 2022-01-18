package com.novartis.winnovators.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserUtils {

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
    }

    public static void setAccessToken(Context context, String accessToken) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("accessToken", "Bearer "+accessToken);
        editor.apply();
    }

    public static String getAccessToken(Context context) {
        return getSharedPreferences(context).getString("accessToken", "default");
    }

    public static void setLoginData(Context context,String loginName,String loginPassword){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("loginName", loginName);
        editor.putString("loginPassword", loginPassword);
        editor.apply();
    }

    public static String getLoginName(Context context) {
        return getSharedPreferences(context).getString("loginName", "");
    }

    public static String getLoginPassword(Context context) {
        return getSharedPreferences(context).getString("loginPassword", "");
    }

    public static void setUserId(Context context, int user_id) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt("user_id", user_id);
        editor.apply();
    }

    public static int getUserId(Context context) {
        return getSharedPreferences(context).getInt("user_id", 0);
    }

    public static void setUserName(Context context,String userName){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("userName", userName);
        editor.apply();
    }

    public static String getUserName(Context context) {
        return getSharedPreferences(context).getString("userName", "");
    }

    public static void setUserEmail(Context context,String userName){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("userEmail", userName);
        editor.apply();
    }

    public static String getUserEmail(Context context) {
        return getSharedPreferences(context).getString("userEmail", "");
    }

    public static void setUserPhoto(Context context,String userName){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("userPhoto", userName);
        editor.apply();
    }

    public static String getUserPhoto(Context context) {
        return getSharedPreferences(context).getString("userPhoto", "");
    }
}
