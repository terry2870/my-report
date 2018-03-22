package com.myreport.dao;

import java.util.List;

import com.myreport.bean.SysPwdHistoryBean;
/**
 * 历史密码dao
 * @author huangping<br />
 * 2014-02-14
 */
public interface SysPwdHistoryDao extends BaseDAO<SysPwdHistoryBean> {

	/**
	 * 查询该用户的历史密码
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<SysPwdHistoryBean> queryPwdHistory(long userId) throws Exception;

}


