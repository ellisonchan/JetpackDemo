package com.ellison.jetpackdemo

import android.app.Application
import androidx.camera.core.CameraXConfig
import android.util.Log
import androidx.camera.camera2.Camera2Config
import com.ellison.jetpackdemo.dagger2.view.DemoActivity
import com.ellison.jetpackdemo.dagger2.model.NetworkModule
import com.ellison.jetpackdemo.lifecycle.ActivityLifecycleCallbackImpl
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface ApplicationGraph {
    fun inject(activity: DemoActivity)
}

//@Singleton
//@Component(modules = [NetworkModule::class, MovieGraphModule::class]) // 子模块添加到应用图
//interface ApplicationGraph {
//    fun repository(): MovieRepository
//    fun movieGraph(): MovieGraph.Factory // 告知Dagger如何创建子组件
//}

class MyApplication : Application(), CameraXConfig.Provider {
    private var mActivityLifecycle: ActivityLifecycleCallbacks? = null
    val appGraph = DaggerApplicationGraph.create()
    // val appGraph = DaggerApplicationGraph.builder().networkModule().build()

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