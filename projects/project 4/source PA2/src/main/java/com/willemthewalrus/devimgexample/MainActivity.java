package com.willemthewalrus.devimgexample;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    String mCurrentPhotoPath;
    String currentTempPath;
    public Button btnpic;
    ImageView Viewer;
    ImageView imgPicTaken;
    ImageButton noRedButton;
    ImageButton noGreenButton;
    ImageButton noBlueButton;
    ImageButton negateRedButton;
    ImageButton negateGreenButton;
    ImageButton negateBlueButton;
    ImageButton greyScale;
    ImageButton highContrast;
    ImageButton addNoise;

    Bitmap noRedmap;
    Bitmap noGreenmap;
    Bitmap noBluemap;
    Bitmap negateRedmap;
    Bitmap negateGreenmap;
    Bitmap negateBluemap;
    Bitmap contrastmap;
    Bitmap greyscalemap;
    Bitmap noisemap;

    public int noRedID;
    public int noGreenID;
    public int noBlueID;
    public int negateGreenID;
    public int negateBlueID;
    public int negateRedID;
    public int contrastID;
    public int greyscaleID;
    public int noiseID;



    private static final int CAN_REQUEST = 1313;
    private static final int GAL_REQUEST = 1133;
    private static Bitmap defaultbitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Viewer = findViewById(R.id.viewer);
        btnpic =  findViewById(R.id.savePhoto);
        imgPicTaken = findViewById(R.id.image);
        noRedButton = findViewById(R.id.noRed);
        noBlueButton = findViewById(R.id.noBlue);
        noGreenButton = findViewById(R.id.noGreen);
        negateRedButton = findViewById(R.id.negateRed);
        negateGreenButton = findViewById(R.id.negateGreen);
        negateBlueButton = findViewById(R.id.negateBlue);
        greyScale =  findViewById(R.id.greyScale);
        highContrast= findViewById(R.id.highContrast);
        addNoise = findViewById(R.id.addNoise);

        addListenerOnButton(noRedButton);
        addListenerOnButton(noBlueButton);
        addListenerOnButton(noGreenButton);
        addListenerOnButton(negateBlueButton);
        addListenerOnButton(negateGreenButton);
        addListenerOnButton(negateRedButton);
        addListenerOnButton(greyScale);
        addListenerOnButton(highContrast);
        addListenerOnButton(addNoise);
    }

    public void addListenerOnButton(final ImageButton butt){

        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int ID = butt.getId();
                setContentView(R.layout.selectphoto);
                ImageView iv = findViewById(R.id.viewer);
                Bitmap bitmap = ((BitmapDrawable)butt.getDrawable()).getBitmap();
                defaultbitmap = bitmap;
                updateFileimg(bitmap, mCurrentPhotoPath);
                iv.setImageBitmap(bitmap);

                if(ID == noGreenID){
                    updateFileimg(noGreenmap, mCurrentPhotoPath);
                }
                else if(ID == noRedID){
                    updateFileimg(noRedmap, mCurrentPhotoPath);
                }
                else if(ID == noBlueID){
                    updateFileimg(noBluemap, mCurrentPhotoPath);
                }
                else if(ID == negateBlueID){
                    updateFileimg(negateBluemap, mCurrentPhotoPath);
                }
                else if(ID == negateGreenID){
                    updateFileimg(negateGreenmap, mCurrentPhotoPath);
                }
                else if(ID == negateRedID){
                    updateFileimg(negateRedmap, mCurrentPhotoPath);
                }
                else if(ID == contrastID){
                    updateFileimg(contrastmap, mCurrentPhotoPath);
                }
                else if(ID == noiseID){
                    updateFileimg(noisemap, mCurrentPhotoPath);
                }
                else if(ID == greyscaleID){
                    updateFileimg(greyscalemap, mCurrentPhotoPath);
                }
            }
        });

    }
    @Override

    /*
     * This method determines what is done after either a photo is taken or a photo
     * is selected from the gallery.
     *
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {

        if(requestCode == CAN_REQUEST) {

            super.onActivityResult(requestCode, resultCode, data);

            //will branch here if the result code is from the camera
            if(resultCode == RESULT_OK ) {
                try {
                    //set the default bitmap equal to that of the photo stored in our photofile
                    defaultbitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);

                    //reduce the size of the photo by 10 in order to increase performance,
                    //otherwise the UI takes too long to load
                    setPhotos(10);

                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            else{
                Toast.makeText(this, "bitmap fetch error", Toast.LENGTH_SHORT).show();
            }
        }

        //will branch here if the result code is from the gallery and there is data
        //within the intent
        if(requestCode == GAL_REQUEST  && data != null && data.getData() != null){
            Uri uri = data.getData();

            try{
                //set our bitmap equal to the data returned within our intent
                defaultbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                mCurrentPhotoPath = saveBitmapToTemp(defaultbitmap);

                setPhotos(10);


            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    /*
     * Returns nothing, applies filters to bitmaps, scales photos down to load
     * within a reasonable time, and loads images into their corresponding
     * ImageViews
     */
    public void setPhotos(int scaler) throws IOException{

        //scale and set image buttons
        defaultbitmap = Bitmap.createScaledBitmap(defaultbitmap, defaultbitmap.getWidth()/scaler, defaultbitmap.getHeight()/scaler,false);

        //load pictures into their imageViews using picasso and apply photo effects
        File pictaken = new File(mCurrentPhotoPath);
        Picasso.with(getApplicationContext()).load(pictaken).rotate(90).into(imgPicTaken);

        noGreenmap = scaleColor(1, 1, 0, 1);
        File noGreen = new File(saveBitmapToTemp(noGreenmap));
        Picasso.with(getApplicationContext()).load(noGreen).rotate(90).into(noGreenButton);

        noRedmap = scaleColor(1, 0, 1, 1);
        File noRed = new File(saveBitmapToTemp(noRedmap));
        Picasso.with(getApplicationContext()).load(noRed).rotate(90).into(noRedButton);

        noBluemap = scaleColor(1, 1, 1, 0);
        File noBlue = new File(saveBitmapToTemp(noBluemap));
        Picasso.with(getApplicationContext()).load(noBlue).rotate(90).into(noBlueButton);

        negateRedmap = scaleColor(1, 1, 1, 0);
        File negateRed = new File(saveBitmapToTemp(negateRedmap));
        Picasso.with(getApplicationContext()).load(negateRed).rotate(90).into(negateRedButton);

        negateBluemap = scaleColor(1, 1, 1, -1);
        File negateBlue = new File(saveBitmapToTemp(negateBluemap));
        Picasso.with(getApplicationContext()).load(negateBlue).rotate(90).into(negateBlueButton);

        negateGreenmap = scaleColor(1, 1, -1, 1);
        File negateGreen = new File(saveBitmapToTemp(negateGreenmap));
        Picasso.with(getApplicationContext()).load(negateGreen).rotate(90).into(negateGreenButton);

        greyscalemap = greyScale();
        File grey = new File(saveBitmapToTemp(greyScale()));
        Picasso.with(getApplicationContext()).load(grey).rotate(90).into(greyScale);

        contrastmap = contrast();
        File contrast = new File(saveBitmapToTemp(contrastmap));
        Picasso.with(getApplicationContext()).load(contrast).rotate(90).into(highContrast);

        noisemap = noise();
        File noise = new File(saveBitmapToTemp(noisemap));
        Picasso.with(getApplicationContext()).load(noise).rotate(90).into(addNoise);

        //these ID integers are used in the onClick listener to determine which button is pressed
        noBlueID = noBlueButton.getId();
        noRedID = noRedButton.getId();
        noGreenID = noGreenButton.getId();
        negateBlueID = negateBlueButton.getId();
        negateGreenID = negateGreenButton.getId();
        negateRedID = negateRedButton.getId();
        contrastID = highContrast.getId();
        noiseID = addNoise.getId();
        greyscaleID = greyScale.getId();

    }


    /*
     * Launches dispatchTakePictureIntent on click
     */
    public void onTakePicClick(View view) {

        dispatchTakePictureIntent();
    }


    /*
     * Creates and sends off an Intent to select a photo from gallery
     */
    public void onChoosePicClick(View view) throws IOException{
        //create a new intent to retreive a photo from the gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        //store this gallery photo within a URI to be returned by the intent
        File imgfile = createImageFile();
        Uri photoURI = FileProvider.getUriForFile(this,
                "com.willemthewalrus.devimgexample.fileprovider",
                imgfile);
        intent.setData(photoURI);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GAL_REQUEST);



    }

    /*
     * Creates a file with a unique name to store our images in
     */
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
        Log.i("filepath", mCurrentPhotoPath);
        return image;
    }

    /*
     * Save bitmap to a temporary file so that we can load photos into ImageViews more easily
     * using Picasso. This is done because Picasso will not load a bitmap on its own into a view,
     * but it can be done with a file
     */
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
        currentTempPath = image.getAbsolutePath();
        Log.i("filepath", currentTempPath);

        try{
            FileOutputStream fos = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
            fos.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return currentTempPath;

    }


    /*
     * send off an Intent to take a photo with the devices camera, which will then store the taken
     * photo within the designated file path created using the createImageFile method
     */
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

    /*
     * will return a new bitmap that has scaled RGB values based off of the parameters
     * ex. if you were to call scalerColor(1,1,0,1) , it would return a version of
     * defaultbitmap without any green
     */
    public static Bitmap scaleColor( int alphaScale, int redScale, int greenScale, int blueScale){

        Bitmap rmmap = defaultbitmap.copy(Bitmap.Config.ARGB_8888, true);

        for(int x = 0; x < rmmap.getWidth(); x++){
            for(int y = 0; y < rmmap.getHeight(); y++){

                int pixel = defaultbitmap.getPixel(x,y);
                int Alpha = Color.alpha(pixel);
                int Red = Color.red(pixel);
                int Green = Color.green(pixel);
                int Blue = Color.blue(pixel);

                rmmap.setPixel(x,y,Color.argb(Alpha *alphaScale, Red *redScale,
                        Green*greenScale, Blue*blueScale));

            }
        }

        return rmmap;
    }

    /*
     * returns a greyscale bitmap
     */
    public static Bitmap greyScale(){Bitmap rmmap = Bitmap.createBitmap(defaultbitmap.getWidth(), defaultbitmap.getHeight(),
            defaultbitmap.getConfig());

        for(int x = 0; x < rmmap.getWidth(); x++){
            for(int y = 0; y < rmmap.getHeight(); y++){

                int pixel = defaultbitmap.getPixel(x,y);
                int Alpha = Color.alpha(pixel);
                int Red = Color.red(pixel);
                int Green = Color.green(pixel);
                int Blue = Color.blue(pixel);
                Red = Green = Blue = (int)(0.299*Red + 0.587*Green + 0.144*Blue);
                rmmap.setPixel(x,y,Color.argb(Alpha, Red,
                        Green, Blue));

            }
        }

        return rmmap;
    }

    /*
     * returns a highcontrast bitmap
     */
    public static Bitmap contrast(){
        Bitmap rmmap = Bitmap.createBitmap(defaultbitmap.getWidth(), defaultbitmap.getHeight(),
                defaultbitmap.getConfig());
        int contrast = 4;
        for(int x = 0; x < rmmap.getWidth(); x++){
            for(int y = 0; y < rmmap.getHeight(); y++){

                int pixel = defaultbitmap.getPixel(x,y);
                int Alpha = Color.alpha(pixel);

                int Red = Color.red(pixel);
                Red = (int)(((((Red / 255.0) - 0.5) * contrast) + 0.5) * 255.0);

                int Green = Color.green(pixel);
                Green = (int)(((((Green / 255.0) - 0.5) * contrast) + 0.5) * 255.0);

                int Blue = Color.blue(pixel);
                Blue = (int)(((((Blue/ 255.0) - 0.5) * contrast) + 0.5) * 255.0);

                rmmap.setPixel(x,y,Color.argb(Alpha, Red,
                        Green, Blue));

            }
        }

        return rmmap;
    }

    /*
     * returns a bitmap with randomly added noise
     */
    public static Bitmap noise(){
        Bitmap rmmap = Bitmap.createBitmap(defaultbitmap.getWidth(), defaultbitmap.getHeight(),
                defaultbitmap.getConfig());
        Random random = new Random();
        int noiser = random.nextInt(60) + (int)Math.pow(-1, random.nextInt(2));
        for(int x = 0; x < rmmap.getWidth(); x++){
            for(int y = 0; y < rmmap.getHeight(); y++){

                int pixel = defaultbitmap.getPixel(x,y);
                int Alpha = Color.alpha(pixel);

                int Red = Color.red(pixel) + noiser;

                int Green = Color.green(pixel) + noiser;

                int Blue = Color.blue(pixel) + noiser;

                rmmap.setPixel(x,y,Color.argb(Alpha, Red,
                        Green, Blue));

            }
        }

        return rmmap;
    }


    /*
     * updates the designated filepath with the inputted bitmap
     */
    public void updateFileimg(Bitmap bitmap, String path){

        File img = new File(path);
        FileOutputStream fos = null;

        try{
            fos = new FileOutputStream(img);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

        } catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                fos.flush();
                fos.close();

            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    /*
     * Saves the photo to the gallery with a unique name by broadcasting an intent
     */
    public void saveToGallery() throws IOException{


        File alteredImg = new File(mCurrentPhotoPath);

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Log.i("datetime",currentDateTimeString);
        MediaStore.Images.Media.insertImage(getContentResolver(),bitmap, currentDateTimeString,"Filtered Photo");

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(alteredImg));
        sendBroadcast(intent);

    }

    /*
     * saves the photo and restarts the application
     */
    public void saveOnClick(View view) throws IOException{

        saveToGallery();

        //restart activity
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        finish();
        startActivity(intent);


    }

}




