package com.myreport.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.myreport.bean.DatabaseInfoBean;
import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.mytools.beans.PageBean;

/**
 * 
 * @author huangping<br />
 * 2013-9-22
 */
public interface QueryReportService {
	
	/**
	 * 查询报表的数据
	 * @param request
	 * @param reportId
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public JSONObject queryDataListForReport(HttpServletRequest request, Integer reportId, PageBean page) throws Exception;
	
	/**
	 * 动态创建数据源
	 * @param opera
	 * @param returnInfo
	 * @param db
	 * @throws Exception
	 */
	public void createDataSource(OperaBean opera, ReturnInfoBean returnInfo, DatabaseInfoBean db) throws Exception;
	
	/**
	 * 获取生成图表的json
	 * @param request
	 * @param reportId
	 * @return
	 * @throws Exception
	 */
	public JSONObject queryChart(HttpServletRequest request, Integer reportId) throws Exception;
	
	/**
	 * 生成excel
	 * @param request
	 * @param response
	 * @param reportId
	 * @throws Exception
	 */
	public void createExcel(HttpServletRequest request, HttpServletResponse response, Integer reportId) throws Exception;
	
	/**
	 * 获取下转的数据
	 * @param request
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getDownData(HttpServletRequest request, PageBean page) throws Exception;
}

