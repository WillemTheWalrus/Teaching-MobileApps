package com.willemthewalrus.whatisit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fasterxml.jackson.core.JsonFactory;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.Json;

import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private final String APIkey = "AIzaSyBC9ExL_zo1LKXYWSZxccj4mRaIJ7qJpQE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    VisionRequestInitializer initializer = new VisionRequestInitializer(APIkey);

    Vision vision = new Vision.Builder(
            new NetHttpTransport(),
            new AndroidJsonFactory(),
            null).setVisionRequestInitializer(initializer).build();

     








}
