package net.dowish.common.utils;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 */
public class Apis extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	public Apis() {
		put("code", 0);
	}
	
	public static Apis error() {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
	}
	
	public static Apis error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
	}
	
	public static Apis error(int code, String msg) {
		Apis apis = new Apis();
		apis.put("code", code);
		apis.put("msg", msg);
		return apis;
	}

	public static Apis ok(String msg) {
		Apis apis = new Apis();
		apis.put("msg", msg);
		apis.put("code",HttpStatus.SC_OK);
		return apis;
	}
	
	public static Apis ok(Map<String, Object> map) {
		Apis apis = new Apis();
		apis.putAll(map);
		apis.put("code",HttpStatus.SC_OK);
		return apis;
	}
	
	public static Apis ok() {
		Apis apis= new Apis();
		apis.put("code",HttpStatus.SC_OK);
		return apis;
	}

	public Apis put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
