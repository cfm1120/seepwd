package com.sbstudio.seepwd.util.http;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkDetector {
    
    public static boolean detect(Activity act) {
      
       ConnectivityManager manager = (ConnectivityManager) act
              .getApplicationContext().getSystemService(
                     Context.CONNECTIVITY_SERVICE);
      
       if (manager == null) {
           return false;
       }
      
       NetworkInfo networkinfo = manager.getActiveNetworkInfo();
      
       if (networkinfo == null || !networkinfo.isAvailable()) {
           return false;
       }
 
       return true;
    }
}