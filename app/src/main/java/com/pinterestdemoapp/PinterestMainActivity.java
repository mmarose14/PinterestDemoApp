package com.pinterestdemoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;


public class PinterestMainActivity extends Activity {

    public static final String TAG = "Pinterest Main Activity";
    private static final String BUNDLE_PINS_DATA = "BUNDLE_PINS_DATA";

    private SearchView userSearchBar;
    private GridView pinsGridView;
    private PinterestPinsData pinterestPinsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pinterest_main);

        userSearchBar = (SearchView) findViewById(R.id.searchView);
        userSearchBar.setSubmitButtonEnabled(true);

        userSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                GetPinsTask task = new GetPinsTask();
                task.execute(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        pinsGridView = (GridView) findViewById(R.id.pins_grid);

        if (savedInstanceState != null && savedInstanceState.getSerializable(BUNDLE_PINS_DATA) != null) {
            pinterestPinsData = (PinterestPinsData) savedInstanceState.getSerializable(BUNDLE_PINS_DATA);
            buildGridView(pinterestPinsData);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private class GetPinsTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String[] params) {
            return PinterestApiHelper.getPins(params[0]);
        }

        @Override
        protected void onPostExecute(JSONObject response) {

            if (response != null
                    && "success".equalsIgnoreCase(response.optString("status"))) {

                Gson gson = new Gson();
                PinterestPinsData data = gson.fromJson(response.toString(), PinterestPinsData.class);
                buildGridView(data);
                pinterestPinsData = data;
            } else {
                Toast.makeText(PinterestMainActivity.this, "Error retrieving pins", Toast.LENGTH_LONG).show();
            }


        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_PINS_DATA, pinterestPinsData);
    }

    private void buildGridView(PinterestPinsData response) {
        final List<PinterestPinsData.PinterestPins> pins = response.getData().getPins();
        ImageAdapter imageAdapter = new ImageAdapter(this, pins);
        pinsGridView.setAdapter(imageAdapter);
        pinsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PinterestPinsData.ImageDetails imageDetails = pins.get(position).getImages().getDetails();
                String desc = pins.get(position).getDescription();
                Intent intent = new Intent(PinterestMainActivity.this, ImageViewerActivity.class);
                intent.putExtra(ImageViewerActivity.IMAGE_DATA, imageDetails);
                intent.putExtra(ImageViewerActivity.IMAGE_DESC, desc);
                startActivity(intent);
            }
        });
    }
}
