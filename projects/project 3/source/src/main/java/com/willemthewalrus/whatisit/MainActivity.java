package com.willemthewalrus.whatisit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonFactory;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.Json;

import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private String mCurrentPhotoPath;
    private final int CAN_REQUEST = 6969;
    private final int REQUEST_IMAGE_CAPTURE = 1313;
    private final String APIkey = "AIzaSyBC9ExL_zo1LKXYWSZxccj4mRaIJ7qJpQE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private File createImageFile() throws IOException {
        //code taken from https://developer.android.com/training/camera/photobasics.html
        //for this method

        // Create an image file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        //code taken from https://developer.android.com/training/camera/photobasics.html
        //for this method

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.i("file","Error occurred while creating the File");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.willemthewalrus.devimgexample.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAN_REQUEST);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //make sure we are responding to the correct activity
        if(requestCode ==  CAN_REQUEST && requestCode == RESULT_OK){





        }
    }

    public static byte[] fileToByteArray(String filepath){
        File workingfile = new File(filepath);

        //make sure the photo that we took exists in storage
        if(workingfile.exists()) {

            //set our bitmap equal to the file in storage
            Bitmap ourmap = BitmapFactory.decodeFile(workingfile.getAbsolutePath());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ourmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] ar = byteArrayOutputStream.toByteArray();
            return ar;
        }
        else {
            return null;
        }
    }

    /**
     * Code for this method was taken from the following URL:
     *    https://developer.android.com/things/training/doorbell/cloud-vision.html
     *
     * @param path (String)
     * @return response (BatchAnnotateImagesResponse)
     *
     **/
    public BatchAnnotateImagesResponse annotateIMG(String path) throws IOException{

        //make sure there is a photo at our filepath
        if(fileToByteArray(path) != null) {

            //encode a byte array into our image object
            Image inputIMG = new Image();
            inputIMG.encodeContent(fileToByteArray(path));

            //create a image request with our stored file
            AnnotateImageRequest imageRequest = new AnnotateImageRequest();
            imageRequest.setImage(inputIMG);

            Feature label = new Feature();
            label.setType("LABEL_DETECTION");
            imageRequest.setFeatures(Collections.singletonList(label));

            VisionRequestInitializer initializer = new VisionRequestInitializer(APIkey);
            Vision myvision = new Vision.Builder(
                    new NetHttpTransport(),
                    new AndroidJsonFactory(),
                    null).setVisionRequestInitializer(initializer).build();

            BatchAnnotateImagesRequest requestBatch = new BatchAnnotateImagesRequest();
            requestBatch.setRequests(Collections.singletonList(imageRequest));
            BatchAnnotateImagesResponse response = myvision.images()
                    .annotate(requestBatch)
                    .setDisableGZipContent(true)
                    .execute();

            return response;
        }
        else{
            Toast.makeText(this,"issue with pathtobytearray call", Toast.LENGTH_LONG);
            return null;
        }


    }


}
