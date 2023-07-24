package com.example.testknowledge;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public final class QueryUtils {

    private QueryUtils(){}
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static ArrayList<Earthquake> fetchEarthquakeData(String requestUrl){
    // this code is to temporary force the background thread to sleep.
//        try{
//            Thread.sleep(2000);
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }
        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG,"Error closing input stream");
        }
        ArrayList<Earthquake> earthquakes = extractFeaturesFromJson(jsonResponse);
        return earthquakes;
    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"Error with creating url");
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse="";
        if(url == null)return jsonResponse;

        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            // this will give you url object so we have to typecast it.
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG,"Error response code :"+urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG,"Problem retrieving the earthquake JSON result :",e);
        }finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }




    public static ArrayList<Earthquake> extractFeaturesFromJson(String earthquakeJSON){
        if(TextUtils.isEmpty(earthquakeJSON))return null;

        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(earthquakeJSON);
            JSONArray features = root.getJSONArray("features");

            if(features.length() > 0) {

                for (int i = 0; i < features.length(); i++) {
                    JSONObject currentEarthquake = features.getJSONObject(i);
                    JSONObject properties = currentEarthquake.getJSONObject("properties");
                    double mag = properties.getDouble("mag");
                    String place = properties.getString("place");
                    long time = properties.getLong("time");
                    String url = properties.getString("url");
                    earthquakes.add(new Earthquake(mag, place, time,url));
                }

            }
        }catch (JSONException e){
            Log.e("QueryUtils","Problem parsing the earthquake JSON results",e);
        }
        return earthquakes;
    }


}
