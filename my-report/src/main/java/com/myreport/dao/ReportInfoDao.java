package com.myreport.dao;

import java.util.List;

import com.myreport.bean.ReportInfoBean;

/**
 * 报表的数据库操作<br />
 * 
 * @author huangping <br />
 *         创建日期 2014-04-02
 */
public interface ReportInfoDao extends BaseDAO<ReportInfoBean> {

	/**
	 * 查询报表组下的报表
	 * 
	 * @param reportGroupId
	 * @return
	 * @throws Exception
	 */
	public List<ReportInfoBean> queryReportByReportGroupId(Integer reportGroupId) throws Exception;

	/**
	 * 查询该关联此数据库的报表
	 * 
	 * @param databaseId
	 * @return
	 * @throws Exception
	 */
	public List<ReportInfoBean> queryReportByDatabaseId(Integer databaseId) throws Exception;

}
