package com.myreport.bean;

import java.text.MessageFormat;

import com.myreport.constants.Constant;


/**
 * service返回信息对象
 * @author hp
 * 2014-03-11
 */
public class ReturnInfoBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	private String returnCode;
	private String returnInfo;
	
	private Object returnObj;

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
		this.returnInfo = Constant.returnCodeMap.get(this.returnCode);
	}
	
	public void setReturnCode(String returnCode, Object[] arr) {
		setReturnCode(returnCode);
		this.returnInfo = MessageFormat.format(this.returnInfo, arr);
	}

	public String getReturnInfo() {
		return returnInfo;
	}

	public void setReturnInfo(String returnInfo) {
		this.returnInfo = returnInfo;
	}

	public Object getReturnObj() {
		return returnObj;
	}

	public void setReturnObj(Object returnObj) {
		this.returnObj = returnObj;
	}

}

