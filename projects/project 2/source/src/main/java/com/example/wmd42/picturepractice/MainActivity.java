package com.example.wmd42.picturepractice;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public Button btnpic;
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

    private static final int CAN_REQUEST = 1313;
    private static final int GAL_REQUEST = 1133;
    private static Bitmap defaultbitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnpic = (Button) findViewById(R.id.savePhoto);
        imgPicTaken = (ImageView)findViewById(R.id.image);
        noRedButton = (ImageButton)findViewById(R.id.noRed);
        noBlueButton = (ImageButton)findViewById(R.id.noBlue);
        noGreenButton = (ImageButton)findViewById(R.id.noGreen);
        negateRedButton = (ImageButton)findViewById(R.id.negateRed);
        negateGreenButton = (ImageButton)findViewById(R.id.negateGreen);
        negateBlueButton = (ImageButton)findViewById(R.id.negateBlue);
        greyScale = (ImageButton) findViewById(R.id.greyScale);
        highContrast= (ImageButton)findViewById(R.id.highContrast);
        addNoise = (ImageButton)findViewById(R.id.addNoise);

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
                setContentView(R.layout.selectphoto);
                ImageView iv = (ImageView) findViewById(R.id.viewer);
                Bitmap bitmap = ((BitmapDrawable)butt.getDrawable()).getBitmap();
                iv.setImageBitmap(bitmap);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAN_REQUEST){
            defaultbitmap = (Bitmap) data.getExtras().get("data");


            if(defaultbitmap != null) {
                imgPicTaken.setImageBitmap(defaultbitmap);
                noRedButton.setImageBitmap(scaleColor(1,0,1,1));
                noGreenButton.setImageBitmap(scaleColor(1,1,0,1));
                noBlueButton.setImageBitmap(scaleColor(1,1,1,0));
                negateRedButton.setImageBitmap(scaleColor(1,-1,1,1));
                negateGreenButton.setImageBitmap(scaleColor(1,1,-1,1));
                negateBlueButton.setImageBitmap(scaleColor(1,1,1,-1));
                greyScale.setImageBitmap(greyScale());
                highContrast.setImageBitmap(contrast());
                addNoise.setImageBitmap(noise());
            }
            else{
                Toast.makeText(this, "bitmap fetch error", Toast.LENGTH_SHORT);
            }
        }

        if(requestCode == GAL_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri uri = data.getData();

            try{
                defaultbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                defaultbitmap =  Bitmap.createScaledBitmap(defaultbitmap, 200,200,false);


                imgPicTaken.setImageBitmap(defaultbitmap);
                noRedButton.setImageBitmap(scaleColor(1,0,1,1));
                noGreenButton.setImageBitmap(scaleColor(1,1,0,1));
                noBlueButton.setImageBitmap(scaleColor(1,1,1,0));
                negateRedButton.setImageBitmap(scaleColor(1,-1,1,1));
                negateGreenButton.setImageBitmap(scaleColor(1,1,-1,1));
                negateBlueButton.setImageBitmap(scaleColor(1,1,1,-1));
                greyScale.setImageBitmap(greyScale());
                highContrast.setImageBitmap(contrast());
                addNoise.setImageBitmap(noise());
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void onTakePicClick(View view) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, CAN_REQUEST);

            }
        }

    public void onChoosePicClick(View view){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GAL_REQUEST);

        }





    public static Bitmap scaleColor(int alphaScale, int redScale, int greenScale, int blueScale){

            Bitmap rmmap = Bitmap.createBitmap(defaultbitmap.getWidth(), defaultbitmap.getHeight(),
                        defaultbitmap.getConfig());

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
            ;
            return rmmap;
        }

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

    public static Bitmap noise(){
        Bitmap rmmap = Bitmap.createBitmap(defaultbitmap.getWidth(), defaultbitmap.getHeight(),
                defaultbitmap.getConfig());
        Random random = new Random();
        int noiser = random.nextInt(11) + (int)Math.pow(-1, random.nextInt(2));
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


    public void saveToStorage(View view){


        ImageView v = (ImageView)findViewById(R.id.viewer);
        Bitmap map = ((BitmapDrawable)v.getDrawable()).getBitmap();

        File root = Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath()+"/DCIM/Camera/img.jpg");
        try
        {
            file.createNewFile();
            FileOutputStream ostream = new FileOutputStream(file);
            map.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
            ostream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));


        Toast.makeText(MainActivity.this, file.getAbsolutePath(), Toast.LENGTH_LONG).show();

        setContentView(R.layout.activity_main);

    }


}

