package com.myreport.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量类
 * 
 * @author huangping <br />
 * 2014-03-11
 */
public class Constant {

	// 登录用户的session字符串
	public static final String USER_SESSION = "USER_SESSION";
	
	public static final String IF2_SERVICE_MAP = "IF2_SERVICE_MAP";

	// 用户所能看到的所有菜单的session字符串
	public static final String USER_MENU_LIST = "USER_MENU_LIST";

	// 返回码的对照Map
	public static Map<String, String> returnCodeMap = new HashMap<String, String>();
	
	// 报表的数据源的bean前缀
	public static final String DATASOURCE_FOR_REPORT = "DATASOURCE_FOR_REPORT_";
	
	// 报表的JDBC的bean名称前缀
	public static final String JDBCTEMPLATE_FOR_REPORT = "JDBCTEMPLATE_FOR_REPORT_";
	
	public static final String REPORTINFO_CACHE = "REPORTINFO_CACHE_";
	public static final String DATABASE_CACHE = "DATABASE_CACHE_";

		
}

