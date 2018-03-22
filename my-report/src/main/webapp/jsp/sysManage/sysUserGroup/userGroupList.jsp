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
		<table align="center" id="userGroupSearchTable" cellpadding="0" cellspacing="0" style="margin-top: 7px" width="100%">
			<tr>
				<td width="10%" align="right">用户组名称：</td>
				<td width="20%"><input name="groupName" id="groupName" /></td>
				<td width="10%" align="right">状态：</td>
				<td width="20%"><input name="status" id="status" /></td>
				<td><a id="searchBtn" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
			</tr>
		</table>
	</div>
	<div region="center" id="userGroupListDiv">
		<table id="userGroupListTable"></table>
	</div>
<script>
	$(function() {
		$("#userGroupSearchTable #status").combobox({
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
		$("#userGroupSearchTable,#userGroupSearchTable input[type='text']").keydown(function(e) {
			if (e.keyCode == 13) {
				searchUserGroup();
			}
		});
		function searchUserGroup() {
			$("#userGroupListTable").datagrid("load", {
				groupName : $("#userGroupSearchTable #groupName").val(),
				status : $("#userGroupSearchTable #status").combobox("getValue")
			});
		}
		$("#userGroupSearchTable #searchBtn").click(function() {
			searchUserGroup();
		});
		$("#userGroupListTable").myDatagrid({
			title : "用户组表列表",
			fit : true,
			fitColumns : true,
			nowrap : true,
			striped : true,
			url : "<t:path />/jsp/queryAllSysUserGroup.do",
			columns : [[{
				title : "",
				field : "groupId",
				width : 100,
				align : "center",
				checkbox : true
			}, {
				title : "用户组名称",
				field : "groupName",
				width : 100,
				align : "center"
			}, {
				title : "用户组描述",
				field : "groupInfo",
				width : 100,
				align : "center"
			}, {
				title : "创建者ID",
				field : "createUserName",
				width : 100,
				align : "center"
			}, {
				title : "创建日期",
				field : "createDate",
				width : 100,
				align : "center"
			}, {
				title : "状态",
				field : "statusName",
				width : 100,
				align : "center"
			}]],
			cache : false,
			pagination : true,
			pageSize : 20,
			rownumbers : true,
			idField : "groupId",
			showFooter : true,
			singleSelect : true,
			toolbar : [{
				id : "addUserGroupBtn",
				disabled : true,
				iconCls : "icon-add",
				text : "新增",
				handler : function() {
					userGroupEdit(0);
				}
			}, "-", {
				id : "detailUserGroupBtn",
				disabled : true,
				iconCls : "icon-tip",
				text : "详情",
				handler : function() {
					var data = {};
					var select = $("#userGroupListTable").datagrid("getSelections");
					if (!select || select.length == 0) {
						window.top.$.messager.alert("提示", "请选择一行记录！", "error");
						return;
					}
					data = select[0];
					var div = $("<div>").appendTo($(window.top.document.body));
					window.top.$(div).myDialog({
						width : 400,
						height : 200,
						title : "用户组表详细",
						href : "<t:path />/jsp/sysManage/sysUserGroup/userGroupEdit.jsp",
						method : "post",
						queryParams : data,
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
				id : "editUserGroupBtn",
				disabled : true,
				iconCls : "icon-edit",
				text : "修改",
				handler : function() {
					userGroupEdit();
				}
			}, "-", {
				id : "delUserGroupBtn",
				disabled : true,
				iconCls : "icon-remove",
				text : "删除",
				handler : function() {
					var select = $("#userGroupListTable").datagrid("getSelections");
					if (!select || select.length == 0) {
						window.top.$.messager.alert("提示", "请选择一行记录！", "error");
						return;
					}
					var groupIdArr = [];
					for (var i = 0; i < select.length; i++) {
						groupIdArr.push(select[i].groupId);
					}
					window.top.$.messager.confirm("确认", "您确定要删除该用户组表吗？", function(flag) {
						if (flag) {
							window.top.$.messager.progress({
								title : "正在执行",
								msg : "正在执行，请稍后...",
								interval : 500,
								text : ""
							});
							$.ajax({
								url : "<t:path />/jsp/deleteSysUserGroup.do",
								data : {
									groupIds : groupIdArr.join(",")
								},
								type : "POST",
								dataType : "json",
								success : function(data) {
									window.top.$.messager.progress("close");
									if (data.returnCode == "<%=ReturnCodeEnum.CODE_OK.toString()%>") {
										$("#userGroupListTable").datagrid("reload");
										window.top.$.messager.show({
											title : "提示",
											msg : "删除用户组表成功！"
										});
									} else {
										window.top.$.messager.alert("失败", data.returnInfo, "error");
									}
								}
							});
						}
					});
				}
			}, "-", {
				id : "configUserGroupUserBtn",
				disabled : true,
				iconCls : "icon-edit",
				text : "关联用户",
				handler : function() {
					var data = {};
					var select = $("#userGroupListTable").datagrid("getSelections");
					if (!select || select.length == 0) {
						window.top.$.messager.alert("提示", "请选择一行记录！", "error");
						return;
					} else if (select.length > 1) {
						window.top.$.messager.alert("提示", "只能选择一行记录！", "error");
						return;
					}
					data = select[0];
					var div = $("<div>").appendTo($(window.top.document.body));
					var title = "关联用户";
					window.top.$(div).myDialog({
						width : 700,
						height : 500,
						title : title,
						href : "<t:path />/jsp/sysManage/sysUserGroup/userGroupUser.jsp",
						method : "post",
						queryParams : {
							groupId : data.groupId
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
							text : "保存",
							id : "saveConfigUserGroupUserBtn",
							disabled : true,
							iconCls : "icon-save",
							handler : function() {
								var users = window.top.$("#selectedUser").datagrid("getRows");
								var userArr = [];
								for (var i = 0; i < users.length; i++) {
									userArr.push(users[i].userId);
								}
								window.top.$.messager.progress({
									title : "正在执行",
									msg : "正在执行，请稍后...",
									interval : 500,
									text : ""
								});
								$.ajax({
									url : "<t:path />/jsp/addSysUserGroupUser.do",
									data : {
										groupId : data.groupId,
										userIds : userArr.join()
									},
									type : "POST",
									dataType : "json",
									success : function(data) {
										window.top.$.messager.progress("close");
										if (data.returnCode == "<%=ReturnCodeEnum.CODE_OK.toString()%>") {
											window.top.$(div).dialog("close");
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
								"saveConfigUserGroupUserBtn" : function() {
									return true;
								}
							});
						}
					});
				}
			}, "-", {
				id : "linkReportGroupBtn",
				disabled : true,
				iconCls : "icon-edit",
				text : "关联报表组",
				handler : function() {
					var data = {};
					var select = $("#userGroupListTable").datagrid("getSelections");
					if (!select || select.length == 0) {
						window.top.$.messager.alert("提示", "请选择一行记录！", "error");
						return;
					} else if (select.length > 1) {
						window.top.$.messager.alert("提示", "只能选择一行记录！", "error");
						return;
					}
					data = select[0];
					var div = $("<div>").appendTo($(window.top.document.body));
					var title = "关联报表组";
					window.top.$(div).myDialog({
						width : 300,
						height : 500,
						title : title,
						href : "<t:path />/jsp/sysManage/sysUserGroup/userGroupReportGroupTree.jsp",
						method : "post",
						queryParams : {
							groupId : data.groupId
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
							text : "保存",
							id : "saveLinkReportGroupBtn",
							disabled : true,
							iconCls : "icon-save",
							handler : function() {
								window.top.saveUserGroupReportGroup(div);
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
								"saveLinkReportGroupBtn" : function() {
									return true;
								}
							});
						}
					});
				}
			}],
			onLoadSuccess : function() {
				$(this).datagrid("clearSelections");
				$(this).datagrid("clearChecked");
				window.top.showAddButton("<t:write name='menuId' />", "#userGroupListDiv", parent.document.getElementById("<t:write name='iframeId' />").contentWindow, {
					"addUserGroupBtn" : function() {
						return true;
					},
					"editUserGroupBtn" : function() {
						return false;
					},
					"delUserGroupBtn" : function() {
						return false;
					},
					"detailUserGroupBtn" : function() {
						return false;
					},
					"configUserGroupUserBtn" : function() {
						return false;
					},
					"linkReportGroupBtn" : function() {
						return false;
					}
				});
			},
			onClickRow : function(rowIndex, rowData) {
				window.top.getPageListButton("<t:write name='menuId' />", "#userGroupListDiv", parent.document.getElementById("<t:write name='iframeId' />").contentWindow, {
					"editUserGroupBtn" : function() {
						return true;
					},
					"delUserGroupBtn" : function() {
						return true;
					},
					"detailUserGroupBtn" : function() {
						return true;
					},
					"configUserGroupUserBtn" : function() {
						return true;
					},
					"linkReportGroupBtn" : function() {
						return true;
					}
				});
			}
		});
	});
	function userGroupEdit(flag) {
		var data = {};
		if (flag == 0) {
			data = {
				groupId : 0
			};
		} else {
			var select = $("#userGroupListTable").datagrid("getSelections");
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
		var title = (flag == 0 ? "新增" : "修改") + "用户组表";
		window.top.$(div).myDialog({
			width : 400,
			height : 200,
			title : title,
			href : "<t:path />/jsp/sysManage/sysUserGroup/userGroupEdit.jsp",
			method : "post",
			queryParams : data,
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
				text : "保存",
				id : "saveUserGroupBtn",
				disabled : true,
				iconCls : "icon-save",
				handler : function() {
					window.top.$("#userGroupEditForm").form("submit", {
						url : "<t:path />/jsp/saveSysUserGroup.do",
						onSubmit : function() {
							if (!window.top.$("#userGroupEditForm").form("validate")) {
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
								$("#userGroupListTable").datagrid("reload");
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
					"saveUserGroupBtn" : function() {
						return true;
					}
				});
			}
		});
	}
</script>
</body>
</html>

