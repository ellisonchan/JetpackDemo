package com.ellison.jetpackdemo.appCompat;

import android.os.Bundle;

import com.ellison.jetpackdemo.R;

import androidx.appcompat.app.AppCompatActivity;

public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        // setContentView(R.layout.activity_main_test_2);
    }
}