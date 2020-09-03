package com.example.msc_project_app.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.msc_project_app.structs.Rpi;
import java.util.ArrayList;

public class PageViewModel extends ViewModel {

    private static final String TAG = "PageViewModel";
    public static PageViewModel pageViewModel;
    private static MutableLiveData<Rpi> data;

    /**
    * This class holds the master data object Rpi and makes it accessible for various fragments to
    * observe and update ui accordingly
     */


    /**
     * Creates instance of class so that all observers point to same set of data
     */

    public static PageViewModel getInstance() {
        if (pageViewModel == null) {
            pageViewModel = new PageViewModel();
            return pageViewModel;
        } else {
            return pageViewModel;
        }
    }

    private PageViewModel()
    {
        data = new MutableLiveData<>();
    }

    public void setData(ArrayList newData)
    {
        Log.d(TAG, "New data set");
        data.setValue(new Rpi(newData));
    }

    public LiveData<Rpi> getData(){
        return data;
    }









}