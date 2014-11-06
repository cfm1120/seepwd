package com.sbstudio.seepwd.util.http;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sbstudio.seepwd.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 自动更新
 * 
 * 需要添加USER权限
 * android.permission.ACCESS_WIFI_STATE
 * android.permission.ACCESS_NETWORK_STATE
 */
public class AutoUpdate
{
    // 
    private Context context;

    //服务器地址 
    private String serverAddress =Config.SERVER;
    
	// 软件版本
    public static final String softVersion = "1.02";
    
    // 获取系统最新版本号的url地址
    private String versionUrl = "version.html";
    
    // 软件下载地址  
    private String apkUrl = "seepwd.apk";
    
    // 弹出框 
    private Dialog downloadDialog;
    
   	// 安装包保存路径
    private final String savePath;
    
    // 安装包保存完整路径
    private final String saveFilePath;
    
    // 进度条
    private ProgressBar progress;  
     
    // 下载进度
    private int downProgress = 0;  
   
    // 取消下载标志
    private boolean downloadFlag = false;  
    
    // 网络侦听
//    private ServerListener svrListener;
    
    // wifi未启用或未连接/网络异常
//    private static final int UPDATE_STATE_WIFI_EXCEPTION = 1;
    
    // wifi正在连接中
//    private static final int UPDATE_STATE_WIFI_CONNECTING = 2;
    
    // 提示更新软件
    private static final int UPDATE_STATE_NOTICE_UPDATE = 3;
    
    // 获取软件版本失败
//    private static final int UPDATE_STATE_SET_SERVER_ADDRESS = 4;
    
    // 更新中
    private static final int UPDATE_STATE_UPDATING = 5;  
     
    // 下载完毕，安装软件中
    private static final int UPDATE_STATE_COMPLETED = 6;  
    
    /**
     * 
     */
    @SuppressLint("HandlerLeak")
    private Handler MsgHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {  
            switch (msg.what)
            {  
            //注释掉，不提示wifi连接不上
//            	case UPDATE_STATE_WIFI_EXCEPTION:
//            		SetWifi();
//		        	break;
//		        case UPDATE_STATE_WIFI_CONNECTING:
//		        	WifiConnecting();
//		        	break;
            
//	            case UPDATE_STATE_SET_SERVER_ADDRESS:
//	            	SetServerAddress();
//            		break;
	            case UPDATE_STATE_NOTICE_UPDATE:
	        		NoticeUpdate();  
	        		break;
	            case UPDATE_STATE_UPDATING:
                	progress.setProgress(downProgress);  
                	break;  
	            case UPDATE_STATE_COMPLETED:
            		InstallApk();
	                break;
	            default:  
	                break;
            }  
        };  
    };
     
    /**
     * 构造函数
     * @param context
     */
    public AutoUpdate(Context context)
    {  
    	this.savePath = Environment.getExternalStorageDirectory().getPath() +"/"+ context.getPackageName() +"/";
    	this.saveFilePath = savePath + "seepwd.apk";
        this.context = context;
//        this.svrListener = (ServerListener)context;
    }
   
    /**
     * 启动自动更新组件
     */
    public void Start()
    {
//    	if(this.svrListener != null)
//    	{
//    		String strAddress =  svrListener.GetServerInfo();
//    		String strAddress =  this.serverAddress;
//    		if(strAddress != "")
//    		{
//    			this.GetSoftVersion(serverAddress);
//    		}
//    		else
//    		{
//    			MsgHandler.sendEmptyMessage(UPDATE_STATE_SET_SERVER_ADDRESS);
//    		}
//    	}
    	new Thread(InspectionVersionRunnable).start();
    }
    
    /**
     * 检查软件版本
     * @param svrAddress
     */
