package com.myreport.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.UserGroupReportGroupBean;
import com.myreport.dao.UserGroupReportGroupDao;
import com.myreport.enums.ActionTypeEnum;
import com.myreport.enums.ReturnCodeEnum;
import com.myreport.service.UserGroupReportGroupService;

@Service
public class UserGroupReportGroupImpl implements UserGroupReportGroupService {

	@Resource
	UserGroupReportGroupDao userGroupReportGroupDao;
	
	@Override
	public List<UserGroupReportGroupBean> queryUserGroupByReportGroupId(Integer reportGroupId) throws Exception {
		return userGroupReportGroupDao.queryUserGroupByReportGroupId(reportGroupId);
	}

	@Override
	public List<UserGroupReportGroupBean> queryReportGroupByUserGroupId(Integer userGroupId) throws Exception {
		return userGroupReportGroupDao.queryReportGroupByUserGroupId(userGroupId);
	}

	@Override
	public void batchInsert(OperaBean opera, ReturnInfoBean returnInfo, Integer userGroupId, String reportGroupIds) throws Exception {
		opera.setOperaType(ActionTypeEnum.ADD.toString());
		userGroupReportGroupDao.deleteUserGroupReportGroupByUserGroupId(userGroupId);
		if (!StringUtils.isEmpty(reportGroupIds)) {
			String[] arr = reportGroupIds.split(",");
			List<UserGroupReportGroupBean> list = new ArrayList<UserGroupReportGroupBean>();
			for (String s : arr) {
				list.add(new UserGroupReportGroupBean(userGroupId, new Integer(s)));
			}
			userGroupReportGroupDao.batchInsert(list);
		}
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}

}
