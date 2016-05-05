package com.zendeka.guildwars2fieldmanual;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.zendeka.guildwars2fieldmanual.adapters.ContinentsAdapter;
import com.zendeka.gw2apiandroid.gw2api.Continent;
import com.zendeka.gw2apiandroid.gw2api.parsers.ContinentsParser;
import com.zendeka.gw2apiandroid.gw2api.tasks.ContinentsRequestTask;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class MapsActivity
        extends ActionBarActivity
        implements AdapterView.OnItemSelectedListener, GoogleMap.OnCameraChangeListener
{
    private static final String CONTINENTS_URL = "https://api.guildwars2.com/v2/continents.json?ids=all";
//    private static final String TAG = "MAPS_ACTIVITY";
//    private static final int GOOGLE_PLAY_SERVICES_REQUEST_CODE = 1;

    private GuildWars2UrlTileProvider mUrlTileProvider;
    private TileOverlay mTileOverlay;
    private Toolbar mToolbar;
    private Spinner mContinentsSpinner;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private List<Continent> mContinents;
    private ContinentsRequestTask mContinentsRequestTask;
    private ContinentsAdapter mContinentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mUrlTileProvider = new GuildWars2UrlTileProvider(256, 256);
        setupContinentsAdapter();
        setupToolbar();
        setupContinentsSpinner();
        setupActionBar();
        setUpMapIfNeeded();
        fetchContinentsIfNeeded();
//        CheckGooglePlayServices.checkGooglePlayServicesAvailability(this, GOOGLE_PLAY_SERVICES_REQUEST_CODE, TAG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        fetchContinentsIfNeeded();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (mContinentsAdapter != null) {
            selectContinent(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (mUrlTileProvider == null || mMap == null) {
            return;
        }

        Continent continent = mUrlTileProvider.getContinent();

        if (continent == null) {
            return;
        }

        if (cameraPosition.zoom < continent.getMinZoom()) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(continent.getMinZoom()));
        } else if (cameraPosition.zoom > continent.getMaxZoom()) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(continent.getMaxZoom()));
        }
    }

    private void selectContinent(int position) {
        Continent continent = mContinentsAdapter.getItem(position);
        boolean setContinent = mUrlTileProvider.getContinent() != continent;

        if (!setContinent) {
            return;
        }

        mUrlTileProvider.setContinent(continent);
        mUrlTileProvider.setFloor(1);
        mTileOverlay.clearTileCache();

        if (mMap != null) {
//            mMap.clear();
//            setUpMap();
            mMap.animateCamera(CameraUpdateFactory.zoomTo(continent.getMinZoom()));
        }
    }

    private void setupToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.maps_toolbar);
        }

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    private void setupContinentsAdapter() {
        if (mContinentsAdapter == null) {
            mContinentsAdapter = new ContinentsAdapter(this, R.layout.item_continent);

            if (mContinents != null) {
                mContinentsAdapter.addAll(mContinents);
            }
        }
    }

    private void setupContinentsSpinner() {
        if (mContinentsSpinner == null) {
            mContinentsSpinner = (Spinner) findViewById(R.id.continents_spinner);
        }

        if (mContinentsSpinner != null) {
            mContinentsSpinner.setOnItemSelectedListener(this);

            if (mContinentsAdapter != null) {
                mContinentsSpinner.setAdapter(mContinentsAdapter);
            }
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link MapFragment} (and
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
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setOnCameraChangeListener(this);
        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        TileOverlayOptions opts = new TileOverlayOptions();
        opts.tileProvider(mUrlTileProvider);
        mTileOverlay = mMap.addTileOverlay(opts);
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

                    if (mContinentsAdapter != null) {
                        mContinentsAdapter.clear();
                        mContinentsAdapter.addAll(mContinents);

                        if (mContinents.size() > 0) {
                            selectContinent(0);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        jsonReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mContinentsRequestTask.execute(CONTINENTS_URL);
    }
}
