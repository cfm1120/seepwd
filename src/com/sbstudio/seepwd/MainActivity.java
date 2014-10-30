
package com.sbstudio.seepwd;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobads.IconsAd;
import com.baidu.mobads.InterstitialAd;
import com.baidu.mobads.InterstitialAdListener;
import com.sbstudio.seepwd.entity.Network;
import com.sbstudio.seepwd.util.NetworkAdapter;
import com.sbstudio.seepwd.util.NetworkConnectChangedReceiver;
import com.sbstudio.seepwd.util.Parser;
import com.sbstudio.seepwd.util.RootCmd;
import com.sbstudio.seepwd.util.http.AutoUpdate;
import com.sbstudio.seepwd.util.http.Config;
import com.sbstudio.seepwd.util.http.NetworkDetector;
import com.sbstudio.seepwd.util.http.WeiXinUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author water3
 *
 */
public class MainActivity extends Activity {
    final String cmd="cat /data/misc/wifi/wpa_supplicant.conf";
    final int READ_COMPLELTE=1;
    String out = null;
    TextView tv;
    private ListView lv;
    private SearchView searchView;
    private NetworkAdapter networkAdapter;
    Dialog alertDialog;
    
    private long mExitTime;
    AutoUpdate autoUpdate;
    Activity act;
    
    //ad
    InterstitialAd interAd;
    IconsAd iconsAd;
    
    ActionBar actionBar;
    WeiXinUtils wx;
    

    //当前连接的wifi
//    public String connectingSsid;
    //广播接收
    private NetworkConnectChangedReceiver receiver;
    
   
    //是否分享了
    boolean isShared;
    //是否捐赠了
//    boolean isDonated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        SharedPreferences settings=getSharedPreferences(Config.PREFS_NAME, MODE_PRIVATE);
        isShared=settings.getBoolean("isShared", false);
        
        IntentFilter filter = new IntentFilter();
//      filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//      filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver=new NetworkConnectChangedReceiver();
        registerReceiver(receiver, filter);
        
        
        wx=WeiXinUtils.getInstance();
        wx.init(MainActivity.this);
        
        actionBar=getActionBar();
        actionBar.show();
        //广告
        ad(Config.WHEN_OPEN);
        
        act=this;
        autoUpdate = new AutoUpdate(this);
        
        tv=(TextView) findViewById(R.id.tv);
        lv=(ListView) findViewById(R.id.networkLv);
        dataList=new ArrayList<Network>();
        

        
        //获取root并得到wifi列表
        boolean flag;
        //首先取得root权限
//        flag=RootCmd.haveRoot();
//        flag=RootCmd.getRootAhth();
        flag=RootCmd.isRootSystem();
        //然后读取/data/misc/wifi/wpa_supplicant.conf文件
        if(flag){
//        new Thread(){
//            @SuppressWarnings("deprecation")
//            public void run()
//            {
                try {
                    StringBuffer sb=new StringBuffer();
                    DataInputStream dis=RootCmd.execRootCmd(cmd);
                    if(dis.read()==-1){
                        tv.setVisibility(View.VISIBLE);
                        tv.setText(R.string.mustauth);
                        return;
                    }
                    String temp=null;
                    while((temp=dis.readLine())!=null)
                        sb.append(temp);
                    
                    out=sb.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                myHandler.sendEmptyMessage(READ_COMPLELTE);
                if(out==null)
                {
                   tv.setVisibility(View.VISIBLE);//break;
                }
               dataList=Parser.getNetworks(out);
               networkAdapter=new NetworkAdapter(this,dataList);
               lv.setAdapter(networkAdapter);
               //自动更新
               if(NetworkDetector.detect(act))
               {
                   autoUpdate.Start();
               }
            } 
//        }.run();
        else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(R.string.mustroot);
        }
        
