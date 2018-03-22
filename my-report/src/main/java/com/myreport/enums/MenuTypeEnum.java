package com.myreport.enums;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 菜单类型枚举
 * @author hp
 * @date 2014-03-11
 */
public enum MenuTypeEnum {

	/**
	 * 根节点
	 */
	ROOT("根节点"),

	/**
	 * 子菜单
	 */
	CHILD("子节点"),

	
	/**
	 * 按钮
	 */
	BUTTON("按钮");

	private MenuTypeEnum(String text) {
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
}

