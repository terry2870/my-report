package com.myreport.enums;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author huangping<br />
 * @date 2014-02-17
 */
public enum RegionTypeEnum {
	
	PROVINCE("省"),
	CITY("地级市"),
	CITY_2("区/县"),
	TOWN("街道/乡镇"),
	COUNTRY("社区/村");
	
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
	
	private String text;
	
	private RegionTypeEnum(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}


