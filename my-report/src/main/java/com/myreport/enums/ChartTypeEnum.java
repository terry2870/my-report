package com.myreport.enums;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;

/**
 * @author huangping <br />
 * 2014-7-3
 */
public enum ChartTypeEnum {

	LINE("1", "折线图"),
	COLUMN("2", "柱状图"),
	PIE("3", "饼图")
	;
	
	private ChartTypeEnum(String value, String text) {
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
	
	/**
	 * 根据值，获取对应的枚举的
	 * @param value
	 * @return
	 */
	public static String getChartTextByValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		for (ChartTypeEnum chart : values()) {
			if (value.equalsIgnoreCase(chart.getValue())) {
				return chart.toString();
			}
		}
		return null;
	}

	public String getValue() {
		return value;
	}
}
