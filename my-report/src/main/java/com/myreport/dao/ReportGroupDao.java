package com.myreport.dao;

import java.util.List;

import com.myreport.bean.ReportGroupBean;
import com.myreport.bean.SysUserBean;

/**
 * 分组信息的数据库操作<br />
 * @author huangping <br />
 * 创建日期 2014-04-02
 */
public interface ReportGroupDao extends BaseDAO<ReportGroupBean> {


	/**
	 * 查询该组下的子组
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public List<ReportGroupBean> queryChildGroupById(Integer groupId) throws Exception;
	
	/**
	 * 查询分配给该用户的报表组（向上递归）
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<ReportGroupBean> queryUserReportGroup(SysUserBean user) throws Exception;

}

