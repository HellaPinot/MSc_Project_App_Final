package com.example.msc_project_app.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.msc_project_app.R;
import com.example.msc_project_app.structs.Rpi;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

import java.util.Date;

/**
 * A fragment for presenting graphs relating to the previous days temperature and humidity readings
 */
public class YesterdayGraphFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "YesterdayGraphFragment";

    private GraphView tempGraph;
    private GraphView humGraph;
    private TextView tempRange;
    private  TextView humRange;

    public static YesterdayGraphFragment newInstance(int index) {
        YesterdayGraphFragment fragment = new YesterdayGraphFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.yesterday_graph_fragment, container, false);
        tempRange = root.findViewById(R.id.yestemptext);
        humRange = root.findViewById(R.id.yeshumtext);
        tempGraph = (GraphView) root.findViewById(R.id.temperature2);
        humGraph = (GraphView) root.findViewById(R.id.humidity2);
        humGraph.getViewport().setYAxisBoundsManual(true);
        humGraph.getViewport().setMaxY(100);

        /**
         * Observer that points to viewmodel master data object.
         */
        PageViewModel.getInstance().getData().observe(getViewLifecycleOwner(), new Observer<Rpi>() {
            @Override
            public void onChanged(Rpi rpi) {
                updateGraphs(rpi);
            }
        });
        return root;
    }

    /**
     * Takes all data currently stored in Viewmodel and translates all data from yesterday to
     * temperature and humidity graphs.
     * @param newData
     */
    public void updateGraphs(Rpi newData){
        LineGraphSeries<DataPoint> temperature = new LineGraphSeries<DataPoint>(new DataPoint[] {});
        LineGraphSeries<DataPoint> humidity = new LineGraphSeries<DataPoint>(new DataPoint[] {});
        double tempHigh = 0;
        double tempLow = 100;
        double humHigh = 0;
        double humLow = 100;

        for(int i =0; i < newData.getReadings().size()-1; i++) {
            if(isYesterday(newData.getReadings().get(i).getDate())) {
                temperature.appendData(new DataPoint(newData.getReadings().get(i).getTime(),
                        newData.getReadings().get(i).getTemperature()), false, 288, true);
                humidity.appendData(new DataPoint(newData.getReadings().get(i).getTime(),
                        newData.getReadings().get(i).getHumidity()), false, 288, true);
                if(newData.getReadings().get(i).getTemperature() > tempHigh){
                    tempHigh = newData.getReadings().get(i).getTemperature();
                }
                if(newData.getReadings().get(i).getTemperature() < tempLow){
                    tempLow = newData.getReadings().get(i).getTemperature();
                }
                if(newData.getReadings().get(i).getHumidity() > humHigh){
                    humHigh = newData.getReadings().get(i).getHumidity();
                }
                if(newData.getReadings().get(i).getHumidity() < humLow){
                    humLow = newData.getReadings().get(i).getHumidity();
                }

            }

        }

        LineGraphSeries<DataPoint> boundaries = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0,0),
                new DataPoint(24,0)
        });


        tempRange.setText("Temperature Range: " + tempLow + "C - " + tempHigh + "C");
        humRange.setText("Humidity Range: "+ humLow + "% - " + humHigh + "%");
        tempGraph.removeAllSeries();
        humGraph.removeAllSeries();
        boundaries.setColor(0);
        tempGraph.addSeries(boundaries);
        tempGraph.addSeries(temperature);
        humGraph.addSeries(boundaries);
        humGraph.addSeries(humidity);
        tempGraph.getGridLabelRenderer().setNumHorizontalLabels(13);
        humGraph.getGridLabelRenderer().setNumHorizontalLabels(13);
    }


    /**
     * Checks the parameters date to verify if yesterdays date.
     * @param aDate
     * @return true if date is yesterdays date, false if not
     */
    private boolean isYesterday(Date aDate){
        Date today = new Date();
        Date temp = new Date();
        today.setDate(temp.getDate()-1);
        if(aDate.getDate() == today.getDate() && aDate.getMonth() == today.getMonth() && aDate.getYear() == today.getYear()){
            //Log.d(TAG, "Returning true: " + aDate.getDate() + " " + aDate.getMonth() + " " + aDate.getYear() + " " + today.getDate() + " " + today.getMonth() + " " + today.getYear());
            return true;
        } else{
            //Log.d(TAG, "Returning false: " + aDate.getDate() + " " + aDate.getMonth() + " " + aDate.getYear() + " " + today.getDate() + " " + today.getMonth() + " " + today.getYear());
            return false;
        }
    }
}