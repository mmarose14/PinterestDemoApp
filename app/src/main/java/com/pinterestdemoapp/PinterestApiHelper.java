package com.pinterestdemoapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class PinterestApiHelper {
    private static final String TAG = PinterestMainActivity.TAG;

    public static JSONObject getPins(String username) {

        JSONObject jobj = null;
        String json = "";

        URL url;
        try {
            String urlString = String.format("https://api.pinterest.com/v3/pidgets/users/%s/pins/", username);
            url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try {
                InputStream in = new BufferedInputStream(conn.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");

                    }
                    in.close();
                    json = sb.toString();
                    try {
                        jobj = new JSONObject(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return jobj;

    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        ImageView _imageView;
        String _source;
        ImageCallBack _callBack;

        public DownloadImageTask(ImageView imageView, String source, ImageCallBack callBack) {
            _imageView = imageView;
            _source = source;
            _callBack = callBack;

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            URL url;
            Bitmap bitmap = null;
            try {
                url = new URL(_source);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);
                options.inSampleSize = calculateInSampleSize(options, 500, 500);

                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);

            } catch (Exception ignored) {
                Log.e(TAG, "Exception", ignored);
            }

            return bitmap;
        }

        private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            _callBack.onTaskFinished(bitmap);
        }
    }

    public interface ImageCallBack {
        public void onTaskFinished(final Bitmap bmp);
    }
}
