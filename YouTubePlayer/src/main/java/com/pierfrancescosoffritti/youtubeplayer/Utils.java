package com.pierfrancescosoffritti.youtubeplayer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

    public static String formatTime(float sec) {
        int minutes = (int) (sec / 60);
        int seconds = (int) (sec % 60);
        return String.format("%d:%02d", minutes, seconds);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
