package com.example.fugro;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class Jsonparser {

 String response = null;
 public final static int GET = 1;
 public final static int POST = 2;
 static JSONObject jObj = null; 
 static InputStream is = null;
 
 public Jsonparser() {

 }

 public JSONObject makeServiceCall(String url, int method) {
  return this.makeServiceCall(url, method, null);
 }


 public JSONObject makeServiceCall(String url, int method,
   List<NameValuePair> params) {
  try {
   // http client

   //client = new DefaultHttpClient(httpParameters);
   
   
   HttpClient httpClient = new DefaultHttpClient();
   HttpEntity httpEntity = null;
   HttpResponse httpResponse = null;
 
   // Checking http request method type
   if (method == POST) {
    HttpPost httpPost = new HttpPost(url);
    // adding post params
    if (params != null) {
     httpPost.setEntity(new UrlEncodedFormEntity(params));
    }

    httpResponse = httpClient.execute(httpPost);

   } else if (method == GET) {
    // appending params to url
    if (params != null) {
     String paramString = URLEncodedUtils.format(params, "utf-8");
     url += "?" + paramString;
    }
    HttpGet httpGet = new HttpGet(url);

    httpResponse = httpClient.execute(httpGet);

   }
   httpEntity = httpResponse.getEntity();
   response = EntityUtils.toString(httpEntity);

  } catch (UnsupportedEncodingException e) {
   e.printStackTrace();
  } catch (ClientProtocolException e) {
   e.printStackTrace();
  } catch (IOException e) {
   e.printStackTrace();
  }
  
  try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            response = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 
        // try parse the string to a JSON object
        try {
        	if(response != null){
        		jObj = new JSONObject(response);
        	}
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
 
        // return JSON String
        return jObj;
 
    }
}
