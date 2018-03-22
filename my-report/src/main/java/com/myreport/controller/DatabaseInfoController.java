package com.myreport.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mytools.beans.PageBean;
import com.myreport.bean.DatabaseInfoBean;
import com.myreport.bean.OperaBean;
import com.myreport.bean.PageEntity;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysUserBean;
import com.myreport.constants.Constant;
import com.myreport.service.DatabaseInfoService;

/**
 * 数据库表信息的控制对象<br />
 * @author huangping <br />
 * 创建日期 2014-04-02
 */
@Controller
public class DatabaseInfoController extends BaseController {

	Logger log = Logger.getLogger(getClass());
	
	@Resource
	DatabaseInfoService databaseInfoService;
	
	@RequestMapping("/jsp/queryAllDatabaseInfo.do")
	public void queryAllDatabaseInfo(DatabaseInfoBean databaseInfo, PageEntity page, HttpSession session, HttpServletResponse response) {
		PrintWriter out = null;
		JSONObject json = new JSONObject();
		try {
			out = response.getWriter();
			PageBean p = page.toPageBean();
			List<DatabaseInfoBean> list = databaseInfoService.queryAll(databaseInfo, p, (SysUserBean) session.getAttribute(Constant.USER_SESSION));
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

	@RequestMapping("/jsp/deleteDatabaseInfo.do")
	public void deleteDatabaseInfo(String databaseIds, HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		OperaBean opera = getOpera(request);
		ReturnInfoBean returnInfo = getReturnInfo();
		try {
			out = response.getWriter();
			databaseInfoService.deleteDatabaseInfo(opera, returnInfo, databaseIds.split(","));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(returnInfo.toString());
				out.close();
			}
		}
	}

	@RequestMapping("/jsp/saveDatabaseInfo.do")
	public void saveDatabaseInfo(HttpServletRequest request, HttpServletResponse response, DatabaseInfoBean databaseInfo) {
		PrintWriter out = null;
		OperaBean opera = getOpera(request);
		ReturnInfoBean returnInfo = getReturnInfo();
		databaseInfo.setCreateUserId(opera.getUser().getUserId());
		try {
			out = response.getWriter();
			databaseInfoService.editDatabaseInfo(opera, returnInfo, databaseInfo);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(returnInfo.toString());
				out.close();
			}
		}
	}
	
	@RequestMapping("/jsp/checkConnect.do")
	public void checkConnect(HttpServletRequest request, HttpServletResponse response, DatabaseInfoBean databaseInfo) {
		PrintWriter out = null;
		JSONObject json = new JSONObject();
		try {
			out = response.getWriter();
			json.put("result", databaseInfoService.checkConnect(databaseInfo));
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

