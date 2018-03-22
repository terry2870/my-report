package com.myreport.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReportGroupBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysUserBean;
import com.myreport.bean.SysUserGroupBean;
import com.myreport.dao.SysUserGroupDao;
import com.myreport.enums.ActionTypeEnum;
import com.myreport.enums.ReturnCodeEnum;
import com.myreport.service.ReportGroupService;
import com.myreport.service.SysUserGroupService;
import com.myreport.service.SysUserGroupUserService;
import com.mytools.beans.PageBean;

/**
 * 用户组表的接口实现<br />
 * @author huangping <br />
 * 创建日期 2014-03-17
 */
@Service
public class SysUserGroupImpl implements SysUserGroupService {

	@Resource
	SysUserGroupDao sysUserGroupDao;
	@Resource
	SysUserGroupUserService sysUserGroupUserService;
	@Resource
	ReportGroupService reportGroupService;
	
	@Override
	public List<SysUserGroupBean> queryAll(SysUserGroupBean userGroup, PageBean page, SysUserBean sessionUser) throws Exception {
		return sysUserGroupDao.queryAll(userGroup, page, sessionUser);
	}

	@Override
	public void deleteUserGroup(OperaBean opera, ReturnInfoBean returnInfo, String[] groupIds) throws Exception {
		opera.setOperaType(ActionTypeEnum.DELETE.toString());
		List<SysUserBean> userList = null;
		for (String id : groupIds) {
			userList = sysUserGroupUserService.queryUserByGroupId(new Integer(id));
			if (userList != null && userList.size() > 0) {
				String[] arr = { sysUserGroupDao.queryObjectById(new Integer(id)).getGroupName() };
				returnInfo.setReturnCode(ReturnCodeEnum.USER_GROUP_HAVE_USER_BIND.toString(), arr);
				return;
			}
		}
		sysUserGroupDao.delete(groupIds);
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}

	@Override
	public void editUserGroup(OperaBean opera, ReturnInfoBean returnInfo, SysUserGroupBean userGroup) throws Exception {
		SysUserGroupBean bean = new SysUserGroupBean();
		bean.setGroupName(userGroup.getGroupName());
		List<SysUserGroupBean> list = sysUserGroupDao.queryAll(bean, null);
		if (userGroup.getGroupId() == 0) {
			opera.setOperaType(ActionTypeEnum.ADD.toString());
			if (list != null && list.size() > 0) {
				returnInfo.setReturnCode(ReturnCodeEnum.USER_GROUP_NAME_EXIST.toString());
				return;
			}
			sysUserGroupDao.insert(userGroup);
		} else {
			opera.setOperaType(ActionTypeEnum.MODIFY.toString());
			if (list != null && list.size() > 0 && list.get(0).getGroupId().intValue() != userGroup.getGroupId().intValue()) {
				returnInfo.setReturnCode(ReturnCodeEnum.USER_GROUP_NAME_EXIST.toString());
				return;
			}
			sysUserGroupDao.update(userGroup);
		}
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}

	@Override
	public List<ReportGroupBean> queryReportGroupByUserId(SysUserBean user) throws Exception {
		return reportGroupService.queryAll(null, null, user);
	}
	
}

