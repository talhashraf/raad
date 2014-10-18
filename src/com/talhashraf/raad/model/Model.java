package com.talhashraf.raad.model;

import java.lang.reflect.Field;


public class Model {
	public String get(String name) {
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.getName().equals(name)){
				try {
					return (String) field.get(this);
				} catch(Exception e) {}
			}
		}
		return "";
	}

	public void set(String name, String value) {
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.getName().equals(name)){
				try {
					field.set(this, value);
				} catch(Exception e) {}
			}
		}
	}

	public String[] getAllFields() {
		Field[] fields = this.getClass().getDeclaredFields();
		String[] all_fields = new String[fields.length];
		int index = 0;
		for (Field field : fields) {
			String field_name = field.getName();
			all_fields[index] = field_name;
			index++;
		}
		return all_fields;
	}
}