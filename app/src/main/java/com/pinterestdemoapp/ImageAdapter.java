package com.pinterestdemoapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    private List<PinterestPinsData.PinterestPins> pins;
    private PinterestApplication app;

    public ImageAdapter(Context context, List<PinterestPinsData.PinterestPins> pins) {
        this.context = context;
        this.pins = pins;
        this.app = (PinterestApplication) context.getApplicationContext();
    }

    @Override
    public int getCount() {
        return pins.size();
    }

    @Override
    public Object getItem(int position) {
        return this.pins.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT,
                    500));
            imageView.setScaleType(ImageView.ScaleType.CENTER);
        } else {
            imageView = (ImageView) convertView;
        }

        String url = "";

        PinterestPinsData.PinterestPins pinterestPins = (PinterestPinsData.PinterestPins) getItem(position);
        url = pinterestPins.getImages().getDetails().getUrl();

        setDisplayImage(imageView, url);

        return imageView;
    }

    private void setDisplayImage(final ImageView iv, final String url) {
        if (app.getFromCache(url) != null) {
            iv.setImageBitmap(app.getFromCache(url));
        } else {
            new PinterestApiHelper.DownloadImageTask(iv, url, new PinterestApiHelper.ImageCallBack() {

                @Override
                public void onTaskFinished(Bitmap bmp) {
                    iv.setImageBitmap(bmp);
                    notifyDataSetChanged();
                    app.putToCache(url, bmp);
                }
            }).execute();
        }
    }


}
