package com.example.testknowledge;
import android.graphics.drawable.GradientDrawable;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    public EarthquakeAdapter(Activity context, ArrayList<Earthquake> earthquakes){
        super(context,0,earthquakes);
    }

    // for separating the string location into two parts
    private static final String LOCATION_SEPARATOR = " of ";

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item,parent,false);
        }

        Earthquake currentEarthquake = getItem(position);

        TextView magView = listItemView.findViewById(R.id.magnitude);
        TextView locView1 = listItemView.findViewById(R.id.primary_location);
        TextView locView2 = listItemView.findViewById(R.id.location_offset);

        TextView datView = listItemView.findViewById(R.id.date);
        TextView timView = listItemView.findViewById(R.id.time);


        GradientDrawable magnitudeCircle = (GradientDrawable) magView.getBackground();
        int magColor = getMagnitudeColor(currentEarthquake.getMagnitude());
        magnitudeCircle.setColor(magColor);

        magView.setText(DecimalFormat(currentEarthquake.getMagnitude()));


        String orignalLocation = currentEarthquake.getLocation();
        String primaryLocation;
        String locationOffset;

        if(orignalLocation.contains(LOCATION_SEPARATOR)){
            String parts[] = orignalLocation.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        }else{
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = orignalLocation;
        }
        locView1.setText(primaryLocation);
        locView2.setText(locationOffset);


        Date dateObject = new Date(currentEarthquake.getTime());
        String date = formatDate(dateObject);
        String time = formatTime(dateObject);

        datView.setText(date);
        timView.setText(time);





        return listItemView;

    }
    private String formatDate(Date dateObject){
        SimpleDateFormat d = new SimpleDateFormat("LLL dd, yyyy");
        return d.format(dateObject);
    }
    private String formatTime(Date dateObject){
        SimpleDateFormat d = new SimpleDateFormat("h:mm a");
        return d.format(dateObject);
    }

    private String DecimalFormat(double magnitude){
        DecimalFormat formatter = new DecimalFormat("0.0");
        return formatter.format(magnitude);
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        // convert resource id into an actual integer color value.
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
