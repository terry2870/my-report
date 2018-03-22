package com.myreport.utils;

import com.mytools.database.DatabaseAbst;
import com.mytools.enums.DatabaseTypeEnum;
import com.mytools.utils.SpringContextUtil;

/**
 * 处理数据库相关的工具类
 * @author ping.huang
 *
 */
public class DatabaseUtils {

	/**
	 * 根据数据库类型，获取不同的实现类
	 * @param databaseType
	 * @return DatabaseAbst
	 */
	public static DatabaseAbst getDatabaseAbst(String databaseType) {
		String name = "";
		if (DatabaseTypeEnum.ORACLE.toString().equalsIgnoreCase(databaseType)) {
			name = "oracleDatabaseAbst";
		} else if (DatabaseTypeEnum.MYSQL.toString().equalsIgnoreCase(databaseType)) {
			name = "mysqlDatabaseAbst";
		} else if (DatabaseTypeEnum.SQLSERVER.toString().equalsIgnoreCase(databaseType)) {
			name = "sqlserverDatabaseAbst";
		} else if (DatabaseTypeEnum.DB2.toString().equalsIgnoreCase(databaseType)) {
			name = "db2DatabaseAbst";
		} else {
			return null;
		}
		return SpringContextUtil.getBean(name, DatabaseAbst.class);
	}
}
