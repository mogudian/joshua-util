package com.mogudiandian.util.json.fastjson;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Collections;

/**
 * 快速构造JSONArray
 * @author Joshua Sun
 * @since 1.0.0
 */
public final class JSONArrayBuilder {

    private JSONArrayBuilder() {
        super();
    }

    public static JSONArray build(JSONObject o) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(o);
        return jsonArray;
    }

    public static JSONArray build(JSONObject o1, JSONObject o2) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(o1);
        jsonArray.add(o2);
        return jsonArray;
    }

    public static JSONArray build(JSONObject o1, JSONObject o2, JSONObject o3) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(o1);
        jsonArray.add(o2);
        jsonArray.add(o3);
        return jsonArray;
    }

    public static JSONArray build(JSONObject o1, JSONObject o2, JSONObject o3, JSONObject o4) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(o1);
        jsonArray.add(o2);
        jsonArray.add(o3);
        jsonArray.add(o4);
        return jsonArray;
    }

    public static JSONArray build(JSONObject o1, JSONObject o2, JSONObject o3, JSONObject o4, JSONObject o5) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(o1);
        jsonArray.add(o2);
        jsonArray.add(o3);
        jsonArray.add(o4);
        jsonArray.add(o5);
        return jsonArray;
    }

    public static JSONArray build(JSONObject o1, JSONObject o2, JSONObject o3, JSONObject o4, JSONObject o5, JSONObject o6) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(o1);
        jsonArray.add(o2);
        jsonArray.add(o3);
        jsonArray.add(o4);
        jsonArray.add(o5);
        jsonArray.add(o6);
        return jsonArray;
    }

    public static JSONArray build(JSONObject... os) {
        JSONArray jsonArray = new JSONArray();
        if (os != null) {
            Collections.addAll(jsonArray, os);
        }
        return jsonArray;
    }

}
