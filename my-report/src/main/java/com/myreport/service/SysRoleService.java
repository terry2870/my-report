package com.myreport.service;

import java.util.List;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysRoleBean;
import com.myreport.bean.SysUserBean;
import com.mytools.beans.PageBean;

/**
 * @author huangping
 * 2014-03-11
 */
public interface SysRoleService {

	/**
	 * 查询
	 * @param t
	 * @param page
	 * @param sessionUser
	 * @return
	 * @throws Exception
	 */
	public List<SysRoleBean> queryAll(SysRoleBean t, PageBean page, SysUserBean sessionUser) throws Exception;
	
	/**
	 * 删除角色
	 * @param opera
	 * @param returnInfo
	 * @param roleIds
	 * @throws Exception
	 */
	public void deleteRoles(OperaBean opera, ReturnInfoBean returnInfo, String[] roleIds) throws Exception;
	

	/**
	 * 新增或修改角色
	 * @param opera
	 * @param returnInfo
	 * @param t
	 * @throws Exception
	 */
	public void roleEdit(OperaBean opera, ReturnInfoBean returnInfo, SysRoleBean t) throws Exception;
}

