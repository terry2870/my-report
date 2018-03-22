package com.myreport.dao;

import java.util.List;

import com.myreport.bean.UserGroupReportGroupBean;

/**
 * 用户组与报表组关系的dao
 * @author ping.huang
 *
 */
public interface UserGroupReportGroupDao extends BaseDAO<UserGroupReportGroupBean> {
	
	/**
	 * 批量添加用户组与报表组关系
	 * @param list
	 * @throws Exception
	 */
	public void batchInsert(List<UserGroupReportGroupBean> list) throws Exception;
	
	/**
	 * 删除该用户组关联的所有报表组
	 * @param userGroupId
	 * @throws Exception
	 */
	public void deleteUserGroupReportGroupByUserGroupId(Integer userGroupId) throws Exception;
	
	/**
	 * 查询关联该报表组的用户组
	 * @param reportGroupId
	 * @return
	 * @throws Exception
	 */
	public List<UserGroupReportGroupBean> queryUserGroupByReportGroupId(Integer reportGroupId) throws Exception;
	
	/**
	 * 查询关联该用户组的报表组
	 * @param userGroupId
	 * @return
	 * @throws Exception
	 */
	public List<UserGroupReportGroupBean> queryReportGroupByUserGroupId(Integer userGroupId) throws Exception;

}
