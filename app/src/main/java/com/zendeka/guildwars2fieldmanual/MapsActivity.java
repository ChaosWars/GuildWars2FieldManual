package com.zendeka.guildwars2fieldmanual;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.JsonReader;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.zendeka.gw2apiandroid.gw2api.Continent;
import com.zendeka.gw2apiandroid.gw2api.parsers.ContinentsParser;
import com.zendeka.gw2apiandroid.gw2api.tasks.ContinentsRequestTask;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class MapsActivity extends FragmentActivity {
    private static final String CONTINENTS_URL = "https://api.guildwars2.com/v1/continents.json";

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private List<Continent> mContinents;
    private ContinentsRequestTask mContinentsRequestTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        fetchContinentsIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        fetchContinentsIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        TileOverlayOptions opts = new TileOverlayOptions();
        opts.tileProvider(new GuildWars2UrlTileProvider(256, 256));
        TileOverlay overlay = mMap.addTileOverlay(opts);
    }

    private void fetchContinentsIfNeeded() {
        if (mContinents == null &&
            mContinentsRequestTask == null) {
            fetchContinents();
        }
    }

    private void fetchContinents() {
        mContinentsRequestTask = new ContinentsRequestTask(new ContinentsRequestTask.OnContinentsRequestTaskCompleted() {
            @Override
            public void continentsRequestTaskResult(String s) {
                StringReader stringReader = new StringReader(s);
                JsonReader jsonReader = new JsonReader(stringReader);
                ContinentsParser parser = new ContinentsParser();

                try {
                    mContinents = parser.readContinents(jsonReader);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mContinentsRequestTask.execute(CONTINENTS_URL);
    }
}
