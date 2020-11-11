package com.example.thescannerapp;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class HomeActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void openCamera(View v) {
        int REQUEST_CODE = 99;
        int preference = ScanConstants.OPEN_CAMERA;
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        startActivityForResult(intent, REQUEST_CODE);
    }

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

        final ImageView imageView;
        imageView = findViewById(R.id.imagePost);
        Button btnShare;
        btnShare = findViewById(R.id.button3);



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

    }//ends onActivityResult







}