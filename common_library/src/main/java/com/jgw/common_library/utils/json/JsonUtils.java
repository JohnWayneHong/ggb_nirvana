package com.jgw.common_library.utils.json;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.PropertyFilter;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private final static List<Class<?>> mClassFilter = new ArrayList<>();

    /**
     * 对象序列化为JSON字符串
     * 仅支持字段为基本数据类型或引用对象中字段也全部为基本数据类型的对象
     * 不能包含公共的非基本数据类型的get开头的方法(如Drawable(崩溃)(替换为_get),ToOne(非target类型)等)
     *
     * @return jsonString
     */
    public static String toJsonString(Object object) {
        //noinspection IfStatementWithIdenticalBranches
        if (object instanceof List) {
            return JSONArray.toJSONString(object, getFilter());
        } else {
            return JSON.toJSONString(object, getFilter());
        }
    }

    private static PropertyFilter getFilter() {
        PropertyFilter filter = null;
        if (!mClassFilter.isEmpty()) {
            filter = (object, name, value) -> {
                for (Class<?> c : mClassFilter) {
                    if (c.isAssignableFrom(object.getClass())) {
                        return false;
                    }
                }
                return true;
            };

        }
        return filter;
    }

    public static void addFilterClass(Class<?> clazz) {
        mClassFilter.add(clazz);
    }

    public static void clearFilterClass() {
        mClassFilter.clear();
    }

    @Nullable
    public static <T> T parseObject(String json, Class<T> clazz) {
        JsonObject jsonObject = parseObject(json);
        if (jsonObject==null){
            return null;
        }
        return jsonObject.toJavaObject(clazz);
    }

    @Nullable
    public static JsonObject parseObject(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return new JsonObject(json);
    }

    @Nullable
    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        JsonArray jsonArray = parseArray(json);
        if (jsonArray==null){
            return null;
        }
        return jsonArray.toJavaList(clazz);
    }

    @Nullable
    public static JsonArray parseArray(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        if (TextUtils.isEmpty(json) || TextUtils.equals("{}", json)) {
            return new JsonArray();
        }
        return new JsonArray(json);
    }

}
