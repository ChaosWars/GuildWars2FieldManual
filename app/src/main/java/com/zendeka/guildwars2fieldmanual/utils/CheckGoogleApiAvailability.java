package com.zendeka.guildwars2fieldmanual.utils;

import android.app.Activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by lawrence on 3/20/15.
 */
public class CheckGoogleApiAvailability {
    public static int checkGooglePlayServices(Activity activity, int requestCode, String tag) {
        // Check status of Google Play Services
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int status = apiAvailability.isGooglePlayServicesAvailable(activity);

        // Check Google Play Service Available
        if (status != ConnectionResult.SUCCESS) {
            apiAvailability.getErrorDialog(activity, status, requestCode).show();
        }

        return status;
    }
}
