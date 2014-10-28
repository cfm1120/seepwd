package com.sbstudio.seepwd.wxapi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.Toast;

import com.sbstudio.seepwd.R;
import com.sbstudio.seepwd.util.http.Config;
import com.sbstudio.seepwd.util.http.WeiXinUtils;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{

    
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
//        WeiXinUtils.api = WXAPIFactory.createWXAPI(this,  
//                Config.APP_ID, false);  
        WeiXinUtils.api.handleIntent(getIntent(), this);  
  
    }  
    /**
     * 微信发送的请求将回调到onReq方法
     */
    @Override
    public void onReq(BaseReq arg0) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 发送到微信请求的响应结果将回调到onResp方法
     */
    @Override
    public void onResp(BaseResp arg0) {
        //调用成功
        if(arg0.errCode==0){
            //去插屏广告...
            SharedPreferences settings=getSharedPreferences(Config.PREFS_NAME, MODE_PRIVATE);
          //存入数据
           Editor editor = settings.edit();
           editor.putBoolean("isShared", true);
           editor.commit();
           Toast.makeText(this, R.string.share_success, Toast.LENGTH_SHORT).show();
        }else Toast.makeText(this, R.string.share_fail, Toast.LENGTH_SHORT).show();
       
       // TODO 微信分享 成功之后调用接口  
       this.finish();  
        
    }

    
}
