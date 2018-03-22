package com.myreport.service;

import java.util.List;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysRegionBean;
import com.myreport.bean.SysUserBean;
import com.myreport.bean.SysUserRegionBean;

/**
 * 用户所属地区接口
 * @author huangping<br />
 * 2014-03-11
 */
public interface SysUserRegionService {

	/**
	 * 添加
	 * @param opera
	 * @param returnInfo
	 * @param bean
	 * @throws Exception
	 */
	public void saveUserRegion(OperaBean opera, ReturnInfoBean returnInfo, SysUserRegionBean bean) throws Exception;
	
	/**
	 * 查询该用户所属地区
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<SysRegionBean> queryUserRegion(SysUserBean user) throws Exception;
}

