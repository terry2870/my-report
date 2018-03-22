package com.myreport.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.myreport.bean.OperaBean;
import com.myreport.bean.ReportInfoBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysUserBean;
import com.mytools.beans.PageBean;

/**
 * 报表的接口定义<br />
 * @author huangping <br />
 * 创建日期 2014-04-02
 */
public interface ReportInfoService {

	/**
	 * 查询
	 * @param reportInfo
	 * @param page
	 * @param sessionUser
	 * @return
	 * @throws Exception
	 */
	public List<ReportInfoBean> queryAll(ReportInfoBean reportInfo, PageBean page, SysUserBean sessionUser) throws Exception;
	
	/**
	 * 删除报表
	 * @param opera
	 * @param returnInfo
	 * @param reportIds
	 * @throws Exception
	 */
	public void deleteReportInfo(OperaBean opera, ReturnInfoBean returnInfo, String[] reportIds) throws Exception;
	

	/**
	 * 新增或修改报表
	 * @param opera
	 * @param returnInfo
	 * @param reportInfo
	 * @throws Exception
	 */
	public void editReportInfo(OperaBean opera, ReturnInfoBean returnInfo, ReportInfoBean reportInfo) throws Exception;
	
	/**
	 * 查询报表组下的报表
	 * @param reportGroupId
	 * @return
	 * @throws Exception
	 */
	public List<ReportInfoBean> queryReportByReportGroupId(Integer reportGroupId) throws Exception;
	
	/**
	 * 查询该关联此数据库的报表
	 * @param databaseId
	 * @return
	 * @throws Exception
	 */
	public List<ReportInfoBean> queryReportByDatabaseId(Integer databaseId) throws Exception;
	
	/**
	 * 处理用户输入的sql
	 * @param opera
	 * @param returnInfo
	 * @param sql
	 * @throws Exception
	 */
	public void dealSql(OperaBean opera, ReturnInfoBean returnInfo, String sql) throws Exception;
	
	/**
	 * 根据Id查询该报表信息
	 * @param reportId
	 * @return
	 * @throws Exception
	 */
	public ReportInfoBean queryReportInfoByid(Integer reportId) throws Exception;
	
	/**
	 * 测试sql正确性
	 * @param opera
	 * @param returnInfo
	 * @param databaseId
	 * @param sql
	 * @param paramKeys
	 * @throws Exception
	 */
	public void testSql(OperaBean opera, ReturnInfoBean returnInfo, Integer databaseId, String sql, String paramKeys) throws Exception;
	
	/**
	 * 解析sql
	 * @param sql
	 * @return
	 */
	public JSONObject resolveSql(String sql);
	
}

