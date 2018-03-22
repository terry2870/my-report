package com.myreport.enums;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 状态的枚举类
 * @author huangping<br />
 * @date 2014-03-11
 */
public enum StatusEnum {

	/**
	 * 正常
	 */
	A("正常"),
	
	/**
	 * 非正常
	 */
	U("非正常");
	
	private StatusEnum(String text) {
		this.text = text;
	}

	private String text;

	public String getText() {
		return this.text;
	}

	/**
	 * 返回json格式的数据
	 * @return 返回json格式的数据
	 */
	public static JSONArray toJson() {
		JSONArray arr = new JSONArray();
		JSONObject json = null;
		for (int i = 0; i < values().length; i++) {
			json = new JSONObject();
			json.put("text", values()[i].getText());
			json.put("value", values()[i].toString());
			arr.add(json);
		}
		return arr;
	}
	
	/**
	 * 获取sql语句
	 * @param fieldName
	 * @param defaultValue
	 * @return
	 */
	public static String toSql(String fieldName, String defaultValue) {
		StringBuffer sb = new StringBuffer("CASE ").append(fieldName);
		for (int i = 0; i < values().length; i++) {
			sb.append(" WHEN '").append(values()[i].toString()).append("' THEN '").append(values()[i].getText()).append("' ");
		}
		sb.append(" ELSE '").append(defaultValue).append("' END");
		return sb.toString();
	}
	
	/**
	 * 获取sql语句
	 * @param fieldName
	 * @return
	 */
	public static String toSql(String fieldName) {
		return toSql(fieldName, "");
	}
}

