package com.talhashraf.raad.utils;

import java.io.ByteArrayOutputStream;

import org.apache.http.StatusLine;
import org.apache.http.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


public class Requests {
	public static HttpResponse get(String url) {
        HttpResponse response = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            response = httpclient.execute(new HttpGet(url));
        } catch (Exception e) {
        }
        return response;
    }

    public static String getJSONData(String url) {
        HttpResponse response = Requests.get(url);
        String json_data = "";
        try {
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                response.getEntity().writeTo(output);
                output.close();
                json_data = output.toString();
            }
        } catch (Exception e) {
        }
        return json_data;
    }
}