package com.example.msc_project_app.ui.main;

import android.os.Bundle;
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

import java.util.Date;

public class TodayGraphFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "TodayGraphFragment";

    private GraphView tempGraph;
    private GraphView humGraph;
    private TextView tempText;
    private TextView humText;
    private TextView timeText;


    /**
     * A fragment for presenting graphs relating to the current days temperature and humidity readings
     */
    public static TodayGraphFragment newInstance(int index) {
        TodayGraphFragment fragment = new TodayGraphFragment();
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
        View root = inflater.inflate(R.layout.today_graph_fragment, container, false);
        tempText = root.findViewById(R.id.todtemptext);
        humText = root.findViewById(R.id.yestemptext);
        timeText = root.findViewById(R.id.todtimetext);
        tempGraph = (GraphView) root.findViewById(R.id.temperature);
        humGraph = (GraphView) root.findViewById(R.id.humidity);
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
     * Takes all data currently stored in Viewmodel and translates all data from today to
     * temperature and humidity graphs.
     * @param newData
     */
    public void updateGraphs(Rpi newData){
        LineGraphSeries<DataPoint> temperature = new LineGraphSeries<DataPoint>(new DataPoint[] {});
        LineGraphSeries<DataPoint> humidity = new LineGraphSeries<DataPoint>(new DataPoint[] {});

        for(int i =0; i <= newData.getReadings().size()-1; i++) {

            if(isToday(newData.getReadings().get(i).getDate())) {
                temperature.appendData(new DataPoint(newData.getReadings().get(i).getTime(),
                newData.getReadings().get(i).getTemperature()), false, 288, true);
                humidity.appendData(new DataPoint(newData.getReadings().get(i).getTime(),
                newData.getReadings().get(i).getHumidity()), false, 288, true);
            }
        }

        LineGraphSeries<DataPoint> boundaries = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0,0),
                new DataPoint(24,0)
        });

        tempText.setText("Temperature: " + newData.getReadings().get(newData.getReadings().size()-1).getTemperature() + "C");
        humText.setText("Humidity: " + newData.getReadings().get(newData.getReadings().size()-1).getHumidity() + "%");
        timeText.setText(newData.getReadings().get(newData.getReadings().size()-1).getDate().toString());
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
     * Checks the parameters date to verify if today's date.
     * @param aDate
     * @return true if date is today's date, false if not
     */
    private boolean isToday(Date aDate){
        Date today = new Date();
        if(aDate.getDate() == today.getDate() && aDate.getMonth() == today.getMonth() && aDate.getYear() == today.getYear()){
            //Log.d(TAG, "Returning true: " + aDate.getDate() + " " + aDate.getMonth() + " " + aDate.getYear() + " " + today.getDate() + " " + today.getMonth() + " " + today.getYear());
            return true;
        } else{
            //Log.d(TAG, "Returning false: " + aDate.getDate() + " " + aDate.getMonth() + " " + aDate.getYear() + " " + today.getDate() + " " + today.getMonth() + " " + today.getYear());
            return false;
        }
    }
}