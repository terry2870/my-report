package com.myreport.utils;

import com.myreport.configs.ConfigFactory;

/**
 * @author hp
 * 2014-03-11
 */
public class SessionUtil {

	/**
	 * 判断用户是否是超级管理员
	 * @param loginName
	 * @return
	 */
	public static boolean isSuperManager(String loginName) {
		return ConfigFactory.getSysConfig().getSuperManagerList().contains(loginName);
	}
	
	public static <T> T getServiceFromSession(Class<T> className) {
		return null;
	}
}

