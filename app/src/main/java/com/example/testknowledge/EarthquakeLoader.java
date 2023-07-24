package com.example.testknowledge;

import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<Earthquake>> {

    private String mUrl;

    public EarthquakeLoader(Context context,String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading(){
         forceLoad();
    }

    @Override
    public ArrayList<Earthquake> loadInBackground(){
        if(mUrl == null)return null;
        ArrayList<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        return earthquakes;
    }
}
