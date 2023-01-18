package com.mogudiandian.util.json.fastjson;

import com.alibaba.fastjson.JSONObject;

/**
 * 快速构造JSONObject
 * @author sunbo
 */
public final class JSONObjectBuilder {

    private JSONObjectBuilder() {
        super();
    }

    public static JSONObject build(String k, Object v) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(k, v);
        return jsonObject;
    }

    public static JSONObject build(String k1, Object v1, String k2, Object v2) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(k1, v1);
        jsonObject.put(k2, v2);
        return jsonObject;
    }

    public static JSONObject build(String k1, Object v1, String k2, Object v2, String k3, Object v3) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(k1, v1);
        jsonObject.put(k2, v2);
        jsonObject.put(k3, v3);
        return jsonObject;
    }

    public static JSONObject build(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(k1, v1);
        jsonObject.put(k2, v2);
        jsonObject.put(k3, v3);
        jsonObject.put(k4, v4);
        return jsonObject;
    }

    public static JSONObject build(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(k1, v1);
        jsonObject.put(k2, v2);
        jsonObject.put(k3, v3);
        jsonObject.put(k4, v4);
        jsonObject.put(k5, v5);
        return jsonObject;
    }

    public static JSONObject build(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(k1, v1);
        jsonObject.put(k2, v2);
        jsonObject.put(k3, v3);
        jsonObject.put(k4, v4);
        jsonObject.put(k5, v5);
        jsonObject.put(k6, v6);
        return jsonObject;
    }

    public static JSONObject build(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(k1, v1);
        jsonObject.put(k2, v2);
        jsonObject.put(k3, v3);
        jsonObject.put(k4, v4);
        jsonObject.put(k5, v5);
        jsonObject.put(k6, v6);
        jsonObject.put(k7, v7);
        return jsonObject;
    }

    public static JSONObject build(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(k1, v1);
        jsonObject.put(k2, v2);
        jsonObject.put(k3, v3);
        jsonObject.put(k4, v4);
        jsonObject.put(k5, v5);
        jsonObject.put(k6, v6);
        jsonObject.put(k7, v7);
        jsonObject.put(k8, v8);
        return jsonObject;
    }

    public static JSONObject build(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(k1, v1);
        jsonObject.put(k2, v2);
        jsonObject.put(k3, v3);
        jsonObject.put(k4, v4);
        jsonObject.put(k5, v5);
        jsonObject.put(k6, v6);
        jsonObject.put(k7, v7);
        jsonObject.put(k8, v8);
        jsonObject.put(k9, v9);
        return jsonObject;
    }

}
