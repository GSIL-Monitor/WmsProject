package com.wanhao.wms.utils;

import com.google.gson.Gson;
import com.wanhao.wms.C;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Administrator on 2017-2-23.
 */
public class DataUtils {

    public static <T> T getResultObj(Object obj , Class clazzT){

            T t = (T) C.sGson.fromJson(C.sGson.toJson(obj),clazzT);
        return t;
    }

    public static <T>ArrayList<T> getArrayResult(Object obj,Class clazzT){
        Gson gson = new Gson();
        ArrayList<Map<?,?>> al = (ArrayList<Map<?, ?>>) obj;
        if (al == null){
            return null;
        }
        if (al.size() == 0){
            return null;
        }
        ArrayList<T> list = new ArrayList<>();
        int size = al.size();
        for (int i = 0; i < size; i++) {
            Map<?,?> map = al.get(i);
            T t = (T) gson.fromJson(gson.toJson(map),clazzT);
            list.add(t);
        }
        return list;
    }
    public static <T>LinkedList<T> getLinkedResult(Object obj, Class clazzT){
        Gson gson = new Gson();
        ArrayList<Map<?,?>> al = (ArrayList<Map<?, ?>>) obj;
        if (al == null){
            return null;
        }
        if (al.size() == 0){
            return null;
        }
        LinkedList<T> list = new LinkedList<>();
        int size = al.size();
        for (int i = 0; i < size; i++) {
            Map<?,?> map = al.get(i);
            T t = (T) gson.fromJson(gson.toJson(map),clazzT);
            list.add(t);
        }
        return list;
    }
}
