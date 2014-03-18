package com.example.pulllayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by kai.wang on 3/18/14.
 */
public class MainActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    public void zoomLayout(View view){
        Intent intent = new Intent(MainActivity.this,ZoomLayoutActivity.class);
        startActivity(intent);
    }


    public void zoomList(View view){
        Intent intent = new Intent(MainActivity.this,ZoomListActivity.class);
        startActivity(intent);
    }

}