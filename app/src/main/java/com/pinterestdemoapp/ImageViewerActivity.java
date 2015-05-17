package com.pinterestdemoapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageViewerActivity extends Activity {
    public static final String IMAGE_DATA = "IMAGE_URL";
    public static final String IMAGE_DESC = "IMAGE_DESC";

    private ImageView imageView;
    private PinterestPinsData.ImageDetails imageDetails;
    private String description;
    private TextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pinterest_image_details);

        PinterestApplication app = (PinterestApplication) getApplication();

        imageView = (ImageView) findViewById(R.id.image);
        descriptionTextView = (TextView) findViewById(R.id.description);


        if (getIntent() != null) {
            imageDetails = (PinterestPinsData.ImageDetails) getIntent().getExtras().get(IMAGE_DATA);

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            float scaleWidth = metrics.scaledDensity;
            float scaleHeight = metrics.scaledDensity;

            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);

            Bitmap imageFromCache = app.getFromCache(imageDetails.getUrl());

            Bitmap bitmap = Bitmap.createBitmap(imageFromCache, 0, 0, imageDetails.getWidth(), imageDetails.getHeight(), matrix, true);
            imageView.setImageBitmap(bitmap);


            description = getIntent().getStringExtra(IMAGE_DESC);
            descriptionTextView.setText(description);

        }


    }
}
