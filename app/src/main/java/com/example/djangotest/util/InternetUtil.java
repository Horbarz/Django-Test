package com.example.djangotest.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class InternetUtil {

    public static boolean myInternetCOnnection(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if(mWifi.isConnected()){
            return true;

        }return false;
    }

    public static boolean isOnline(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnectedOrConnecting()){
            return true;
        }else
            {return false;
            }
    }

    public static boolean isInternetOnline(Context context){
        if(InternetUtil.isOnline(context)){
            return true;
        }else{
            Toast.makeText(context, "Check Internet", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
