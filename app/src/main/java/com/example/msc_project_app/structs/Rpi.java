package com.example.msc_project_app.structs;
import android.util.Log;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Rpi {

    private ArrayList<Reading> readings;
    private static final String TAG = "Rpi";

    /**
    This class structure individual Reading class objects into an arraylist

     */
    public Rpi(ArrayList newStateList)
    {
        Log.d(TAG, "called");
        readings = new ArrayList<>();
        if (newStateList != null) {
            for(int i = 0; i <  newStateList.size(); i++){
                Map<String, Object> temp = (Map<String, Object>) newStateList.get(i);
                readings.add(new Reading(toDouble(temp.get("temperature")), toDouble(temp.get("humidity")), (Date) temp.get("time")));
            }
        }
        Log.d(TAG, readings.get(18).getStateMap().get("timestamp").toString());
    }

    /**
    *Checks incoming data types and converts to type Double if not allready so.
     */
    private double toDouble(Object number){
        Double d = Double.valueOf(0);
        Long l = Long.valueOf(0);


        if(number.getClass() == d.getClass()){
            d = (Double)number;
           return d;
        } else if(number.getClass() == l.getClass()){
            l = (Long)number;
            return Double.valueOf(l);
        } else{
            return 0;
        }
    }

    public ArrayList<Reading> getReadings() {
        return readings;
    }
}
