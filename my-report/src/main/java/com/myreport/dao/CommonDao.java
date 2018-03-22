package com.myreport.dao;

import java.util.List;
import java.util.Map;

import com.mytools.beans.PageBean;


/**
 * 
 * @author huangping<br />
 * 2013-9-22
 */

public interface CommonDao extends BaseDAO<Object> {

	/**
	 * 获取数据
	 * @param sql
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryDataList(String sql, Object[] obj) throws Exception;
	
	/**
	 * 获取数据
	 * @param sql
	 * @param obj
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryDataList(String sql, Object[] obj, PageBean page) throws Exception;
	
}


