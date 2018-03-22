package com.myreport.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.myreport.bean.DatabaseInfoBean;
import com.myreport.bean.OperaBean;
import com.myreport.bean.ReportInfoBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.constants.Constant;
import com.myreport.dao.QueryReportDao;
import com.myreport.enums.ActionTypeEnum;
import com.myreport.enums.ChartTypeEnum;
import com.myreport.enums.ReturnCodeEnum;
import com.myreport.enums.StatisTypeEnum;
import com.myreport.service.DatabaseInfoService;
import com.myreport.service.QueryReportService;
import com.myreport.service.ReportInfoService;
import com.myreport.utils.DatabaseUtils;
import com.mytools.beans.JxlBean;
import com.mytools.beans.JxlBean.JxlDataBean;
import com.mytools.beans.JxlBean.JxlSheetBean;
import com.mytools.beans.PageBean;
import com.mytools.database.DatabaseAbst;
import com.mytools.utils.ExcelUtil;
import com.mytools.utils.SpringContextUtil;

/**
 * 
 * @author huangping<br />
 * 2013-9-22
 */
@Service
public class QueryReportImpl implements QueryReportService {

	@Resource
	QueryReportDao queryReportDao;
	
	@Resource
	ReportInfoService reportInfoService;
	
	@Resource
	DatabaseInfoService databaseInfoService;

	@Override
	public JSONObject queryDataListForReport(HttpServletRequest request, Integer reportId, PageBean page) throws Exception {
		JSONObject json = new JSONObject();
		String firstQueryList = request.getParameter("firstQueryList");
		if ("1".equals(firstQueryList)) {
			return null;
		}
		ReportInfoBean report = reportInfoService.queryReportInfoByid(reportId);
		DatabaseInfoBean db = databaseInfoService.queryDatabaseInfoById(report.getDatabaseId());
		OperaBean opera = new OperaBean();
		ReturnInfoBean returnInfo = new ReturnInfoBean();
		createDataSource(opera, returnInfo, db);
		String paramKeys = report.getParamKeys();
		Object[] obj = null;
		if (!StringUtils.isEmpty(paramKeys)) {
			JSONArray arr = JSON.parseArray(paramKeys);
			obj = new Object[arr.size()];
			for (int i = 0; i < obj.length; i++) {
				obj[i] = request.getParameter(arr.getString(i));
			}
		}
		List<Map<String, Object>> list = queryReportDao.queryDataListForReport(report.getExecuteSql(), obj, page, report.getDatabaseId(), db.getDatabaseType());
		json.put("rows", list == null ? "[]" : list);
		json.put("total", page.getTotalCount());
		String firstField = "";
		JSONObject tableParams = JSON.parseObject(report.getTableParams());
		JSONArray arr = tableParams.getJSONArray("columns");
		JSONArray arr2 = tableParams.getJSONArray("frozenColumns");
		if (arr2 != null && arr2.size() > 0 && arr2.getJSONArray(0).size() > 0) {
			for (int i = 0; i < arr2.getJSONArray(0).size(); i++) {
				if (i == 0) {
					firstField = arr2.getJSONArray(0).getJSONObject(i).getString("field");
				}
				arr.getJSONArray(0).add(arr2.getJSONArray(0).get(i));
			}
		}
		JSONObject j = null;
		List<String> sumList = new ArrayList<String>();
		List<String> countList = new ArrayList<String>();
		List<String> avgList = new ArrayList<String>();
		Object field = null;
		JSONArray statisTypeArr = null;
		List<Map<String, Object>> tmpList = null;
		List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
		for (int i = 0 ; i < arr.getJSONArray(0).size(); i++) {
			j = arr.getJSONArray(0).getJSONObject(i);
			field = j.get("field");
			if (i == 0 && StringUtils.isEmpty(firstField)) {
				firstField = (String) field;
				continue;
			}
			if (!j.containsKey("statisType")) {
				continue;
			}
			statisTypeArr = j.getJSONArray("statisType");
			if (statisTypeArr == null || statisTypeArr.size() == 0) {
				continue;
			}
			for (int k = 0; k < statisTypeArr.size(); k++) {
				if (StatisTypeEnum.SUM.getValue().equals(statisTypeArr.getString(k))) {
					sumList.add("SUM("+ field +") "+ field);
				} else if (StatisTypeEnum.COUNT.getValue().equals(statisTypeArr.getString(k))) {
					countList.add("COUNT("+ field +") "+ field);
				} else if (StatisTypeEnum.AVG.getValue().equals(statisTypeArr.getString(k))) {
					avgList.add("AVG("+ field +") "+ field);
				}
			}
		}
		if (sumList.size() > 0) {
			//sumList.add(0, firstField + " \"求和\"");
			String sql = "SELECT " + StringUtils.join(sumList, ",") + ", '1' FOOTERROW FROM ("+ report.getExecuteSql() +") STATICSSQL";
			tmpList = queryReportDao.queryDataListForReport(sql, obj, null, report.getDatabaseId(), db.getDatabaseType());
			tmpList.get(0).put(firstField, "求和");
			footerList.addAll(tmpList);
		}
		if (countList.size() > 0) {
			String sql = "SELECT " + StringUtils.join(countList, ",") + ", '1' FOOTERROW FROM ("+ report.getExecuteSql() +") STATICSSQL";
			tmpList = queryReportDao.queryDataListForReport(sql, obj, null, report.getDatabaseId(), db.getDatabaseType());
			tmpList.get(0).put(firstField, "条数");
			footerList.addAll(tmpList);
		}
		if (avgList.size() > 0) {
			String sql = "SELECT " + StringUtils.join(avgList, ",") + ", '1' FOOTERROW FROM ("+ report.getExecuteSql() +") STATICSSQL";
			tmpList = queryReportDao.queryDataListForReport(sql, obj, null, report.getDatabaseId(), db.getDatabaseType());
			tmpList.get(0).put(firstField, "平均值");
			footerList.addAll(tmpList);
		}
		json.put("footer", footerList == null ? "[]" : footerList);
		return json;
	}
	
