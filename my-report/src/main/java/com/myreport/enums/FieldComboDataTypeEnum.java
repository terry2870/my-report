package com.myreport.enums;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author huangping <br />
 * 2014-7-3
 */
public enum FieldComboDataTypeEnum {

	
	SELF("1", "自定义"),

	SQL_RESULT("2", "来自sql结果")
	;

	private FieldComboDataTypeEnum(String value, String text) {
		this.value = value;
		this.text = text;
	}

	private String text;
	private String value;

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
