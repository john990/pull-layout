package com.example.pulllayout;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import com.pulllayout.ListZoomView;

/**
 * Created by kai.wang on 3/18/14.
 */
public class ZoomListActivity extends Activity {
    private ListZoomView listZoomView;

    private String[] list = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoom_list_activity);
        listZoomView = (ListZoomView) findViewById(R.id.zoom_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.textview,list);
        listZoomView.setAdapter(adapter);
    }
}