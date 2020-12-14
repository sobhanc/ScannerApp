package com.example.thescannerapp;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    //implement ad
    private InterstitialAd mInterstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Code for image labeling starts here
        //Create variables for the home activity
        Button choose;
        TextView result;
        ImageView imageView;
        //instantiation of interstitialAd and load
        mInterstitial=new InterstitialAd(this);
        mInterstitial.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        AdRequest request= new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mInterstitial.loadAd(request);

        //binding of views
        result = findViewById(R.id.textView);
        imageView = findViewById(R.id.imagePost);
        //button to Tag the picture
        choose = findViewById(R.id.button5);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //loading the ad
                if(mInterstitial.isLoaded()){
                    mInterstitial.show();
                }
                //the intent will show new image according to the
                //image the user chooses
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"select images"), 121);

            }
        });

        //this is the bottom navigation binding
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //this is the navigation for the home activity
        bottomNavigationView.setSelectedItemId(R.id.home);
        //upon selection the switch will be activated
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.home:
                        //no action is needed for home
                        return true;

                    case R.id.tutorial:
                        //new intent is activated to the tutorial
                        startActivity(new Intent(getApplicationContext(),Tutorial.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.aboutus:
                        //new intent is created for the about us
                        startActivity(new Intent(getApplicationContext(),AboutUs.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }
    //This is an onClick method
    //activates when the user wants to pick an image to
    //convert to PDF
    public void pickImage(View v) {
        Intent myIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(myIntent, 120);
    }
    //This is an onClick method that activates upon
    //the user request to
    //open the Camera
    public void openCamera(View v) {
        int REQUEST_CODE = 99;
        int preference = ScanConstants.OPEN_CAMERA;
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        startActivityForResult(intent, REQUEST_CODE);
    }
    //This is an onClick Method that will open
    //The gallery upon the user pressing the button
    public void openGallery(View v) {
        int REQUEST_CODE = 99;
        int preference = ScanConstants.OPEN_MEDIA;
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //This is creation and binding of the
        //imageview to show the image selected
        final ImageView imageView;
        imageView = findViewById(R.id.imagePost);
        //This is creation and binding of the
        //share button
        Button btnShare;
        btnShare = findViewById(R.id.button3);
        //this textview shows the
        //tags that are associated with the image
        final TextView result;
        result = findViewById(R.id.textView);
        //this will clear the tags in the textview
        Button btnClearTxt;
        btnClearTxt = findViewById(R.id.clearText);
        Button btnConvert;
        btnConvert = findViewById(R.id.button4);


        if (requestCode == 99 && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                //getContentResolver().delete(uri, null, null);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //This will let you share the scanned image you took
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageView.getDrawable());
                Bitmap bitmap = bitmapDrawable.getBitmap();

                Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/jpeg");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(shareIntent,"Share Image"));

            }
        });

        //code for image labeling
        if (requestCode == 121) {
            imageView.setImageURI(data.getData());

            FirebaseVisionImage image;
            try {
                image = FirebaseVisionImage.fromFilePath(getApplicationContext(), data.getData());
                FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                        .getOnDeviceImageLabeler();

                labeler.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                            @Override
                            public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                                // Task completed successfully
                                // ...
                                for (FirebaseVisionImageLabel label: labels) {
                                    String text = label.getText();
                                    String entityId = label.getEntityId();
                                    float confidence = label.getConfidence();
                                    result.setMovementMethod(new ScrollingMovementMethod());
                                    result.append(text+"   "+"\n");
                                }
                            }



                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                // ...
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //convert to pdf
        if (requestCode == 120 && resultCode == RESULT_OK && data!=null) {
            Uri selectedImageUri = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImageUri, filePath, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePath[0]);
            String myPath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bitmap = BitmapFactory.decodeFile(myPath);

            imageView.setImageBitmap(bitmap);

            PdfDocument pdfDocument = new PdfDocument();
            PdfDocument.PageInfo pi = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(),1).create();

            PdfDocument.Page page = pdfDocument.startPage(pi);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#FFFFFF"));
            canvas.drawPaint(paint);

            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(),true);
            paint.setColor(Color.BLUE);
            canvas.drawBitmap(bitmap,0,0,null);

            pdfDocument.finishPage(page);

            //save bitmap img
            File root = new File(Environment.getExternalStorageDirectory(), "PDF FOLDER");
            if (!root.exists()) {
                root.mkdir();
            }
            File file = new File(root,"picture.pdf");

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                pdfDocument.writeTo(fileOutputStream);
            } catch(IOException e) {
                e.printStackTrace();
            }

            pdfDocument.close();

        }

        //code for clearing textView text
        btnClearTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setText("");
            }
        });

    }//ends onActivityResult













}