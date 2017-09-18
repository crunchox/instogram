package com.example.albert.exstogram;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class upload extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_PICK = 2;
    ImageView ivUpload;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ivUpload=(ImageView)findViewById(R.id.ivUpload);
    }
    public void btnCamera_Clicked(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
            }else{
                GetCamera();
            }
        }else {
            GetCamera();
        }

    }
    public void btnGallery_Clicked(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_IMAGE_PICK);
            }else{
                GetGallery();
            }
        }else {
            GetGallery();
        }
    }

    private void GetGallery() {
        Intent getGalleryIntent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(getGalleryIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(getGalleryIntent,REQUEST_IMAGE_PICK);
        }

    }

    private void GetCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //mengharapkan return
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQUEST_IMAGE_CAPTURE){
            if (resultCode==RESULT_OK&&data!=null){
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ivUpload.setImageBitmap(imageBitmap);
                bitmap=imageBitmap;
            }else{
                Toast.makeText(this,"JAHAT LU!!",Toast.LENGTH_SHORT).show();
            }




        }else if(requestCode==REQUEST_IMAGE_PICK){
            if (resultCode==RESULT_OK&&data!=null){
                Uri selectedImage=data.getData();
                try{
                    Bitmap imageBitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                    ivUpload.setImageBitmap(imageBitmap);
                    bitmap=imageBitmap;

                }catch(IOException e){
                    e.printStackTrace();
                    Toast.makeText(this,"ERROR - "+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this,"JAHAT LU!!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void btnUpload_Clicked(View view){
        if (bitmap==null){
            Toast.makeText(this,"Ambil gambar dulu bos",Toast.LENGTH_SHORT).show();
            return;
        }
        //bitmap compress ke file
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        ParseFile file = new ParseFile("image.png", byteArray);

        ParseObject obj=new ParseObject("Gambar");
        obj.put("username", ParseUser.getCurrentUser().getUsername());
        obj.put("image",file);

        obj.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    Toast.makeText(upload.this,"Upload DONE!!",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(upload.this,"Upload FAIL!! "+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_IMAGE_CAPTURE){
            if(grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                GetCamera();
            }
        }else if(requestCode==REQUEST_IMAGE_PICK){
            if(grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                GetCamera();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
