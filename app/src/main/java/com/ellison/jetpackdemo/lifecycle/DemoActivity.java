package com.ellison.jetpackdemo.lifecycle;

import android.os.Bundle;
import android.util.Log;

import com.ellison.jetpackdemo.databinding.ActivityLifecycleBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

public class DemoActivity extends AppCompatActivity {
    static final String TAG = DemoActivity.class.getSimpleName();
    LifecycleObserver mLifecycleObserver;
    private LifecycleRegistry lifecycleRegistry;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "MYSELF onCreate()");
        super.onCreate(savedInstanceState);

        ActivityLifecycleBinding binding = ActivityLifecycleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getLifecycle().addObserver(new DefaultLifecycleObserver() {
            @Override
            public void onCreate(@NonNull LifecycleOwner owner) {
                Log.d(TAG, "DefaultLifecycleObserver#onCreate()");
            }
        });

        getLifecycle().addObserver(new GenericLifecycleObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                Log.d(TAG, "GenericLifecycleObserver#onStateChanged() source:" + source + " event:" + event);
            }
        });

        mLifecycleObserver = new LifeCycleObserverImpl(getLifecycle());
        getLifecycle().addObserver(mLifecycleObserver);

//        lifecycleRegistry = new LifecycleRegistry(this);
//        lifecycleRegistry.markState(Lifecycle.State.CREATED);
    }

//    @NonNull
//    @Override
//    public Lifecycle getLifecycle() {
//        return lifecycleRegistry;
//    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "MYSELF onDestroy()");
        super.onDestroy();
        getLifecycle().removeObserver(mLifecycleObserver);
    }

    private static final class MyTestInterface implements TestInterface {
        @Override
        public int getAge() {
            return 0;
        }
    }
}