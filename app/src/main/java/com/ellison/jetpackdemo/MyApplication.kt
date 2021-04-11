package com.ellison.jetpackdemo

import android.app.Application
import androidx.camera.core.CameraXConfig
import android.util.Log
import androidx.camera.camera2.Camera2Config
import com.ellison.jetpackdemo.lifecycle.ActivityLifecycleCallbackImpl
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application(), CameraXConfig.Provider {
    private var mActivityLifecycle: ActivityLifecycleCallbacks? = null

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate()")
        super.onCreate()
        mActivityLifecycle = ActivityLifecycleCallbackImpl()
        registerActivityLifecycleCallbacks(mActivityLifecycle)
    }

    override fun onTerminate() {
        Log.d(TAG, "onTerminate()")
        super.onTerminate()
        unregisterActivityLifecycleCallbacks(mActivityLifecycle)
    }

    companion object {
        @JvmField
        val TAG = MyApplication::class.java.simpleName
    }
}