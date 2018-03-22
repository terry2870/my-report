<%@page import="com.myreport.enums.ChartTypeEnum"%>
<%@page import="com.myreport.enums.FieldComboDataTypeEnum"%>
<%@page import="com.myreport.enums.YesOrNoEnum"%>
<%@page import="com.myreport.enums.FieldTypeEnum"%>
<%@page import="com.myreport.enums.ReturnCodeEnum"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="t" uri="/my-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>main</title>
<jsp:include page="/jsp/common/includeJquery.jsp" flush="true" />
<script src="<t:path/>/jquery/jquery.jqChart.min.js"></script>
<link rel="stylesheet" type="text/css" href="<t:path/>/css/jquery.jqChart.css" />
<!--[if IE]><script src="<t:path/>/jquery/excanvas.js"></script><![endif]-->
</head>
<body>
	<div region="north" title="查询条件" style="border-style: none">
		<form id="searchForm">
			<table align="center" id="searchTable" cellpadding="0" cellspacing="0" style="margin-top: 7px" width="100%">
				
			</table>
		</form>
	</div>
	<div region="center" id="listDiv">
		<div id="listTabs">
			<div title="列表显示">
				<table id="listTable"></table>
			</div>
			<div title="图表显示">
				<div class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north'" style="height:35px" id="listChartSearch">
						<table>
							<tr>
								<td>图表类型：</td>
								<td><input id="chartType" /></td>
								<td id="categoriesLabel">横坐标：</td>
								<td id="categoriesTr"><input id="categories" /></td>
								<td id="legendLabel">图例：</td>
								<td id="legendTr"><input id="legend" /></td>
								<td id="seriesLabel">显示指标：</td>
								<td id="seriesTr"><input id="seriesInput" /></td>
								<td id="groupLabel">分组字段：</td>
								<td id="groupText"><input id="groupName" /></td>
								<td><a id="showChartBtn" /></td>
							</tr>
						</table>
					</div>
					<div data-options="region:'center'" style="overflow: auto">
						<div id="listChartDiv" style="overflow: auto;"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
