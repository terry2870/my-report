package com.myreport.enums;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 操作日志类型枚举类
 * @author huangping<br />
 * 2014-03-11
 */
public enum ActionTypeEnum {

	/**
	 * 登录
	 */
	LOGIN("登录"),
	
	/**
	 * 查询
	 */
	SEARCH("查询"),
	
	/**
	 * 新增
	 */
	ADD("新增"),
	
	/**
	 * 修改
	 */
	MODIFY("修改"),
	
	/**
	 * 删除
	 */
	DELETE("删除");
	
	private ActionTypeEnum(String text) {
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

