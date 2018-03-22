package com.myreport.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysUserBean;
import com.myreport.bean.SysUserGroupUserBean;
import com.myreport.dao.SysUserGroupUserDao;
import com.myreport.enums.ActionTypeEnum;
import com.myreport.enums.ReturnCodeEnum;
import com.myreport.service.SysUserGroupUserService;

@Service
public class SysUserGroupUserImpl implements SysUserGroupUserService {
	
	@Resource
	SysUserGroupUserDao sysUserGroupUserDao;

	@Override
	@Transactional
	public void batchInsert(OperaBean opera, ReturnInfoBean returnInfo, Integer groupId, String userIds) throws Exception {
		opera.setOperaType(ActionTypeEnum.ADD.toString());
		sysUserGroupUserDao.deleteFromGroupId(groupId);
		if (!StringUtils.isEmpty(userIds)) {
			String[] arr = userIds.split(",");
			List<SysUserGroupUserBean> list = new ArrayList<SysUserGroupUserBean>();
			for (String s : arr) {
				list.add(new SysUserGroupUserBean(new Integer(s), groupId));
			}
			sysUserGroupUserDao.batchInsert(list);
		}
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}

	@Override
	public List<SysUserBean> queryUserByGroupId(Integer groupId) throws Exception {
		return sysUserGroupUserDao.queryUserByGroupId(groupId);
	}

	@Override
	public List<SysUserBean> queryForSelectUserByGroupId(Integer groupId, SysUserBean sessionUser) throws Exception {
		return sysUserGroupUserDao.queryForSelectUserByGroupId(groupId, sessionUser);
	}

}
