package com.willemthewalrus.whatisit;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;

import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private String mCurrentPhotoPath;
    private final String APIkey = keyclass.key;
    private final int REQUEST_IMAGE_CAPTURE = 1313;

    public List<EntityAnnotation> responses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        
    }

    private File createImageFile() throws IOException {
        //taken from android developer 'taking photos simply' page
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

    public void onClick(View view) {
        dispatchTakePictureIntent();
    }

    public void noClick(View view){
        setContentView(R.layout.apifailure);
    }

    public void confirmClick(View view){

        String inIMG;
        EditText IMGobject = (EditText) findViewById(R.id.editText);
        TextView finalText;
        inIMG = IMGobject.getText().toString();
        float secondaryscore =0;
        boolean inResponses = false;
        setContentView(R.layout.apifailureresult);

        for(int i = 0; i < responses.size(); i++){
            Log.i("description", responses.get(i).getDescription());
            Log.i("userinput", inIMG);
            if (inIMG.equals(responses.get(i).getDescription())){
                inResponses = true;
                secondaryscore = responses.get(i).getScore();
                break;
            }
        }

        if(inResponses){
            finalText = (TextView)findViewById(R.id.FinalText);
            finalText.setText(String.format("I thought %s was in your picture. There was a %.3f " +
                    "chance that was in your picture",inIMG,secondaryscore));
        }
        else{
            finalText = (TextView)findViewById(R.id.FinalText);
            finalText.setText("Dang! I had no idea that was in your picture");
        }

    }

    public void restartactivity(View view){
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void yesClick(View view){
        setContentView(R.layout.apisuccess);
    }

    private void dispatchTakePictureIntent() {

        //taken from
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.i("error", "IOexception");

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.willemthewalrus.whatisit.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("result", "made it in");

        //make sure we are responding to the correct activity

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.i("inner", "made it in");


            new getResponse().execute(mCurrentPhotoPath, APIkey);
            TextView textView = (TextView) findViewById(R.id.textView);


        }

    }



    public static byte[] fileToByteArray(String filepath) {
        File workingfile = new File(filepath);

        //make sure the photo that we took exists in storage
        if (workingfile.exists()) {

            //set our bitmap equal to the file in storage
            Bitmap ourmap = BitmapFactory.decodeFile(workingfile.getAbsolutePath());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ourmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] ar = byteArrayOutputStream.toByteArray();
            return ar;
        } else {
            return null;
        }
    }


    class getResponse extends AsyncTask<String, String, BatchAnnotateImagesResponse> {

        private String path;
        private String key;
        TextView txt;
        ImageView img;
        Button yesButton;
        Button noButton;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setContentView(R.layout.scoreguess);

            txt = (TextView)findViewById(R.id.textView2);
            txt.setText("Please wait while we analyze the image");

            //display picture that is being analyzed
            img = (ImageView)findViewById(R.id.analyzedIMG);
            File pic = new File(mCurrentPhotoPath);

            Picasso.with(getApplicationContext()).load(pic).into(img);
            Log.i("context", getApplicationContext().toString());

            yesButton = (Button)findViewById(R.id.yes);
            noButton = (Button)findViewById(R.id.no);

            yesButton.setEnabled(false);
            noButton.setEnabled(false);



        }

        @Override
        protected BatchAnnotateImagesResponse doInBackground(String... args) {

            path = args[0];
            key = args[1];
            if (fileToByteArray(path) != null) {

                //encode a byte array into our image object
                Image inputIMG = new Image();
                inputIMG.encodeContent(fileToByteArray(path));

                //create a image request with our stored file
                AnnotateImageRequest imageRequest = new AnnotateImageRequest();
                imageRequest.setImage(inputIMG);

                //use the label detection feature of the cloud vision api
                Feature label = new Feature();
                label.setType("LABEL_DETECTION");
                label.setMaxResults(10);
                imageRequest.setFeatures(Collections.singletonList(label));

                VisionRequestInitializer initializer = new VisionRequestInitializer(key);
                Vision myvision = new Vision.Builder(
                        new NetHttpTransport(),
                        new AndroidJsonFactory(),
                        null).setVisionRequestInitializer(initializer).build();

                BatchAnnotateImagesRequest requestBatch = new BatchAnnotateImagesRequest();

                try {
                    requestBatch.setRequests(Collections.singletonList(imageRequest));
                    BatchAnnotateImagesResponse response = myvision.images()
                            .annotate(requestBatch)
                            .setDisableGZipContent(true)
                            .execute();
                    return response;

                } catch (IOException e) {
                    Log.i("vision", "IOexception in getResponse");
                    return null;
                }


            } else {
                Log.i("vision", "null filepath");
                return null;
            }
        }

        @Override
        protected void onPostExecute(BatchAnnotateImagesResponse result) {
            super.onPostExecute(result);
            String responseout = "";
            List<AnnotateImageResponse> imgresponses = result.getResponses();
            List<EntityAnnotation> annotations = imgresponses.get(0).getLabelAnnotations();
            String[] descriptions = new String[annotations.size()];
            Float[] scores = new Float[annotations.size()];
            for (int i = 0; i < annotations.size(); i++) {
                descriptions[i] = annotations.get(i).getDescription();
                scores[i] = annotations.get(i).getScore();
                responseout += annotations.get(i).toString() + "\n";
            }

            Log.i("display", responseout);
            txt.setText("Does your picture containt a " + descriptions[0]);
            noButton.setEnabled(true);
            yesButton.setEnabled(true);

            responses = annotations;


        }
    }
}
