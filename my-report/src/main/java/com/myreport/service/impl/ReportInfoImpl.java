package com.myreport.service.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.myreport.bean.DatabaseInfoBean;
import com.myreport.bean.OperaBean;
import com.myreport.bean.ReportInfoBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysUserBean;
import com.myreport.constants.Constant;
import com.myreport.dao.DatabaseInfoDao;
import com.myreport.dao.QueryReportDao;
import com.myreport.dao.ReportInfoDao;
import com.myreport.enums.ActionTypeEnum;
import com.myreport.enums.ReturnCodeEnum;
import com.myreport.service.QueryReportService;
import com.myreport.service.ReportInfoService;
import com.mytools.beans.PageBean;
import com.mytools.extend.cache.MyCache;

/**
 * 报表的接口实现<br />
 * @author huangping <br />
 * 创建日期 2014-04-02
 */
@Service
public class ReportInfoImpl implements ReportInfoService {

	@Resource
	ReportInfoDao reportInfoDao;
	
	@Resource
	QueryReportDao queryReportDao;
	
	@Resource
	DatabaseInfoDao databaseInfoDao;
	
	@Resource
	QueryReportService queryReportService;
	
	@Resource
	MyCache myCache;
	
	@Override
	public List<ReportInfoBean> queryAll(ReportInfoBean reportInfo, PageBean page, SysUserBean sessionUser) throws Exception {
		return reportInfoDao.queryAll(reportInfo, page, sessionUser);
	}

	@Override
	public void deleteReportInfo(OperaBean opera, ReturnInfoBean returnInfo, String[] reportIds) throws Exception {
		opera.setOperaType(ActionTypeEnum.DELETE.toString());
		reportInfoDao.delete(reportIds);
		for (String id : reportIds) {
			myCache.removeByKey(Constant.REPORTINFO_CACHE + id);
		}
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}

	@Override
	public void editReportInfo(OperaBean opera, ReturnInfoBean returnInfo, ReportInfoBean reportInfo) throws Exception {
		if (reportInfo.getReportId() == 0) {
			opera.setOperaType(ActionTypeEnum.ADD.toString());
			reportInfo.setReportId(reportInfoDao.insert(reportInfo));
		} else {
			opera.setOperaType(ActionTypeEnum.MODIFY.toString());
			reportInfoDao.update(reportInfo);
		}
		myCache.putOrUpdate(Constant.REPORTINFO_CACHE + reportInfo.getReportId(), reportInfo);
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}

	@Override
	public List<ReportInfoBean> queryReportByReportGroupId(Integer reportGroupId) throws Exception {
		return reportInfoDao.queryReportByReportGroupId(reportGroupId);
	}

	@Override
	public List<ReportInfoBean> queryReportByDatabaseId(Integer databaseId) throws Exception {
		return reportInfoDao.queryReportByDatabaseId(databaseId);
	}

	@Override
	public void dealSql(OperaBean opera, ReturnInfoBean returnInfo, String sql) throws Exception {
		opera.setOperaType(ActionTypeEnum.MODIFY.toString());
		JSONObject json = resolveSql(sql);
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
		returnInfo.setReturnObj(json);
	}
	
	@Override
	public JSONObject resolveSql(String sql) {
		// 解析sql
		JSONObject json = new JSONObject();
		Set<String> set = new HashSet<String>();
		JSONArray paramKeys = new JSONArray();
		String tmp = "", tmp2 = "", param = "";
		while (sql.indexOf("${") >= 0 && sql.indexOf("}$") >= 0) {
			tmp = sql.substring(0, sql.indexOf("${"));
			tmp2 = sql.substring(sql.indexOf("}$") + 2);
			param = sql.substring(sql.indexOf("${") + 2, sql.indexOf("}$"));
			paramKeys.add(param);
			set.add(param);
			sql = tmp + "?" + tmp2;
		}
		json.put("executeSql", sql);
		JSONArray arr = new JSONArray();
		JSONObject j = null;
		Iterator<String> it = set.iterator();
		String s = null;
		while (it.hasNext()) {
			s = it.next();
			j = new JSONObject();
			j.put("fieldName", s);
			arr.add(j);
		}
		json.put("params", arr);
		json.put("paramKeys", paramKeys.size() == 0 ? null : paramKeys);
		return json;
	}

	@Override
	public ReportInfoBean queryReportInfoByid(Integer reportId) throws Exception {
		ReportInfoBean report = myCache.get(Constant.REPORTINFO_CACHE + reportId, ReportInfoBean.class);
		if (report != null) {
			return report;
		}
		report = reportInfoDao.queryObjectById(reportId);
		myCache.putOrUpdate(Constant.REPORTINFO_CACHE + reportId, report);
		return report;
	}

	@Override
	public void testSql(OperaBean opera, ReturnInfoBean returnInfo, Integer databaseId, String sql, String paramKeys) throws Exception {
		opera.setOperaType(ActionTypeEnum.MODIFY.toString());
		DatabaseInfoBean db = databaseInfoDao.queryObjectById(databaseId);
		queryReportService.createDataSource(opera, returnInfo, db);
		queryReportDao.checkSql(sql, db);
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}

}

