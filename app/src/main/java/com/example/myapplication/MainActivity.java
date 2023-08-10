 package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

 public class MainActivity extends AppCompatActivity {
    private TextView button;
    private ImageView image;
    private static final int CAMERA_PERMISSION_CODE =101;
    private static final int CAMERA_INTENT_RESULT =201;
    private RelativeLayout parent;
    private static final int SETTING_INTENT_CODE=301;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        image = findViewById(R.id.image);
        parent=findViewById(R.id.parent);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                handlePermission();
            }
        });

    }

     private void handlePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            openCamera();
        }else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
                showSnackbar();
            }else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            }
        }
     }

     private void showSnackbar() {
         Snackbar.make(parent,"This Apllication Need Permission from your camera",Snackbar.LENGTH_INDEFINITE)
                 .setAction("Grant Permission", new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         Intent intent= new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                         intent.setData(Uri.parse("package" + getPackageName()));
                         startActivityForResult(intent,SETTING_INTENT_CODE);
                     }
                 }).show();
     }

     private void openCamera() {
        Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,CAMERA_INTENT_RESULT);
     }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         switch (requestCode){
             case  CAMERA_INTENT_RESULT:

                 if (requestCode == RESULT_OK && data != null){
                     Bundle bundle = data.getExtras();
                     if (bundle != null){
                         Bitmap bitmap =(Bitmap) bundle.get("data");
                         image.setImageBitmap(bitmap);
                     }
                 }

                 break;

             case SETTING_INTENT_CODE:
                 handlePermission();
                 break;
             default:
                 break;
         }
     }

     @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults);

         switch (requestCode){
             case CAMERA_PERMISSION_CODE:
                 if (grantResults.length > 0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED) {
                     openCamera();

                 }else {
                     Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                 }
                 break;
             default:
                 break;
         }
     }
 }