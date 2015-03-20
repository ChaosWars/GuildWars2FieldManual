package com.zendeka.guildwars2fieldmanual;

import com.google.android.gms.maps.model.UrlTileProvider;
import com.zendeka.gw2apiandroid.gw2api.Continent;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lawrence on 9/1/14.
 */
public class GuildWars2UrlTileProvider extends UrlTileProvider {
    private static final String FORMAT;
    private Continent mContinent;
    private int mFloor;

    static {
        //continentId, floor, x, y, zoom
        FORMAT = "https://tiles.guildwars2.com/%d/%d/%d/%d/%d.jpg";
    }

    public GuildWars2UrlTileProvider(int width, int height) {
        super(width, height);
    }

    public void setContinent(Continent continent) {
        mContinent = continent;
    }

    public Continent getContinent() {
        return mContinent;
    }

    public void setFloor(int floor) {
        if (mContinent == null) {
            return;
        }

//        if (Arrays.asList(mContinent.getFloors()).contains(floor)) {
//            mFloor = floor;
//        }

        mFloor = floor;
    }

    @Override
    public URL getTileUrl(int x, int y, int z) {
        if (mContinent == null) {
            return null;
        }

        try {
            return new URL(String.format(FORMAT, mContinent.getIdentifier(), mFloor, z, x, y));
        }
        catch (MalformedURLException e) {
            return null;
        }
    }
}
