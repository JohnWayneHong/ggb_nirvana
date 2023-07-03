package com.jgw.common_library.utils;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.jgw.common_library.utils.json.JsonUtils;
import com.tencent.mmkv.MMKV;

import java.util.List;

/**
 * Created by XiongShaoWu
 * on 2019/9/26
 */
public class MMKVUtils {

    private static final String TEMP_DATA = "temp_data";//临时存储文件

    private static MMKV mmkv;

    private MMKVUtils() {
    }

    private static class SingleInstance {
        private static final MMKV INSTANCE = MMKV.defaultMMKV();
    }


    public static MMKV getInstance() {
        return SingleInstance.INSTANCE;
    }

    public static void init(Context context) {
        MMKV.initialize(context);
        mmkv = getInstance();
    }

    public static void save(String key, String value) {
        mmkv.encode(key, value);
    }

    public static void save(String key, long value) {
        mmkv.encode(key, value);
    }

    public static void save(String key, int value) {
        mmkv.encode(key, value);
    }

    public static void save(String key, double value) {
        mmkv.encode(key, value);
    }

    public static void save(String key, boolean value) {
        mmkv.encode(key, value);
    }

    public static String getString(@NonNull String key) {
        String value = mmkv.decodeString(key);
        return TextUtils.isEmpty(value) ? "" : value;
    }

    public static int getInt(@NonNull String key) {
        return mmkv.decodeInt(key, -1);
    }

    public static long getLong(@NonNull String key) {
        return mmkv.decodeLong(key, -1);
    }

    public static double getDouble(@NonNull String key) {
        return mmkv.decodeDouble(key, 0d);
    }

    public static boolean getBoolean(@NonNull String key) {
        return mmkv.decodeBool(key, false);
    }

    public static boolean getBoolean(@NonNull String key, boolean defaultValue) {
        return mmkv.decodeBool(key, defaultValue);
    }

    public static int getInt(@NonNull String key, int defaultValue) {
        return mmkv.decodeInt(key, defaultValue);
    }

    public static void saveTempData(Object object) {
        String json = JsonUtils.toJsonString(object);
        MMKVUtils.save(MMKVUtils.TEMP_DATA, json);
    }

    public static <T> T getTempData(Class<T> clazz) {
        String json = MMKVUtils.getString(MMKVUtils.TEMP_DATA);
        //noinspection UnnecessaryLocalVariable
        T object = JsonUtils.parseObject(json, clazz);
        return object;
    }

    public static <T> List<T> getTempDataList(Class<T> clazz) {
        String json = MMKVUtils.getString(MMKVUtils.TEMP_DATA);
//        MMKVUtils.save(MMKVUtils.TEMP_DATA, "");
        //noinspection UnnecessaryLocalVariable
        List<T> list = JsonUtils.parseArray(json, clazz);
        return list;
    }
}
