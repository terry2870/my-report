package com.myreport.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysRegionBean;
import com.myreport.bean.SysUserBean;
import com.myreport.bean.SysUserRegionBean;
import com.myreport.dao.SysUserRegionDao;
import com.myreport.enums.ActionTypeEnum;
import com.myreport.enums.ReturnCodeEnum;
import com.myreport.service.SysUserRegionService;

/**
 * 
 * @author huangping<br />
 * 2014-03-11
 */
@Service
public class SysUserRegionImpl implements SysUserRegionService {

	@Resource
	SysUserRegionDao sysUserRegionDao;

	@Override
	public void saveUserRegion(OperaBean opera, ReturnInfoBean returnInfo, SysUserRegionBean bean) throws Exception {
		opera.setOperaType(ActionTypeEnum.ADD.toString());
		sysUserRegionDao.delete(new String[] { String.valueOf(bean.getUserId()) });
		if (bean.getRegionIds() != null) {
			sysUserRegionDao.insert(bean);
		}
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}

	@Override
	public List<SysRegionBean> queryUserRegion(SysUserBean user) throws Exception {
		return sysUserRegionDao.queryUserRegion(user);
	}
}