<script>
	$(function() {
		$("#listChartSearch #groupLabel,#listChartSearch #groupText").hide();
		$("#searchForm").form();
		var columnNumForRow = 4;
		$.post("<t:path/>/jsp/queryReportInfoById.do", {
			reportId : <t:write name="reportId" />
		}, function(reportInfo) {
			var condition = $.parseJSON(reportInfo.queryConditions);
			var n = condition.length / columnNumForRow;
			var hei = 0;
			if (condition.length % columnNumForRow == 0) {
				hei = n;
			} else {
				hei = n + 1;
			}
			var h = 30 + hei * 45;
			if (h < 75) {
				h = 75
			}
			$("div[region='north']").css("height", h);
			$("body").layout({
				fit : true
			});
			var td = null, tr = null, input = null, searchTable = $("#searchTable");
			if (condition.length > 0) {
				var item = null;
				for (var i = 0; i < condition.length; i++) {
					item = condition[i];
					if (i == 0 || i % columnNumForRow == 0) {
						tr = $("<tr>").css("line-height", 3).appendTo(searchTable);
					}
					tr.append($("<td align='right'>").text(item.fieldLabel + "："));
					td = $("<td>").appendTo(tr);
					input = $("<input type='text'>").attr({
						name : item.fieldName,
						id : item.fieldName
					}).appendTo(td);
					switch (item.fieldType) {
					case "<%=FieldTypeEnum.TEXT.getValue()%>":
						input.validatebox({
							required : item.fieldRequired == "<%=YesOrNoEnum.YES.getValue()%>"
						});
						if (item.fieldDefaultValue) {
							input.val(item.fieldDefaultValue);
						}
						break;
					case "<%=FieldTypeEnum.NUMBER.getValue()%>":
						input.numberbox({
							required : item.fieldRequired == "<%=YesOrNoEnum.YES.getValue()%>",
							value : item.fieldDefaultValue
						});
						break;
					case "<%=FieldTypeEnum.COMBO.getValue()%>":
						if (!item.fieldComboData) {
							break;
						}
						var d = item.fieldComboData.split(",");
						if (!d || d.length <= 0) {
							break;
						}
						var e = null, arr = [];
						if (item.fieldComboDataType == "<%=FieldComboDataTypeEnum.SELF.getValue()%>") {
							for (var j = 0; j < d.length; j++) {
								e = d[j].split(":");
								arr.push({
									text : e[1],
									value : e[0]
								});
							}
							input.combobox({
								required : item.fieldRequired == "<%=YesOrNoEnum.YES.getValue()%>",
								value : item.fieldDefaultValue,
								data : arr,
								editable : item.fieldEditAble == "<%=YesOrNoEnum.YES.getValue()%>"
							});
						} else if (item.fieldComboDataType == "<%=FieldComboDataTypeEnum.SQL_RESULT.getValue()%>") {
							input.combobox({
								required : item.fieldRequired == "<%=YesOrNoEnum.YES.getValue()%>",
								value : item.fieldDefaultValue,
								url : "<t:path/>/jsp/noFilter.do?method=queryDataList&sql=" + item.fieldComboData,
								editable : item.fieldEditAble == "<%=YesOrNoEnum.YES.getValue()%>"
							});
						}
						break;
					case "<%=FieldTypeEnum.DATE.getValue()%>":
						input.data("thisData", item).attr("class", "Wdate").click(function() {
							WdatePicker({
								readOnly : true,
								dateFmt : $(this).data("thisData").fieldDateFormatter
							});
						}).validatebox({
							required : item.fieldRequired == "<%=YesOrNoEnum.YES.getValue()%>"
						});
						break;
					default:
						break;
					}
				}
				td.append("&nbsp;&nbsp;").append($("<a>").linkbutton({
					text : "查询",
					iconCls : "icon-search",
					onClick : function() {
						if (!$("#searchForm").form("validate")) {
							return;
						}
						var obj = {
							reportId : reportInfo.reportId
						};
						for (var i = 0; i < condition.length; i++) {
							if (condition[i].fieldType == "<%=FieldTypeEnum.TEXT.getValue()%>") {
								obj[condition[i].fieldName] = $("#searchTable #" + condition[i].fieldName).val();
							} else if (condition[i].fieldType == "<%=FieldTypeEnum.NUMBER.getValue()%>") {
								obj[condition[i].fieldName] = $("#searchTable #" + condition[i].fieldName).val();
							} else if (condition[i].fieldType == "<%=FieldTypeEnum.COMBO.getValue()%>") {
								obj[condition[i].fieldName] = $("#searchTable #" + condition[i].fieldName).combobox("getValue");
							} else if (condition[i].fieldType == "<%=FieldTypeEnum.DATE.getValue()%>") {
								obj[condition[i].fieldName] = $("#searchTable #" + condition[i].fieldName).val();
							}
						}
						$("#listTable").datagrid("load", obj);
					}
				})).append("&nbsp;&nbsp;").append($("<a>").linkbutton({
					text : "导出",
					iconCls : "icon-save",
					onClick : function() {
						if (!$("#searchForm").form("validate")) {
							return;
						}
						var obj = {
							reportId : reportInfo.reportId,
							fileName : reportInfo.reportName
						};
						for (var i = 0; i < condition.length; i++) {
							if (condition[i].fieldType == "<%=FieldTypeEnum.TEXT.getValue()%>") {
								obj[condition[i].fieldName] = $("#searchTable #" + condition[i].fieldName).val();
							} else if (condition[i].fieldType == "<%=FieldTypeEnum.NUMBER.getValue()%>") {
								obj[condition[i].fieldName] = $("#searchTable #" + condition[i].fieldName).val();
							} else if (condition[i].fieldType == "<%=FieldTypeEnum.COMBO.getValue()%>") {
								obj[condition[i].fieldName] = $("#searchTable #" + condition[i].fieldName).combobox("getValue");
							} else if (condition[i].fieldType == "<%=FieldTypeEnum.DATE.getValue()%>") {
								obj[condition[i].fieldName] = $("#searchTable #" + condition[i].fieldName).val();
							}
						}
						window.location.href = "<t:path/>/jsp/createExcel.do?" + encodeURI($.param(obj));
					}
				}));
			} else {
				tr = $("<tr>").css("line-height", 3).appendTo(searchTable);
				td = $("<td>").appendTo(tr).append($("<a>").linkbutton({
					text : "查询",
					iconCls : "icon-search",
					onClick : function() {
						$("#listTable").datagrid("load", {
							reportId : reportInfo.reportId
						});
					}
				})).append("&nbsp;&nbsp;").append($("<a>").linkbutton({
					text : "导出",
					iconCls : "icon-save",
					onClick : function() {
						if (!$("#searchForm").form("validate")) {
							return;
						}
						var obj = {
							reportId : reportInfo.reportId,
							fileName : reportInfo.reportName
						};
						for (var i = 0; i < condition.length; i++) {
							if (condition[i].fieldType == "<%=FieldTypeEnum.TEXT.getValue()%>") {
								obj[condition[i].fieldName] = $("#searchTable #" + condition[i].fieldName).val();
							} else if (condition[i].fieldType == "<%=FieldTypeEnum.NUMBER.getValue()%>") {
								obj[condition[i].fieldName] = $("#searchTable #" + condition[i].fieldName).val();
							} else if (condition[i].fieldType == "<%=FieldTypeEnum.COMBO.getValue()%>") {
								obj[condition[i].fieldName] = $("#searchTable #" + condition[i].fieldName).combobox("getValue");
							} else if (condition[i].fieldType == "<%=FieldTypeEnum.DATE.getValue()%>") {
								obj[condition[i].fieldName] = $("#searchTable #" + condition[i].fieldName).val();
							}
						}
						window.location.href = "<t:path/>/jsp/createExcel.do?" + encodeURI($.param(obj));
					}
				}));
			}
			$("#listTabs").tabs({
				fit : true,
				plain : true
			});			
			var tableParams = $.parseJSON(reportInfo.tableParams);
			var columns = tableParams.columns, frozenColumns = tableParams.frozenColumns;
			for (var i = 0; i < columns[0].length; i++) {
				columns[0][i].formatter = function(value, rowData, rowIndex) {
					if (value == undefined) {
						return "";
					}
					if ($.isNumeric(value) && value > 0 && this.downAble == "<%=YesOrNoEnum.YES.getValue()%>" && rowData.FOOTERROW != "1") {
						return "<a href='javascript:down("+ reportInfo.reportId +", \""+ this.field +"\", "+ $.toJSON(rowData) +");'>"+ value +"</a>";
					} else {
						return "<span title='"+ value +"'>"+ value +"</span>";
					}
				};
			}
			for (var i = 0; i < frozenColumns[0].length; i++) {
				frozenColumns[0][i].formatter = function(value, rowData, rowIndex) {
					if (value) {
						return "<span title='"+ value +"'>"+ value +"</span>";
					}
					return "";
				};
			}
			$("#listTable").myDatagrid({
				title : "报表列表",
				fit : true,
				fitColumns : false,
				nowrap : true,
				striped : true,
				url : "<t:path/>/jsp/queryDataListForReport.do",
				queryParams : {
					firstQueryList : 1
				},
				frozenColumns : frozenColumns,
				columns : columns,
				cache : false,
				pagination : true,
				pageSize : 20,
				rownumbers : true,
				singleSelect : true,
				showFooter : true,
				toolbar : [{
					iconCls : "icon-add",
					text : "新增",
					handler : function() {
						var data = $("#listTable").datagrid("getRows");
						alert($.toJSON(data[0]));
					}
				}]
			});
			$("#chartType").combobox({
				url : "<t:path />/jsp/noFilter.do?method=queryEnumForSelect&className=ChartTypeEnum",
				width : 150,
				panelHeight : 150,
				editable : false,
				required : true,
				onSelect : function(record) {
					if (record.value == "<%=ChartTypeEnum.COLUMN.getValue()%>" || record.value == "<%=ChartTypeEnum.LINE.getValue()%>") {
						$("#categoriesTr,#categoriesLabel,#seriesTr,#seriesLabel").show();
						$("#legendTr,#legendLabel").hide();
						$("#listChartSearch #groupLabel,#listChartSearch #groupText").show();
					} else {
						$("#legendTr,#legendLabel,#seriesTr,#seriesLabel").show();
						$("#categoriesTr,#categoriesLabel").hide();
						$("#listChartSearch #groupLabel,#listChartSearch #groupText").hide();
					}
					var options = $("#listTable").myDatagrid("options");
					var column = options.columns;
					var frozen = options.frozenColumns;
					var data = [];
					if (frozen && frozen.length > 0 && frozen[0].length > 0) {
						for (var i = 0; i < frozen[0].length; i++) {
							data.push({
								text : frozen[0][i].title,
								value : frozen[0][i].field
							});
						}
					}
					if (column && column.length > 0 && column[0].length > 0) {
						for (var i = 0; i < column[0].length; i++) {
							data.push({
								text : column[0][i].title,
								value : column[0][i].field
							});
						}
					}
					$("#categories").combobox({
						editable : false,
						required : true,
						width : 150,
						data : data
					});
					$("#seriesInput").combobox({
						editable : false,
						required : true,
						data : data,
						width : 150,
						multiple : record.value != "<%=ChartTypeEnum.PIE.getValue()%>"
					});
					$("#legend").combobox({
						editable : false,
						required : true,
						data : data,
						width : 150
					});
					$("#listChartSearch #groupName").combobox({
						editable : true,
						required : false,
						width : 150,
						data : data
					});
				}
			});
			$("#categoriesTr,#categories,#categoriesLabel,#seriesTr,#seriesInput,#seriesLabel,#legendTr,#legend,#legendLabel").hide();
			$("#showChartBtn").linkbutton({
				text : "显示图表",
				iconCls : "icon-search",
				onClick : function() {
					var chartType = $("#chartType").combobox("getValue");
					var categories = $("#categories").combobox("getValue");
					var seriesInput = $("#seriesInput").combobox("getValues");
					var legend = $("#legend").combobox("getValue");
					if (chartType == "") {
						window.top.$.messager.alert("错误", "请选择图表类型", "error");
						return;
					}
					if (chartType == "<%=ChartTypeEnum.COLUMN.getValue()%>" || chartType == "<%=ChartTypeEnum.LINE.getValue()%>") {
						if (categories == "") {
							window.top.$.messager.alert("错误", "请选择横坐标", "error");
							return;
						}
					} else {
						if (legend == "") {
							window.top.$.messager.alert("错误", "请选择图例", "error");
							return;
						}
					}
					if (seriesInput == "") {
						window.top.$.messager.alert("错误", "请选择显示指标", "error");
						return;
					}
					if (!$("#searchForm").form("validate")) {
						return;
					}
					var obj = {
						reportId : reportInfo.reportId,
						chartType : chartType,
						categories : categories,
						seriesInput : seriesInput.join(","),
						legend : legend,
						groupName : $("#groupName").combobox("getValue")
					};
					for (var i = 0; i < condition.length; i++) {
						if (condition[i].fieldType == "<%=FieldTypeEnum.TEXT.getValue()%>") {
							obj[condition[i].fieldName] = $("#searchTable #" + condition[i].fieldName).val();
						} else if (condition[i].fieldType == "<%=FieldTypeEnum.NUMBER.getValue()%>") {
							obj[condition[i].fieldName] = $("#searchTable #" + condition[i].fieldName).val();
						} else if (condition[i].fieldType == "<%=FieldTypeEnum.COMBO.getValue()%>") {
							obj[condition[i].fieldName] = $("#searchTable #" + condition[i].fieldName).combobox("getValue");
						} else if (condition[i].fieldType == "<%=FieldTypeEnum.DATE.getValue()%>") {
							obj[condition[i].fieldName] = $("#searchTable #" + condition[i].fieldName).val();
						}
					}
					window.top.$.messager.progress({
						title : "正在执行",
						msg : "正在执行，请稍后...",
						interval : 500,
						text : ""
					});
					$.post("<t:path/>/jsp/queryChart.do", obj, function(json) {
						window.top.$.messager.progress("close");
						$("#listChartDiv").jqChart(json);
					});
				}
			});
		});
	});
	function down(reportId, field, rowData) {
		var div = $("<div>").appendTo($(window.top.document.body));
		window.top.$(div).myDialog({
			width : 800,
			height : 500,
			title : "下转详情",
			href : "<t:path />/jsp/index/downData.jsp",
			queryParams : {
				MY_REPORT_reportId : reportId,
				MY_REPORT_field : field,
				rowData : $.toJSON(rowData)
			},
			modal : true,
			collapsible : true,
			cache : false,
			buttons : [{
				text : "刷新",
				iconCls : "icon-reload",
				handler : function() {
					window.top.$(div).dialog("refresh");
				}
			}, {
				text : "关闭",
				iconCls : "icon-cancel",
				handler : function() {
					window.top.$(div).dialog("close");
				}
			}]
		});
	}
</script>
</body>
</html>

