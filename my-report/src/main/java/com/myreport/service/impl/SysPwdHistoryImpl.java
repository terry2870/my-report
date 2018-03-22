package com.myreport.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysPwdHistoryBean;
import com.myreport.dao.SysPwdHistoryDao;
import com.myreport.enums.ActionTypeEnum;
import com.myreport.enums.ReturnCodeEnum;
import com.myreport.service.SysPwdHistoryService;

/**
 * @author huangping<br />
 * 2014-03-11
 */
@Service
public class SysPwdHistoryImpl implements SysPwdHistoryService {

	@Resource
	SysPwdHistoryDao sysPwdHistoryDao;

	@Override
	public void insertPwdHistory(OperaBean opera, ReturnInfoBean returnInfo, SysPwdHistoryBean t) throws Exception {
		opera.setOperaType(ActionTypeEnum.ADD.toString());
		sysPwdHistoryDao.insert(t);
		opera.setLogInfo("成功新增历史密码【"+ t.toString() +"】");
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}

	@Override
	public List<SysPwdHistoryBean> queryPwdHistory(long userId) throws Exception {
		return sysPwdHistoryDao.queryPwdHistory(userId);
	}

}

