<%@page import="com.myreport.enums.ReturnCodeEnum"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="t" uri="/my-tags" %>
<!doctype html>
<html>
<head>
<title>main</title>
<jsp:include page="/jsp/common/includeJquery.jsp" flush="true" />
<script src="<t:write name='jsPath' />/jquery/easyui/extenPlugins/datagrid-dnd.js"></script>
</head>
<body class="easyui-layout" fit="true">
	<div region="north" title="查询条件" style="height: 70px;border-style: none">
		<table align="center" id="reportInfoSearchTable" cellpadding="0" cellspacing="0" style="margin-top: 7px" width="100%">
			<tr>
				<td width="10%" align="right">报表名称：</td>
				<td width="10%"><input name="reportName" id="reportName" /></td>
				<td width="10%" align="right">状态：</td>
				<td width="20%"><input name="status" id="status" /></td>
				<td width="10" align="right">查询日期：</td>
				<td width="25%"><input name="queryStartDate" id="queryStartDate" size="10" class="Wdate" onclick="WdatePicker({readOnly:true})" />&nbsp;-&nbsp;<input name="queryEndDate" id="queryEndDate" size="10" class="Wdate" onclick="WdatePicker({readOnly:true})" /></td>
				<td width="15%"><a id="searchBtn" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
			</tr>
			<tr>
				
			</tr>
		</table>
	</div>
	<div region="center" id="reportInfoListDiv">
		<table id="reportInfoListTable"></table>
	</div>
