package com.example.android1.facetracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.IOException;

public class StaticFaceTracker extends Activity {
    private static final int TAKE_PICTURE_CODE = 100;
    private static final int MAX_FACES = 5;
    String TAG = "MainActivity";
    private Bitmap cameraBitmap = null;
    private ProgressDialog progress;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_face_tracker);
        progress = new ProgressDialog(this);
        progress.setMessage("Loading Image");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();



        Intent intent = getIntent();
        String uri = intent.getStringExtra("uri");
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(uri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(true)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Face> faces = detector.detect(frame);


        Log.d(TAG, faces.size() + " size");

        if (!detector.isOperational()) {
            Toast.makeText(this, "Face detector dependencies are not yet available.", Toast.LENGTH_SHORT).show();
        } else {
            StaticCustomView overlay = (StaticCustomView) findViewById(R.id.customview);
            overlay.setContent(bitmap, faces);
            progress.dismiss();
        }

        detector.release();

    }
}