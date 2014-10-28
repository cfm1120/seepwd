package com.sbstudio.seepwd.util.http;

import com.google.gson.Gson;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * json字符串 和对象互换 工具类
 * 依赖Gson包
 * @see com.google.gson.Gson
 * @author pasino
 *
 */
public class JsonUtil {
 
	/**
	 * 将json字符串转换成对象
	 * 
	 * @param json
	 * @param type
	 * @return
	 */
	public static <T> T parse(String json, Class<T> type) {
		Gson gson = new Gson();
		return gson.fromJson(json, type);
	}

	/**
	 * 将json转成数组
	 * 
	 * @param json
	 * @param type
	 * @return
	 */
	public static <T> T[] parseArr(String json, Class<T[]> type) {
		return parse(json, type);
	}

	/**
	 * 将json转成集合
	 * 
	 * @param json
	 * @param type
	 * @return
	 */
	public static <T> List<T> parseList(String json, Class<T[]> type) {
		return new ArrayList<T>(Arrays.asList(parse(json, type)));
	}

	/**
	 * 将对象转成json字符串
	 * 
	 * @param o
	 * @return
	 */
	public static String format(Object o) {
		Gson gson = new Gson();
		return gson.toJson(o);
	}
	
	/**
	 * 将对象转成json字符串 并使用url编码
	 * @param o
	 * @return
	 */
	public static String formatURLString(Object o)
	{
		try
		{
			return URLEncoder.encode(format(o), "utf-8");
		}catch (Exception e) {
			return null;
		}
	}
}
