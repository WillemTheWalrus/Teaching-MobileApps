package com.example.wmd42.picturepractice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button btnpic;
    ImageView imgPicTaken;
    private static final int CAN_REQUEST = 1313;
    private static final int GAL_REQUEST = 1133;
    Bitmap defaultbitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnpic = (Button) findViewById(R.id.button);
        imgPicTaken = (ImageView)findViewById(R.id.image);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAN_REQUEST){
            defaultbitmap = (Bitmap) data.getExtras().get("data");
            if(defaultbitmap != null) {
                imgPicTaken.setImageBitmap(defaultbitmap);
            }
            else{
                Toast.makeText(this, "bitmap fetch error", Toast.LENGTH_SHORT);
            }
        }

        if(requestCode == GAL_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri uri = data.getData();

            try{
                defaultbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imgPicTaken.setImageBitmap(defaultbitmap);

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

}
