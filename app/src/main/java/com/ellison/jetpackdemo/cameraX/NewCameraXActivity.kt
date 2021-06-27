package com.ellison.jetpackdemo.cameraX

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.util.Size
import android.view.*
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.core.impl.utils.executor.CameraXExecutors
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.ellison.jetpackdemo.R
import com.ellison.jetpackdemo.cameraX.analysis.AnalysisResult
import com.ellison.jetpackdemo.cameraX.analyzer.AnalyzeCallback
import com.ellison.jetpackdemo.cameraX.analysis.ChooseAnalysisFragment
import com.ellison.jetpackdemo.cameraX.analyzer.MyAnalyzer
import com.ellison.jetpackdemo.cameraX.capture.MyCaptureCallback
import com.ellison.jetpackdemo.cameraX.extender.MyExtenderHelper
import com.ellison.jetpackdemo.cameraX.utils.BeepManager
import com.ellison.jetpackdemo.cameraX.utils.Constants
import com.ellison.jetpackdemo.cameraX.utils.Utils
import com.ellison.jetpackdemo.cameraX.video.MyRecordCallback
import com.ellison.jetpackdemo.cameraX.viewmodel.CameraViewModel
import com.ellison.jetpackdemo.databinding.ActivityCameraxLiteBinding
import com.google.zxing.Result
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executor

class NewCameraXActivity : AppCompatActivity() {
    // CameraX or view instance
    private lateinit var mCameraProvider: ProcessCameraProvider
    private lateinit var mCamera: Camera

    private lateinit var cameraZoomState: LiveData<ZoomState>

    // CameraX's use case
    private lateinit var mPreview: Preview
    private var mImageCapture: ImageCapture? = null
    private var mImageAnalysis: ImageAnalysis? = null
    private var mVideoCapture: VideoCapture? = null

    // Analyzer implementation
    private var mAnalyzer: ImageAnalysis.Analyzer? = null

    // Executor that used to do heavy work
    private var heavyExecutor: Executor = CameraXExecutors.ioExecutor()
    // Executor that used to do light work
    private val lightExecutor: Executor by lazy {
        ContextCompat.getMainExecutor(this)
    }

    private val binding: ActivityCameraxLiteBinding by lazy {
        ActivityCameraxLiteBinding.inflate(layoutInflater)
    }
    private val viewModel: CameraViewModel by viewModels()
    private var scaleDetector: ScaleGestureDetector? = null
    private var doubleClickDetector: GestureDetector? = null
    private var singleTapDetector: GestureDetector? = null

    // Values that control camerax's case status.
    private var isBack = true
    private var isAnalyzing = false
    private var isVideoMode = false
    private var isRecording = false
    private var isCameraXHandling = false

    // Mark for file's name for capturing or recording occasion.
    private var recCount = 0
    private var picCount = 0

