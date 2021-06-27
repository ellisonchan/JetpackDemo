package com.ellison.jetpackdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("ViewModel", "onCreate()");
        try {
            AppCompatDelegate appCompatDelegate = getDelegate();

            Field field = appCompatDelegate.getClass().getDeclaredField("DEBUG");
            field.setAccessible(true);

            Log.d("ViewModel", "onCreate() field:" + field.get(appCompatDelegate));
            field.set(appCompatDelegate, true);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            Log.d("ViewModel", "Exception:" + e);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("ViewModel", "onConfigurationChanged() newConfig:" + newConfig, new Throwable());
    }

    @Override
    protected void onNightModeChanged(int mode) {
        super.onNightModeChanged(mode);
        Log.d("ViewModel", "onNightModeChanged() mode:" + mode);
    }

    public void showViewBindingDemo(View view) {
        jumpToActivity(com.ellison.jetpackdemo.viewBinding.DemoActivity.class);
        getDelegate().applyDayNight();
    }

    public void showViewModelDemo(View view) {
        jumpToActivity(com.ellison.jetpackdemo.viewModel.DemoActivity.class);
    }

    public void showDataBindingDemo(View view) {
        jumpToActivity(com.ellison.jetpackdemo.databinding.DemoActivity.class);
    }

    public void showViewModelBindingDemo(View view) {
        jumpToActivity(com.ellison.jetpackdemo.viewModelBinding.DemoActivity.class);
    }

    public void showLifecycleDemo(View view) {
        jumpToActivity(com.ellison.jetpackdemo.lifecycle.DemoActivity.class);
    }

    public void showLivedataDemo(View view) {
        jumpToActivity(com.ellison.jetpackdemo.liveData.DemoActivity.class);
    }

    public void showOldDemo(View view) {
        jumpToActivity(com.ellison.jetpackdemo.old.DemoActivity.class);
    }

    public void showAppCompatDemo(View view) {
        jumpToActivity(com.ellison.jetpackdemo.appCompat.DemoActivity.class);
    }

    public void showCameraXCompatDemo(View view) {
        // jumpToActivity(com.ellison.jetpackdemo.cameraX.DemoActivityLite.class);
        jumpToActivity(com.ellison.jetpackdemo.cameraX.NewCameraXActivity.class);
    }

    public void showCamera2CompatDemo(View view) {
        jumpToActivity(com.ellison.jetpackdemo.camera2.DemoActivity.class);
    }

    public void showRoomDemo(View view) {
        jumpToActivity(com.ellison.jetpackdemo.room.DemoActivity.class);
    }

    public void showCoroutinesDemo(View view) {
        jumpToActivity(com.ellison.jetpackdemo.coroutines.DemoActivity.class);
    }

    public void showHiltDemo(View view) {
        jumpToActivity(com.ellison.jetpackdemo.hilt.DemoActivity.class);
    }

    private void jumpToActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Target screen not found.", Toast.LENGTH_SHORT).show();
        }
    }
}