//    public void GetSoftVersion(String svrAddress)
//    {
//    	serverAddress = svrAddress;
//    	new Thread(InspectionVersionRunnable).start();
//    }
    
    /**
     * 软件版本检测
     */
    private Runnable InspectionVersionRunnable = new Runnable()
    {
		public void run()
		{	
//			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//			
//			if(!wifiManager.isWifiEnabled())
//			{
//				MsgHandler.sendEmptyMessage(UPDATE_STATE_WIFI_EXCEPTION);
//				return;
//			}
			
//			ConnectivityManager conMan = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//			State state = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
//			 
//			if(state == State.CONNECTING)
//			{
//				MsgHandler.sendEmptyMessage(UPDATE_STATE_WIFI_CONNECTING);
//				return;
//			}
//			else if(state == State.CONNECTED) { }
//			else
//			{
//				MsgHandler.sendEmptyMessage(UPDATE_STATE_WIFI_EXCEPTION);
//				return;
//			}
			
			String strUrl = serverAddress + versionUrl;
			if(strUrl != "")
			{
				String strVersion = HttpUtils.getHttpResponseText(strUrl);
				
				//这里转一下float是为了防止连接上CMCC等跳转页的WIFI还会提示新版本下载
				try {
                    Float.parseFloat(strVersion);
                } catch (Exception e) {
                    return;
                }
				
				Log.i("自动更新", "版本号：" + strVersion);
//				if(strVersion == ""||strVersion==null)
//				{
//					MsgHandler.sendEmptyMessage(UPDATE_STATE_SET_SERVER_ADDRESS);
//				}
//				else if(!strVersion.equals(softVersion))
				 if(!softVersion.equals(strVersion))
				{
					MsgHandler.sendEmptyMessage(UPDATE_STATE_NOTICE_UPDATE);
				}
			}
//			else
//			{
//				MsgHandler.sendEmptyMessage(UPDATE_STATE_SET_SERVER_ADDRESS);
//			}
		}
    };
      
	/**
	 * 提示更新
	 */
    private void NoticeUpdate()
    {
    	new AlertDialog.Builder(context)
        .setTitle("版本更新")
        .setMessage("发布了最新版本，请下载！")
        .setPositiveButton("下载", new OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {  
                dialog.dismiss();
                Update();
            }
        })
        .setNegativeButton("下次更新", new OnClickListener()
        {             
            public void onClick(DialogInterface dialog, int which)
            {  
                dialog.dismiss();                 
            }  
        }).show();
    }
  
    /**
     * 更新软件
     */
    private void Update()
    {  
        View v = LayoutInflater.from(context).inflate(R.layout.auto_update, null);
        progress = (ProgressBar)v.findViewById(R.id.progress);  
        downloadDialog = new AlertDialog.Builder(context) 
        								.setTitle("软件更新中...")
        								.setView(v)
        								.show();
          
        DownloadApk();
    }
    
	/** 
	 * 下载apk
	 */  
	private void DownloadApk()
	{  
		new Thread(DownloadApkRunnable).start();  
	}  
     
    /**
     * 下载软件线程
     */
    private Runnable DownloadApkRunnable = new Runnable()
    {      
        public void run()
        {Looper.prepare();
        	HttpURLConnection httpConn = null;
            try
            {  
                URL url = new URL(serverAddress + apkUrl);
                httpConn = (HttpURLConnection)url.openConnection();  
                httpConn.setConnectTimeout(3000);
                httpConn.connect();  
                int length = httpConn.getContentLength();  
                InputStream inputStream = httpConn.getInputStream();  
                
                File file = new File(savePath);  
                if(!file.exists())
                {
                    file.mkdirs();
                }
                
                File apkFile = new File(saveFilePath);
                if(!apkFile.exists())
                {
                	apkFile.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(apkFile);  
                  
                int count = 0;
                byte buf[] = new byte[1024];
                do
                {
                    int readLength = inputStream.read(buf);
                    fos.write(buf, 0, readLength);
                    count += readLength; 
                    
                    if(count >= length)
                    {
                        MsgHandler.sendEmptyMessage(UPDATE_STATE_COMPLETED);
                        break;
                    }
                    
                    downProgress =(int)(((float)count / length) * 100);  
                    // 更新进度
                    MsgHandler.sendEmptyMessage(UPDATE_STATE_UPDATING);  
                }
                while(!downloadFlag);
                  
                fos.close();  
                inputStream.close();  
            }
            catch (Exception e)
            {
                downloadDialog.dismiss();
                Toast.makeText(context, "软件下载失败！", Toast.LENGTH_SHORT).show();
            }
            finally
            {
            	if(httpConn != null)
            	{
            		httpConn.disconnect();
            	}
            } 
        }  
    };  
    
    /**
     * 安装apk
     */  
    private void InstallApk()
    {  
        downloadDialog.dismiss();
        File file = new File(saveFilePath);  
        if (!file.exists())
        {  
            return;
        }
        
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + file.toString()), "application/vnd.android.package-archive");   
        context.startActivity(intent);
    }
    
    /**
     * 设置WIFI
     */
    @SuppressWarnings("unused")
    private void SetWifi()
    {
    	
		final AlertDialog dialog = new AlertDialog.Builder(context)
            .setTitle("WIFI连接异常")
            .setMessage("是否进入WIFI设置界面进行设置？")
	        .setPositiveButton("是", new DialogInterface.OnClickListener()
	        {
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				    Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
				    context.startActivity(intent);
				}
			})
            .setNegativeButton("否", new DialogInterface.OnClickListener()
            {             
                public void onClick(DialogInterface dialog, int which)
                {  
                    dialog.dismiss();                 
                }
            }).show();
    }
    
    /**
     * 网络连接中
     */
	public void WifiConnecting()
	{
		Toast.makeText(context, "网络正在连接中...", Toast.LENGTH_SHORT).show();
	}
	
    /**
     * 设置服务器地址
     */
//    public void SetServerAddress()a
//    {
//    	if(svrListener != null)
//    	{
//    		svrListener.noticeSetServerAddress();
//    	}
//    	else
//    	{
//    		Log.e("自动更新", "ServerListener对象为空！");
//    	}
//    }
}