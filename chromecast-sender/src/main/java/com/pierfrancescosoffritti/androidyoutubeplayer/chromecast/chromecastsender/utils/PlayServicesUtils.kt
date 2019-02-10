package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.utils

import android.app.Activity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

object PlayServicesUtils {
    /**
     * It's not safe to use Google Cast functionalities if GooglePlay Services aren't available,
     * this utility function is used to check if GooglePlay Services are available and up to date.
     *
     * The answer will be available in onActivityResult.
     */
    @JvmStatic fun checkGooglePlayServicesAvailability(activity: Activity, googlePlayServicesAvailabilityRequestCode: Int, onSuccess: Runnable) {
        val googlePlayServicesAvailabilityResult = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity)
        if(googlePlayServicesAvailabilityResult == ConnectionResult.SUCCESS)
            onSuccess.run()
        else
            GoogleApiAvailability.getInstance().getErrorDialog(activity, googlePlayServicesAvailabilityResult, googlePlayServicesAvailabilityRequestCode, null).show()
    }
}