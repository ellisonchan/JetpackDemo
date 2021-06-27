package com.ellison.jetpackdemo.cameraX.viewmodel

import android.app.Application
import androidx.camera.core.ImageProxy
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ellison.jetpackdemo.cameraX.analysis.*

class CameraViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var pictureAnalysis: RealTimeAnalysis
    val analysisLiveData = MutableLiveData<Boolean>()

    fun chooseAnalysis(clazz: Class<out RealTimeAnalysis?>?) {
        pictureAnalysis = clazz?.newInstance() ?: ZXingAnalysis()
        analysisLiveData.value = true
    }

    fun analysePicture(imageProxy: ImageProxy): AnalysisResult =
        pictureAnalysis.analyzeContent(imageProxy, getApplication())
}