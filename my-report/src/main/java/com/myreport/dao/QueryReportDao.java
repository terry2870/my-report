package com.myreport.dao;

import java.util.List;
import java.util.Map;

import com.myreport.bean.DatabaseInfoBean;
import com.mytools.beans.PageBean;

/**
 * 
 * @author huangping<br />
 * 2013-9-22
 */
public interface QueryReportDao extends BaseDAO<Object> {
	
	/**
	 * 查询报表的数据
	 * @param sql
	 * @param obj
	 * @param page
	 * @param databaseId
	 * @param databaseType
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryDataListForReport(String sql, Object[] obj, PageBean page, Integer databaseId, String databaseType) throws Exception;
	
	/**
	 * 检查sql的正确性
	 * @param sql
	 * @param db
	 * @throws Exception
	 */
	public void checkSql(String sql, DatabaseInfoBean db) throws Exception;
}


