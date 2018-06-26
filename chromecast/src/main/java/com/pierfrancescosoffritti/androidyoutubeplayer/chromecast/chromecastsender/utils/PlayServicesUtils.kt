package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.utils

import android.app.Activity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

/**
 * We can't use cast functionalities if GooglePlay Services aren't available, this utility class is used to check if GooglePlay Services are available and up to date.
 */
object PlayServicesUtils {
    @JvmStatic fun checkGooglePlayServicesAvailability(activity: Activity, googlePlayServicesAvailabilityRequestCode: Int, onSuccess: () -> Unit) {
        val googlePlayServicesAvailabilityResult = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity)
        if(googlePlayServicesAvailabilityResult == ConnectionResult.SUCCESS)
            onSuccess()
        else
            GoogleApiAvailability.getInstance().getErrorDialog(activity, googlePlayServicesAvailabilityResult, googlePlayServicesAvailabilityRequestCode, null).show()
    }
}