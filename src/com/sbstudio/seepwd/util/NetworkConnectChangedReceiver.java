package com.sbstudio.seepwd.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;

import com.sbstudio.seepwd.MainActivity;
import com.sbstudio.seepwd.entity.Network;

public class NetworkConnectChangedReceiver extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        //①这个监听wifi的打开与关闭，与wifi的切换无关
//        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
//            //5个WIFI状态:WIFI_STATE_DISABLED 1,WIFI_STATE_DISABLING 0,WIFI_STATE_ENABLED 3,WIFI_STATE_ENABLING 2,WIFI_STATE_UNKNOWN 4
//            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
//            Log.e("H3c", "wifiState" + wifiState);
//            switch (wifiState) {
//                case WifiManager.WIFI_STATE_DISABLED:
//                    break;
//                case WifiManager.WIFI_STATE_DISABLING:
//                    break;
//            //
//            }
//        }
        
        String connectingSsid="";
        
        // ②这个监听wifi的连接状态即是否连上了一个有效无线路由，当①广播的状态是WifiManager.WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
        // 在①广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，当然刚打开wifi肯定还没有连接到有效的无线网络
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            
            
            Parcelable parcelableExtra = intent
                    .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
//                boolean isConnected=networkInfo.isAvailable();
                boolean isConnected=networkInfo.getState()==State.CONNECTED;
                Log.e("H3c", "isConnected:" + isConnected);
                if (isConnected) {
                    
                   WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                  WifiInfo info = wifiMgr.getConnectionInfo();
                  connectingSsid = info != null ? info.getSSID().replace("\"", "") : null;
                                        Log.e("SSID:", connectingSsid);
                                        
                } 
                if(context instanceof MainActivity &&MainActivity.dataList.size()>5)
                {
                    MainActivity act=(MainActivity) context;
                    act.getNetworkAdapter().setConnectingSsid(connectingSsid);
                    
                    for (Network network : MainActivity.dataList) {
                        if(connectingSsid.equals(network.getSsid()))
                        {
                            //先把正在连接的删掉，再加到List合适的位置
                            MainActivity.dataList.remove(network);
                            int perfectPos=4;
                            MainActivity.dataList.add(perfectPos, network);
                            break;
                        }
                    }
                    
                    
                    if(null!=act.getNetworkAdapter())
                        act.getNetworkAdapter().notifyDataSetChanged();
                }
            }
        }
        
        // ③这个监听网络连接的设置，包括wifi和移动数据的打开和关闭。.
        // 最好用的还是这个监听。wifi如果打开，关闭，以及连接上可用的连接都会接到监听。见log
        // 这个广播的最大弊端是比上边两个广播的反应要慢，如果只是要监听wifi，我觉得还是用上边两个配合比较合适
//        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
//            ConnectivityManager manager = (ConnectivityManager) context
//                    .getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo gprs = manager
//                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//            NetworkInfo wifi = manager
//                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//            Log.i("TAG", "网络状态改变:" + wifi.isConnected() + " 3g:" + gprs.isConnected());
//            NetworkInfo info = intent
//                    .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
//            if (info != null) {
//                Log.e("H3c", "info.getTypeName()" + info.getTypeName());
//                Log.e("H3c", "getSubtypeName()" + info.getSubtypeName());
//                Log.e("H3c", "getState()" + info.getState());
//                Log.e("H3c", "getDetailedState()"
//                        + info.getDetailedState().name());
//                Log.e("H3c", "getDetailedState()" + info.getExtraInfo());
//                Log.e("H3c", "getType()" + info.getType());
//
//                if (NetworkInfo.State.CONNECTED == info.getState()) {
//                } else if (info.getType() == 1) {
//                    if (NetworkInfo.State.DISCONNECTING == info.getState()) {
//
//                    }
//                }
//            }
//        }
    }
}
