package com.example.dg_andriod.ui.home;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.dg_andriod.R;
import com.google.common.util.concurrent.ListenableFuture;

import androidx.camera.core.Preview;
import androidx.camera.core.CameraSelector;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private ExecutorService cameraExecutor;
    private PreviewView mPreviewView;
    private ImageCapture imageCapture;
    private ImageAnalysis barcodeAnalysis;
    private BarcodeAnalyzer barcodeAnalyzer;
    private BarcodeResultFragment barcodeResultFragment;

    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};

    private void startCamera() {
        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {

                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);

                } catch (ExecutionException | InterruptedException e) {
                    // No errors need to be handled for this Future.
                    // This should never be reached.
                }
            }
        }, ContextCompat.getMainExecutor(getContext()));
    }

    public String getImagesDirectoryName() {
        String dirPath = "";
        dirPath = Environment.getExternalStorageDirectory().toString() + "/images";
        File dir = new File(dirPath);
        if (!dir.exists() && !dir.mkdirs()) {

        }
        return dirPath;
    }

    private void captureImage() {
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        File file = new File(getImagesDirectoryName(), mDateFormat.format(new Date())+ ".jpg");

        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(getContext()), new ImageCapture.OnImageSavedCallback () {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                Toast.makeText(getContext(), "Image captured as: "+file.getName(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(@NonNull ImageCaptureException error) {
                Toast.makeText(getContext(), "Image capture failed: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

    private boolean allPermissionsGranted(){
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(getContext(), "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageCapture.Builder builder = new ImageCapture.Builder();
        imageCapture = builder
                .setTargetRotation(getActivity().getWindowManager().getDefaultDisplay().getRotation())
                .build();

        barcodeAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        barcodeAnalyzer = new BarcodeAnalyzer(barcodeFieldList -> onSuccess(barcodeFieldList));

        preview.setSurfaceProvider(mPreviewView.getSurfaceProvider());
        startAnalysis();

        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, barcodeAnalysis, imageCapture);
    }

    void startAnalysis() {
        Toast.makeText(getContext(), "Point your camera at a barcode", Toast.LENGTH_LONG).show();
        barcodeAnalysis.setAnalyzer(cameraExecutor, barcodeAnalyzer);
    }

    void onDismiss(DialogInterface dialogInterface) {
        startAnalysis();
    }

    void onSuccess(ArrayList<BarcodeField> barcodeFieldList) {
        BarcodeResultFragment.show(getActivity().getSupportFragmentManager(), barcodeFieldList, dialog -> onDismiss(dialog));
        barcodeAnalysis.clearAnalyzer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final Button buttonScan = root.findViewById(R.id.button_scan);
        final Button buttonCapture = root.findViewById(R.id.button_capture);
        buttonScan.setVisibility(View.INVISIBLE);
        buttonCapture.setVisibility(View.INVISIBLE);

        mPreviewView = root.findViewById(R.id.camera);
        cameraExecutor = Executors.newSingleThreadExecutor();
        if(allPermissionsGranted()){
            startCamera(); //start camera if permission has been granted by user
        } else{
            ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        buttonScan.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Scan button clicked", Toast.LENGTH_SHORT).show();
        });
        buttonCapture.setOnClickListener(view -> {
            captureImage();
        });
        return root;
    }
}