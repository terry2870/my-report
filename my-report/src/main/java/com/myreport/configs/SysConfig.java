package com.myreport.configs;

import java.util.ArrayList;
import java.util.List;

import com.myreport.constants.Constant;
import com.myreport.enums.ReturnCodeEnum;

/**
 * 存放系统所使用的配置数据
 * @author hp
 * 2014-03-11
 */
public class SysConfig {

	//免过滤列表（不管有没有session都可以访问）
	private List<String> firstNoFilterList = new ArrayList<String>();

	//第二级免过滤列表（只要有session就都可以访问）
	private List<String> secondNoFilterList = new ArrayList<String>();
	
	//超级管理员账号
	private List<String> superManagerList = new ArrayList<String>();
		
	/**
	 * 是否验证密码的复杂度和有效期
	 */
	private boolean checkPwd;
	
	/**
	 * 验证密码复杂度时，取最近的几条历史密码比较
	 */
	private int pwdRecordNum = 5;
	
	/**
	 * 密码有效天数
	 */
	private int pwdCheckMaxDate = 90;

	public void setSuperManagerList(List<String> superManagerList) {
		this.superManagerList = superManagerList;
	}

	public void setSecondNoFilterList(List<String> secondNoFilterList) {
		this.secondNoFilterList = secondNoFilterList;
	}
	
	public void setFirstNoFilterList(List<String> firstNoFilterList) {
		this.firstNoFilterList = firstNoFilterList;
	}
	
	/**
	 * 初始化
	 */
	public void init() {
		Constant.returnCodeMap = ReturnCodeEnum.toMap();
	}
	
	/**
	 * 销毁
	 */
	public void destroy() {
		firstNoFilterList.clear();
		secondNoFilterList.clear();
		superManagerList.clear();
	}

	public void setCheckPwd(boolean checkPwd) {
		this.checkPwd = checkPwd;
	}

	public void setPwdRecordNum(int pwdRecordNum) {
		this.pwdRecordNum = pwdRecordNum;
	}

	public void setPwdCheckMaxDate(int pwdCheckMaxDate) {
		this.pwdCheckMaxDate = pwdCheckMaxDate;
	}

	public List<String> getFirstNoFilterList() {
		return firstNoFilterList;
	}

	public List<String> getSecondNoFilterList() {
		return secondNoFilterList;
	}

	public List<String> getSuperManagerList() {
		return superManagerList;
	}

	public boolean isCheckPwd() {
		return checkPwd;
	}

	public int getPwdRecordNum() {
		return pwdRecordNum;
	}

	public int getPwdCheckMaxDate() {
		return pwdCheckMaxDate;
	}
}

