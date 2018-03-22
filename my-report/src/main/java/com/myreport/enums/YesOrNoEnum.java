package com.myreport.enums;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author huangping <br />
 * 2014-7-3
 */
public enum YesOrNoEnum {

	NO("0", "否"),
	YES("1", "是");
	
	private YesOrNoEnum(String value, String text) {
		this.value = value;
		this.text = text;
	}

	private String value;
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
			json.put("value", values()[i].getValue());
			arr.add(json);
		}
		return arr;
	}

	public String getValue() {
		return value;
	}
}
