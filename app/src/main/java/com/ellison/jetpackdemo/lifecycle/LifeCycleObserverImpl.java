package com.ellison.jetpackdemo.lifecycle;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import static com.ellison.jetpackdemo.lifecycle.DemoActivity.TAG;

public class LifeCycleObserverImpl implements LifecycleObserver {
    private Lifecycle lifecycle;

    public LifeCycleObserverImpl(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        Log.d(TAG, "LifeCycleObserverImpl#onCreate()");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        Log.d(TAG, "LifeCycleObserverImpl#onStart()" + lifecycle.getCurrentState().isAtLeast(Lifecycle.State.STARTED));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        Log.d(TAG, "LifeCycleObserverImpl#onResume()");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        Log.d(TAG, "LifeCycleObserverImpl#onPause()");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        Log.d(TAG, "LifeCycleObserverImpl#onStop()");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestory() {
        Log.d(TAG, "LifeCycleObserverImpl#onDestory()");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    public void onAny() {
        Log.d(TAG, "LifeCycleObserverImpl#onAny()");
    }
}