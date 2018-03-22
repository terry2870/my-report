package com.myreport.service;

import java.util.List;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysUserBean;
import com.myreport.bean.DatabaseInfoBean;
import com.mytools.beans.PageBean;

/**
 * 数据库表信息的接口定义<br />
 * @author huangping <br />
 * 创建日期 2014-04-02
 */
public interface DatabaseInfoService {

	/**
	 * 查询
	 * @param databaseInfo
	 * @param page
	 * @param sessionUser
	 * @return
	 * @throws Exception
	 */
	public List<DatabaseInfoBean> queryAll(DatabaseInfoBean databaseInfo, PageBean page, SysUserBean sessionUser) throws Exception;
	
	/**
	 * 删除数据库表信息
	 * @param opera
	 * @param returnInfo
	 * @param databaseIds
	 * @throws Exception
	 */
	public void deleteDatabaseInfo(OperaBean opera, ReturnInfoBean returnInfo, String[] databaseIds) throws Exception;
	

	/**
	 * 新增或修改数据库表信息
	 * @param opera
	 * @param returnInfo
	 * @param databaseInfo
	 * @throws Exception
	 */
	public void editDatabaseInfo(OperaBean opera, ReturnInfoBean returnInfo, DatabaseInfoBean databaseInfo) throws Exception;
	
	/**
	 * 验证输入的连接有效性
	 * @param bean
	 * @return
	 */
	public boolean checkConnect(DatabaseInfoBean bean);
	
	/**
	 * 根据ID，查询数据库信息
	 * @param databaseId
	 * @return
	 * @throws Exception
	 */
	public DatabaseInfoBean queryDatabaseInfoById(Integer databaseId) throws Exception;
}

