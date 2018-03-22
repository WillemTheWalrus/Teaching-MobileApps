package com.willemthewalrus.whatisit;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private String mCurrentPhotoPath;
    private final String APIkey = keyclass.key;
    private final int REQUEST_IMAGE_CAPTURE = 1313;
    private final int PICK_IMAGE=6969;
    String responseout;

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

    public void selectPic(View view){
        dispatchPickPictureIntent();
    }

    public void noClick(View view){
        setContentView(R.layout.apifailure);
    }

    public void confirmClick(View view){

        String inIMG;
        EditText IMGobject = findViewById(R.id.editText);
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
            finalText = findViewById(R.id.FinalText);
            finalText.setText(String.format("I thought %s was in your picture. There was a %.3f " +
                    "chance that was in your picture",inIMG,secondaryscore));
        }
        else{
            finalText = findViewById(R.id.FinalText);
            finalText.setText(String.format("Dang! I had no idea that was in your picture, here is what I thought was in it:" +
                    "\n %s " , responseout));
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

    private void dispatchPickPictureIntent(){

        //code taken from stack overflow


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("result", "made it in");

        //make sure we are responding to the correct activity

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.i("inner", "made it in");


            new getResponse().execute(mCurrentPhotoPath, APIkey);



        }
        else if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){

            //code taken form http://codetheory.in/android-pick-select-image-from-gallery-with-intents/

           Uri uri = data.getData();

            try{
                InputStream in = getContentResolver().openInputStream(uri);
                Bitmap map = BitmapFactory.decodeStream(in);
                saveBitmapToTemp(map);
                new getResponse().execute(mCurrentPhotoPath,                                                                                           APIkey);

            }catch(IOException e){
                Toast.makeText(this, "file creation failure", Toast.LENGTH_LONG);
            }


            



        }

    }

    private String saveBitmapToTemp(Bitmap bitmap) throws IOException{
        //code taken from https://developer.android.com/training/camera/photobasics.html
        //for this method

        // Create an image file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_"+"temp"+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i("filepath", mCurrentPhotoPath);

        try{
            FileOutputStream fos = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.PNG,30,fos);
            fos.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return mCurrentPhotoPath;

    }


    public static byte[] fileToByteArray(String filepath) {
        File workingfile = new File(filepath);

        //make sure the photo that we took exists in storage
        if (workingfile.exists()) {

            //set our bitmap equal to the file in storage
            Bitmap ourmap = BitmapFactory.decodeFile(workingfile.getAbsolutePath());

            //compress our bitmap into a smaller


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ourmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
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

            txt = findViewById(R.id.textView2);
            txt.setText("Please wait while we analyze the image");

            //display picture that is being analyzed
            img = findViewById(R.id.analyzedIMG);
            File pic = new File(mCurrentPhotoPath);

            Picasso.with(getApplicationContext()).load(pic).into(img);
            Log.i("context", getApplicationContext().toString());

            yesButton = findViewById(R.id.yes);
            noButton = findViewById(R.id.no);

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
            responseout ="";
            List<AnnotateImageResponse> imgresponses = result.getResponses();
            List<EntityAnnotation> annotations = imgresponses.get(0).getLabelAnnotations();
            String[] descriptions = new String[annotations.size()];
            Float[] scores = new Float[annotations.size()];
            for (int i = 0; i < annotations.size(); i++) {
                descriptions[i] = annotations.get(i).getDescription();
                scores[i] = annotations.get(i).getScore();
                responseout += "Description: " + annotations.get(i).getDescription() +
                        " Score: " + annotations.get(i).getScore() + "\n";
            }

            Log.i("display", responseout);
            txt.setText("Does your picture contain a(n) " + descriptions[0]);
            noButton.setEnabled(true);
            yesButton.setEnabled(true);

            responses = annotations;


        }
    }
}
