package com.myreport.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReportInfoBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysUserBean;
import com.myreport.bean.DatabaseInfoBean;
import com.myreport.constants.Constant;
import com.myreport.dao.DatabaseInfoDao;
import com.myreport.enums.ActionTypeEnum;
import com.myreport.enums.ReturnCodeEnum;
import com.myreport.service.DatabaseInfoService;
import com.myreport.service.ReportInfoService;
import com.mytools.beans.PageBean;
import com.mytools.extend.cache.MyCache;

/**
 * 数据库表信息的接口实现<br />
 * @author huangping <br />
 * 创建日期 2014-04-02
 */
@Service
public class DatabaseInfoImpl implements DatabaseInfoService {

	@Resource
	DatabaseInfoDao databaseInfoDao;
	@Resource
	ReportInfoService reportInfoService;
	
	@Resource
	MyCache myCache;
	
	@Override
	public List<DatabaseInfoBean> queryAll(DatabaseInfoBean databaseInfo, PageBean page, SysUserBean sessionUser) throws Exception {
		return databaseInfoDao.queryAll(databaseInfo, page, sessionUser);
	}

	@Override
	public void deleteDatabaseInfo(OperaBean opera, ReturnInfoBean returnInfo, String[] databaseIds) throws Exception {
		opera.setOperaType(ActionTypeEnum.DELETE.toString());
		List<ReportInfoBean> list = null;
		for (String id : databaseIds) {
			//有报表关联此数据库，不能删除
			list = reportInfoService.queryReportByDatabaseId(new Integer(id));
			if (list != null && list.size() > 0) {
				returnInfo.setReturnCode(ReturnCodeEnum.DATABASE_HAVE_REPORT_BIND.toString(), new Object[] { databaseInfoDao.queryObjectById(new Integer(id)).getDatabaseTitle() });
				return;
			}
		}
		databaseInfoDao.delete(databaseIds);
		for (String id : databaseIds) {
			myCache.removeByKey(Constant.DATABASE_CACHE + id);
		}
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}

	@Override
	public void editDatabaseInfo(OperaBean opera, ReturnInfoBean returnInfo, DatabaseInfoBean databaseInfo) throws Exception {
		if (databaseInfo.getDatabaseId() == 0) {
			opera.setOperaType(ActionTypeEnum.ADD.toString());
			databaseInfo.setDatabaseId(databaseInfoDao.insert(databaseInfo));
		} else {
			opera.setOperaType(ActionTypeEnum.MODIFY.toString());
			databaseInfoDao.update(databaseInfo);
		}
		myCache.putOrUpdate(Constant.DATABASE_CACHE + databaseInfo.getDatabaseId(), databaseInfo);
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}

	@Override
	public boolean checkConnect(DatabaseInfoBean bean) {
		return databaseInfoDao.checkConnect(bean);
	}

	@Override
	public DatabaseInfoBean queryDatabaseInfoById(Integer databaseId) throws Exception {
		DatabaseInfoBean db = myCache.get(Constant.DATABASE_CACHE + databaseId, DatabaseInfoBean.class);
		if (db != null) {
			return db;
		}
		db = databaseInfoDao.queryObjectById(databaseId);
		myCache.putOrUpdate(Constant.DATABASE_CACHE + databaseId, db);
		return db;
	}

}

