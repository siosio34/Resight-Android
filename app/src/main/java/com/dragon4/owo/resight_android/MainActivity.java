package com.dragon4.owo.resight_android;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dragon4.owo.resight_android.Activity.RESightMainActivity;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = "MainActivity";

    private static final int RC_LOCATION_REQUEST_PERM = 8001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestBloothPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_LOCATION_REQUEST_PERM)
    public void requestBloothPermission() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // 블루투스는 노말권한 .
            // 하지만 https://developer.android.com/about/versions/marshmallow/android-6.0-changes.html#behavior-notifications
            // 블루투스와 Wi-Fi 스캔으로 근처 외부 기기의 하드웨어 식별자에 액세스하려면 앱에 ACCESS_FINE_LOCATION 또는 ACCESS_COARSE_LOCATION 권한이 있어야한다.
            moveSearchBloothActivity();

        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_location),
                    RC_LOCATION_REQUEST_PERM, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void moveSearchBloothActivity() {
        Intent intent = new Intent(getApplicationContext(),RESightMainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

}