    // Beep manager when recognized qrcode.
    private lateinit var beepManager: BeepManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Start camera preview until view is attached.
        startCameraWhenAttached()
    }

    private fun startCameraWhenAttached() {
        binding.previewView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener{
            override fun onViewAttachedToWindow(v: View?) {
                Log.d(Constants.TAG_CAMERA, "onViewAttachedToWindow")
                ensureCameraPermission()
            }

            override fun onViewDetachedFromWindow(v: View?) {
                Log.d(Constants.TAG_CAMERA, "onViewDetachedFromWindow")
            }
        })
    }

    private fun ensureCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(Constants.TAG_CAMERA, "no camera permission & request")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                Constants.REQUEST_CAMERA
            )
            return
        }
        setupCamera(binding.previewView)
    }

    private fun ensureAudioStoragePermission(requestId: Int) {
        if (requestId == Constants.REQUEST_STORAGE || requestId == Constants.REQUEST_STORAGE_BINDING) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(Constants.TAG_CAMERA, "no storage permission & request")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    requestId
                )
                return
            }
            if (requestId == Constants.REQUEST_STORAGE) {
                takenPictureInternal(true)
            } else {
                toggleVideoMode()
            }
        } else if (requestId == Constants.REQUEST_STORAGE_VIDEO
            || requestId == Constants.REQUEST_STORAGE_VIDEO_BINDING) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(Constants.TAG_CAMERA, "no storage or audio permission & request")
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO
                    ), requestId
                )
                return
            }

            // toggleVideoMode();
            if (requestId == Constants.REQUEST_STORAGE_VIDEO) {
                recordVideo()
            } else {
                toggleVideoMode()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d(
            Constants.TAG_CAMERA, "requestCode:$requestCode"
                    + " permissions:${permissions.contentToString()}"
                    + " grantResults:${grantResults.contentToString()}"
        )
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }

        when (requestCode) {
            Constants.REQUEST_CAMERA -> setupCamera(binding.previewView)
            Constants.REQUEST_STORAGE -> takenPictureInternal(true)
            Constants.REQUEST_STORAGE_VIDEO -> recordVideo()
            Constants.REQUEST_STORAGE_BINDING, Constants.REQUEST_STORAGE_VIDEO_BINDING -> toggleVideoMode()
        }
    }

    fun onChangeGo(view: View?) {
        isBack = !isBack
        bindPreview(mCameraProvider, binding.previewView)
        mImageAnalysis?.clearAnalyzer()
    }

    fun onCaptureGo(view: View?) {
        if (isVideoMode) {
            if (!isRecording) {
                // Check permission first.
                ensureAudioStoragePermission(Constants.REQUEST_STORAGE_VIDEO)
            } else {
                // Update status right now.
                toggleRecordingStatus()
            }
        } else {
            ensureAudioStoragePermission(Constants.REQUEST_STORAGE)
        }
    }

    fun onAnalyzeGo(view: View?) {
        if (mImageAnalysis == null) {
            return
        }

        if (mAnalyzer == null) {
            mAnalyzer = MyAnalyzer(viewModel, object : AnalyzeCallback {
                override fun onZoomPreview(scale: Double) {
                    zoomPicture(scale)
                }

                override fun onAnalyzeResult(result: AnalysisResult) {
                    Log.d(Constants.TAG_CAMERA, "onAnalyzeResult result:$result isAnalyzing:$isAnalyzing")
                    synchronized(isAnalyzing) {
                        if (TextUtils.isEmpty(result.content) || !isAnalyzing) {
                            return
                        }

                        showQRCodeResult(result.content)
                        // val centerPoint = convertPointsToCenter(image, it.points)
                        val centerPoint = Utils.convertRectToPoint(result.rect, binding.previewView)
                        showPointView(centerPoint)
                        showRectView(centerPoint, result.rect)
                    }
                }
            })
        }

        // Listen to analysis picker event.
        Log.d(Constants.TAG_CAMERA, "observe() viewModel:$viewModel")
        viewModel.analysisLiveData.observe(this,
            { isDone -> isDone?.let {
                Log.d(Constants.TAG_CAMERA, "onChange() clazz:$it")
                if (it) analysisGoInternal()
            }}
        )

        val isAnalysisDone = viewModel.analysisLiveData.value
        Log.d(Constants.TAG_CAMERA, "onChange() value:$isAnalysisDone")

        // Show analysis picker screen if analysis not done.
        if (isAnalysisDone == null) {
            ChooseAnalysisFragment().run {
                show(supportFragmentManager, Constants.TAG_FRAGMENT_CHOOSE_ANALYSIS)
            }
        }
    }

    private fun analysisGoInternal() {
        Log.d(Constants.TAG_CAMERA, "analysisGoInternal()")

        mAnalyzer?.let {
            if (!isAnalyzing) {
                Log.d(Constants.TAG_CAMERA, "setAnalyzer()")
                mImageAnalysis?.setAnalyzer(heavyExecutor, it)
            } else {
                Log.d(Constants.TAG_CAMERA, "clearAnalyzer()")
                mImageAnalysis?.clearAnalyzer()
            }
            isAnalyzing = !isAnalyzing
            binding.qrCodeZone.visibility = if (isAnalyzing) View.VISIBLE else View.GONE
        }
    }

    fun onVideoGo(view: View?) {
        if (!isVideoMode) {
            // Check audio and storage permission before go to video mode.
            ensureAudioStoragePermission(Constants.REQUEST_STORAGE_VIDEO_BINDING)
        } else {
            // Check storage permission before go to camera mode.
            ensureAudioStoragePermission(Constants.REQUEST_STORAGE_BINDING)
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d(Constants.TAG_CAMERA, "onStop()")
        mImageAnalysis?.clearAnalyzer()
    }

    private fun setupCamera(previewView: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            try {
                mCameraProvider = cameraProviderFuture.get()
                bindPreview(mCameraProvider, previewView)
                listenGesture()
                initBeepManager()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }, lightExecutor)
    }

    private fun bindPreview(
        cameraProvider: ProcessCameraProvider,
        previewView: PreviewView, isVideo: Boolean = false
    ) {
        // Select specified camera.
        // val cameraSelector = CameraSelector.Builder().addCameraFilter(AllCameraFilter()).build()

        // Selector that determine which camera will be used.
        val cameraSelector = if (isBack) CameraSelector.DEFAULT_BACK_CAMERA else CameraSelector.DEFAULT_FRONT_CAMERA

        // Image preview use case
        val previewBuilder = Preview.Builder()

        // Image capture use case
        val captureBuilder = ImageCapture.Builder()
            .setTargetRotation(previewView.display.rotation)

        // Image analysis use case
        mImageAnalysis = ImageAnalysis.Builder()
            .setTargetRotation(previewView.display.rotation)
            .setTargetResolution(Size(720, 1440))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        // Video recording use case
        mVideoCapture = VideoCapture.Builder()
            .setTargetRotation(previewView.display.rotation)
            .setVideoFrameRate(25)
            .setBitRate(3 * 1024 * 1024)
            .build()

        // Add extended effect for capture and preview.
        val extenderHelper = MyExtenderHelper()
        extenderHelper.setPreviewExtender(previewBuilder, cameraSelector)
        extenderHelper.setCaptureExtender(captureBuilder, cameraSelector)

        // Create preview and capture use case's instance
        mPreview = previewBuilder.build()
        mImageCapture = captureBuilder.build()

        // Must unbind all owner to the camera before binding.
        cameraProvider.unbindAll()

        try {
            // Bind camera provider with selector and necessary use cases.
            mCamera = if (isVideo) {
                cameraProvider.bindToLifecycle(
                    this, cameraSelector,
                    mPreview, mVideoCapture
                )
            } else {
                cameraProvider.bindToLifecycle(
                    this, cameraSelector,
                    mPreview, mImageCapture, mImageAnalysis
                )
            }

            // Bind the view's surface to preview use case.
            mPreview.setSurfaceProvider(previewView.surfaceProvider)
        } catch (e: java.lang.Exception) {
            Log.e(Constants.TAG_CAMERA, "camera provider bind error:", e)
        }

        cameraZoomState = mCamera.cameraInfo.zoomState
    }

    private fun takenPictureInternal(isExternal: Boolean) {
        Log.d(Constants.TAG_CAMERA, "takenPictureInternal isExternal:$isExternal")
        val contentValues = ContentValues()
        contentValues.put(
            MediaStore.MediaColumns.DISPLAY_NAME, Constants.CAPTURED_FILE_NAME
                    + "_" + picCount++
        )
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
        )
            .build()

        // Mirror image
        ImageCapture.Metadata().apply {
            isReversedHorizontal = true
        }

        mImageCapture?.takePicture(outputFileOptions, lightExecutor, MyCaptureCallback(picCount, this))
    }

    private fun toggleVideoMode() {
        Log.d(Constants.TAG_CAMERA, "toggleVideoMode isVideoMode:$isVideoMode")
        isVideoMode = !isVideoMode
        binding.recordVideo.setImageResource(
            if (isVideoMode) R.drawable.ic_camera_new else R.drawable.ic_video
        )
        binding.capture.setImageResource(
            if (isVideoMode) R.drawable.ic_capture_record else R.drawable.ic_capture
        )
        bindPreview(mCameraProvider, binding.previewView, isVideoMode)
    }

    private fun recordVideo() {
        Log.d(Constants.TAG_CAMERA, "recordVideo() isCameraXHandling:$isCameraXHandling")
        // Ensure recording is done before recording again cause stopRecording is async.
        if (isCameraXHandling) return

        Log.d(Constants.TAG_CAMERA, "recordVideo()")
        val contentValues = ContentValues()
        contentValues.put(
            MediaStore.MediaColumns.DISPLAY_NAME, Constants.RECORDED_FILE_NAME
                    + "_" + recCount++
        )
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, Constants.RECORDED_FILE_NAME_END)
        Log.d(Constants.TAG_CAMERA, "recordVideo() startRecording")
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            mVideoCapture?.startRecording(
                VideoCapture.OutputFileOptions.Builder(
                    contentResolver,
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues
                ).build(),  // CameraXExecutors.mainThreadExecutor(),
                lightExecutor,
                MyRecordCallback(this) {
                    videoRecordingPrepared()
                }
            )
        } catch (e: Exception) {
            Log.e(Constants.TAG_CAMERA, "Record video error:", e)
        }
        toggleRecordingStatus()
        isCameraXHandling = true
    }

    // Enable record button after recording stopped.
    private fun videoRecordingPrepared() {
        Log.d(Constants.TAG_CAMERA, "videoRecordingPrepared()")
        isCameraXHandling = false
        // Keep disabled status for a while to avoid fast click error with "Muxer stop failed!".
        binding.capture.postDelayed({ binding.capture.isEnabled = true }, 500)
    }

    private fun toggleRecordingStatus() {
        Log.d(Constants.TAG_CAMERA, "toggleRecordingStatus() isVideoMode:$isVideoMode isRecording:$isRecording")
        if (!isVideoMode) return

        isRecording = !isRecording
        binding.capture.setImageResource(
            if (isRecording) R.drawable.ic_capture_record_pressing else R.drawable.ic_capture_record
        )

        // Stop recording when toggle to false.
        if (!isRecording) {
            Log.d(Constants.TAG_CAMERA, "toggleRecordingStatus() stopRecording")
            mVideoCapture?.stopRecording()
            // Keep record button disabled till video recording truly stopped.
            binding.capture.post { binding.capture.isEnabled = false }
        }
    }

    private fun initBeepManager() {
        beepManager = BeepManager(this)
        beepManager.setPlayBeep(true)
        beepManager.setVibrate(true)
    }

    // Listen to gesture on preview screen to zoom, focus camera.
    private fun listenGesture() {
        binding.previewView.setOnTouchListener { view, event ->
            Log.d(Constants.TAG_CAMERA_UI, "onTouch event:$event")

            // Listen to zoom gesture.
            scalePreview(event)

            // Zoom when double click.
            doubleClickZoom(event)

            // Singe tap for focus.
            singleTapForFocus(event)

            true
        }

        // Focus center first.
        focusOnPosition((binding.previewView.width / 2).toFloat(), (binding.previewView.height / 2).toFloat())
    }

    // Zoom preview screen when scale gesture.
    private fun scalePreview(event: MotionEvent) {
        Log.d(Constants.TAG_CAMERA_UI, "scalePreview event:$event")
        if (scaleDetector == null) {
            scaleDetector = ScaleGestureDetector(this@NewCameraXActivity,
                object : SimpleOnScaleGestureListener() {
                    override fun onScale(detector: ScaleGestureDetector): Boolean {
                        cameraZoomState.value?.let {
                            val zoomRatio = it.zoomRatio
                            Log.d(
                                Constants.TAG_CAMERA_UI,
                                "Scale factor:${detector.scaleFactor} current:$zoomRatio linear:${it.linearZoom}"
                            )
                            mCamera.cameraControl.setZoomRatio(zoomRatio * detector.scaleFactor)
                        }
                        return true
                    }
                })
        }
        Log.d(Constants.TAG_CAMERA_UI, "scalePreview onTouchEvent")
        scaleDetector?.onTouchEvent(event)
    }

    // Zoom preview screen when double click gesture.
    private fun doubleClickZoom(event: MotionEvent) {
        Log.d(Constants.TAG_CAMERA_UI, "doubleClickZoom event:$event")
        if (doubleClickDetector == null) {
            doubleClickDetector = GestureDetector(this@NewCameraXActivity,
                object : GestureDetector.SimpleOnGestureListener() {
                    override fun onDoubleTap(e: MotionEvent?): Boolean {
                        Log.d(Constants.TAG_CAMERA_UI, "Double tap")

                        cameraZoomState.value?.let {
                            val zoomRatio = it.zoomRatio
                            val minRatio = it.minZoomRatio
                            Log.d(Constants.TAG_CAMERA_UI,
                                "Double tap zoomRatio:$zoomRatio min:$minRatio max:${it.maxZoomRatio}")

                            // Ratio parameter from 0f to 1f.
                            if (zoomRatio > minRatio) {
                                // Reset to original ratio
                                mCamera.cameraControl.setLinearZoom(Constants.MIN_ZOOM_SCALE.toFloat())
                            } else {
                                // Or zoom to 0.5 ratio
                                mCamera.cameraControl.setLinearZoom(Constants.MIDDLE_ZOOM_SCALE.toFloat())
                                // Zoom to max
                                // mCamera.cameraControl.setLinearZoom(Constants.MAX_ZOOM_SCALE.toFloat())
                            }
                        }
                        return true
                    }
            })
        }
        Log.d(Constants.TAG_CAMERA_UI, "doubleClickDetector onTouchEvent")
        doubleClickDetector?.onTouchEvent(event)
    }

    // Focus when single tap.
    private fun singleTapForFocus(event: MotionEvent) {
        if (singleTapDetector == null) {
            singleTapDetector = GestureDetector(this@NewCameraXActivity,
                object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                        Log.d(Constants.TAG_CAMERA_UI, "Single tap confirmed with event:$e")
                        // Focus view when tap.
                        focusOnPosition(event.x, event.y, true)
                        return super.onSingleTapConfirmed(e)
                    }
                })
        }

        singleTapDetector?.onTouchEvent(event)
    }

    private fun focusOnPosition(x: Float, y: Float, needShowTap: Boolean = false) {
        Log.d(Constants.TAG_CAMERA_UI, "focusPosition x:$x y:$y")
        val action = FocusMeteringAction.Builder(
            binding.previewView.meteringPointFactory.createPoint(x, y)
        ).build()

        try {
            if (needShowTap) showTapView(x.toInt(), y.toInt())

            Log.d(Constants.TAG_CAMERA_UI, "Focus camera")
            mCamera.cameraControl.startFocusAndMetering(action)
        } catch (e: Exception) {
            Log.e(Constants.TAG_CAMERA_UI, "Error focus camera:e")
        }
    }

    // Show finger tap location on preview screen.
    private fun showTapView(x: Int, y: Int) {
        Log.d(Constants.TAG_CAMERA_UI, "showTapView x:$x y:$y")
        val popupWindow = PopupWindow(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val imageView = ImageView(this)
        imageView.setImageResource(R.drawable.ic_focus_view)

        val offset = resources.getDimensionPixelSize(R.dimen.tap_view_size)
        Log.d(Constants.TAG_CAMERA_UI, "showTapView offset:$offset")

        popupWindow.contentView = imageView
        // popupWindow.setBackgroundDrawable(getDrawable(android.R.color.holo_blue_bright));
        // popupWindow.showAtLocation(binding.previewView, Gravity.CENTER, x, y);
        // popupWindow.showAsDropDown(binding.previewView, x, y)
        popupWindow.showAsDropDown(binding.previewView, x - offset / 2, y - offset /2 )

        binding.previewView.postDelayed({ popupWindow.dismiss() }, 600)
        // binding.previewView.playSoundEffect(SoundEffectConstants.CLICK);
    }

    // Zoom out the preview size to decode bar code clearly.
    private fun zoomPicture(scale: Double) {
        Log.d(Constants.TAG_CAMERA, "zoomPicture scale:$scale")
        // mCamera.cameraControl.setLinearZoom(scale.toFloat())
        mCamera.cameraControl.setZoomRatio(scale.toFloat())
    }

    private fun showQRCodeResult(result: Result?) {
        binding.qrCodeResult.post {
            binding.qrCodeResult.text =
                if (result != null) """ Link: ${result.text} """.trimIndent() else ""
        }
        binding.qrCodeResult.playSoundEffect(SoundEffectConstants.CLICK)
    }

    // Show bar code's decoded content.
    private fun showQRCodeResult(result: String) {
        Log.d(Constants.TAG_CAMERA_UI, "showQRCodeResult result:$result")
        stopAnalysis()
        beepManager.playBeepSoundAndVibrate()

        binding.qrCodeResult.post {
            binding.qrCodeResult.visibility = View.VISIBLE
            binding.qrCodeResult.text = result
        }
    }

    // Show a green point to indicate bar code.
    private fun showPointView(point: Point) {
        Log.d(Constants.TAG_CAMERA_UI, "showPointView point:$point")
        if (point.x == 0 || point.y == 0)
            return

        val popupWindow = PopupWindow(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val imageView = ImageView(this)

        runOnUiThread {
            popupWindow.contentView = imageView
            imageView.setImageResource(R.drawable.ic_point_view)
            popupWindow.showAsDropDown(binding.previewView, point.x, point.y)
            binding.previewView.postDelayed({ popupWindow.dismiss() }, 1000)
        }
    }

    // Show a green rectangle to indicate bar code.
    private fun showRectView(point: Point, rect: Rect) {
        Log.d(Constants.TAG_CAMERA_UI, "showRectView rect:$rect")

        val popupWindow = PopupWindow(
            // ViewGroup.LayoutParams.WRAP_CONTENT,
            // ViewGroup.LayoutParams.WRAP_CONTENT
            rect.height(),
            rect.width()
        )
        val imageView = ImageView(this)
        // imageView.layoutParams = LinearLayout.LayoutParams(
            // rect.height(),
            // rect.width()
        // )

        runOnUiThread {
            popupWindow.contentView = imageView
            imageView.setImageResource(R.drawable.ic_rect_view)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            try {
                // popupWindow.showAsDropDown(binding.previewView, point.x, point.y)
                popupWindow.showAsDropDown(binding.previewView,
                point.x - (rect.width() / 2), point.y)
            } catch (e: Exception) {
            }
            binding.previewView.postDelayed({ popupWindow.dismiss() }, 1000)
        }
    }

    private fun stopAnalysis() {
        isAnalyzing = false
        mImageAnalysis?.clearAnalyzer()
        binding.qrCodeZone.postDelayed({
            binding.qrCodeZone.visibility = View.GONE
            binding.qrCodeResult.text = ""
            mCamera.cameraControl.setZoomRatio(Constants.DEFAULT_ZOOM_SCALE.toFloat())
        }, Constants.ANALYSIS_RESULT_SHOW_DURATION)
    }
}