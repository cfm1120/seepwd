package com.sbstudio.seepwd.util.http;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 网络get和post通用类
 * @author pasino
 *
 */
public class HttpUtils {
	
	/**网络请求超时设定*/
	private static final  int HTTP_TIMEOUT = 6000;
	
	/**网络读取超时设定*/
	private static final int HTTP_READTIMEOUT = 6000;
	
	/**网络编码设定*/
	private static final String CHARCODE = HTTP.UTF_8;
	
	/**
	 *  得到网络请求字符串
	 * @param url 请求的网络地址
	 * @return 返回网络返回的字符串 网络有误返回null
	 * 
	 */
	public static String getHttpResponseText(String url)
	{
		
		BufferedReader reader = null;
		HttpURLConnection conn = null;
		
		try
		{
				URL httpUrl = new URL(url);
		
				conn  = (HttpURLConnection) httpUrl.openConnection();
//				conn.setDoInput(true);
//				conn.setDoOutput(true);
				conn.setConnectTimeout(HTTP_TIMEOUT);
				conn.setReadTimeout(HTTP_READTIMEOUT);
				conn.setRequestMethod("GET");
				conn.setUseCaches(false);
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("Charset", CHARCODE);
//				conn.setRequestProperty("Content-Type", "application/json");
//				conn.set
				
//				获得响应状态
				int responseCode = conn.getResponseCode();
				 
				if (HttpURLConnection.HTTP_OK == responseCode) {// 连接成功
					reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),CHARCODE));
					StringBuffer buffer = new StringBuffer();
					String temp = null;
					while (( temp = reader.readLine()) != null) {
						buffer.append(temp);
					}
					reader.close();
					return buffer.toString();
				}
				
		}
		catch (Exception e) 
		{
			e.printStackTrace() ;
		}
		finally
		{
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
			if(conn != null)
			{
				conn.disconnect();
			}
		}
		
		return null;
		
	}
	
	
	/**
	 *  得到网络请求对象
	 * @param <T>
	 * @param url 请求的网络地址
	 * @return 返回网络返回的json字符串转换成pojo对象 
	 * @throws IOException 网络错误抛出此异常
	 * @throws JSONException 解析json字符串有误抛出此异常
	 */
	public static <T> T getHttpResponseObject(String url, Class<T> cls)
		throws IOException, JSONException
	{
		String json = getHttpResponseText(url);
		if(json == null)
		{
			throw new IOException("读取网络有误！");
		}
		
		try {
			return JsonUtil.parse(json, cls);
		} catch (Exception e) {
			throw new JSONException("解析json字符串异常");
		}
		
	}
	
	/**
	 *  得到网络请求对象
	 * @param <T>
	 * @param url 请求的网络地址
	 * @return 返回网络返回的json字符串转换成pojo对象 
	 * @throws IOException 网络错误抛出此异常
	 * @throws JSONException 解析json字符串有误抛出此异常
	 */
	public static <T> List<T> getHttpResponseObjectList(String url, Class<T[]> cls)
		throws IOException, JSONException
	{
		String json = getHttpResponseText(url);
		if(json == null)
		{
			throw new IOException("读取网络有误！");
		}
		
		try {
			return JsonUtil.parseList(json, cls);
		} catch (Exception e) {
			throw new JSONException("解析json字符串异常");
		}
		
	}
	
	/**
	 *  得到网络请求对象
	 * @param <T>
	 * @param url 请求的网络地址
	 * @return 返回网络返回的json字符串转换成pojo对象 
	 * @throws IOException 网络错误抛出此异常
	 * @throws JSONException 解析json字符串有误抛出此异常
	 */
	public static <T> T postHttpResponseObject(String url, String post, Class<T> cls)
		throws IOException, JSONException
	{
		String json = postHttpResponseText(url, post);
		if(json == null)
		{
			throw new IOException("读取网络有误！");
		}
		
		try {
			return JsonUtil.parse(json, cls);
		} catch (Exception e) {
			throw new JSONException("解析json字符串异常");
		}
		
	}
	
	/**
	 *  得到网络请求对象
	 * @param <T>
	 * @param url 请求的网络地址
	 * @return 返回网络返回的json字符串转换成pojo对象 
	 * @throws IOException 网络错误抛出此异常
	 * @throws JSONException 解析json字符串有误抛出此异常
	 */
	public static <T> T postHttpResponseObject(String url, Object post, Class<T> cls)
		throws IOException, JSONException
	{
		return postHttpResponseObject(url, JsonUtil.format(post), cls);
	}
	
	/**
	 *  得到网络请求字符串
	 * @param url 请求的网络地址
	 * @param post 要发送的数据
	 * @return 返回网络返回的字符串  网络有误返回null
	 */
	public static String postHttpResponseText(String url, String post)
	{
		
		BufferedReader reader = null;
		HttpURLConnection conn = null;
		
		try
		{
			
			URL httpUrl = new URL(url);
			HttpURLConnection httpConn = (HttpURLConnection) httpUrl.openConnection();

			// //设置连接属性
			httpConn.setDoOutput(true);// 使用 URL 连接进行输出
			httpConn.setDoInput(true);// 使用 URL 连接进行输入
			httpConn.setUseCaches(false);// 忽略缓存
			httpConn.setRequestMethod("POST");// 设置URL请求方法
			String requestString = post;

			// 设置请求属性
			// 获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
			byte[] requestStringBytes = requestString.getBytes(CHARCODE);
			httpConn.setRequestProperty("Content-length", "" + requestStringBytes.length);
			httpConn.setRequestProperty("Content-Type", "application/json");
			httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			httpConn.setRequestProperty("Charset", CHARCODE);

			// 建立输出流，并写入数据
			OutputStream outputStream = httpConn.getOutputStream();
			outputStream.write(requestStringBytes);
			outputStream.close();
			
			//	获得响应状态
			int responseCode = httpConn.getResponseCode();
			 
			if (HttpURLConnection.HTTP_OK == responseCode) {// 连接成功

				// 当正确响应时处理数据
				StringBuffer buffer = new StringBuffer();
				String readLine =  null;
				// 处理响应流，必须与服务器响应流输出的编码一致
				reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), CHARCODE));
				while ((readLine = reader.readLine()) != null) {
					buffer.append(readLine).append("\n");
				}
				reader.close();
				
				return buffer.toString();
			}
		}
		catch (Exception e) 
		{
			
		}
		finally
		{
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
			if(conn != null)
			{
				conn.disconnect();
			}
		}
		
		return null;
		
	}
	
	/***
	 * 发送表单数据
	 */
    /**上传文件*/
	public static <T> T post(String actionUrl, Map<String, String> params,  
	           Map<String, File> files , Class<T> cls)
	        		   throws IOException , JSONException
	{
		String json = post(actionUrl, params, files);
		if(json == null)
		{
			throw new IOException("读取网络有误！");
		}
		
		try {
			return JsonUtil.parse(json, cls);
		} catch (Exception e) {
			throw new JSONException("解析json字符串异常");
		}
	}
	
	/***
	 * 发送表单数据
	 */
    /**上传文件*/
	public static String post(String actionUrl, Map<String, String> params,  
			           Map<String, File> files) throws IOException 
	{ 
		
	       String BOUNDARY = java.util.UUID.randomUUID().toString();  
	       String PREFIX = "--", LINEND = "\r\n";  
	       String MULTIPART_FROM_DATA = "multipart/form-data";  
	       String CHARSET = "UTF-8";  
	        
	       URL uri = new URL(actionUrl);  
	       HttpURLConnection conn = (HttpURLConnection) uri.openConnection();  
	       conn.setReadTimeout(5 * 1000);  
	       conn.setDoInput(true);// 允许输入  
	       conn.setDoOutput(true);// 允许输出  
	       conn.setUseCaches(false);  
	       conn.setRequestMethod("POST"); // Post方式  
	       conn.setRequestProperty("connection", "keep-alive");  
	       conn.setRequestProperty("Charsert", "UTF-8");  
	       conn.setConnectTimeout(5000);
	       conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA  
	                + ";boundary=" + BOUNDARY);  
	       
       // 首先组拼文本类型的参数  
	       StringBuilder sb = new StringBuilder();
	       if(params != null)
	       {
		       for (Map.Entry<String, String> entry : params.entrySet()) 
		       {  
		           sb.append(PREFIX);  
		           sb.append(BOUNDARY);  
		           sb.append(LINEND);  
		           sb.append("Content-Disposition: form-data; name=\""  
		                   + entry.getKey() + "\"" + LINEND);  
		           sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);  
		           sb.append("Content-Transfer-Encoding: 8bit" + LINEND);  
		           sb.append(LINEND);  
		           sb.append(entry.getValue());  
		           sb.append(LINEND);  
		       }
	       }
	       
	       DataOutputStream outStream = new DataOutputStream(  
	                conn.getOutputStream());  
	       outStream.write(sb.toString().getBytes()); 
        // 发送文件数据  
	       if (files != null)
	       {
	    	   for (Map.Entry<String, File> file : files.entrySet()) 
	    	   {  
	               StringBuilder sb1 = new StringBuilder();  
	               sb1.append(PREFIX);  
	               sb1.append(BOUNDARY);  
		           sb1.append(LINEND);  
	               sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""  
		                       + file.getKey() + "\"" + LINEND);  
		           sb1.append("Content-Type: application/octet-stream; charset="  
		                      + CHARSET + LINEND);  
		           sb1.append(LINEND);  
		           outStream.write(sb1.toString().getBytes());  
	               
		           InputStream is = new FileInputStream(file.getValue());
	               byte[] buffer = new byte[1024];  
		           int len = 0;  
		           while ((len = is.read(buffer)) != -1) 
		           {  
	                  outStream.write(buffer, 0, len);  
		           }  
		           is.close();  
	               outStream.write(LINEND.getBytes());  
	           }
	       }
	       
	       // 请求结束标志  
	       byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();  
	       outStream.write(end_data);  
	       outStream.flush();  
	       // 得到响应码  
	       int res = conn.getResponseCode();  
	       InputStream in = conn.getInputStream(); 
	       if (res == 200) 
	       {  
	              
	           BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
	           StringBuffer buffer = new StringBuffer();
			   String temp = null;
			   while (( temp = reader.readLine()) != null) 
			   {
					buffer.append(temp);
			   }
			   reader.close();
	           return buffer.toString();
	       }  
	       
	       outStream.close();  
	       conn.disconnect();  
	       
	       return null;  
	}  
      
	/**
	 * 编码字符串
	 * @param url
	 * @return
	 */
    public static String encode(String url) 
    {     
        try 
        {     
            return URLEncoder.encode(url, "UTF-8");     
        } catch (UnsupportedEncodingException ex) {     
            return url;     
        }     
    }     
	
}
