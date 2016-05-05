package com.zendeka.guildwars2fieldmanual;

import android.util.Log;

import com.google.android.gms.maps.model.UrlTileProvider;
import com.zendeka.gw2apiandroid.gw2api.Continent;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lawrence on 9/1/14.
 */
public class GuildWars2UrlTileProvider extends UrlTileProvider {
    private static final String TAG = "GW2_URL_TILE_PROVIDER";
    private static final String FORMAT;
    private Continent mContinent;
    private Integer mFloor;

    static {
        //continentId, floor, zoom, x, y
        FORMAT = "https://tiles.guildwars2.com/%d/%d/%d/%d/%d.jpg";
    }

    public GuildWars2UrlTileProvider(int width, int height) {
        super(width, height);
    }

    public void setContinent(Continent continent) {
        if (continent != mContinent) {
            mContinent = continent;
        }
    }

    public Continent getContinent() {
        return mContinent;
    }

    public void setFloor(Integer floor) {
        if (mContinent == null) {
            return;
        }

        if (!mContinent.getFloors().contains(floor)) {
            Log.d(TAG, String.format("Trying to set invalid floor %d for continent %s", floor, mContinent.getName()));
            return;
        }

        mFloor = floor;
    }

    @Override
    public URL getTileUrl(int x, int y, int z) {
        if (mContinent == null) {
            return null;
        }

        if (z < mContinent.getMinZoom() || z > mContinent.getMaxZoom()) {
            Log.d(TAG, String.format("Invalid zoom level requested: %d. Min: %d, Max: %d", z, mContinent.getMinZoom(), mContinent.getMaxZoom()));
            return null;
        }

        try {
            return new URL(String.format(FORMAT, mContinent.getIdentifier(), mFloor, z, x, y));
        }
        catch (MalformedURLException e) {
            Log.e(TAG, "Error building Tile URL", e);
            return null;
        }
    }
}
