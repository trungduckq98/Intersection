package com.deathmarch.intersection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
@SuppressWarnings("deprecation")
public class CheckNetwork {

    public static boolean check(Context context){
        boolean haveWIFI = false;
        boolean haveMOBILE = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
        for (NetworkInfo ni : networkInfos){
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveWIFI=true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveMOBILE=true;
        }
        return haveWIFI || haveMOBILE;
    }
}