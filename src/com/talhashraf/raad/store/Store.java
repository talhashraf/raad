package com.talhashraf.raad.store;


import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

import java.lang.Class;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.app.Activity;
import android.widget.SimpleAdapter;

import com.talhashraf.raad.model.Model;
import com.talhashraf.raad.utils.Requests;
import com.talhashraf.raad.utils.JSONReader;
import com.talhashraf.raad.interfaces.StoreEventInterface;


public class Store extends AsyncTask<Void, Void, String> {
	private String url;
	private String[] from;
	private Class modelClass;
	private String rootProperty;
	private List<Model> collection;
	private List<StoreEventInterface> listeners;

	public Store(Class modelClass, String url, String rootProperty) {
		this.url = url;
		this.modelClass = modelClass;
		this.rootProperty = rootProperty;
		this.collection = new ArrayList<Model>();
		this.listeners = new ArrayList<StoreEventInterface>();
	}

	public void load() {
		this.execute();
	}

	protected String doInBackground(Void... params) {
		try {
			return Requests.getJSONData(this.url);
		} catch (Exception e) {}
		return "";
	}

	protected void onPostExecute(String result) {
		onLoad(result);
	}

	public void onLoad(String result) {
		JSONArray data = this.getJSONData(result, this.rootProperty);
		for (int i = 0; i < data.length(); i++) {
			try {
				Model model = (Model) this.modelClass.newInstance();
				JSONObject json_object = data.getJSONObject(i);
				Iterator keys = json_object.keys();
				while(keys.hasNext()) {
					String key = (String) keys.next();
					model.set(key, json_object.getString(key));
				}
				this.collection.add(model);
			} catch (Exception e) {}
		}

		for (StoreEventInterface listener : this.listeners) {
			listener.onComplete(this);
		}
	}

	public List<Map<String, String>> getData() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (Model model : this.collection) {
        	this.from = model.getAllFields();
        	Map<String, String> map = new HashMap<String, String>();
        	for (String field : this.from) {
	        	map.put(field, model.get(field));
	        }
	        list.add(map);
	    }
	    return list;
	}

	public SimpleAdapter getAdapter(Activity activity) {
        return new SimpleAdapter(activity,
								getData(),
                    		    android.R.layout.simple_list_item_2,
                        	    this.from,
                            	new int[] {android.R.id.text1, android.R.id.text2});
	}

	public SimpleAdapter getAdapter(Activity activity, String[] from) {
        return new SimpleAdapter(activity,
								getData(),
                    		    android.R.layout.simple_list_item_2,
                        	    from,
                            	new int[] {android.R.id.text1, android.R.id.text2});
	}

	public SimpleAdapter getAdapter(Activity activity, int resource, String[] from, int[] to) {
        return new SimpleAdapter(activity, getData(), resource, from, to);
	}

	public JSONArray getJSONData(String result, String rootProperty) {
		if (rootProperty != "") {
			return JSONReader.getJSONArray(result, rootProperty);
		}
		return JSONReader.getJSONArray(result);
	}

	public void addEventListener(StoreEventInterface listener) {
		this.listeners.add(listener);
	}
}