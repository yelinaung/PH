package com.yelinaung.ph.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Ye Lin Aung on 14/06/14.
 */
public class NetUtils {

  public static boolean isOnline(Context c) {
    NetworkInfo netInfo = null;
    try {
      ConnectivityManager cm =
          (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
      netInfo = cm.getActiveNetworkInfo();
    } catch (SecurityException e) {
      e.printStackTrace();
    }
    return netInfo != null && netInfo.isConnectedOrConnecting();
  }
}
