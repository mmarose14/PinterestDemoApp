package com.pinterestdemoapp;

import android.app.Application;
import android.graphics.Bitmap;

import java.util.HashMap;

public class PinterestApplication extends Application {
    private HashMap<String, Bitmap> mCache = new HashMap<String, Bitmap>(20);

    public Bitmap getFromCache(String key) {
        return mCache.get(key);
    }

    public void putToCache(String key, Bitmap bmp) {
        if (getFromCache(key) == null) {
            mCache.put(key, bmp);
        }
    }


}
