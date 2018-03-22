package com.myreport.service;

import java.util.List;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.UserGroupReportGroupBean;

/**
 * 用户组与报表组关系的service
 * @author ping.huang
 *
 */
public interface UserGroupReportGroupService {

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
	
	/**
	 * 批量添加用户组与报表组关系
	 * @param opera
	 * @param returnInfo
	 * @param userGroupId
	 * @param reportGroupIds
	 * @throws Exception
	 */
	public void batchInsert(OperaBean opera, ReturnInfoBean returnInfo, Integer userGroupId, String reportGroupIds) throws Exception;
}
