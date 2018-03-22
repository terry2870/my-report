package com.myreport.dao;

import java.util.List;

import com.myreport.bean.SysRegionBean;
import com.myreport.bean.SysUserBean;
import com.myreport.bean.SysUserRegionBean;

/**
 * 
 * @author huangping<br />
 * 2013-2-4
 */
public interface SysUserRegionDao extends BaseDAO<SysUserRegionBean> {

	/**
	 * 查询该用户所属的地市
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<SysRegionBean> queryUserRegion(SysUserBean user) throws Exception;

}