	@Override
	public void createDataSource(OperaBean opera, ReturnInfoBean returnInfo, DatabaseInfoBean db) throws Exception {
		opera.setOperaType(ActionTypeEnum.MODIFY.toString());
		if (!SpringContextUtil.containsBean(Constant.DATASOURCE_FOR_REPORT + db.getDatabaseId())) {
			//生成数据源
			DatabaseAbst abst = DatabaseUtils.getDatabaseAbst(db.getDatabaseType());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("driverClassName", abst.getDriverClassName());
			map.put("url", abst.getConnectUrl(db.getDatabaseIp(), db.getDatabasePort().intValue(), db.getDatabaseName()));
			map.put("username", db.getUserName());
			map.put("password", db.getPassword());
			map.put("maxActive", 20);
			map.put("maxIdle", 5);
			map.put("initialSize", 5);
			map.put("maxWait", 10000);
			map.put("timeBetweenEvictionRunsMillis", 10000);
			map.put("testWhileIdle", true);
			map.put("numTestsPerEvictionRun", 1);
			map.put("validationQuery", "");
			SpringContextUtil.loadBean(Constant.DATASOURCE_FOR_REPORT + db.getDatabaseId(), BasicDataSource.class, map);
			map = new HashMap<String, Object>();
			map.put("dataSource", SpringContextUtil.getBean(Constant.DATASOURCE_FOR_REPORT + db.getDatabaseId(), DataSource.class));
			SpringContextUtil.loadBean(Constant.JDBCTEMPLATE_FOR_REPORT + db.getDatabaseId(), JdbcTemplate.class, map);
			map.clear();
		}
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}

