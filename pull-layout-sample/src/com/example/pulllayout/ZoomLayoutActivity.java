package com.example.pulllayout;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.pulllayout.PullToZoomLayout;

public class ZoomLayoutActivity extends Activity {

    private ImageView imageView;
    private PullToZoomLayout zoomLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoom_layout_activity);

        zoomLayout = (PullToZoomLayout) findViewById(R.id.zoom_layout);
        ((ImageView)(zoomLayout.getHeaderView())).setImageResource(R.drawable.head);
    }
}
