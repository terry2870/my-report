package com.myreport.dao;

import java.util.List;

import com.myreport.bean.SysUserBean;
import com.myreport.bean.SysUserGroupUserBean;

/**
 * 
 * @author ping.huang
 *
 */
public interface SysUserGroupUserDao extends BaseDAO<SysUserGroupUserBean> {

	/**
	 * 批量添加用户与用户组关系
	 * @param list
	 * @throws Exception
	 */
	public void batchInsert(List<SysUserGroupUserBean> list) throws Exception;
	
	/**
	 * 根据组ID，删除用户与用户组关系
	 * @param groupId
	 * @throws Exception
	 */
	public void deleteFromGroupId(Integer groupId) throws Exception;
	
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
