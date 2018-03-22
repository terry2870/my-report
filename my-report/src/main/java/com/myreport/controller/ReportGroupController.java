package com.myreport.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.myreport.bean.OperaBean;
import com.myreport.bean.PageEntity;
import com.myreport.bean.ReportGroupBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysUserBean;
import com.myreport.constants.Constant;
import com.myreport.service.ReportGroupService;
import com.mytools.beans.PageBean;

/**
 * 分组信息的控制对象<br />
 * @author huangping <br />
 * 创建日期 2014-04-02
 */
@Controller
public class ReportGroupController extends BaseController {

	Logger log = Logger.getLogger(getClass());
	
	@Resource
	ReportGroupService reportGroupService;
	
	@RequestMapping("/jsp/queryAllReportGroup.do")
	public void queryAllReportGroup(ReportGroupBean reportGroup, PageEntity page, HttpSession session, HttpServletResponse response) {
		PrintWriter out = null;
		List<ReportGroupBean> list = null;
		try {
			out = response.getWriter();
			PageBean p = page.toPageBean();
			list = reportGroupService.queryAll(reportGroup, p, (SysUserBean) session.getAttribute(Constant.USER_SESSION));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(list == null ? "[]" : JSON.toJSONString(list));
				out.close();
			}
		}
	}

	@RequestMapping("/jsp/deleteReportGroup.do")
	public void deleteReportGroup(Integer groupId, HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		OperaBean opera = getOpera(request);
		ReturnInfoBean returnInfo = getReturnInfo();
		try {
			out = response.getWriter();
			reportGroupService.deleteReportGroup(opera, returnInfo, groupId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(returnInfo.toString());
				out.close();
			}
		}
	}

	@RequestMapping("/jsp/saveReportGroup.do")
	public void saveReportGroup(HttpServletRequest request, HttpServletResponse response, ReportGroupBean reportGroup) {
		PrintWriter out = null;
		OperaBean opera = getOpera(request);
		ReturnInfoBean returnInfo = getReturnInfo();
		reportGroup.setCreateUserId(opera.getUser().getUserId());
		try {
			out = response.getWriter();
			reportGroupService.editReportGroup(opera, returnInfo, reportGroup);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(returnInfo.toString());
				out.close();
			}
		}
	}
	
	/**
	 * 查询分配给该用户的报表组（向上递归）
	 * @param session
	 * @param response
	 */
	@RequestMapping("/jsp/queryUserReportGroup.do")
	public void queryUserReportGroup(HttpSession session, HttpServletResponse response) {
		PrintWriter out = null;
		List<ReportGroupBean> list = null;
		try {
			out = response.getWriter();
			list = reportGroupService.queryUserReportGroup((SysUserBean) session.getAttribute(Constant.USER_SESSION));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(list == null ? "[]" : JSON.toJSONString(list));
				out.close();
			}
		}
	}
}

