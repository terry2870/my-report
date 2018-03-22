package com.myreport.service;

import java.util.List;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysUserBean;
import com.myreport.bean.ReportGroupBean;
import com.mytools.beans.PageBean;

/**
 * 分组信息的接口定义<br />
 * @author huangping <br />
 * 创建日期 2014-04-02
 */
public interface ReportGroupService {

	/**
	 * 查询
	 * @param reportGroup
	 * @param page
	 * @param sessionUser
	 * @return
	 * @throws Exception
	 */
	public List<ReportGroupBean> queryAll(ReportGroupBean reportGroup, PageBean page, SysUserBean sessionUser) throws Exception;
	
	/**
	 * 删除分组信息
	 * @param opera
	 * @param returnInfo
	 * @param groupId
	 * @throws Exception
	 */
	public void deleteReportGroup(OperaBean opera, ReturnInfoBean returnInfo, Integer groupId) throws Exception;
	

	/**
	 * 新增或修改分组信息
	 * @param opera
	 * @param returnInfo
	 * @param reportGroup
	 * @throws Exception
	 */
	public void editReportGroup(OperaBean opera, ReturnInfoBean returnInfo, ReportGroupBean reportGroup) throws Exception;
	
	/**
	 * 查询分配给该用户的报表组（向上递归）
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<ReportGroupBean> queryUserReportGroup(SysUserBean user) throws Exception;
}

