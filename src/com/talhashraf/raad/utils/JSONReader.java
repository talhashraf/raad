package com.talhashraf.raad.utils;

import org.json.JSONObject;
import org.json.JSONArray;


public class JSONReader {
	public static JSONObject getJSONObject(String json_data) {
        JSONObject json_object = null;
        try {
            if (json_data != null) {
	        	return new JSONObject(json_data);
	        }
            json_object = new JSONObject("{}");
        } catch (Exception e) {}
        return json_object;
    }

    public static JSONArray getJSONArray(String json_data) {
        JSONArray json_array = null;
        try {
            if (json_data != null) {
                return new JSONArray(json_data);
            }
            json_array = new JSONArray("[]");
        } catch (Exception e) {}
        return json_array;
    }

    public static JSONArray getJSONArray(String json_data, String field) {
    	JSONArray json_array = null;
    	try {
            if (json_data != null) {
                return JSONReader.getJSONObject(json_data).getJSONArray(field);
            }
            json_array = new JSONArray("[]");
        } catch (Exception e) {}
        return json_array;
    }

    public static String getString(String json_data, String name) {
        try {
            return JSONReader.getJSONObject(json_data).getString(name);
        } catch (Exception e) {}
        return "";
    }
}