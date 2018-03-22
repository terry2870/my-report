<%@page import="com.myreport.enums.ReturnCodeEnum"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="t" uri="/my-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>main</title>
<jsp:include page="/jsp/common/includeJquery.jsp" flush="true" />
</head>
<body class="easyui-layout" fit="true">
	<div region="north" title="查询条件" style="height: 70px;border-style: none">
		<table align="center" border="0" id="databaseInfoSearchTable" cellpadding="0" cellspacing="0" style="margin-top: 7px" width="100%">
			<tr>
				<td width="10%" align="right">数据库类型：</td>
				<td width="10%"><input name="databaseType" id="databaseType" /></td>
				<td width="10%" align="right">数据库标题：</td>
				<td width="20%"><input name="databaseTitle" id="databaseTitle" /></td>
				<td width="10%" align="right">创建日期：</td>
				<td width="25%"><input name="queryStartDate" id="queryStartDate" size="10" class="Wdate" onclick="WdatePicker({readOnly:true})" />&nbsp;-&nbsp;<input name="queryEndDate" id="queryEndDate" size="10" class="Wdate" onclick="WdatePicker({readOnly:true})" /></td>
				<td width="15%"><a id="searchBtn" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
			</tr>
		</table>
	</div>
	<div region="center" id="databaseInfoListDiv">
		<table id="databaseInfoListTable"></table>
	</div>
