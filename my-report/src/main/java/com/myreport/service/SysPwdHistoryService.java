package com.myreport.service;

import java.util.List;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysPwdHistoryBean;

/**
 * 历史密码接口
 * @author huangping<br />
 * 2014-03-11
 */
public interface SysPwdHistoryService {

	/**
	 * 新增历史密码
	 * @param opera
	 * @param returnInfo
	 * @param t
	 * @throws Exception
	 */
	public void insertPwdHistory(OperaBean opera, ReturnInfoBean returnInfo, SysPwdHistoryBean t) throws Exception;
	
	/**
	 * 查询该用户的历史密码记录
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<SysPwdHistoryBean> queryPwdHistory(long userId) throws Exception;
}

