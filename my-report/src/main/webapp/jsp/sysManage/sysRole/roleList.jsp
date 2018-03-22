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
		<table align="center" id="roleSearchTable" cellpadding="0" cellspacing="0" style="margin-top: 7px" width="100%">
			<tr>
				<td width="10%" align="right">角色名称：</td>
				<td width="20%"><input type="text" id="roleName" /></td>
				<td width="10%" align="right">状态：</td>
				<td width="20%">
					<input name="status" id="status" size="30" />
				</td>
				<td width="40%"><a id="searchBtn" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
			</tr>
		</table>
	</div>
	<div region="center" id="roleListDiv">
		<table id="roleListTable"></table>
	</div>
<script>
	$(function() {
		$("#roleSearchTable #status").combobox({
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
		$("#roleSearchTable,#roleSearchTable input[type='text']").keydown(function(e) {
			if (e.keyCode == 13) {
				searchRole();
			}
		});
		function searchRole() {
			$("#roleListTable").datagrid("load", {
				roleName : $("#roleSearchTable #roleName").val(),
				status : $("#roleSearchTable #status").combobox("getValue")
			});
		}
		$("#roleSearchTable #searchBtn").click(function() {
			searchRole();
		});
		$("#roleListTable").myDatagrid({
			title : "角色列表",
			fit : true,
			fitColumns : true,
			nowrap : true,
			striped : true,
			url : "<t:path />/jsp/queryAllRoles.do",
			columns : [[{
				title : "角色名称",
				field : "roleName",
				width : 100,
				align : "center",
				sortable : true,
				formatter : function(value) {
					return "<span title='"+ value +"'>"+ value +"</span>";
				}
			}, {
				title : "角色描述",
				field : "roleInfo",
				width : 600,
				align : "center",
				formatter : function(value) {
					return "<span title='"+ value +"'>"+ value +"</span>";
				}
			}, {
				title : "状态",
				field : "statusName",
				width : 100,
				align : "center",
				sortable : true
			}, {
				title : "创建者",
				field : "createUserName",
				width : 200,
				align : "center",
				sortable : true
			}, {
				title : "创建时间",
				field : "createDate",
				width : 200,
				align : "center",
				sortable : true
			}]],
			frozenColumns : [[{
				field : "roleId",
				width : 200,
				align : "center",
				checkbox : true
			}]],
			cache : false,
			pagination : true,
			pageSize : 20,
			rownumbers : true,
			idField : "roleId",
			showFooter : true,
			singleSelect : true,
			toolbar : [{
				id : "addRoleBtn",
				disabled : true,
				iconCls : "icon-add",
				text : "新增",
				handler : function() {
					roleEdit(0);
				}
			}, "-", {
				id : "detailRoleBtn",
				disabled : true,
				iconCls : "icon-tip",
				text : "详情",
				handler : function() {
					var data = {};
					var select = $("#roleListTable").datagrid("getSelections");
					if (!select || select.length == 0) {
						window.top.$.messager.alert("提示", "请选择一行记录！", "error");
						return;
					}
					data = select[0];
					var div = $("<div>").appendTo($(window.top.document.body));
					window.top.$(div).myDialog({
						width : 400,
						height : 200,
						title : "角色详细",
						href : "<t:path />/jsp/sysManage/sysRole/roleEdit.jsp",
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
				id : "editRoleBtn",
				disabled : true,
				iconCls : "icon-edit",
				text : "修改",
				handler : function() {
					roleEdit();
				}
			}, "-", {
				id : "delRoleBtn",
				disabled : true,
				iconCls : "icon-remove",
				text : "删除",
				handler : function() {
					var select = $("#roleListTable").datagrid("getSelections");
					if (!select || select.length == 0) {
						window.top.$.messager.alert("提示", "请选择一行记录！", "error");
						return;
					}
					var roleIdArr = [];
					for (var i = 0; i < select.length; i++) {
						roleIdArr.push(select[i].roleId);
					}
					window.top.$.messager.confirm("确认", "删除角色后，同时也删除该角色与菜单的绑定关系。您确定要删除该角色吗，？", function(flag) {
						if (flag) {
							window.top.$.messager.progress({
								title : "正在执行",
								msg : "正在执行，请稍后...",
								interval : 500,
								text : ""
							});
							$.ajax({
								url : "<t:path />/jsp/roleDelete.do",
								data : {
									roleIds : roleIdArr.join(",")
								},
								type : "POST",
								dataType : "json",
								success : function(data) {
									window.top.$.messager.progress("close");
									if (data.returnCode == "<%=ReturnCodeEnum.CODE_OK.toString()%>") {
										$("#roleListTable").datagrid("reload");
										window.top.$.messager.show({
											title : "提示",
											msg : "删除角色成功！"
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
				id : "addMenuRightBtn",
				disabled : true,
				text : "分配菜单权限",
				handler : function() {
					var select = $("#roleListTable").datagrid("getSelections");
					if (!select || select.length == 0) {
						window.top.$.messager.alert("提示", "请选择一行记录！", "error");
						return;
					} else if (select.length > 1) {
						window.top.$.messager.alert("提示", "只能选择一行记录！", "error");
						return;
					}
					var div = $("<div>").appendTo($(window.top.document.body));
					window.top.$(div).myDialog({
						width : 400,
						height : 600,
						title : "为【"+ select[0].roleName +"】分配菜单权限",
						href : "<t:path />/jsp/sysManage/sysRole/roleMenuEdit.jsp",
						method : "post",
						queryParams : {
							roleId : select[0].roleId
						},
						modal : true,
						collapsible : true,
						cache : false,
						buttons : [{
							text : "保存",
							disabled : true,
							id : "saveMenuRightBtn",
							iconCls : "icon-save",
							handler : function() {
								window.top.saveRoleMenu(div);
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
								"saveMenuRightBtn" : function() {
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
				window.top.showAddButton("<t:write name='menuId' />", "#roleListDiv", parent.document.getElementById("<t:write name='iframeId' />").contentWindow, {
					"addRoleBtn" : function() {
						return true;
					},
					"editRoleBtn" : function() {
						return false;
					},
					"delRoleBtn" : function() {
						return false;
					},
					"addMenuRightBtn" : function() {
						return false;
					},
					"detailRoleBtn" : function() {
						return false;
					}
				});
			},
			onClickRow : function(rowIndex, rowData) {
				window.top.getPageListButton("<t:write name='menuId' />", "#roleListDiv", parent.document.getElementById("<t:write name='iframeId' />").contentWindow, {
					"editRoleBtn" : function() {
						return true;
					},
					"delRoleBtn" : function() {
						return true;
					},
					"addMenuRightBtn" : function() {
						return true;
					},
					"detailRoleBtn" : function() {
						return true;
					}
				});
			}
		});
	});
	function roleEdit(flag) {
		var data = {};
		if (flag == 0) {
			data = {
				roleId : 0
			};
		} else {
			var select = $("#roleListTable").datagrid("getSelections");
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
		var title = flag == 0 ? "新增角色" : "修改角色";
		window.top.$(div).myDialog({
			width : 400,
			height : 200,
			title : title,
			href : "<t:path />/jsp/sysManage/sysRole/roleEdit.jsp",
			method : "post",
			queryParams : data,
			modal : true,
			collapsible : true,
			cache : false,
			buttons : [{
				text : "保存",
				id : "saveRoleBtn",
				disabled : true,
				iconCls : "icon-save",
				handler : function() {
					window.top.$("#roleEditForm").form("submit", {
						url : "<t:path />/jsp/roleSave.do",
						onSubmit : function() {
							if (!window.top.$("#roleEditForm").form("validate")) {
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
								$("#roleListTable").datagrid("reload");
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
					"saveRoleBtn" : function() {
						return true;
					}
				});
			}
		});
	}
</script>
</body>
</html>

