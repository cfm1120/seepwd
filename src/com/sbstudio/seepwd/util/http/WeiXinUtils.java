package com.sbstudio.seepwd.util.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.sbstudio.seepwd.R;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

import java.io.ByteArrayOutputStream;

/**
 * 分享到微信和朋友圈的帮助类
 * @author yw-tony
 *
 */
public class WeiXinUtils{
    private WeiXinUtils(){}
    private static WeiXinUtils instance;
    public static IWXAPI api;
    public SendMessageToWX.Req req;
    public static WeiXinUtils getInstance(){
        synchronized (WeiXinUtils.class) {
            if(instance == null){
                instance = new WeiXinUtils();
            }
        }
        return instance;
    }
    /**
     * 初始化分享功能
     * @param context
     * 
     */
    public void init(Context context){
        //通过WXAPIFactory工厂，获取IWXAPI实例
        api = WXAPIFactory.createWXAPI(context, Config.APP_ID, true);
        //将应用appid注册到微信
        api.registerApp(Config.APP_ID);
        
        
        //test start
        String url = Config.MARKET;//收到分享的好友点击信息会跳转到这个地址去
        WXWebpageObject localWXWebpageObject = new WXWebpageObject();
        localWXWebpageObject.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(localWXWebpageObject);
        
        msg.title = context.getString(R.string.text);//不能太长，否则微信会提示出错。不过博主没验证过具体能输入多长。
        msg.description = context.getString(R.string.description);
//        msg.thumbData = getBitmapBytes(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher), false);
        msg.thumbData = Bitmap2Bytes(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
        
        req = new SendMessageToWX.Req();
        req.transaction = System.currentTimeMillis() + "";
        req.message = msg;
        
        //test end
//        WXTextObject textObject = new WXTextObject();
//        textObject.text = context.getString(R.string.text);//内容
//        
//        WXMediaMessage  msg = new WXMediaMessage();
//        msg.mediaObject = textObject;
//        msg.description = context.getString(R.string.description);//描述
//        
//        req = new SendMessageToWX.Req();
//        req.transaction = String.valueOf(System.currentTimeMillis());
//        req.message = msg;
        
    }
    /**
     * 分享消息到微信
     * type 0:分享到微信
     * type 1:分享到朋友圈
     */
    public void sendToWeiXin(int type){
        if(type == 1){
            req.scene = SendMessageToWX.Req.WXSceneTimeline;//加上这一段代码即分享到朋友圈，否则分享到微信
        }
        api.sendReq(req);
    }
    
 // 需要对图片进行处理，否则微信会在log中输出thumbData检查错误
    private static byte[] getBitmapBytes(Bitmap bitmap, boolean paramBoolean) {
        Bitmap localBitmap = Bitmap.createBitmap(80, 80, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);
        int i;
        int j;
        if (bitmap.getHeight() > bitmap.getWidth()) {
            i = bitmap.getWidth();
            j = bitmap.getWidth();
        } else {
            i = bitmap.getHeight();
            j = bitmap.getHeight();
        }
        while (true) {
            localCanvas.drawBitmap(bitmap, new Rect(0, 0, i, j), new Rect(0, 0,
                    80, 80), null);
            if (paramBoolean)
                bitmap.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                
            }
            i = bitmap.getHeight();
            j = bitmap.getHeight();
        }
    }
    private static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}