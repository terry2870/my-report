package com.myreport.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReportInfoBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysUserBean;
import com.myreport.bean.ReportGroupBean;
import com.myreport.bean.UserGroupReportGroupBean;
import com.myreport.dao.ReportGroupDao;
import com.myreport.enums.ActionTypeEnum;
import com.myreport.enums.ReturnCodeEnum;
import com.myreport.service.ReportGroupService;
import com.myreport.service.ReportInfoService;
import com.myreport.service.UserGroupReportGroupService;
import com.mytools.beans.PageBean;

/**
 * 分组信息的接口实现<br />
 * @author huangping <br />
 * 创建日期 2014-04-02
 */
@Service
public class ReportGroupImpl implements ReportGroupService {

	@Resource
	ReportGroupDao reportGroupDao;
	@Resource
	UserGroupReportGroupService userGroupReportGroupService;
	@Resource
	ReportInfoService reportInfoService;
	
	@Override
	public List<ReportGroupBean> queryAll(ReportGroupBean reportGroup, PageBean page, SysUserBean sessionUser) throws Exception {
		return reportGroupDao.queryAll(reportGroup, page, sessionUser);
	}

	@Override
	public void deleteReportGroup(OperaBean opera, ReturnInfoBean returnInfo, Integer groupId) throws Exception {
		opera.setOperaType(ActionTypeEnum.DELETE.toString());
		//有下级报表组，不能删除
		List<ReportGroupBean> li = reportGroupDao.queryChildGroupById(groupId);
		if (li != null && li.size() > 0) {
			returnInfo.setReturnCode(ReturnCodeEnum.REPORT_GROUP_HAVE_CHILD.toString());
			return;
		}
		//组下有报表，不能删除
		List<ReportInfoBean> l = reportInfoService.queryReportByReportGroupId(groupId);
		if (l != null && l.size() > 0) {
			returnInfo.setReturnCode(ReturnCodeEnum.REPORT_GROUP_HAVE_REPORT_BIND.toString());
			return;
		}
		//报表组中关联用户组，不能删除
		List<UserGroupReportGroupBean> list = userGroupReportGroupService.queryUserGroupByReportGroupId(groupId);
		if (list != null && list.size() > 0) {
			returnInfo.setReturnCode(ReturnCodeEnum.REPORT_GROUP_HAVE_USER_GROUP_BIND.toString());
			return;
		}
		reportGroupDao.delete(new String[] { String.valueOf(groupId) });
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}

	@Override
	public void editReportGroup(OperaBean opera, ReturnInfoBean returnInfo, ReportGroupBean reportGroup) throws Exception {
		if (reportGroup.getGroupId() == 0) {
			opera.setOperaType(ActionTypeEnum.ADD.toString());
			reportGroupDao.insert(reportGroup);
		} else {
			opera.setOperaType(ActionTypeEnum.MODIFY.toString());
			reportGroupDao.update(reportGroup);
		}
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}

	@Override
	public List<ReportGroupBean> queryUserReportGroup(SysUserBean user) throws Exception {
		return reportGroupDao.queryUserReportGroup(user);
	}

}