	@Override
	public JSONObject queryChart(HttpServletRequest request, Integer reportId) throws Exception {
		JSONObject result = new JSONObject();
		String chartType = request.getParameter("chartType");
		String categories = request.getParameter("categories");//横轴
		String series = request.getParameter("seriesInput");//数值
		String legend = request.getParameter("legend");//图例
		String groupName = request.getParameter("groupName");
		ReportInfoBean report = reportInfoService.queryReportInfoByid(reportId);
		DatabaseInfoBean db = databaseInfoService.queryDatabaseInfoById(report.getDatabaseId());
		Object[] obj = null;
		String paramKeys = report.getParamKeys();
		if (!StringUtils.isEmpty(paramKeys)) {
			JSONArray arr = JSON.parseArray(paramKeys);
			obj = new Object[arr.size()];
			for (int i = 0; i < obj.length; i++) {
				obj[i] = request.getParameter(arr.getString(i));
			}
		}
		List<Map<String, Object>> list = queryReportDao.queryDataListForReport(report.getExecuteSql(), obj, null, report.getDatabaseId(), db.getDatabaseType());
		JSONObject title = new JSONObject(), seriesObj = null, animationObj = new JSONObject(), tooltips = new JSONObject(), shadows = new JSONObject();
		JSONArray seriesArr = new JSONArray(), data = null;
		title.put("text", report.getReportName());
		result.put("title", title);
		animationObj.put("duration", 2);
		result.put("animation", animationObj);
		tooltips.put("type", "shared");
		result.put("tooltips", tooltips);
		shadows.put("enabled", true);
		result.put("shadows", shadows);
		if (ChartTypeEnum.COLUMN.getValue().equals(chartType) || ChartTypeEnum.LINE.getValue().equals(chartType)) {
			JSONArray axes = new JSONArray();
			JSONObject axesObj = new JSONObject(), labels = new JSONObject(), crosshairs = new JSONObject(), hLine = new JSONObject(), vLine = new JSONObject();
			hLine.put("strokeStyle", "#cc0a0c");
			vLine.put("strokeStyle", "#cc0a0c");
			crosshairs.put("enabled", true);
			crosshairs.put("hLine", hLine);
			crosshairs.put("vLine", vLine);
			result.put("crosshairs", crosshairs);
			axesObj.put("type", "category");
			axesObj.put("location", "bottom");
			labels.put("angle", 0);
			axesObj.put("labels", labels);
//			JSONArray categoriesArr = new JSONArray();
//			for (Map<String, Object> map : list) {
//				categoriesArr.add(map.get(categories));
//			}
//			axesObj.put("categories", categoriesArr);
			axes.add(axesObj);
			if (StringUtils.isEmpty(groupName)) {
				long min = 0, max = 0, tmp = 0;
				boolean first = true;
				String[] arr = series.split(",");
				JSONArray dataArr = new JSONArray();
				for (String str : arr) {
					labels = new JSONObject();
					labels.put("stringFormat", "%d");
					labels.put("font", "12px sans-serif");
					data = new JSONArray();
					seriesObj = new JSONObject();
					seriesObj.put("type", ChartTypeEnum.getChartTextByValue(chartType));
					seriesObj.put("title", str);
					for (Map<String, Object> map : list) {
						dataArr = new JSONArray();
						tmp = Long.parseLong(String.valueOf(map.get(str)));
						if (first) {
							min = tmp;
							max = tmp;
							first = false;
						} else {
							if (tmp > max) {
								max = tmp;
							}
							if (tmp < min) {
								min = tmp;
							}
						}
						dataArr.add(map.get(categories));
						dataArr.add(map.get(str));
						data.add(dataArr);
					}
					seriesObj.put("labels", labels);
					seriesObj.put("data", data);
					seriesArr.add(seriesObj);
					axesObj = new JSONObject();
					axesObj.put("location", "left");
					if (min % 10 != 0) {
						min = (long) min;
					}
					if (max % 10 != 0) {
						max = ((long) max / 10) * 11;
					}
					if (min == max) {
						axesObj.put("minimum", 0);
						axesObj.put("maximum", max);
						axesObj.put("interval", max / 10);
					} else {
						long t =  (max - min) / 10;
						axesObj.put("minimum", min - t);
						axesObj.put("maximum", max + t);
						axesObj.put("interval", (max - min) / 10);
					}
					axes.add(axesObj);
					result.put("axes", axes);
					result.put("series", seriesArr);
				}
			} else {
				
				result.put("axes", axes);
				seriesArr = getSeries(request, list);
				result.put("series", seriesArr);
				
			}
		} else if (ChartTypeEnum.PIE.getValue().equals(chartType)) {
			seriesObj = new JSONObject();
			seriesObj.put("type", ChartTypeEnum.PIE.toString());
			JSONArray dArr = null;
			data = new JSONArray();
			for (Map<String, Object> map : list) {
				dArr = new JSONArray();
				dArr.add(map.get(legend));
				dArr.add(map.get(series));
				data.add(dArr);
			}
			JSONObject labels = new JSONObject();
			labels.put("stringFormat", "%d%%");
			labels.put("valueType", "percentage");
			labels.put("font", "15px sans-serif");
			labels.put("fillStyle", "white");
			seriesObj.put("labels", labels);
			seriesObj.put("data", data);
			seriesArr.add(seriesObj);
			result.put("series", seriesArr);
		}
		
		result.put("height", "500px");
		return result;
	}