<script>
	$(function() {
		$("#databaseInfoSearchTable #databaseType").combobox({
			url : '<t:path />/jsp/noFilter.do?method=queryEnumForSelect&className=com.mytools.enums.DatabaseTypeEnum&firstValue=&firstText=' + encodeURI('请选择'),
			width : 100,
			panelHeight : 150,
			editable : false
		});
		
		$("#databaseInfoSearchTable,#databaseInfoSearchTable input[type='text']").keydown(function(e) {
			if (e.keyCode == 13) {
				searchDatabaseInfo();
			}
		});
		function searchDatabaseInfo() {
			$("#databaseInfoListTable").datagrid("load", {
				databaseType : $("#databaseInfoSearchTable #databaseType").combobox("getValue"),
				databaseTitle : $("#databaseInfoSearchTable #databaseTitle").val(),
				queryStartDate : $("#databaseInfoSearchTable #queryStartDate").val(),
				queryEndDate : $("#databaseInfoSearchTable #queryEndDate").val()
			});
		}
		$("#databaseInfoSearchTable #searchBtn").click(function() {
			searchDatabaseInfo();
		});
		$("#databaseInfoListTable").myDatagrid({
			title : "数据库表信息列表",
			fit : true,
			fitColumns : true,
			nowrap : true,
			striped : true,
			url : "<t:path />/jsp/queryAllDatabaseInfo.do",
			columns : [[{
				title : "",
				field : "databaseId",
				width : 100,
				align : "center",
				checkbox : true
			}, {
				title : "数据库标题",
				field : "databaseTitle",
				width : 100,
				align : "center"
			}, {
				title : "数据库类型",
				field : "databaseType",
				width : 100,
				align : "center"
			}, {
				title : "数据库用户名",
				field : "userName",
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
			}]],
			cache : false,
			pagination : true,
			pageSize : 20,
			rownumbers : true,
			idField : "databaseId",
			showFooter : true,
			singleSelect : true,
			toolbar : [{
				id : "addDatabaseInfoBtn",
				disabled : true,
				iconCls : "icon-add",
				text : "新增",
				handler : function() {
					databaseInfoEdit(0);
				}
			}, "-", {
				id : "detailDatabaseInfoBtn",
				disabled : true,
				iconCls : "icon-tip",
				text : "详情",
				handler : function() {
					var data = {};
					var select = $("#databaseInfoListTable").datagrid("getSelections");
					if (!select || select.length == 0) {
						window.top.$.messager.alert("提示", "请选择一行记录！", "error");
						return;
					}
					data = select[0];
					var div = $("<div>").appendTo($(window.top.document.body));
					window.top.$(div).myDialog({
						width : "40%",
						height : "50%",
						title : "数据库表信息详细",
						href : "<t:path />/jsp/reportManage/databaseInfo/databaseInfoEdit.jsp?" + $.param(data),
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
				id : "editDatabaseInfoBtn",
				disabled : true,
				iconCls : "icon-edit",
				text : "修改",
				handler : function() {
					databaseInfoEdit();
				}
			}, "-", {
				id : "delDatabaseInfoBtn",
				disabled : true,
				iconCls : "icon-remove",
				text : "删除",
				handler : function() {
					var select = $("#databaseInfoListTable").datagrid("getSelections");
					if (!select || select.length == 0) {
						window.top.$.messager.alert("提示", "请选择一行记录！", "error");
						return;
					}
					var databaseIdArr = [];
					for (var i = 0; i < select.length; i++) {
						databaseIdArr.push(select[i].databaseId);
					}
					window.top.$.messager.confirm("确认", "您确定要删除该数据库表信息吗？", function(flag) {
						if (flag) {
							window.top.$.messager.progress({
								title : "正在执行",
								msg : "正在执行，请稍后...",
								interval : 500,
								text : ""
							});
							$.ajax({
								url : "<t:path />/jsp/deleteDatabaseInfo.do",
								data : {
									databaseIds : databaseIdArr.join(",")
								},
								type : "POST",
								dataType : "json",
								success : function(data) {
									window.top.$.messager.progress("close");
									if (data.returnCode == "<%=ReturnCodeEnum.CODE_OK.toString()%>") {
										$("#databaseInfoListTable").datagrid("reload");
										window.top.$.messager.show({
											title : "提示",
											msg : "删除数据库表信息成功！"
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
				$("#editDatabaseInfoBtn").linkbutton("disable");
				$("#delDatabaseInfoBtn").linkbutton("disable");
				$("#detailDatabaseInfoBtn").linkbutton("disable");
				$(this).datagrid("clearSelections");
				$(this).datagrid("clearChecked");
				window.top.showAddButton("<t:write name='menuId' />", "#databaseInfoListDiv", parent.document.getElementById("<t:write name='iframeId' />").contentWindow, {
					"addDatabaseInfoBtn" : function() {
						return true;
					},
					"editDatabaseInfoBtn" : function() {
						return false;
					},
					"delDatabaseInfoBtn" : function() {
						return false;
					},
					"detailDatabaseInfoBtn" : function() {
						return false;
					}
				});
			},
			onClickRow : function(rowIndex, rowData) {
				window.top.getPageListButton("<t:write name='menuId' />", "#databaseInfoListDiv", parent.document.getElementById("<t:write name='iframeId' />").contentWindow, {
					"editDatabaseInfoBtn" : function() {
						return true;
					},
					"delDatabaseInfoBtn" : function() {
						return true;
					},
					"detailDatabaseInfoBtn" : function() {
						return true;
					}
				});
			}
		});
	});
	function databaseInfoEdit(flag) {
		var data = {};
		if (flag == 0) {
			data = {
				databaseId : 0
			};
		} else {
			var select = $("#databaseInfoListTable").datagrid("getSelections");
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
		var title = (flag == 0 ? "新增" : "修改") + "数据库表信息";
		window.top.$(div).myDialog({
			width : "40%",
			height : "50%",
			title : title,
			href : "<t:path />/jsp/reportManage/databaseInfo/databaseInfoEdit.jsp?" + $.param(data),
			modal : true,
			collapsible : true,
			cache : false,
			buttons : [{
				text : "测试数据库连接",
				id : "testDatabaseInfoBtn",
				disabled : true,
				iconCls : "icon-search",
				handler : function() {
					window.top.$("#databaseInfoEditForm").form("submit", {
						url : "<t:path />/jsp/checkConnect.do",
						onSubmit : function() {
							if (!window.top.$("#databaseInfoEditForm").form("validate")) {
								return false;
							}
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
							if (data.result == true) {
								window.top.$.messager.alert("提示", "测试成功", "info");
							} else {
								window.top.$.messager.alert("失败", "测试失败", "error");
							}
						}
					});
				}
			}, {
				text : "刷新",
				iconCls : "icon-reload",
				handler : function() {
					window.top.$(div).dialog("refresh");
				}
			}, {
				text : "保存",
				id : "saveDatabaseInfoBtn",
				disabled : true,
				iconCls : "icon-save",
				handler : function() {
					window.top.$("#databaseInfoEditForm").form("submit", {
						url : "<t:path />/jsp/saveDatabaseInfo.do",
						onSubmit : function() {
							if (!window.top.$("#databaseInfoEditForm").form("validate")) {
								return false;
							}
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
								$("#databaseInfoListTable").datagrid("reload");
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
					"saveDatabaseInfoBtn" : function() {
						return true;
					},
					"testDatabaseInfoBtn" : function() {
						return true;
					}
				});
			}
		});
	}
</script>
</body>
</html>

