package com.ellison.jetpackdemo.cameraX.utils

import com.ellison.jetpackdemo.R

class Constants {
    companion object {
        const val TAG_CAMERA = "Camera"
        const val TAG_CAMERA_UI = "CameraUI"
        const val TAG_BEEP = "beep"

        const val MIN_ZOOM_SCALE = 0.0
        const val MIDDLE_ZOOM_SCALE = 0.5
        const val MAX_ZOOM_SCALE = 1.0
        const val DEFAULT_ZOOM_SCALE = 1.0

        const val ANALYSIS_RESULT_SHOW_DURATION = 1500L
        const val ANALYSIS_HOLDING_DURATION = 3000L

        const val BEEP_VOLUME = 0.05f
        const val VIBRATE_DURATION = 200L
        const val BEEP_OGG_FILE = R.raw.beep_sound
        
        const val REQUEST_CAMERA = 20
        const val REQUEST_STORAGE = 30
        const val REQUEST_STORAGE_BINDING = 35
        const val REQUEST_STORAGE_VIDEO = 40
        const val REQUEST_STORAGE_VIDEO_BINDING = 45
        
        const val CAPTURED_FILE_NAME = "captured_picture"
        const val RECORDED_FILE_NAME = "recorded_video"
        const val RECORDED_FILE_NAME_END = "video/mp4"

        const val TAG_FRAGMENT_CHOOSE_ANALYSIS = "choose_analysis_screen"
    }
}