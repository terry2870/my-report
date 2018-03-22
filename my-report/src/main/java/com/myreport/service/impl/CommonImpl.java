package com.myreport.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.myreport.dao.CommonDao;
import com.myreport.service.CommonService;
import com.mytools.beans.PageBean;
import com.mytools.beans.SqlParamsBean;
import com.mytools.utils.MyToolsUtil;

/**
 * 
 * @author huangping<br />
 * 2013-9-22
 */
@Service
public class CommonImpl implements CommonService {

	@Resource
	CommonDao commonDao;
	
	@Override
	public List<Map<String, Object>> queryDataList(HttpServletRequest request, PageBean page) throws Exception {
		SqlParamsBean sql = MyToolsUtil.getSqlByXml(request.getParameter("xml"), request);
		return commonDao.queryDataList(sql.getSqlValue(), sql.getParamObjectValue(), page);
	}

}


