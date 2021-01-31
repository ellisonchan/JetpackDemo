package com.ellison.jetpackdemo.old;

import android.os.Bundle;

import com.ellison.jetpackdemo.R;

import androidx.annotation.Nullable;
import android.app.Activity;

public class DemoActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old);
    }
}
