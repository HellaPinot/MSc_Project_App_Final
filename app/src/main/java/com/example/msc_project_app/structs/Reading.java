package com.example.msc_project_app.structs;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

public class Reading {

    /**
    This class is used to create a reading object that holds the
    temperature, humidity and timestamp of a single reading.
     */

    private Map<String, Object> stateMap;
    private static final String TAG = "Reading";

    public Reading(Double temperature, Double humidity, Date timestamp)
    {
        stateMap = new HashMap<>();
        stateMap.put("temperature", temperature);
        stateMap.put("humidity", humidity);
        stateMap.put("timestamp", timestamp);
    }

    public Map<String, Object> getStateMap() {
        return stateMap;
    }

    public double getTemperature(){
        return (double) stateMap.get("temperature");
    }

    public double getHumidity(){
        return (double) stateMap.get("humidity");
    }

    /**
     * Formats time into HH.mm and returns as a long value for use in graph
     */

    public double getTime(){
        Date date = (Date) stateMap.get("timestamp");
        DateFormat hour = new SimpleDateFormat("HH");
        DateFormat minute = new SimpleDateFormat("0.mm");
        Double time = Double.valueOf(hour.format(date));
        time = time + ((Double.valueOf(minute.format(date))/60)*100);
        return time;
    }

    public Date getDate(){
        Date date = (Date) stateMap.get("timestamp");
        return date;
    }


}