<script>
	$(function() {
		$("#reportInfoSearchTable #status").combobox({
			url : '<t:path />/jsp/noFilter.do',
			queryParams : {
				method : "queryEnumForSelect",
				className : "StatusEnum",
				firstValue : "",
				firstText : "请选择"
			},
			width : 100,
			panelHeight : 100,
			editable : false
		});
		$("#reportInfoSearchTable,#reportInfoSearchTable input[type='text']").keydown(function(e) {
			if (e.keyCode == 13) {
				searchReportInfo();
			}
		});
		function searchReportInfo() {
			$("#reportInfoListTable").datagrid("load", {
				reportName : $("#reportInfoSearchTable #reportName").val(),
				status : $("#reportInfoSearchTable #status").combobox("getValue"),
				queryStartDate : $("#reportInfoSearchTable #queryStartDate").val(),
				queryEndDate : $("#reportInfoSearchTable #queryEndDate").val()
			});
		}
		$("#reportInfoSearchTable #searchBtn").click(function() {
			searchReportInfo();
		});
		$("#reportInfoListTable").myDatagrid({
			title : "报表列表",
			fit : true,
			fitColumns : true,
			nowrap : true,
			striped : true,
			url : "<t:path />/jsp/queryAllReportInfo.do",
			columns : [[{
				title : "",
				field : "reportId",
				width : 100,
				align : "center",
				checkbox : true
			}, {
				title : "报表名称",
				field : "reportName",
				width : 100,
				align : "center"
			}, {
				title : "状态",
				field : "statusName",
				width : 100,
				align : "center"
			}, {
				title : "创建者",
				field : "createUserName",
				width : 100,
				align : "center"
			}, {
				title : "创建日期",
				field : "createDate",
				width : 100,
				align : "center"
			}, {
				title : "所属分组",
				field : "groupId",
				width : 100,
				align : "center"
			}, {
				title : "数据库ID",
				field : "databaseId",
				width : 100,
				align : "center"
			}]],
			cache : false,
			pagination : true,
			pageSize : 20,
			rownumbers : true,
			idField : "reportId",
			showFooter : true,
			singleSelect : true,
			toolbar : [{
				id : "addReportInfoBtn",
				disabled : true,
				iconCls : "icon-add",
				text : "新增",
				handler : function() {
					reportInfoEdit(0);
				}
			}, "-", {
				id : "detailReportInfoBtn",
				disabled : true,
				iconCls : "icon-tip",
				text : "详情",
				handler : function() {
					var data = {};
					var select = $("#reportInfoListTable").datagrid("getSelections");
					if (!select || select.length == 0) {
						window.top.$.messager.alert("提示", "请选择一行记录！", "error");
						return;
					}
					data = select[0];
					var div = $("<div>").appendTo($(window.top.document.body));
					window.top.$(div).myDialog({
						width : "65%",
						height : "95%",
						title : "报表详细",
						href : "<t:path />/jsp/reportManage/reportInfo/reportInfoEdit.jsp?reportId=" + data.reportId,
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
			}, "-", {
				id : "editReportInfoBtn",
				disabled : true,
				iconCls : "icon-edit",
				text : "修改",
				handler : function() {
					reportInfoEdit();
				}
			}, "-", {
				id : "delReportInfoBtn",
				disabled : true,
				iconCls : "icon-remove",
				text : "删除",
				handler : function() {
					var select = $("#reportInfoListTable").datagrid("getSelections");
					if (!select || select.length == 0) {
						window.top.$.messager.alert("提示", "请选择一行记录！", "error");
						return;
					}
					var reportIdArr = [];
					for (var i = 0; i < select.length; i++) {
						reportIdArr.push(select[i].reportId);
					}
					window.top.$.messager.confirm("确认", "您确定要删除该报表吗？", function(flag) {
						if (flag) {
							window.top.$.messager.progress({
								title : "正在执行",
								msg : "正在执行，请稍后...",
								interval : 500,
								text : ""
							});
							$.ajax({
								url : "<t:path />/jsp/deleteReportInfo.do",
								data : {
									reportIds : reportIdArr.join(",")
								},
								type : "POST",
								dataType : "json",
								success : function(data) {
									window.top.$.messager.progress("close");
									if (data.returnCode == "<%=ReturnCodeEnum.CODE_OK.toString()%>") {
										$("#reportInfoListTable").datagrid("reload");
										window.top.$.messager.show({
											title : "提示",
											msg : "删除报表成功！"
										});
									} else {
										window.top.$.messager.alert("失败", data.returnInfo, "error");
									}
								}
							});
						}
					});
				}
			}],
			onLoadSuccess : function() {
				$("#editReportInfoBtn").linkbutton("disable");
				$("#delReportInfoBtn").linkbutton("disable");
				$("#detailReportInfoBtn").linkbutton("disable");
				$(this).datagrid("clearSelections");
				$(this).datagrid("clearChecked");
				window.top.showAddButton("<t:write name='menuId' />", "#reportInfoListDiv", parent.document.getElementById("<t:write name='iframeId' />").contentWindow, {
					"addReportInfoBtn" : function() {
						return true;
					},
					"editReportInfoBtn" : function() {
						return false;
					},
					"delReportInfoBtn" : function() {
						return false;
					},
					"detailReportInfoBtn" : function() {
						return false;
					}
				});
			},
			onClickRow : function(rowIndex, rowData) {
				window.top.getPageListButton("<t:write name='menuId' />", "#reportInfoListDiv", parent.document.getElementById("<t:write name='iframeId' />").contentWindow, {
					"editReportInfoBtn" : function() {
						return true;
					},
					"delReportInfoBtn" : function() {
						return true;
					},
					"detailReportInfoBtn" : function() {
						return true;
					}
				});
			}
		});
	});
	function reportInfoEdit(flag) {
		var data = {};
		if (flag == 0) {
			data = {
				reportId : 0
			};
		} else {
			var select = $("#reportInfoListTable").datagrid("getSelections");
			if (!select || select.length == 0) {
				window.top.$.messager.alert("提示", "请选择一行记录！", "error");
				return;
			} else if (select.length > 1) {
				window.top.$.messager.alert("提示", "只能选择一行记录！", "error");
				return;
			}
			data = select[0];
		}
		var div = $("<div>").appendTo($(window.top.document.body));
		var title = (flag == 0 ? "新增" : "修改") + "报表";
		window.top.$(div).myDialog({
			width : "65%",
			height : "95%",
			title : title,
			href : "<t:path />/jsp/reportManage/reportInfo/reportInfoEdit.jsp?reportId=" + data.reportId,
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
				text : "检查sql",
				iconCls : "icon-ok",
				handler : function() {
					var databaseId = window.top.$("#reportInfoEditForm #databaseId").combobox("getValue");
					if (!databaseId) {
						window.top.$.messager.alert("失败", "请选择数据库！", "error");
						return;
					}
					var originalSql = window.top.$("#reportInfoEditForm #originalSql").val();
					if (!originalSql) {
						window.top.$.messager.alert("失败", "请输入sql语句！", "error");
						return;
					}
					var executeSql = window.top.$("#reportInfoEditForm #executeSql").val();
					if (!executeSql) {
						window.top.$.messager.alert("失败", "请先解析sql语句！", "error");
						return;
					}
					window.top.$.messager.progress({
						title : "正在执行",
						msg : "正在执行，请稍后...",
						interval : 500,
						text : ""
					});
					$.post("<t:path />/jsp/testSql.do", {
						databaseId : databaseId,
						sql : window.top.$("#reportInfoEditForm #executeSql").val(),
						paramKeys : window.top.$("#reportInfoEditForm #paramKeys").val()
					}, function(json) {
						window.top.$.messager.progress("close");
						if (json.returnCode == "<%=ReturnCodeEnum.CODE_OK.toString()%>") {
							window.top.$.messager.alert("提示", "测试成功", "info");
						} else {
							window.top.$.messager.alert("失败", json.returnInfo, "error");
						}
					});
				}
			}, {
				text : "解析sql",
				iconCls : "icon-reload",
				handler : function() {
					if (window.top.$("#reportInfoEditForm #originalSql").val() == "") {
						window.top.$.messager.alert("提示", "请输入sql！", "error");
						return;
					}
					window.top.$.messager.progress({
						title : "正在执行",
						msg : "正在执行，请稍后...",
						interval : 500,
						text : ""
					});
					$.post("<t:path />/jsp/dealSql.do", {
						sql : window.top.$("#reportInfoEditForm #originalSql").val()
					}, function(json) {
						window.top.$.messager.progress("close");
						if (json.returnCode == "<%=ReturnCodeEnum.CODE_OK.toString()%>") {
							window.top.$("#reportInfoEditForm #executeSql").val(json.returnObj.executeSql);
							window.top.$("#reportInfoEditForm #paramsTable").datagrid("loadData", json.returnObj.params);
							window.top.$("#reportInfoEditForm #paramKeys").val($.toJSON(json.returnObj.paramKeys));
							window.top.$.messager.alert("提示", "解析成功", "info");
						} else {
							window.top.$.messager.alert("失败", json.returnInfo, "error");
						}
					});
				}
			}, {
				text : "保存",
				id : "saveReportInfoBtn",
				disabled : true,
				iconCls : "icon-save",
				handler : function() {
					window.top.$("#reportInfoEditForm").form("submit", {
						url : "<t:path />/jsp/saveReportInfo.do",
						onSubmit : function() {
							if (!window.top.$("#reportInfoEditForm").form("validate")) {
								return false;
							}
							if (window.top.$("#reportInfoEditForm #executeSql").val() == "") {
								window.top.$.messager.alert("失败", "请先解析sql", "error");
								return false;
							}
							var options = window.top.$("#reportInfoEditForm #tableColumnsTable").datagrid("options");
							var tableParams = {
								//fitColumns : window.top.$("#reportInfoEditForm #fitColumns").combobox("getValue"),
								columns : options.columns,
								frozenColumns : options.frozenColumns
							};
							var queryConditionsRows = window.top.$("#reportInfoEditForm #paramsTable").datagrid("getRows");
							window.top.$("#reportInfoEditForm #queryConditions").val($.toJSON(queryConditionsRows));
							window.top.$("#reportInfoEditForm #tableParams").val($.toJSON(tableParams));
							window.top.$.messager.progress({
								title : "正在执行",
								msg : "正在执行，请稍后...",
								interval : 500,
								text : ""
							});
							return true;
						},
						success : function(data) {
							window.top.$.messager.progress("close");
							data = $.parseJSON(data);
							if (data.returnCode == "<%=ReturnCodeEnum.CODE_OK.toString()%>") {
								window.top.$(div).dialog("close");
								$("#reportInfoListTable").datagrid("reload");
								window.top.$.messager.show({
									title : "提示",
									msg : title + "成功！"
								});
							} else {
								window.top.$.messager.alert("失败", data.returnInfo, "error");
							}
						}
					});
				}
			}, {
				text : "关闭",
				iconCls : "icon-cancel",
				handler : function() {
					window.top.$(div).dialog("close");
				}
			}],
			onLoad : function() {
				window.top.getEditPageButton("<t:write name='menuId' />", {
					"saveReportInfoBtn" : function() {
						return true;
					}
				});
			}
		});
	}
</script>
</body>
</html>