	private JSONArray getSeries(HttpServletRequest request, List<Map<String, Object>> list) {
		JSONArray seriesArr = new JSONArray(), dataArr = null, data = null;
		JSONObject seriesObj = null, labels = null;
		String chartType = request.getParameter("chartType");
		String categories = request.getParameter("categories");//横轴
		String series = request.getParameter("seriesInput");//数值
		String groupName = request.getParameter("groupName");
		String lastName = "", thisName = "";
		Map<String, Object> map = null;
		Set<String> dataSet = new TreeSet<String>();
		for (int i = 0; i < list.size(); i ++) {
			map = list.get(i);
			thisName = (String) map.get(groupName);
			if (!lastName.equals(thisName)) {
				if (i != 0) {
					seriesObj.put("data", dataArr);
					seriesArr.add(seriesObj);
				}
				seriesObj = new JSONObject();
				labels = new JSONObject();
				labels.put("stringFormat", "%d");
				labels.put("font", "12px sans-serif");
				dataArr = new JSONArray();
				seriesObj = new JSONObject();
				seriesObj.put("type", ChartTypeEnum.getChartTextByValue(chartType));
				seriesObj.put("title", thisName);
				seriesObj.put("labels", labels);
				dataArr = new JSONArray();
			}
			data = new JSONArray();
			data.add(map.get(categories));
			data.add(map.get(series));
			dataArr.add(data);
			dataSet.add(String.valueOf(map.get(categories)));
			lastName = thisName;
		}
		seriesObj.put("data", dataArr);
		seriesArr.add(seriesObj);
		
		//处理数据不全的问题
		for (int i = 0; i < seriesArr.size(); i++) {
			JSONArray dataList = seriesArr.getJSONObject(i).getJSONArray("data");
			if (dataList.size() == dataSet.size()) {
				continue;
			}
			seriesArr.getJSONObject(i).put("data", dealData(dataSet, dataList));
		}
		
		return seriesArr;
	}
	
	@SuppressWarnings("unchecked")
	private JSONArray dealData(Set<String> dataSet, JSONArray dataList) {
		Iterator<String> it = dataSet.iterator();
		JSONArray arr = null;
		while (it.hasNext()) {
			String key = it.next();
			if (!hasKey(key, dataList)) {
				arr = new JSONArray();
				arr.add(key);
				arr.add(0);
				dataList.add(arr);
			}
		}
		List<Object> list = new ArrayList<Object>(dataList);
		Collections.sort(list, new Comparator<Object>() {

			@Override
			public int compare(Object o1, Object o2) {
				List<Object> list1 = (List<Object>) o1;
				List<Object> list2 = (List<Object>) o2;
				return ((String) list1.get(0)).compareTo((String) list2.get(0));
			}
			
		});
		return new JSONArray(list);
	}
	
