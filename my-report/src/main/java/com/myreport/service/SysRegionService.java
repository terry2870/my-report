package com.myreport.service;

import java.util.List;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysRegionBean;
import com.myreport.bean.SysUserBean;

/**
 * 地区的接口
 * @author huangping<br />
 * 2013-1-31
 */
public interface SysRegionService {

	/**
	 * 根据父节点编号，查询子节点（不递归）
	 * @param parentRegionId 父节点编号
	 * @param user 用户信息
	 * @return 返回所有的子结点
	 * @throws Exception
	 */
	public List<SysRegionBean> queryRegionInfoByParentRegionId(long parentRegionCode, SysUserBean user) throws Exception;
	
	/**
	 * 查询所有的地区
	 * @param user
	 * @return 返回所有的地区信息
	 * @throws Exception
	 */
	public List<SysRegionBean> queryAllRegion(SysUserBean user) throws Exception;
	
	/**
	 * 删除地区
	 * @param opera
	 * @param returnInfo
	 * @param regionIds
	 * @throws Exception
	 */
	public void deleteSysRegion(OperaBean opera, ReturnInfoBean returnInfo, String[] regionIds) throws Exception;
	

	/**
	 * 新增或修改地区
	 * @param opera
	 * @param returnInfo
	 * @param sysRegionBean
	 * @throws Exception
	 */
	public void editSysRegion(OperaBean opera, ReturnInfoBean returnInfo, SysRegionBean sysRegionBean) throws Exception;
}

