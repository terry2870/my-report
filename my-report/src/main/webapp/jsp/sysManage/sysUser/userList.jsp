<%@page import="com.myreport.enums.ReturnCodeEnum"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="t" uri="/my-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>main</title>
<script src="<t:path />/myTags/util.js"></script>
<jsp:include page="/jsp/common/includeJquery.jsp" flush="true" />
</head>
<body class="easyui-layout" fit="true">
	<div region="north" title="查询条件" style="height: 70px; border-style: none">
		<jsp:include page="/jsp/sysManage/sysUser/userSearch.jsp" flush="true" />
	</div>
	<div region="center" id="userListDiv">
		<table id="userListTable"></table>
	</div>
<script>
	$(function() {
		$("#userListTable").myDatagrid({
			title : "用户列表",
			fit : true,
			fitColumns : true,
			nowrap : true,
			striped : true,
			url : "<t:path />/jsp/queryAllSysUser.do",
			columns : [[{
				title : "登录名",
				field : "loginName",
				width : 80,
				align : "center",
				sortable : true
			}, {
				title : "用户名称",
				field : "userName",
				width : 100,
				align : "center",
				sortable : true
			}, {
				title : "手机号码",
				field : "mobile",
				width : 100,
				align : "center"
			}, {
				title : "电子邮箱地址",
				field : "email",
				width : 170,
				align : "center",
				formatter : function(value) {
					return "<span title='"+ value +"'>"+ value +"</span>";
				}
			}, {
				title : "用户地址",
				field : "address",
				width : 200,
				align : "center",
				formatter : function(value) {
					return "<span title='"+ value +"'>"+ value +"</span>";
				}
			}, {
				title : "开户时间",
				field : "createDate",
				width : 150,
				align : "center",
				sortable : true
			}, {
				title : "所属角色",
				field : "roleName",
				width : 80,
				align : "center",
				sortable : true
			}, {
				title : "用户状态",
				field : "statusName",
				width : 80,
				align : "center",
				sortable : true
			}]],
			frozenColumns : [[{
				field : "userId",
				width : 200,
				align : "center",
				checkbox : true
			}]],
			cache : false,
			pagination : true,
			pageSize : 20,
			rownumbers : true,
			idField : "userId",
			showFooter : true,
			singleSelect : true,
			toolbar : [{
				id : "addUserBtn",
				disabled : true,
				iconCls : "icon-add",
				text : "新增",
				handler : function() {
					userEdit(0);
				}
			}, "-", {
				id : "detailUserBtn",
				disabled : true,
				iconCls : "icon-tip",
				text : "详情",
				handler : function() {
					var data = {};
					var select = $("#userListTable").datagrid("getSelections");
					if (!select || select.length == 0) {
						window.top.$.messager.alert("提示", "请选择一行记录！", "error");
						return;
					}
					data = select[0];
					var div = $("<div>").appendTo($(window.top.document.body));
					window.top.$(div).myDialog({
						width : 400,
						height : 400,
						title : "用户详细",
						href : "<t:path />/jsp/sysManage/sysUser/userEdit.jsp",
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
				id : "editUserBtn",
				disabled : true,
				iconCls : "icon-edit",
				text : "修改",
				handler : function() {
					userEdit();
				}
			}, "-", {
				id : "delUserBtn",
				disabled : true,
				iconCls : "icon-remove",
				text : "删除",
				handler : function() {
					userDelete();
				}
			}],
			onLoadSuccess : function(a) {
				$(this).datagrid("clearSelections");
				$(this).datagrid("clearChecked");
				window.top.showAddButton("<t:write name='menuId' />", "#userListDiv", parent.document.getElementById("<t:write name='iframeId' />").contentWindow, {
					"addUserBtn" : function() {
						return true;
					},
					"editUserBtn" : function() {
						return false;
					},
					"delUserBtn" : function() {
						return false;
					},
					"detailUserBtn" : function() {
						return false;
					}
				});
			},
			onClickRow : function(rowIndex, rowData) {
				window.top.getPageListButton("<t:write name='menuId' />", "#userListDiv", parent.document.getElementById("<t:write name='iframeId' />").contentWindow, {
					"editUserBtn" : function() {
						return true;
					},
					"delUserBtn" : function() {
						return true;
					},
					"detailUserBtn" : function() {
						return true;
					}
				});
			}
		});
		function userEdit(flag) {
			var data = {};
			if (flag == 0) {
				$.extend(data, {
					userId : 0
				});
			} else {
				var select = $("#userListTable").datagrid("getSelections");
				if (!select || select.length == 0) {
					window.top.$.messager.alert("提示", "请选择一行记录！", "error");
					return;
				} else if (select.length > 1) {
					window.top.$.messager.alert("提示", "只能选择一行记录！", "error");
					return;
				}
				$.extend(data, select[0]);
			}
			var div = $("<div>").appendTo($(window.top.document.body));
			var title = flag == 0 ? "新增用户" : "修改用户";
			window.top.$(div).myDialog({
				width : 400,
				height : 400,
				title : title,
				href : "<t:path />/jsp/sysManage/sysUser/userEdit.jsp",
				method : "post",
				queryParams : data,
				modal : true,
				collapsible : true,
				cache : false,
				buttons : [{
					text : "保存",
					id : "saveUserBtn",
					disabled : true,
					iconCls : "icon-save",
					handler : function() {
						window.top.$("#userEditForm").form("submit", {
							url : "<t:path />/jsp/saveUser.do",
							onSubmit : function() {
								if (!window.top.$("#userEditForm").form("validate")) {
									return false;
								}
								var roleId = window.top.$("#userEditForm #roleIdForSelect").combobox("getValue");
								if (roleId == "") {
									roleId = "0";
								}
								window.top.$("#userEditForm #roleId").val(roleId);
								var checked =  window.top.$("#userRegionTree").combotree("tree").tree("getChecked");
								var arr = [];
								if (checked && checked.length > 0) {
									for (var i = 0; i < checked.length; i++) {
										if (window.top.$("#userRegionTree").combotree("tree").tree("isLeaf", checked[i].target)) {
											arr.push(checked[i].attributes.regionId);
										}
									}
								}
								window.top.$("#userEditForm #userRegionHidden").val(arr.join(","));
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
									$("#userListTable").datagrid("reload");
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
						"saveUserBtn" : function() {
							return true;
						}
					});
				}
			});
		}
		function userDelete() {
			var select = $("#userListTable").datagrid("getSelections");
			if (!select || select.length == 0) {
				window.top.$.messager.alert("提示", "请至少选择一行记录！", "error");
				return;
			}
			var userIdArr = [];
			for (var i = 0; i < select.length; i++) {
				userIdArr.push(select[i].userId);
			}
			window.top.$.messager.confirm("确认", "您确定要删除该用户吗？", function(flag) {
				if (flag) {
					window.top.$.messager.progress({
						title : "正在执行",
						msg : "正在执行，请稍后...",
						interval : 500,
						text : ""
					});
					$.ajax({
						url : "<t:path />/jsp/deleteUser.do",
						data : {
							userIds : userIdArr.join(",")
						},
						type : "POST",
						dataType : "json",
						success : function(data) {
							window.top.$.messager.progress("close");
							if (data.returnCode == "<%=ReturnCodeEnum.CODE_OK.toString()%>") {
								$("#userListTable").datagrid("reload");
								window.top.$.messager.show({
									title : "提示",
									msg : "删除用户成功！"
								});
							} else {
								window.top.$.messager.alert("失败", data.returnInfo, "error");
							}
						}
					});
				}
			});
		}
	});
</script>
</body>
</html>

