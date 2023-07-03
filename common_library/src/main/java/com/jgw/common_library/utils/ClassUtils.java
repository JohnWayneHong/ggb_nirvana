package com.jgw.common_library.utils;


import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.AndroidViewModel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by jingbin on 2018/12/26.
 */

public class ClassUtils {

    /**
     * 获取泛型ViewModel的class对象
     */
    public static <T> Class<T> getViewModel(Object obj) {
        //noinspection unchecked
        Class<T> t= (Class<T>) AndroidViewModel.class;
        return getClassBySuperClass(obj,t);
    }

    public static <T> Class<T> getViewBinding(Object obj) {
        //noinspection unchecked
        Class<T> t= (Class<T>) ViewDataBinding.class;
        return getClassBySuperClass(obj,t);
    }

    public static <T> Class<T> getClassBySuperClass(Object obj, Class<T> clazz) {
        Class<?> currentClass = obj.getClass();
        Class<T> tClass = getGenericClass(currentClass, clazz);
        if (tClass == null || tClass == clazz) {
            return null;
        }
        return tClass;
    }

    private static <T> Class<T> getGenericClass(Class<?> klass, Class<?> filterClass) {
        Type type = klass.getGenericSuperclass();
        if (!(type instanceof ParameterizedType)) {
            return null;
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] types = parameterizedType.getActualTypeArguments();
        for (Type t : types) {
            Class<T> tClass;
            if (!(t instanceof Class)) {
                return null;
            }
            //noinspection unchecked
            tClass = (Class<T>) t;
            if (!filterClass.isAssignableFrom(tClass)) {
                continue;
            }
            return tClass;
        }
        return null;
    }
}
