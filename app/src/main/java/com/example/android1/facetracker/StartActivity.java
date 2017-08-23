package com.example.android1.facetracker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    final public static int PERMISSION_ALL = 1;
    private static final int SELECT_PICTURE = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CAMERA
    };
    String TAG = "Start";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        if (!hasPermissions(StartActivity.this, PERMISSIONS)) {
            Log.d(TAG, "permission");
            ActivityCompat.requestPermissions(StartActivity.this, PERMISSIONS, PERMISSION_ALL);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    public boolean hasPermissions(Context context, String... permissions) {


        if (android.os.Build.VERSION.SDK_INT >= 21 && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.livecamera:
                Toast.makeText(getApplicationContext(), "live camera", Toast.LENGTH_LONG).show();
                Intent in = new Intent(StartActivity.this, DynamicFaceTracker.class);
                startActivity(in);
                break;
            case R.id.chooseimagefromgallary:
                Toast.makeText(getApplicationContext(), "choose image from gallary", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

                break;
            case R.id.photoviewer:
                Toast.makeText(getApplicationContext(), "photoviewer", Toast.LENGTH_LONG).show();
                Intent viewerintent = new Intent(StartActivity.this, PhotoViewer.class);
                startActivity(viewerintent);
                break;

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                ///selectedImagePath = getPath(selectedImageUri);
                Toast.makeText(getApplicationContext(), selectedImageUri.toString(), Toast.LENGTH_SHORT).show();
                Intent selectintent = new Intent(StartActivity.this, StaticFaceTracker.class);
                selectintent.putExtra("uri", selectedImageUri.toString());
                startActivity(selectintent);

            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


}
