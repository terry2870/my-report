package com.myreport.service;

import java.util.List;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReportGroupBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysUserBean;
import com.myreport.bean.SysUserGroupBean;
import com.mytools.beans.PageBean;

/**
 * 用户组表的接口定义<br />
 * @author huangping <br />
 * 创建日期 2014-03-17
 */
public interface SysUserGroupService {

	/**
	 * 查询
	 * @param userGroup
	 * @param page
	 * @param sessionUser
	 * @return
	 * @throws Exception
	 */
	public List<SysUserGroupBean> queryAll(SysUserGroupBean userGroup, PageBean page, SysUserBean sessionUser) throws Exception;
	
	/**
	 * 删除用户组表
	 * @param opera
	 * @param returnInfo
	 * @param groupIds
	 * @throws Exception
	 */
	public void deleteUserGroup(OperaBean opera, ReturnInfoBean returnInfo, String[] groupIds) throws Exception;
	

	/**
	 * 新增或修改用户组表
	 * @param opera
	 * @param returnInfo
	 * @param userGroup
	 * @throws Exception
	 */
	public void editUserGroup(OperaBean opera, ReturnInfoBean returnInfo, SysUserGroupBean userGroup) throws Exception;
	
	/**
	 * 查询该用户所能看到的报表组
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<ReportGroupBean> queryReportGroupByUserId(SysUserBean user) throws Exception;
}

