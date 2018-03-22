package com.myreport.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysPwdHistoryBean;
import com.myreport.bean.SysRoleBean;
import com.myreport.bean.SysUserBean;
import com.myreport.configs.ConfigFactory;
import com.myreport.dao.SysRoleDao;
import com.myreport.dao.SysUserDao;
import com.myreport.enums.ActionTypeEnum;
import com.myreport.enums.ReturnCodeEnum;
import com.myreport.service.SysPwdHistoryService;
import com.myreport.service.SysUserService;
import com.mytools.beans.PageBean;
import com.mytools.utils.DateUtil;
import com.mytools.utils.MD5Util;

/**
 * 用户接口实现
 * @author huangping
 * 2014-02-14
 */
@Service
public class SysUserImpl implements SysUserService {

	@Resource
	SysUserDao sysUserDao;
	
	@Resource
	SysRoleDao sysRoleDao;
	
	@Resource
	SysPwdHistoryService sysPwdHistoryService;
	
	@Override
	public void login(OperaBean opera, ReturnInfoBean returnInfo, SysUserBean user) throws Exception {
		opera.setOperaType(ActionTypeEnum.LOGIN.toString());
		SysUserBean u = sysUserDao.queryUserByLoginName(user.getLoginName());
		if (u == null) {
			returnInfo.setReturnCode(ReturnCodeEnum.USER_IS_NOT_EXIST.toString());
			return;
		}
		u = sysUserDao.login(user);
		if (u == null) {
			returnInfo.setReturnCode(ReturnCodeEnum.LOGIN_ERROR.toString());
			return;
		}
		if (ConfigFactory.getSysConfig().isCheckPwd()) {
			if (StringUtils.isEmpty(u.getTimeOutDate())) {
				returnInfo.setReturnCode(ReturnCodeEnum.PASSWORD_TIME_OUT.toString());
				return;
			}
			if (DateUtil.formatDate(u.getTimeOutDate(), DateUtil.DATE_TIME_FORMAT, DateUtil.DATE_FORMAT).compareTo(DateUtil.getcurrentDate()) < 0) {
				returnInfo.setReturnCode(ReturnCodeEnum.PASSWORD_TIME_OUT.toString());
				return;
			}
			String reg = "^(?![0-9a-zA-Z]+$)(?![0-9a-zA-Z]+$)(?![0-9a-z\\W]+$)(?![0-9A-Z\\W]+$)(?![a-zA-Z\\W]+$)[a-zA-Z0-9\\W_]{8,20}$";
			if (!user.getLoginPwd().matches(reg)) {
				returnInfo.setReturnCode(ReturnCodeEnum.PASSWORD_IS_SIMPLE.toString());
				return;
			}
			List<SysPwdHistoryBean> l = sysPwdHistoryService.queryPwdHistory(u.getUserId());
			if (l != null && l.size() > 0) {
				for (SysPwdHistoryBean s : l) {
					if (s.getLoginPwd().equals(MD5Util.getMD5(u.getLoginPwd()))) {
						returnInfo.setReturnCode(ReturnCodeEnum.PASSWORD_USED_FREQUENTLY.toString());
						return;
					}
				}
			}
		}
		sysUserDao.updateLoginDate(user);
		returnInfo.setReturnObj(u);
		opera.setUser(u);
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}
	
	@Override
	public void saveUser(OperaBean opera, ReturnInfoBean returnInfo, SysUserBean user) throws Exception {
		SysUserBean tmp = new SysUserBean();
		tmp.setLoginName(user.getLoginName());
		List<SysUserBean> checkUserList = sysUserDao.queryAll(tmp, null);
		int userId = user.getUserId().intValue();
		if (userId == 0) {
			opera.setOperaType(ActionTypeEnum.ADD.toString());
			if (checkUserList != null && checkUserList.size() > 0) {
				returnInfo.setReturnCode(ReturnCodeEnum.LOGIN_NAME_EXIST.toString());
				return;
			}
			tmp = new SysUserBean();
			tmp.setUserName(user.getUserName());
			checkUserList = sysUserDao.queryAll(tmp, null);
			if (checkUserList != null && checkUserList.size() > 0) {
				returnInfo.setReturnCode(ReturnCodeEnum.USER_NAME_EXIST.toString());
				return;
			}
			user.setTimeOutDate(DateUtil.dateAdd(DateUtil.getcurrentDate(), "d", ConfigFactory.getSysConfig().getPwdCheckMaxDate(), DateUtil.DATE_FORMAT));
			userId = sysUserDao.insert(user);
		} else {
			opera.setOperaType(ActionTypeEnum.MODIFY.toString());
			if (checkUserList != null && checkUserList.size() > 0 && checkUserList.get(0).getUserId() != userId) {
				returnInfo.setReturnCode(ReturnCodeEnum.LOGIN_NAME_EXIST.toString());
				return;
			}
			tmp = new SysUserBean();
			tmp.setUserName(user.getUserName());
			checkUserList = sysUserDao.queryAll(tmp, null);
			if (checkUserList != null && checkUserList.size() > 0 && checkUserList.get(0).getUserId() != userId) {
				returnInfo.setReturnCode(ReturnCodeEnum.USER_NAME_EXIST.toString());
				return;
			}
			sysUserDao.update(user);
		}
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
		returnInfo.setReturnObj(userId);
	}

	@Override
	public void deleteUser(OperaBean opera, ReturnInfoBean returnInfo, String[] userIds) throws Exception {
		opera.setOperaType(ActionTypeEnum.DELETE.toString());
		List<SysRoleBean> roleList = null;
		SysUserBean user = null;
		List<SysUserBean> userList = null;
		Integer userId = 0;
		for (String u : userIds) {
			user = new SysUserBean();
			userId = new Integer(u);
			user.setCreateUserId(userId);
			userList = sysUserDao.queryAll(user, null);
			if (userList != null && userList.size() > 0) {
				returnInfo.setReturnCode(ReturnCodeEnum.USER_HAVE_CHILD_USER.toString());
				return;
			}
			SysRoleBean role = new SysRoleBean();
			role.setCreateUserId(userId);
			roleList = sysRoleDao.queryAll(role, null);
			if (roleList != null && roleList.size() > 0) {
				returnInfo.setReturnCode(ReturnCodeEnum.USER_HAVE_CHILD_ROLE.toString());
				return;
			}
		}
		sysUserDao.delete(userIds);
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}

	@Override
	public List<SysUserBean> queryAll(SysUserBean t, PageBean page, SysUserBean sessionUser) throws Exception {
		return sysUserDao.queryAll(t, page, sessionUser);
	}
	
	@Override
	public void modifyPwd(OperaBean opera, ReturnInfoBean returnInfo, SysUserBean t) throws Exception {
		opera.setOperaType(ActionTypeEnum.MODIFY.toString());
		SysUserBean uu = new SysUserBean();
		uu.setLoginName(t.getLoginName());
		uu.setLoginPwd(t.getOldPwd());
		SysUserBean user = sysUserDao.login(uu);
		if (user == null) {
			returnInfo.setReturnCode(ReturnCodeEnum.OLD_PASSWORD_ERROR.toString());
			return;
		}
		opera.setUser(user);
		t.setTimeOutDate(DateUtil.dateAdd(DateUtil.getcurrentDate(), "d", ConfigFactory.getSysConfig().getPwdCheckMaxDate(), DateUtil.DATE_FORMAT));
		sysUserDao.modifyPwd(t);
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}

}


