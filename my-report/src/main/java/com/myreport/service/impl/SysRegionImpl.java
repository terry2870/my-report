package com.myreport.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysRegionBean;
import com.myreport.bean.SysUserBean;
import com.myreport.dao.SysRegionDao;
import com.myreport.enums.ActionTypeEnum;
import com.myreport.enums.ReturnCodeEnum;
import com.myreport.service.SysRegionService;

/**
 * 地区接口实现
 * 
 * @author huangping<br />
 *         2013-1-31
 */
@Service
public class SysRegionImpl implements SysRegionService {

	@Resource
	SysRegionDao sysRegionDao;

	@Override
	public List<SysRegionBean> queryRegionInfoByParentRegionId(long parentRegionId, SysUserBean user) throws Exception {
		return sysRegionDao.queryRegionInfoByParentRegionId(parentRegionId, user);
	}

	@Override
	public List<SysRegionBean> queryAllRegion(SysUserBean user) throws Exception {
		return sysRegionDao.queryAll(null, null, user);
	}

	@Override
	public void deleteSysRegion(OperaBean opera, ReturnInfoBean returnInfo, String[] regionIds) throws Exception {
		for (String regionId : regionIds) {
			List<SysRegionBean> list = queryRegionInfoByParentRegionId(Long.parseLong(regionId), opera.getUser());
			if (list != null && list.size() > 0) {
				returnInfo.setReturnCode(ReturnCodeEnum.REGION_HAVE_CHILD.toString());
				return;
			}
		}
		opera.setOperaType(ActionTypeEnum.DELETE.toString());
		sysRegionDao.delete(regionIds);
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}

	@Override
	public void editSysRegion(OperaBean opera, ReturnInfoBean returnInfo, SysRegionBean sysRegionBean) throws Exception {
		if (sysRegionBean.getRegionId() == 0) {
			opera.setOperaType(ActionTypeEnum.ADD.toString());
			sysRegionDao.insert(sysRegionBean);
		} else {
			opera.setOperaType(ActionTypeEnum.MODIFY.toString());
			sysRegionDao.update(sysRegionBean);
		}
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}

}
