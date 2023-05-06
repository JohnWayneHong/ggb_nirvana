package com.ggb.nirvanaclub.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesUtil {

    public static final String SP_COMMON_NAME = "sp_common";

    public static final String SP_REGISTER = "sp_register";

    public static final String SP_SEARCH_HISTORY = "sp_search_history";
    public static final String SP_USER_NAME = "sp_user";

    public static void putUserString(Context mContext,String key,String value){
        SharedPreferences sp = mContext.getSharedPreferences(SP_USER_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(key,value);
        ed.commit();
    }

    public static String getUserString(Context mContext,String key){
        SharedPreferences sp = mContext.getSharedPreferences(SP_USER_NAME,Context.MODE_PRIVATE);
        return sp.getString(key,"");
    }

    public static void putUserBoolean(Context mContext,String key,Boolean value){
        SharedPreferences sp = mContext.getSharedPreferences(SP_USER_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(key,value);
        ed.commit();
    }

    public static boolean getUserBoolean(Context mContext,String key){
        SharedPreferences sp = mContext.getSharedPreferences(SP_USER_NAME,Context.MODE_PRIVATE);
        return sp.getBoolean(key,true);
    }

    public static void clearUserData(Context mContext){
        SharedPreferences sp = mContext.getSharedPreferences(SP_USER_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.clear();
        ed.commit();
    }

    public static void putCommonString(Context mContext,String key,String value){
        SharedPreferences sp = mContext.getSharedPreferences(SP_COMMON_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(key,value);
        ed.commit();
    }

    public static String getCommonString(Context mContext,String key){
        SharedPreferences sp = mContext.getSharedPreferences(SP_COMMON_NAME,Context.MODE_PRIVATE);
        return sp.getString(key,"");
    }

    public static void putCommonBoolean(Context mContext,String key,boolean value){
        SharedPreferences sp = mContext.getSharedPreferences(SP_COMMON_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(key,value);
        ed.commit();
    }


    public static boolean getCommonBoolean(Context mContext,String key){
        SharedPreferences sp = mContext.getSharedPreferences(SP_COMMON_NAME,Context.MODE_PRIVATE);
        return sp.getBoolean(key,false);
    }

    public static void putCommonInt(Context mContext,String key,int value){
        SharedPreferences sp = mContext.getSharedPreferences(SP_COMMON_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(key,value);
        ed.commit();
    }

    public static int getCommonInt(Context mContext,String key){
        SharedPreferences sp = mContext.getSharedPreferences(SP_COMMON_NAME,Context.MODE_PRIVATE);
        return sp.getInt(key,0);
    }

    public static void putRegisterString(Context mContext,String key,String value){
        SharedPreferences sp = mContext.getSharedPreferences(SP_REGISTER,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(key,value);
        ed.commit();
    }

    public static String getRegisterString(Context mContext,String key){
        SharedPreferences sp = mContext.getSharedPreferences(SP_REGISTER,Context.MODE_PRIVATE);
        return sp.getString(key,"");
    }

    public static void putSearchHistoryString(Context mContext,String key,String value){
        SharedPreferences sp = mContext.getSharedPreferences(SP_SEARCH_HISTORY,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(key,value);
        ed.commit();
    }

    public static String getSearchHistoryString(Context mContext,String key){
        SharedPreferences sp = mContext.getSharedPreferences(SP_SEARCH_HISTORY,Context.MODE_PRIVATE);
        return sp.getString(key,"");
    }

    public static void clearSearchHistoryData(Context mContext){
        SharedPreferences sp = mContext.getSharedPreferences(SP_SEARCH_HISTORY,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.clear();
        ed.commit();
    }

}
