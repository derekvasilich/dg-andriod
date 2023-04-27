package com.example.dg_andriod.ui.home;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.OnSuccessListener;

import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.ArrayList;

public class BarcodeAnalyzer implements ImageAnalysis.Analyzer {

    BarcodeScannerOptions options =
            new BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(
                            Barcode.FORMAT_PDF417,
                            Barcode.FORMAT_QR_CODE
                    )
                    .enableAllPotentialBarcodes()
                    .build();

    String prevRawValue;
    String prevPrevRawValue;
    Rect bounds;
    Point[] corners;
    @NonNull OnSuccessListener<ArrayList<BarcodeField>> onSuccess;

    final String MANUAL_TESTING_LOG = "Barcodes";

    public BarcodeAnalyzer(@NonNull OnSuccessListener<ArrayList<BarcodeField>> onSuccess) {
        this.onSuccess = onSuccess;
    }

    @Override
    @SuppressLint({"UnsafeOptInUsageError"})
    public void analyze(ImageProxy imageProxy) {
        Image mediaImage = imageProxy.getImage();
        if (mediaImage == null) {
            return;
        }
        InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
        BarcodeScanner scanner = BarcodeScanning.getClient(options);
        scanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    // get parts of the barcode
                    if (barcodes.isEmpty()) {
                        Log.v(MANUAL_TESTING_LOG, "No barcode has been detected");
                    }

                    for (Barcode barcode: barcodes) {
                        bounds = barcode.getBoundingBox();
                        corners = barcode.getCornerPoints();
                        String rawValue = barcode.getRawValue();
                        Barcode.UrlBookmark url = barcode.getUrl();
                        int valueType = barcode.getValueType();
                        String type = barcodeTypeToString(valueType);
                        // require three consecutive matches
                        if (valueType > 0 &&
                                rawValue != null && prevRawValue != null && prevPrevRawValue != null &&
                                rawValue.compareTo(prevRawValue) == 0 && prevRawValue.compareTo(prevPrevRawValue) == 0) {
                            switch (valueType) {
                                case Barcode.TYPE_DRIVER_LICENSE:
                                    Barcode.DriverLicense dl = barcode.getDriverLicense();
                                    if (dl != null) {
                                        onSuccess.onSuccess(displayDriversLicenseCard(type, dl, bounds, corners));
                                    }
                                    break;

                                default:
                                    onSuccess.onSuccess(displayBarcodeCard(type, rawValue, bounds, corners));
                            }
                        }
                        prevPrevRawValue = prevRawValue;
                        prevRawValue = rawValue;
                    }
                })
                .addOnFailureListener(e -> {
                    // display failure
                })
                .addOnCompleteListener(task -> {
                    imageProxy.close();
                });
    }

    private String barcodeTypeToString(int valueType) {
        switch (valueType) {
            case Barcode.TYPE_UNKNOWN:              return "Unknown";
            case Barcode.TYPE_CONTACT_INFO:         return "Contact Info";
            case Barcode.TYPE_EMAIL:                return "Email";
            case Barcode.TYPE_ISBN:                 return "ISBN";
            case Barcode.TYPE_PHONE:                return "Phone";
            case Barcode.TYPE_PRODUCT:              return "Product";
            case Barcode.TYPE_SMS:                  return "SMS";
            case Barcode.TYPE_TEXT:                 return "Text";
            case Barcode.TYPE_URL:                  return "URL";
            case Barcode.TYPE_WIFI:                 return "WiFi";
            case Barcode.TYPE_GEO:                  return "Geo";
            case Barcode.TYPE_CALENDAR_EVENT:       return "Calendar Event";
            case Barcode.TYPE_DRIVER_LICENSE:       return "Drivers License";
            default:                                return "Undefined";
        }
    }

    private ArrayList<BarcodeField> displayBarcodeCard(String valueType, String rawValue, Rect bounds, Point[] corners) {
        ArrayList<BarcodeField> barcodeList = new ArrayList<>();
        barcodeList.add(new BarcodeField("Type detected:", valueType));
        barcodeList.add(new BarcodeField("Other value: ", rawValue));
        barcodeList.add(new BarcodeField("Bounds: ", bounds.toString()));
        barcodeList.add(new BarcodeField("Corners: ", corners.toString()));
        return barcodeList;
    }

    private ArrayList<BarcodeField> displayDriversLicenseCard(String valueType, Barcode.DriverLicense dl, Rect bounds, Point[] corners) {
        ArrayList<BarcodeField> barcodeList = new ArrayList<>();
        barcodeList.add(new BarcodeField("Type detected:", valueType));
        barcodeList.add(new BarcodeField("City: ", dl.getAddressCity()));
        barcodeList.add(new BarcodeField("State: ", dl.getAddressState()));
        barcodeList.add(new BarcodeField("Street: ", dl.getAddressStreet()));
        barcodeList.add(new BarcodeField("Zip code: ", dl.getAddressZip()));
        barcodeList.add(new BarcodeField("Birthday: ", dl.getBirthDate()));
        barcodeList.add(new BarcodeField("Document type: ", dl.getDocumentType()));
        barcodeList.add(new BarcodeField("Expiry date: ", dl.getExpiryDate()));
        barcodeList.add(new BarcodeField("First name: ", dl.getFirstName()));
        barcodeList.add(new BarcodeField("Middle name: ", dl.getMiddleName()));
        barcodeList.add(new BarcodeField("Last name: ", dl.getLastName()));
        barcodeList.add(new BarcodeField("Gender: ", dl.getGender()));
        barcodeList.add(new BarcodeField("Issue date: ", dl.getIssueDate()));
        barcodeList.add(new BarcodeField("Issue country: ", dl.getIssuingCountry()));
        barcodeList.add(new BarcodeField("License number: ", dl.getLicenseNumber()));
        barcodeList.add(new BarcodeField("Bounds: ", bounds.toString()));
        barcodeList.add(new BarcodeField("Corners: ", corners.toString()));
        return barcodeList;
    }
}
