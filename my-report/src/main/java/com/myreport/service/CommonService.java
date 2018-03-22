package com.myreport.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mytools.beans.PageBean;

/**
 * 
 * @author huangping<br />
 * 2013-9-22
 */
public interface CommonService {

	/**
	 * 获取数据
	 * @param request
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryDataList(HttpServletRequest request, PageBean page) throws Exception;
}

