package com.myreport.service;

import java.util.List;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysUserBean;

public interface SysUserGroupUserService {

	/**
	 * 批量添加用户与用户组关系
	 * @param opera
	 * @param returnInfo
	 * @param groupId
	 * @param userIds
	 * @throws Exception
	 */
	public void batchInsert(OperaBean opera, ReturnInfoBean returnInfo, Integer groupId, String userIds) throws Exception;

	/**
	 * 查询该用户组下面的所有用户
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public List<SysUserBean> queryUserByGroupId(Integer groupId) throws Exception;
	
	/**
	 * 查询可以供该用户组分配的用户
	 * @param groupId
	 * @param sessionUser
	 * @return
	 * @throws Exception
	 */
	public List<SysUserBean> queryForSelectUserByGroupId(Integer groupId, SysUserBean sessionUser) throws Exception;
}
