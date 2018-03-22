package com.myreport.bean;

/**
 * 枚举型类型数据的bean
 * @author huangping <br />
 * 2014-03-11
 */
public class EnumTypeBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	private String className;
	private String firstText;
	private String firstValue;
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getFirstText() {
		return firstText;
	}
	public void setFirstText(String firstText) {
		this.firstText = firstText;
	}
	public String getFirstValue() {
		return firstValue;
	}
	public void setFirstValue(String firstValue) {
		this.firstValue = firstValue;
	}
}

