package com.myreport.configs;

import com.mytools.utils.SpringContextUtil;

/**
 * 配置文件对象的工厂
 * @author ping.huang
 * @date 2014-12-05
 *
 */
public class ConfigFactory {

	private static SysConfig sysConfig = null;
	
	static {
		sysConfig = SpringContextUtil.getBean(SysConfig.class);
	}

	public static SysConfig getSysConfig() {
		return sysConfig;
	}
}
