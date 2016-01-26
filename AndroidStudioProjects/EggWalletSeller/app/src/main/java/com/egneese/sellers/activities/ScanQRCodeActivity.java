package com.egneese.sellers.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by adityaagrawal on 11/01/16.
 */
public class ScanQRCodeActivity extends Activity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;
    private String TAG = ScanQRCodeActivity.class.getName();
    private Boolean isFirst = false;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.containsKey("key") && bundle.getString("key") != null){
                isFirst = true;
            }
        }
        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        if(isFirst) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("qrDataDTO", rawResult.getContents().toString());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }else{
            Intent intent = new Intent(this, DashboardActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("qrDataDTO", rawResult.getContents().toString());
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


        // If you would like to resume scanning, call this method below:
        // mScannerView.resumeCameraPreview(this);
    }
}