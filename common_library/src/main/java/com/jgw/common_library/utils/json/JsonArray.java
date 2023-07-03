package com.jgw.common_library.utils.json;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class JsonArray {
    private final JSONArray ja;

    public JsonArray() {
        ja = new JSONArray();
    }

    protected JsonArray(String json) {
        ja = JSONArray.parseArray(json);
    }

    protected JsonArray(JSONArray jsonArray) {
        ja = jsonArray;
    }

    public void add(Object o) {
        ja.add(o);
    }

    public void add(int index, Object o) {
        ja.add(index, o);
    }

    public void addAll(List<?> list) {
        ja.addAll(list);
    }

    public void remove(Object o) {
        ja.remove(o);
    }

    public void remove(int index) {
        ja.remove(index);
    }

    public void removeAll(List<?> list) {
        ja.removeAll(list);
    }

    public String getString(int index) {
        return ja.getString(index);
    }

    public byte getByte(int index) {
        return ja.getByteValue(index);
    }

    public short getShort(int index) {
        return ja.getShortValue(index);
    }

    public int getInt(int index) {
        return ja.getIntValue(index);
    }

    public long getLong(int index) {
        return ja.getLongValue(index);
    }

    public float getFloat(int index) {
        return ja.getFloatValue(index);
    }

    public double getDouble(int index) {
        return ja.getDoubleValue(index);
    }

    public boolean getBoolean(int index) {
        return ja.getBooleanValue(index);
    }

    public int size() {
        return ja.size();
    }

    @Nullable
    public JsonObject getJsonObject(int index) {
        JSONObject jsonObject = ja.getJSONObject(index);
        if (jsonObject == null) {
            return null;
        }
        return new JsonObject(jsonObject);
    }

    @Nullable
    public JsonArray getJsonArray(int index) {
        JSONArray jsonArray = ja.getJSONArray(index);
        if (jsonArray == null) {
            return null;
        }
        return new JsonArray(jsonArray);
    }

    public <T> List<T> toJavaList(Class<T> clazz) {
        if (ja == null) {
            return null;
        }
        return ja.toJavaList(clazz);
    }
}
