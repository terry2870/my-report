package com.myreport.service;

import java.util.List;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysUserBean;
import com.mytools.beans.PageBean;

/**
 * 用户接口定义
 * @author huangping
 * 2014-03-11
 */
public interface SysUserService {

	/**
	 * 登录
	 * @param opera 操作者信息
	 * @param returnInfo 返回对象
	 * @param user 用户对象
	 * @throws Exception
	 */
	public void login(OperaBean opera, ReturnInfoBean returnInfo, SysUserBean user) throws Exception;
	
	/**
	 * 保存用户信息
	 * @param opera 操作者信息
	 * @param returnInfo 返回对象
	 * @param user 用户对象
	 * @throws Exception
	 */
	public void saveUser(OperaBean opera, ReturnInfoBean returnInfo, SysUserBean user) throws Exception;
	
	/**
	 * 删除用户信息
	 * @param opera 操作者信息
	 * @param returnInfo 返回对象
	 * @param userIds 需要删除的用户id
	 * @throws Exception
	 */
	public void deleteUser(OperaBean opera, ReturnInfoBean returnInfo, String[] userIds) throws Exception;
	
	/**
	 * 修改密码
	 * @param opera 操作者信息
	 * @param returnInfo 返回对象
	 * @param t 用户信息
	 * @throws Exception
	 */
	public void modifyPwd(OperaBean opera, ReturnInfoBean returnInfo, SysUserBean t) throws Exception;
	
	/**
	 * 查询用户信息
	 * @param t 查询条件
	 * @param page 分页信息
	 * @param sessionUser 操作者用户对象
	 * @return 用户列表
	 * @throws Exception
	 */
	public List<SysUserBean> queryAll(SysUserBean t, PageBean page, SysUserBean sessionUser) throws Exception;
}

