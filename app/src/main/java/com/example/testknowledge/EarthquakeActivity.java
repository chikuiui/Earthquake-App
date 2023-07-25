package com.example.testknowledge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Earthquake>> {

    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-05-02&minfelt=50&minmagnitude=5";

    private static final int EARTHQUAKE_LOADER_ID = 1;
    private EarthquakeAdapter adapter;
    private TextView mEmptyStateTextView;
    private ProgressBar pb;




    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.earthquake_activity);


//*******************************************AsyncTask************************************************************************************************************************
//        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
//        task.execute(USGS_REQUEST_URL);
//****************************************************************************************************************************************************************************
         updateUi();

         ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
         if(networkInfo != null && networkInfo.isConnected()){
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,this);
         }else{
             pb.setVisibility(View.GONE);
             mEmptyStateTextView.setText(R.string.no_internet_connection);
         }
    }




    private void updateUi(){
        pb = findViewById(R.id.loading_spinner);
        mEmptyStateTextView = findViewById(R.id.empty_view);
        ListView earthquakeListView = findViewById(R.id.list);

        earthquakeListView.setEmptyView(mEmptyStateTextView);
        adapter = new EarthquakeAdapter(this,new ArrayList<Earthquake>());
        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Earthquake curr_earthquake = adapter.getItem(i);
                Uri earthquakeUri = Uri.parse(curr_earthquake.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW,earthquakeUri);
                startActivity(websiteIntent);
            }
        });
    }

    @Override
    public Loader<ArrayList<Earthquake>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMag = sharedPrefs.getString(getString(R.string.settings_min_magnitude_key),getString(R.string.settings_min_magnitude_default));
        String orderBy= sharedPrefs.getString(getString(R.string.settings_order_by_key),getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format","geojson");
        uriBuilder.appendQueryParameter("limit","10");
        uriBuilder.appendQueryParameter("minmag",minMag);
        uriBuilder.appendQueryParameter("orderby",orderBy);

        return new EarthquakeLoader(this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Earthquake>> loader, ArrayList<Earthquake> earthquakes) {

        pb.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_earthquakes);
       // clear the adapter with previous earthquake data.
        adapter.clear();
       // if there is valid data then add this data to adapter.
        if(earthquakes!=null && !earthquakes.isEmpty()){
            adapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Earthquake>> loader) {
        adapter.clear();
    }

// ***********************************************************************************************ASYNC TASK USE*******************************************************************************


//    private void updateUi(ArrayList<Earthquake> earthquakes){
//
//        ListView earthquakeListView = findViewById(R.id.list);
//        EarthquakeAdapter adapter = new EarthquakeAdapter(this,earthquakes);
//        earthquakeListView.setAdapter(adapter);
//
//        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Earthquake curr_earthquake = adapter.getItem(i);
//                Uri earthquakeUri = Uri.parse(curr_earthquake.getUrl());
//                Intent websiteIntent = new Intent(Intent.ACTION_VIEW,earthquakeUri);
//                startActivity(websiteIntent);
//            }
//        });
//    }
//    private class EarthquakeAsyncTask extends AsyncTask<String,Void,ArrayList<Earthquake>> {
//        @Override
//        protected  ArrayList<Earthquake> doInBackground(String... strings){
//            if(strings.length < 1 || strings[0] == null)return null;
//            // ** change in below line have to pass the string[0]
//            ArrayList<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(strings[0]);
//            return earthquakes;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<Earthquake> results){
//            if(results == null)return;
//            updateUi(results);
//        }
//
//    }

//********************************************************************************************************************************************************************************************
// *************************************************************** THE BELOW CODE IS FOR PREFERENCE *****************************************************************

    // The below code is for creating Menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // The below code is for selecting the item from the menu.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
