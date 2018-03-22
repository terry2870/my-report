package com.myreport.controller;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myreport.bean.PageEntity;
import com.myreport.service.QueryReportService;
import com.mytools.beans.PageBean;

/**
 * 查询报表的controller
 * @author ping.huang
 *
 */
@Controller
public class QueryReportController {

	Logger log = Logger.getLogger(getClass());
	
	@Resource
	QueryReportService queryReportService;
	
	/**
	 * 查询结果
	 * @param reportId
	 * @param page
	 * @param request
	 * @param response
	 */
	@RequestMapping("/jsp/queryDataListForReport.do")
	public void queryDataListForReport(Integer reportId, PageEntity page, HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		JSONObject json = new JSONObject();
		PageBean p = page.toPageBeanForReport();
		try {
			out = response.getWriter();
			json = queryReportService.queryDataListForReport(request, reportId, p);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(json == null ? "[]" : json.toString());
				out.close();
			}
		}
	}
	
	/**
	 * 生成图表
	 * @param reportId
	 * @param request
	 * @param response
	 */
	@RequestMapping("/jsp/queryChart.do")
	public void queryChart(Integer reportId, HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		JSONObject json = new JSONObject();
		try {
			out = response.getWriter();
			json = queryReportService.queryChart(request, reportId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(json == null ? "[]" : json.toString());
				out.close();
			}
		}
	}
	
	/**
	 * 生成excel
	 * @param reportId
	 * @param request
	 * @param response
	 */
	@RequestMapping("/jsp/createExcel.do")
	public void createExcel(Integer reportId, HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition","attachment; filename="+ URLEncoder.encode(request.getParameter("fileName"), "UTF-8") +".xls");
			queryReportService.createExcel(request, response, reportId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 获取下转的数据
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/jsp/getDownData.do")
	public void getDownData(HttpServletRequest request, HttpServletResponse response, PageEntity page) {
		PrintWriter out = null;
		List<Map<String, Object>> list = null;
		PageBean p = page.toPageBeanForReport();
		JSONObject json = new JSONObject();
		try {
			out = response.getWriter();
			list = queryReportService.getDownData(request, p);
			json.put("rows", list == null ? "[]" : list);
			json.put("total", p.getTotalCount());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(json.toString());
				out.close();
			}
		}
	}
}