	private boolean hasKey(String key, JSONArray dataList) {
		for (int i = 0; i < dataList.size(); i++) {
			String str = dataList.getJSONArray(i).getString(0);
			if (key.equals(str)) {
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		List<JxlSheetBean> sheetList = new ArrayList<JxlSheetBean>();
		JxlSheetBean sheet = new JxlSheetBean();
		List<JxlDataBean> dataList = new ArrayList<JxlDataBean>();
		sheet.setDataList(dataList.toArray(new JxlDataBean[]{}));
		sheetList.add(sheet);
		//dataList.
	}
	
	@Override
	public void createExcel(HttpServletRequest request, HttpServletResponse response, Integer reportId) throws Exception {
		ReportInfoBean report = reportInfoService.queryReportInfoByid(reportId);
		DatabaseInfoBean db = databaseInfoService.queryDatabaseInfoById(report.getDatabaseId());
		OperaBean opera = new OperaBean();
		ReturnInfoBean returnInfo = new ReturnInfoBean();
		createDataSource(opera, returnInfo, db);
		Object[] obj = null;
		String paramKeys = report.getParamKeys();
		if (!StringUtils.isEmpty(paramKeys)) {
			JSONArray arr = JSON.parseArray(paramKeys);
			obj = new Object[arr.size()];
			for (int i = 0; i < obj.length; i++) {
				obj[i] = request.getParameter(arr.getString(i));
			}
		}
		DatabaseAbst abst = DatabaseUtils.getDatabaseAbst(db.getDatabaseType());
		int maxRow = 65535;
		
		/* 分页查询生成excel开始 */
		List<Map<String, Object>> list = queryReportDao.queryDataListForReport(abst.getTotalCountSql(report.getExecuteSql()), obj, null, report.getDatabaseId(), db.getDatabaseType());
		int totalCount = Integer.parseInt(String.valueOf(list.get(0).get("num")));
		int maxPage = abst.getMaxPage(totalCount, maxRow);
		JSONObject tableParams = JSON.parseObject(report.getTableParams());
		JSONArray columns = tableParams.getJSONArray("columns").getJSONArray(0);
		JSONArray frozenColumns = tableParams.getJSONArray("frozenColumns").getJSONArray(0);
		JSONArray arr = new JSONArray();
		PageBean page = null;
		List<JxlDataBean> dataList = null;
		List<JxlSheetBean> sheetList = new ArrayList<JxlSheetBean>();
		for (int i = 0; i < frozenColumns.size(); i++) {
			arr.add(frozenColumns.getJSONObject(i));
		}
		for (int i = 0; i < columns.size(); i++) {
			arr.add(columns.getJSONObject(i));
		}
		for (int i = 1; i <= maxPage; i++) {
			page = new PageBean();
			page.setPageSize(maxRow);
			page.setCurrentPage(i);
			list = queryReportDao.queryDataListForReport(report.getExecuteSql(), obj, page, report.getDatabaseId(), db.getDatabaseType());
			dataList = new ArrayList<JxlDataBean>();
			for (int j = 0; j < arr.size(); j++) {
				dataList.add(new JxlDataBean(j, 0, arr.getJSONObject(j).getString("title"), getDefaultFormat()));
			}
			for (int j = 0; j < list.size(); j++) {
				for (int k = 0; k < arr.size(); k++) {
					dataList.add(new JxlDataBean(k, j + 1, String.valueOf(list.get(j).get(arr.getJSONObject(k).getString("field")))));
				}
			}
			sheetList.add(new JxlSheetBean(i - 1, "sheet_" + (i - 1), dataList.toArray(new JxlDataBean[]{})));
		}
		ExcelUtil.createExcel(new JxlBean(response.getOutputStream(), sheetList.toArray(new JxlSheetBean[]{})));
		/* 分页查询生成excel结束 */
		
		/* 一次全部查询结果，生成excel开始 */
		/*List<Map<String, Object>> list = queryReportDao.getDataListForReport(report.getExecuteSql(), obj, null, report.getDatabaseId(), db.getDatabaseType());
		JSONObject tableParams = JSONObject.fromObject(report.getTableParams());
		JSONArray columns = tableParams.getJSONArray("columns").getJSONArray(0);
		JSONArray frozenColumns = tableParams.getJSONArray("frozenColumns").getJSONArray(0);
		JSONArray arr = new JSONArray();
		for (int i = 0; i < frozenColumns.size(); i++) {
			arr.add(frozenColumns.getJSONObject(i));
		}
		for (int i = 0; i < columns.size(); i++) {
			arr.add(columns.getJSONObject(i));
		}
		//组装sheet
		List<JxlDataBean> dataList = null;
		List<JxlSheetBean> sheetList = new ArrayList<JxlSheetBean>();
		JxlSheetBean sheet = null;
		int maxRow = 65535;
		if (list != null) {
			int sheetIndex = 0;
			for (int i = 0; i < list.size(); i++) {
				if (i % maxRow == 0) {
					sheetIndex = i / maxRow;
					if (sheet != null) {
						sheet.setDataList(dataList.toArray(new JxlDataBean[]{}));
						sheetList.add(sheet);
					}
					dataList = new ArrayList<JxlDataBean>();
					sheet = new JxlSheetBean(sheetIndex, "sheet_" + sheetIndex);
					for (int j = 0; j < arr.size(); j++) {
						dataList.add(new JxlDataBean(j, 0, arr.getJSONObject(j).getString("title"), getDefaultFormat()));
					}
				}
				for (int j = 0; j < arr.size(); j++) {
					dataList.add(new JxlDataBean(j, i  + 1 - (sheetIndex * maxRow), String.valueOf(list.get(i).get(arr.getJSONObject(j).getString("field")))));
				}
			}
			if (sheet != null) {
				sheet.setDataList(dataList.toArray(new JxlDataBean[]{}));
				sheetList.add(sheet);
			}
		}
		ExcelUtil.createExcel(new JxlBean(response.getOutputStream(), sheetList.toArray(new JxlSheetBean[]{})));*/
		/* 一次全部查询结果，生成excel结束 */
	}

	/**
	 * 获取默认的单元格式
	 * @return
	 * @throws Exception
	 */
	private WritableCellFormat getDefaultFormat() throws Exception {
		WritableFont font = null;
		font = new WritableFont(WritableFont.TAHOMA);
		font.setPointSize(12);
		font.setColour(Colour.BLACK);
		font.setBoldStyle(WritableFont.NO_BOLD);
		font.setItalic(false);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		format.setBackground(Colour.TURQOISE2);
		format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
		format.setWrap(false);
		return format;
	}

	/* (non-Javadoc)
	 * @see com.myreport.service.QueryReportService#getDownData(javax.servlet.http.HttpServletRequest, java.lang.Integer)
	 */
	@Override
	public List<Map<String, Object>> getDownData(HttpServletRequest request, PageBean page) throws Exception {
		String reportId = request.getParameter("MY_REPORT_reportId");
		String field = request.getParameter("MY_REPORT_field");
		ReportInfoBean report = reportInfoService.queryReportInfoByid(Integer.parseInt(reportId));
		DatabaseInfoBean db = databaseInfoService.queryDatabaseInfoById(report.getDatabaseId());
		List<Map<String, Object>> list = null;
		JSONObject columJSON = JSON.parseObject(report.getTableParams());
		String downSQL = "";
		JSONArray arr = columJSON.getJSONArray("columns").getJSONArray(0);
		for (int i = 0; i < arr.size(); i++) {
			if (field.equals(arr.getJSONObject(i).getString("field"))) {
				downSQL = arr.getJSONObject(i).getString("downSQL");
				break;
			}
		}
		if (StringUtils.isEmpty(downSQL)) {
			arr = columJSON.getJSONArray("frozenColumns").getJSONArray(0);
			for (int i = 0; i < arr.size(); i++) {
				if (field.equals(arr.getJSONObject(i).getString("field"))) {
					downSQL = arr.getJSONObject(i).getString("downSQL");
					break;
				}
			}
		}
		JSONObject dealSQL = reportInfoService.resolveSql(downSQL);
		String paramKeys = dealSQL.getString("paramKeys");
		Object[] obj = null;
		if (!StringUtils.isEmpty(paramKeys)) {
			arr = JSON.parseArray(paramKeys);
			obj = new Object[arr.size()];
			for (int i = 0; i < obj.length; i++) {
				obj[i] = request.getParameter(arr.getString(i));
			}
		}
		list = queryReportDao.queryDataListForReport(dealSQL.getString("executeSql"), obj, page, db.getDatabaseId(), db.getDatabaseType());
		return list;
	}
	
}


