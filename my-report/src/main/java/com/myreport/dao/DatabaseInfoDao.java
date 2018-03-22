package com.myreport.dao;

import com.myreport.bean.DatabaseInfoBean;

/**
 * 数据库表信息的数据库操作<br />
 * @author huangping <br />
 * 创建日期 2014-04-02
 */
public interface DatabaseInfoDao extends BaseDAO<DatabaseInfoBean> {


	
	/**
	 * 验证输入的连接有效性
	 * @param bean
	 * @return
	 */
	public boolean checkConnect(DatabaseInfoBean bean);
	

}

