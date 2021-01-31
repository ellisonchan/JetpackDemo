package com.ellison.jetpackdemo;

import android.app.Application;
import android.util.Log;

import com.ellison.jetpackdemo.lifecycle.ActivityLifecycleCallbackImpl;

import androidx.annotation.NonNull;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraXConfig;
import androidx.camera.lifecycle.ProcessCameraProvider;

public class MyApplication extends Application implements CameraXConfig.Provider {
    public static final String TAG = MyApplication.class.getSimpleName();
    private ActivityLifecycleCallbacks mActivityLifecycle;

    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");
        super.onCreate();

        mActivityLifecycle = new ActivityLifecycleCallbackImpl();
        registerActivityLifecycleCallbacks(mActivityLifecycle);
    }

    @Override
    public void onTerminate() {
        Log.d(TAG, "onTerminate()");
        super.onTerminate();
        unregisterActivityLifecycleCallbacks(mActivityLifecycle);
    }
}