       /* lv.setOnScrollListener(new OnScrollListener() {
            
            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onScroll(AbsListView view, int first, int visibleItemCount, int totalItemCount) {
//                Log.e("visible item count", visibleItemCount+"");
                if(!lastSsid.equals(connectingSsid))
                for (Network network : dataList) {
                  if(connectingSsid.equals(network.getSsid()))
                  {
                      //先把正在连接的删掉，再加到List合适的位置
                      dataList.remove(network);
                      int perfectPos=(int) (visibleItemCount*0.618);
//                      int perfectPos=4;
                      dataList.add(perfectPos, network);
                      networkAdapter.notifyDataSetChanged();
                      lastSsid=connectingSsid;
                      break;
                  }
              }
                
            }
        });*/
        
    }


    /**
     * 显示广告方法
     * @param when
     */
    private void ad(int when) {
        //全局广告显示与否标示，该标示为false是我debug模式，下面广告就不显示了
        if(!Config.AD)return;
        
        //如果已捐赠，所有广告不显示了
//        if(isDonated)return;
        
        
        //这里判断的都是分享级的一般用户
        switch (when) {
            case Config.WHEN_OPEN:
              //ad start
                iconsAd=new IconsAd(this);
                iconsAd.loadAd(this);
                
                //如果已分享，插屏广告不显示了
                if(isShared)break;
                interAd=new InterstitialAd(this);
                interAd.setListener(new InterstitialAdListener(){

                    @Override
                    public void onAdClick(InterstitialAd arg0) {
                        Log.i("InterstitialAd","onAdClick");
                    }

                    @Override
                    public void onAdDismissed() {
                        Log.i("InterstitialAd","onAdDismissed");
                        interAd.loadAd();
                    }

                    @Override
                    public void onAdFailed(String arg0) {
                        Log.i("InterstitialAd","onAdFailed");
                    }

                    @Override
                    public void onAdPresent() {
                        Log.i("InterstitialAd","onAdPresent");
                    }

                    @Override
                    public void onAdReady() {
                        Log.i("InterstitialAd","onAdReady");
                    }
                    
                });
                interAd.loadAd();
                //ad end
                break;

            case Config.WHEN_CLICK_BACK:
                //如果分享了,就不显示插屏广告
                if(isShared)
                {
                    Toast.makeText(this, R.string.click1, Toast.LENGTH_SHORT).show();
                    break;
                }
                
                if(interAd.isAdReady()){
                    Toast.makeText(this, R.string.adexit, Toast.LENGTH_SHORT).show();
                    interAd.showAd(MainActivity.this);
                }else{
                    Toast.makeText(this, R.string.click1, Toast.LENGTH_SHORT).show();
                    interAd.loadAd();
                }
                
                break;
            case Config.WHEN_RESUME:
                if(iconsAd!=null)
                    iconsAd.loadAd(this);
                //如果已经分享，插屏广告不显示了
                if(isShared) break;
                    
                if(interAd.isAdReady()){
                    Toast.makeText(this, R.string.ad, Toast.LENGTH_SHORT).show();
                    interAd.showAd(MainActivity.this);
                }else{
                    interAd.loadAd();
                }
                break;
        }
        
        
    }
    
    /**
     * 菜单显示
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        //wx图标已改为xml配置
//        MenuItem add=menu.add(0,0,0,R.string.recommend).setIcon(R.drawable.wx);
//        add.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setQueryHint(getResources().getString(R.string.queryHints));
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            
            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public boolean onQueryTextChange(String newText) {
                //监听输入框的文字变化...
//                Toast.makeText(getApplicationContext(), newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        searchView.setOnCloseListener(new OnCloseListener() {
            
            @Override
            public boolean onClose() {
                Toast.makeText(getApplicationContext(), "closed~", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        
        return true;
    }
    /***
     * 菜单响应事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                if(alertDialog==null)
                {
                    String msg=getString(R.string.about)+"\nVersion:"+AutoUpdate.softVersion;
                    alertDialog = new AlertDialog.Builder(this). 
                            setTitle(R.string.action_about). 
                            setMessage(msg). 
                            create(); 
                }
                alertDialog.show(); 
                break;
            case R.id.menu_wx:
                //分享到朋友圈
                wx.sendToWeiXin(1);
                break;
            default:
                break;
        }
        
        
        return super.onOptionsItemSelected(item);
    }


    public static List<Network> dataList;
       
    
    /**
     * 按两次返回退出
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                        mExitTime = System.currentTimeMillis();
                        
                       ad(Config.WHEN_CLICK_BACK);

                } else {
                        finish();
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
}


    @Override
    protected void onResume() {
        super.onResume();
        ad(Config.WHEN_RESUME);
    }
    
    @Override
    public void finish() {
        unregisterReceiver(receiver);
        super.finish();
    }


    public NetworkAdapter getNetworkAdapter() {
        return networkAdapter;
    }
    public void setNetworkAdapter(NetworkAdapter networkAdapter) {
        this.networkAdapter = networkAdapter;
    }

}
