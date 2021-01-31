package com.ellison.jetpackdemo.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.ellison.jetpackdemo.MyApplication.TAG;

public class ActivityLifecycleCallbackImpl implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityPreCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityPreCreated() activity:" + activity);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated() activity:" + activity);
    }

    @Override
    public void onActivityPostCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityPostCreated() activity:" + activity);
    }

    @Override
    public void onActivityPreStarted(@NonNull Activity activity) {
        Log.d(TAG, "onActivityPreStarted() activity:" + activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        Log.d(TAG, "onActivityStarted() activity:" + activity);
    }

    @Override
    public void onActivityPostStarted(@NonNull Activity activity) {
        Log.d(TAG, "onActivityPostStarted() activity:" + activity);
    }

    @Override
    public void onActivityPreResumed(@NonNull Activity activity) {
        Log.d(TAG, "onActivityPreResumed() activity:" + activity);
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        Log.d(TAG, "onActivityResumed() activity:" + activity);
    }

    @Override
    public void onActivityPostResumed(@NonNull Activity activity) {
        Log.d(TAG, "onActivityPostResumed() activity:" + activity);
    }

    @Override
    public void onActivityPrePaused(@NonNull Activity activity) {
        Log.d(TAG, "onActivityPrePaused() activity:" + activity);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        Log.d(TAG, "onActivityPaused() activity:" + activity);
    }

    @Override
    public void onActivityPostPaused(@NonNull Activity activity) {
        Log.d(TAG, "onActivityPostPaused() activity:" + activity);
    }

    @Override
    public void onActivityPreStopped(@NonNull Activity activity) {
        Log.d(TAG, "onActivityPreStopped() activity:" + activity);
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        Log.d(TAG, "onActivityStopped() activity:" + activity);
    }

    @Override
    public void onActivityPostStopped(@NonNull Activity activity) {
        Log.d(TAG, "onActivityPostStopped() activity:" + activity);
    }

    @Override
    public void onActivityPreSaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        Log.d(TAG, "onActivityPreSaveInstanceState() activity:" + activity);
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        Log.d(TAG, "onActivitySaveInstanceState() activity:" + activity);
    }

    @Override
    public void onActivityPostSaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        Log.d(TAG, "onActivityPostSaveInstanceState() activity:" + activity);
    }

    @Override
    public void onActivityPreDestroyed(@NonNull Activity activity) {
        Log.d(TAG, "onActivityPreDestroyed() activity:" + activity);
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        Log.d(TAG, "onActivityDestroyed() activity:" + activity);
    }

    @Override
    public void onActivityPostDestroyed(@NonNull Activity activity) {
        Log.d(TAG, "onActivityPostDestroyed() activity:" + activity);
    }
}
