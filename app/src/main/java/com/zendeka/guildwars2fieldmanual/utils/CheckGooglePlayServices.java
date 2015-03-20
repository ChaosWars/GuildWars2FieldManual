package com.zendeka.guildwars2fieldmanual.utils;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by lawrence on 3/20/15.
 */
public class CheckGooglePlayServices {
    public static int checkGooglePlayServicesAvailability(Activity activity, int requestCode, String tag) {
        // Check status of Google Play Services
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);

        // Check Google Play Service Available
        try {
            if (status != ConnectionResult.SUCCESS) {
                GooglePlayServicesUtil.getErrorDialog(status, activity, requestCode).show();
            }
        } catch (Exception e) {
            Log.e(tag, "Error: GooglePlayServiceUtil: ", e);
        }

        return status;
    }
}
