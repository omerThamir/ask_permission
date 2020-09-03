package com.omar.myapps.askpermission;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    Button openConvertToPdfActivity, givePermBtn;
    private static final int One_PERMISSION_RC = 1;


    private static final int Multi_PERMISSION_RC = 2;
    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
    };

    View permissionDeniedInclude;

    void init() {
        openConvertToPdfActivity = findViewById(R.id.openConvertToPdfActivity);
        permissionDeniedInclude = findViewById(R.id.permissionDeniedInclude);
        givePermBtn = findViewById(R.id.givePermBtn);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

/**         this for asking one permission
 *       if (!isPermissionGranted()) {
 permissionDeniedInclude.setVisibility(View.VISIBLE);
 openConvertToPdfActivity.setVisibility(View.GONE);
 } else {
 openConvertToPdfActivity.setVisibility(View.VISIBLE);
 permissionDeniedInclude.setVisibility(View.GONE);
 }
 **/

        if (!hasPermissions(this, PERMISSIONS)) {

            openConvertToPdfActivity.setVisibility(View.GONE);
            permissionDeniedInclude.setVisibility(View.VISIBLE);
        } else {

            openConvertToPdfActivity.setVisibility(View.VISIBLE);
            permissionDeniedInclude.setVisibility(View.GONE);
        }


        openConvertToPdfActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   Intent intent = new Intent(MainActivity.this, ProcessingActivity.class);
                //    startActivity(intent);
                //   finish();
            }
        });

        givePermBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*** this for one permission
                 * ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, One_PERMISSION_RC);
                 ***/

                if (!hasPermissions(MainActivity.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, Multi_PERMISSION_RC);
                }

            }
        });


        findViewById(R.id.exitAppBtn).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                finishAndRemoveTask();
            }
        });
    }

    public boolean isPermissionGranted() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                //   Log.v("TAG", "Permission is revoked");
                //      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 6);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case One_PERMISSION_RC:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("TAGe", "Permission: " + permissions[0] + "was " + grantResults[0]);

                    permissionDeniedInclude.setVisibility(View.GONE);
                    openConvertToPdfActivity.setVisibility(View.VISIBLE);

                    //resume tasks needing this permission
                }// here show dialog
                break;

            case Multi_PERMISSION_RC:

                if (hasPermissions(getApplicationContext(), permissions)) {
                    openConvertToPdfActivity.setVisibility(View.VISIBLE);
                    permissionDeniedInclude.setVisibility(View.GONE);
                }

        }
    }


    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}

