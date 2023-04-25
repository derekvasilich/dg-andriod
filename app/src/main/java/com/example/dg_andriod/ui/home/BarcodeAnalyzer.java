package com.example.dg_andriod.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;

public class BarcodeAnalyzer implements ImageAnalysis.Analyzer {

    BarcodeScannerOptions options =
            new BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_PDF417)
                    .enableAllPotentialBarcodes()
                    .build();

    Context context;

    final String MANUAL_TESTING_LOG = "Barcodes";

    public BarcodeAnalyzer(Context context) {
        this.context = context;
    }

    @Override
    public void analyze(ImageProxy imageProxy) {
        @SuppressLint({"UnsafeOptInUsageError"}) Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
            BarcodeScanner scanner = BarcodeScanning.getClient(options);
            Task<List<Barcode>> result = scanner.process(image)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            // get parts of the barcode
                            if (barcodes.isEmpty()) {
                                Log.v(MANUAL_TESTING_LOG, "No barcode has been detected");
                            }

                            for (Barcode barcode: barcodes) {
//                                Rect bounds = barcode.getBoundingBox();
//                                Point[] corners = barcode.getCornerPoints();
//
//                                String rawValue = barcode.getRawValue();
//
                                int valueType = barcode.getValueType();

                                // https://developers.google.com/ml-kit/vision/barcode-scanning/android#performance-tips
                                switch (valueType) {
                                    case Barcode.TYPE_DRIVER_LICENSE:
                                        Barcode.DriverLicense dl = barcode.getDriverLicense();
                                        if (dl != null) {
                                            Log.v(MANUAL_TESTING_LOG, "Driver license city: " + dl.getAddressCity());
                                            Log.v(MANUAL_TESTING_LOG, "Driver license state: " + dl.getAddressState());
                                            Log.v(MANUAL_TESTING_LOG, "Driver license street: " + dl.getAddressStreet());
                                            Log.v(MANUAL_TESTING_LOG, "Driver license zip code: " + dl.getAddressZip());
                                            Log.v(MANUAL_TESTING_LOG, "Driver license birthday: " + dl.getBirthDate());
                                            Log.v(MANUAL_TESTING_LOG, "Driver license document type: " + dl.getDocumentType());
                                            Log.v(MANUAL_TESTING_LOG, "Driver license expiry date: " + dl.getExpiryDate());
                                            Log.v(MANUAL_TESTING_LOG, "Driver license first name: " + dl.getFirstName());
                                            Log.v(MANUAL_TESTING_LOG, "Driver license middle name: " + dl.getMiddleName());
                                            Log.v(MANUAL_TESTING_LOG, "Driver license last name: " + dl.getLastName());
                                            Log.v(MANUAL_TESTING_LOG, "Driver license gender: " + dl.getGender());
                                            Log.v(MANUAL_TESTING_LOG, "Driver license issue date: " + dl.getIssueDate());
                                            Log.v(MANUAL_TESTING_LOG, "Driver license issue country: " + dl.getIssuingCountry());
                                            Log.v(MANUAL_TESTING_LOG, "Driver license number: " + dl.getLicenseNumber());
                                        }
                                        // show DL card with info
                                        break;

                                    default:
                                        Log.v(MANUAL_TESTING_LOG, "Other barcode type detected: "+ valueType);
                                        Log.v(MANUAL_TESTING_LOG, "Other barcode value: " + barcode.getRawValue());
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // display failure
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<List<Barcode>>() {
                       @Override
                       public void onComplete(@NonNull Task<List<Barcode>> task) {
                           // complete
                           Log.v(MANUAL_TESTING_LOG, "Closing imageProxy.");
                           imageProxy.close();
                       }
                   });
        }
    }
}
