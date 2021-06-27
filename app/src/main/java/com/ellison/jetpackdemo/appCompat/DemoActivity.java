package com.ellison.jetpackdemo.appCompat;

import android.os.Bundle;
import android.preference.PreferenceGroup;

import com.ellison.jetpackdemo.R;

import androidx.appcompat.app.AppCompatActivity;

public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        // setContentView(R.layout.activity_main_test_2);
        PreferenceGroup preferenceGroup;
        androidx.preference.CheckBoxPreference checkBoxPreference;

        androidx.preference.PreferenceFragmentCompat preferenceFragmentCompat;
        androidx.preference.PreferenceFragment preferenceFragment;
    }
}