package com.talhashraf.raad.store;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import java.lang.reflect.Field;

import org.json.JSONArray;

import android.os.AsyncTask;
import android.app.Activity;
import android.widget.SimpleAdapter;

import com.talhashraf.raad.model.Model;
import com.talhashraf.raad.utils.Requests;
import com.talhashraf.raad.utils.JSONReader;
import com.talhashraf.raad.interfaces.StoreEventInterface;


public class Store extends AsyncTask<Void, Void, String> {
	private String url;
	private Model model;
	private int resource;
	private int[] pattern;
	private String[] fields;
	private Activity activity;
	private String rootProperty;
	private List<Model> collection;
	private List<StoreEventInterface> listeners;

	public Store(Activity activity, Model model, String url, String rootProperty) {
		this.url = url;
		this.model = model;
		this.activity = activity;
		this.rootProperty = rootProperty;
		this.fields = this.model.getAllFields();
		this.collection = new ArrayList<Model>();
		this.listeners = new ArrayList<StoreEventInterface>();
		this.resource = android.R.layout.simple_list_item_2;
		this.pattern = new int[] {android.R.id.text1, android.R.id.text2};
	}

	public Store(Activity activity, Model model, String url, String rootProperty, String[] fields) {
		this.url = url;
		this.model = model;
		this.fields = fields;
		this.activity = activity;
		this.rootProperty = rootProperty;
		this.collection = new ArrayList<Model>();
		this.listeners = new ArrayList<StoreEventInterface>();
		this.resource = android.R.layout.simple_list_item_2;
		this.pattern = new int[] {android.R.id.text1, android.R.id.text2};
	}

	public Store(Activity activity, Model model, String url, String rootProperty, int resource, String[] fields, int[] pattern) {
		this.url = url;
		this.model = model;
		this.fields = fields;
		this.pattern = pattern;
		this.resource = resource;
		this.activity = activity;
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
		Field[] fields = this.model.getClass().getDeclaredFields();
		JSONArray data = this.getData(result, this.rootProperty);
		for (int i = 0; i < data.length(); i++) {
			for (Field field : fields) {
				String field_name = field.getName();
				try {
					this.model.set(field_name,
									data.getJSONObject(i).getString(field_name));
				} catch (Exception e) {}
			}
			this.collection.add(this.model);
		}

		for (StoreEventInterface listener : this.listeners) {
			listener.onComplete(this);
		}
	}

	public SimpleAdapter getAdapter() {
		Field[] fields = this.model.getClass().getDeclaredFields();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (Model model : this.collection) {
        	Map<String, String> map = new HashMap<String, String>();
        	for (Field field : fields) {
        		String field_name = field.getName();
	        	map.put(field_name, model.get(field_name));
	        }
	        list.add(map);
	    }
        return new SimpleAdapter(this.activity,
								list,
                    		    this.resource,
                        	    this.fields,
                            	this.pattern);
	}

	public JSONArray getData(String result, String rootProperty) {
		if (rootProperty != "") {
			return JSONReader.getJSONArray(result, rootProperty);
		}
		return JSONReader.getJSONArray(result);
	}

	public void addEventListener(StoreEventInterface listener) {
		this.listeners.add(listener);
	}
}