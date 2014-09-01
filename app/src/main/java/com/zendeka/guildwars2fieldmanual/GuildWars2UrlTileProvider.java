package com.zendeka.guildwars2fieldmanual;

import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lawrence on 9/1/14.
 */
public class GuildWars2UrlTileProvider extends UrlTileProvider {
    private static final String FORMAT;

    static {
        FORMAT = "https://tiles.guildwars2.com/1/1/%d/%d/%d.jpg";
    }

    public GuildWars2UrlTileProvider(int width, int height) {
        super(width, height);
    }

    @Override
    public URL getTileUrl(int x, int y, int z) {
        try {
            return new URL(String.format(FORMAT, z, x, y));
        }
        catch (MalformedURLException e) {
            return null;
        }
    }
}
