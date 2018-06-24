package com.pierfrancescosoffritti.aytplayersample.examples.chromecastExample.utils;

import android.app.Activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.pierfrancescosoffritti.youtubeplayer.utils.Callable;

public class PlayServicesUtils {
    static public void checkGooglePlayServicesAvailability(Activity activity, int googlePlayServicesAvailabilityRequestCode, Callable onSuccess) {
        int googlePlayServicesAvailabilityResult = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);
        if(googlePlayServicesAvailabilityResult == ConnectionResult.SUCCESS)
            onSuccess.call();
        else
            GoogleApiAvailability.getInstance().getErrorDialog(activity, googlePlayServicesAvailabilityResult, googlePlayServicesAvailabilityRequestCode, null).show();
    }
}
