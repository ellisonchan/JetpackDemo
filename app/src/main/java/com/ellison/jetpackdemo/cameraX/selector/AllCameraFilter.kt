package com.ellison.jetpackdemo.cameraX.selector

import android.util.Log
import androidx.camera.core.CameraFilter
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.impl.CameraInfoInternal
import androidx.camera.core.impl.CameraInternal
import java.util.LinkedHashSet

class AllCameraFilter: CameraFilter {
    override fun filter(cameraInfos: MutableList<CameraInfo>): MutableList<CameraInfo> {
        val result: MutableList<CameraInfo> = mutableListOf()
        for (cameraInfo in cameraInfos) {
            Log.d(
                "Camera", "cameraInfo" +
                        " implementationType:${cameraInfo.implementationType}" +
                        " state:${cameraInfo.exposureState}" +
                        " sensorRotationDegrees:${cameraInfo.sensorRotationDegrees}"
            )

            val id = (cameraInfo as CameraInfoInternal).cameraId
            Log.d("Camera", "cameraInfo id:$id")

            // Specify the camera id that U need, such as front camera which id is 0.
            if (CameraSelector.LENS_FACING_FRONT.equals(id)) {
                Log.d("Camera", "cameraInfo add")
                result.add(cameraInfo)
            }
        }
        return result
    }
}