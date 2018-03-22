package com.myreport.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mytools.beans.PageBean;
import com.myreport.bean.OperaBean;
import com.myreport.bean.PageEntity;
import com.myreport.bean.ReportInfoBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysUserBean;
import com.myreport.constants.Constant;
import com.myreport.service.ReportInfoService;

/**
 * 报表的控制对象<br />
 * @author huangping <br />
 * 创建日期 2014-04-02
 */
@Controller
public class ReportInfoController extends BaseController {

	Logger log = Logger.getLogger(getClass());
	
	@Resource
	ReportInfoService reportInfoService;
	
	@RequestMapping("/jsp/queryAllReportInfo.do")
	public void queryAllReportInfo(ReportInfoBean reportInfo, PageEntity page, HttpSession session, HttpServletResponse response) {
		PrintWriter out = null;
		List<ReportInfoBean> list = null;
		PageBean p = null;
		try {
			out = response.getWriter();
			p = page.toPageBean();
			list = reportInfoService.queryAll(reportInfo, p, (SysUserBean) session.getAttribute(Constant.USER_SESSION));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				if (p != null && !p.isEmpty()) {
					JSONObject json = new JSONObject();
					json.put("rows", list == null ? "[]" : list);
					json.put("total", p.getTotalCount());
					out.print(json.toString());
				} else {
					out.print(list == null ? "[]" : JSON.toJSONString(list));
				}
				out.close();
			}
		}
	}

	@RequestMapping("/jsp/deleteReportInfo.do")
	public void deleteReportInfo(String reportIds, HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		OperaBean opera = getOpera(request);
		ReturnInfoBean returnInfo = getReturnInfo();
		try {
			out = response.getWriter();
			reportInfoService.deleteReportInfo(opera, returnInfo, reportIds.split(","));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(returnInfo.toString());
				out.close();
			}
		}
	}

	@RequestMapping("/jsp/saveReportInfo.do")
	public void saveReportInfo(HttpServletRequest request, HttpServletResponse response, ReportInfoBean reportInfo) {
		PrintWriter out = null;
		OperaBean opera = getOpera(request);
		ReturnInfoBean returnInfo = getReturnInfo();
		reportInfo.setCreateUserId(opera.getUser().getUserId());
		try {
			out = response.getWriter();
			reportInfoService.editReportInfo(opera, returnInfo, reportInfo);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(returnInfo.toString());
				out.close();
			}
		}
	}
	
	@RequestMapping("/jsp/dealSql.do")
	public void dealSql(HttpServletRequest request, HttpServletResponse response, String sql) {
		PrintWriter out = null;
		OperaBean opera = getOpera(request);
		ReturnInfoBean returnInfo = getReturnInfo();
		try {
			out = response.getWriter();
			reportInfoService.dealSql(opera, returnInfo, sql);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(returnInfo.toString());
				out.close();
			}
		}
	}
	
	@RequestMapping("/jsp/testSql.do")
	public void testSql(Integer databaseId, HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		OperaBean opera = getOpera(request);
		ReturnInfoBean returnInfo = getReturnInfo();
		try {
			out = response.getWriter();
			reportInfoService.testSql(opera, returnInfo, databaseId, request.getParameter("sql"), request.getParameter("paramKeys"));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(returnInfo.toString());
				out.close();
			}
		}
	}
	
	@RequestMapping("/jsp/queryReportByReportGroupId.do")
	public void queryReportByReportGroupId(Integer groupId, HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		List<ReportInfoBean> list = null;
		try {
			out = response.getWriter();
			list = reportInfoService.queryReportByReportGroupId(groupId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				JSONArray arr = new JSONArray();
				if (list != null && list.size() > 0) {
					JSONObject json = null;
					for (ReportInfoBean bean : list) {
						json = new JSONObject();
						json.put("id", bean.getReportId());
						json.put("text", bean.getReportName());
						json.put("attributes", bean);
						arr.add(json);
					}
				}
				out.print(list == null ? "[]" : arr.toString());
				out.close();
			}
		}
	}
	
	@RequestMapping("/jsp/queryReportInfoById.do")
	public void queryReportInfoById(Integer reportId, HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		ReportInfoBean report = null;
		try {
			out = response.getWriter();
			report = reportInfoService.queryReportInfoByid(reportId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(report == null ? "{}" : JSON.toJSONString(report));
				out.close();
			}
		}
	}
}

