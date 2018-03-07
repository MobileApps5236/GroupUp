package com.example.tonyrobb.groupup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Log.v("MainPage", "onCreate triggered");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("MainPage", "onPause triggered");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.v("MainPage","onResume triggered");
    }
}
