package com.ellison.jetpackdemo.cameraX;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.ellison.jetpackdemo.R;
import com.ellison.jetpackdemo.databinding.ActivityCameraxLiteBinding;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.extensions.BeautyPreviewExtender;
import androidx.camera.extensions.NightImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class DemoActivityLite extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 20,
            REQUEST_STORAGE = 30,
            REQUEST_STORAGE_BINDING = 35,
            REQUEST_STORAGE_VIDEO = 40,
            REQUEST_STORAGE_VIDEO_BINDING = 45;
    private static final String CAPTURED_FILE_NAME = "captured_picture";
    private static final String RECORDED_FILE_NAME = "recorded_video",
            RECORDED_FILE_NAME_END = "video/mp4";

    private ProcessCameraProvider mCameraProvider;
    private Preview mPreview;
    private Camera mCamera;
    private ImageCapture mImageCapture;
    private ImageAnalysis mImageAnalysis;
    private VideoCapture mVideoCapture;

    ActivityCameraxLiteBinding binding;
    private boolean isBack = true, isAnalyzing, isVideoMode, isRecording, isCameraXHandling;
    private int recCount, picCount;

    private MultiFormatReader multiFormatReader = new MultiFormatReader();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCameraxLiteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ensureCameraPermission();
        binding.previewView.setOnTouchListener((v, event) -> {
            FocusMeteringAction action = new FocusMeteringAction.Builder(
                    binding.previewView.getMeteringPointFactory()
                            .createPoint(event.getX(), event.getY())).build();
            try {
                showTapView((int) event.getX(), (int) event.getY());
                Log.d("Camera", "Focus camera");
                mCamera.getCameraControl().startFocusAndMetering(action);
            } catch (Exception e) {
                Log.e("Camera", "Error focus camera");
            }
            return false;
        });
    }

    private void ensureCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("Camera", "no camera permission & request");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
            return;
        }
        setupCamera(binding.previewView);
    }

    private void ensureAudioStoragePermission(int requestId) {
        if (requestId == REQUEST_STORAGE || requestId == REQUEST_STORAGE_BINDING) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.d("Camera", "no storage permission & request");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestId);
                return;
            }

            if (requestId == REQUEST_STORAGE) {
                takenPictureInternal(true);
            } else {
                toggleVideoMode();
            }
        } else if (requestId == REQUEST_STORAGE_VIDEO || requestId == REQUEST_STORAGE_VIDEO_BINDING) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.d("Camera", "no storage or audio permission & request");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO}, requestId);
                return;
            }

            // toggleVideoMode();
            if (requestId == REQUEST_STORAGE_VIDEO) {
                recordVideo();
            } else {
                toggleVideoMode();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("Camera", "requestCode:" + requestCode
                + " permissions:" + Arrays.toString(permissions)
                + " grantResults:" + Arrays.toString(grantResults));
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        switch (requestCode) {
            case REQUEST_CAMERA:
                setupCamera(binding.previewView);
                break;
            case REQUEST_STORAGE:
                takenPictureInternal(true);
                break;
            case REQUEST_STORAGE_VIDEO:
                recordVideo();
                break;
            case REQUEST_STORAGE_BINDING:
            case REQUEST_STORAGE_VIDEO_BINDING:
                toggleVideoMode();
                break;
            default:
                break;
        }
    }

    public void onChangeGo(View view) {
        if (mCameraProvider != null) {
            isBack = !isBack;
            bindPreview(mCameraProvider, binding.previewView);
            if (mImageAnalysis != null) {
                mImageAnalysis.clearAnalyzer();
            }
        }
    }

    public void onCaptureGo(View view) {
        if (isVideoMode) {
            if (!isRecording) {
                // Check permission first.
                ensureAudioStoragePermission(REQUEST_STORAGE_VIDEO);
            } else {
                // Update status right now.
                toggleRecordingStatus();
            }
        } else {
            ensureAudioStoragePermission(REQUEST_STORAGE);
        }
    }

    public void onAnalyzeGo(View view) {
        if (mImageAnalysis == null) {
            return;
        }

        if (!isAnalyzing) {
            Log.d("Camera", "setAnalyzer()");
            mImageAnalysis.setAnalyzer(CameraXExecutors.mainThreadExecutor(), image -> {
                Log.d("Camera", "analyze() image:" + image);
                analyzeQRCode(image);
            });
        } else {
            Log.d("Camera", "clearAnalyzer()");
            mImageAnalysis.clearAnalyzer();
        }
        isAnalyzing = !isAnalyzing;
        binding.qrCodeZone.setVisibility(isAnalyzing ? View.VISIBLE : View.GONE);
    }

    public void onVideoGo(View view) {
        if (!isVideoMode) {
            // Check audio and storage permission before go to video mode.
            ensureAudioStoragePermission(REQUEST_STORAGE_VIDEO_BINDING);
        } else {
            // Check storage permission before go to camera mode.
            ensureAudioStoragePermission(REQUEST_STORAGE_BINDING);
        }
    }

    private void setupCamera(PreviewView previewView) {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                mCameraProvider = cameraProviderFuture.get();
                bindPreview(mCameraProvider, previewView);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider,
                             PreviewView previewView) {
        bindPreview(cameraProvider, previewView, false);
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider,
                             PreviewView previewView, boolean isVideo) {
        Preview.Builder previewBuilder = new Preview.Builder();
        ImageCapture.Builder captureBuilder = new ImageCapture.Builder()
                .setTargetRotation(previewView.getDisplay().getRotation());
        CameraSelector cameraSelector = isBack ? CameraSelector.DEFAULT_BACK_CAMERA
                : CameraSelector.DEFAULT_FRONT_CAMERA;

        mImageAnalysis = new ImageAnalysis.Builder()
                .setTargetRotation(previewView.getDisplay().getRotation())
                .setTargetResolution(new Size(720, 1440))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        mVideoCapture = new VideoCapture.Builder()
                .setTargetRotation(previewView.getDisplay().getRotation())
                .setVideoFrameRate(25)
                .setBitRate(3 * 1024 * 1024)
                .build();

        setPreviewExtender(previewBuilder, cameraSelector);
        mPreview = previewBuilder.build();

        setCaptureExtender(captureBuilder, cameraSelector);
        mImageCapture =  captureBuilder.build();

        cameraProvider.unbindAll();
        if (isVideo) {
            mCamera = cameraProvider.bindToLifecycle(this, cameraSelector,
                    mPreview, mVideoCapture);
        } else {
            mCamera = cameraProvider.bindToLifecycle(this, cameraSelector,
                    mPreview, mImageCapture, mImageAnalysis);
        }
        mPreview.setSurfaceProvider(previewView.getSurfaceProvider());
    }

    private void showTapView(int x, int y) {
        Log.d("Camera", "Tap x:" + x + " y:" + y);
        PopupWindow popupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // popupWindow.setBackgroundDrawable(getDrawable(android.R.color.holo_blue_bright));
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.ic_focus_view);
        popupWindow.setContentView(imageView);
        // popupWindow.showAtLocation(binding.previewView, Gravity.CENTER, x, y);
        popupWindow.showAsDropDown(binding.previewView, x, y);
        binding.previewView.postDelayed(popupWindow::dismiss, 600);
        // binding.previewView.playSoundEffect(SoundEffectConstants.CLICK);
    }

    private void takenPictureInternal(boolean isExternal) {
        Log.d("Camera", "takenPictureInternal isExternal:" + isExternal);
        final ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, CAPTURED_FILE_NAME
                + "_" + picCount++);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");

        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(
                        getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                .build();
        if (mImageCapture != null) {
            mImageCapture.takePicture(outputFileOptions, CameraXExecutors.mainThreadExecutor(),
                    new ImageCapture.OnImageSavedCallback() {
                        @Override
                        public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                            Log.d("Camera", "outputFileResults:"
                                    + outputFileResults.getSavedUri()
                                    + " picCount:" + picCount);
                            Toast.makeText(DemoActivityLite.this, "Picture got"
                                    + (outputFileResults.getSavedUri() != null
                                    ? " @ " + outputFileResults.getSavedUri().getPath()
                                    : "") + ".", Toast.LENGTH_SHORT)
                                    .show();
                        }

                        @Override
                        public void onError(@NonNull ImageCaptureException exception) {
                            Log.d("Camera", "onError:" + exception.getImageCaptureError());
                        }
                    });
        }
    }

    private void analyzeQRCode(@NonNull ImageProxy imageProxy) {
        ByteBuffer byteBuffer = imageProxy.getPlanes()[0].getBuffer();
        byte[] data = new byte[byteBuffer.remaining()];
        byteBuffer.get(data);

        int width = imageProxy.getWidth(), height = imageProxy.getHeight();
        PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(
                data, width, height, 0, 0, width, height, false);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Result result;
        try {
            result = multiFormatReader.decode(bitmap);
            Log.d("Camera", "result:" + result);
        } catch (Exception e) {
            Log.e("Camera", "Error decoding barcode");
            result = null;
        }
        showQRCodeResult(result);
        imageProxy.close();
    }

    private void showQRCodeResult(@Nullable Result result) {
        if (binding != null && binding.qrCodeResult != null) {
            binding.qrCodeResult.post(() ->
                    binding.qrCodeResult.setText(result != null ? "Link:\n" + result.getText() : ""));
            binding.qrCodeResult.playSoundEffect(SoundEffectConstants.CLICK);
        }
    }

    private void toggleVideoMode() {
        Log.d("Camera", "toggleVideoMode isVideoMode:" + isVideoMode);
        isVideoMode = !isVideoMode;
        binding.recordVideo.setImageResource(isVideoMode ? R.drawable.ic_camera_new
                : R.drawable.ic_video);
        binding.capture.setImageResource(isVideoMode ? R.drawable.ic_capture_record
                : R.drawable.ic_capture);
        bindPreview(mCameraProvider, binding.previewView, isVideoMode);
    }

    private void recordVideo() {
        Log.d("Camera", "recordVideo() isCameraXHandling:" + isCameraXHandling);
        // Ensure recording is done before recording again cause stopRecording is async.
        if (isCameraXHandling) return;

        Log.d("Camera", "recordVideo()");
        final ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, RECORDED_FILE_NAME
                + "_" + recCount++);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, RECORDED_FILE_NAME_END);

        Log.d("Camera", "recordVideo() startRecording");
        try {
            mVideoCapture.startRecording(
                    new VideoCapture.OutputFileOptions.Builder(getContentResolver(),
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)
                            .build(),
                    // CameraXExecutors.mainThreadExecutor(),
                    CameraXExecutors.ioExecutor(),
                    new VideoCapture.OnVideoSavedCallback() {
                        @Override
                        public void onVideoSaved(@NonNull VideoCapture.OutputFileResults outputFileResults) {
                            Log.d("Camera", "onVideoSaved outputFileResults:"
                                    + outputFileResults.getSavedUri().getPath());
                            CameraXExecutors.mainThreadExecutor().execute(() -> Toast.makeText(DemoActivityLite.this,
                                    "Video got" + (outputFileResults.getSavedUri() != null ?
                                            " @ " + outputFileResults.getSavedUri().getPath(): "")
                                            + ".", Toast.LENGTH_LONG).show());
//                            Toast.makeText(DemoActivityLite.this,
//                                    "Video got" + (outputFileResults.getSavedUri() != null ?
//                                            " @ " + outputFileResults.getSavedUri().getPath(): "")
//                                            + ".", Toast.LENGTH_LONG).show();
                            videoRecordingPrepared();
                        }

                        @Override
                        public void onError(int videoCaptureError, @NonNull String message,
                                            @Nullable Throwable cause) {
                            Log.d("Camera", "onError videoCaptureError:"
                                    + videoCaptureError + " message:" + message, cause);
                            videoRecordingPrepared();
                        }
                    }
            );
        } catch (Exception e) {
            Log.e("Camera", "Record video error:", e);
        }
        toggleRecordingStatus();
        isCameraXHandling = true;
    }

    // Enable record button after recording stopped.
    private void videoRecordingPrepared() {
        Log.d("Camera", "videoRecordingPrepared()");
        isCameraXHandling = false;
        // Keep disabled status for a while to avoid fast click error with "Muxer stop failed!".
        binding.capture.postDelayed(() -> binding.capture.setEnabled(true), 500);
    }

    private void toggleRecordingStatus() {
        Log.d("Camera", "toggleRecordingStatus() isVideoMode:" + isVideoMode + " isRecording:" + isRecording);
        if (!isVideoMode) return;

        isRecording = !isRecording;
        binding.capture.setImageResource(isRecording
                ? R.drawable.ic_capture_record_pressing : R.drawable.ic_capture_record);

        // Stop recording when toggle to false.
        if (!isRecording && mVideoCapture != null) {
            Log.d("Camera", "toggleRecordingStatus() stopRecording");
            mVideoCapture.stopRecording();
            // Keep record button disabled till video recording truly stopped.
            binding.capture.post(() -> binding.capture.setEnabled(false));
        }
    }

    private void setPreviewExtender(Preview.Builder builder, CameraSelector cameraSelector) {
        BeautyPreviewExtender beautyPreviewExtender = BeautyPreviewExtender.create(builder);
        if (beautyPreviewExtender.isExtensionAvailable(cameraSelector)) {
            // Enable the extension if available.
            Log.d("Camera", "beauty preview extension enable");
            beautyPreviewExtender.enableExtension(cameraSelector);
        } else {
            Log.d("Camera", "beauty preview extension not available");
        }
    }

    private void setCaptureExtender(ImageCapture.Builder builder, CameraSelector cameraSelector) {
        NightImageCaptureExtender nightImageCaptureExtender = NightImageCaptureExtender.create(builder);
        if (nightImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
            // Enable the extension if available.
            Log.d("Camera", "night capture extension enable");
            nightImageCaptureExtender.enableExtension(cameraSelector);
        } else {
            Log.d("Camera", "night capture extension not available");
        }

//        BokehImageCaptureExtender bokehImageCapture = BokehImageCaptureExtender.create(builder);
//        if (bokehImageCapture.isExtensionAvailable(cameraSelector)) {
//            // Enable the extension if available.
//            Log.d("Camera", "hdr extension enable");
//            bokehImageCapture.enableExtension(cameraSelector);
//        } else {
//            Log.d("Camera", "hdr extension not available");
//        }
//
//        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);
//        if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
//            // Enable the extension if available.
//            Log.d("Camera", "night extension enable");
//            hdrImageCaptureExtender.enableExtension(cameraSelector);
//        } else {
//            Log.d("Camera", "night extension not available");
//        }
//
//        BeautyImageCaptureExtender beautyImageCaptureExtender = BeautyImageCaptureExtender.create(builder);
//        if (beautyImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
//            // Enable the extension if available.
//            Log.d("Camera", "beauty extension enable");
//            beautyImageCaptureExtender.enableExtension(cameraSelector);
//        } else {
//            Log.d("Camera", "beauty extension not available");
//        }
    }
}