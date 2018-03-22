<%@page import="com.myreport.enums.ReturnCodeEnum"%>
<%@page import="com.myreport.enums.MenuTypeEnum"%>
<%@page import="com.myreport.enums.StatusEnum"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="t" uri="/my-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<form name="menuEditForm" id="menuEditForm" method="post">
	<input type="hidden" name="parentMenuId" id="parentMenuId" value="<t:write name='parentMenuId' />" />
	<input type="hidden" name="menuId" id="menuId" value="<t:write name='menuId' />" />
	<input type="hidden" name="menuType" id="menuType" value="<t:write name='menuType' />" />
	<table cellpadding="0" cellspacing="0" border="0" width="80%" style="margin-top:10px">
		<tr>
			<td width="30%" align="right">菜单名称：</td>
			<td width="70%">
				<input type="text" name="menuName" id="menuName" size="40" class="easyui-validatebox" data-options="
					required:true,
					validType : 'checkName',
					invalidMessage : '请输入正确的菜单名，菜单名不能输入形如（@#$）等特殊字符'
				" />
			</td>
		</tr>
		<tr id="menuUrlTr">
			<td align="right">菜单地址：</td>
			<td><input type="text" name="menuUrl" id="menuUrl" size="40" /></td>
		</tr>
		<tr id="extraUrlTr">
			<td align="right">额外菜单地址：</td>
			<td><input type="text" name="extraUrl" id="extraUrl" size="40" /></td>
		</tr>
		<tr id="buttonIdTr">
			<td align="right">按钮ID名称：</td>
			<td><input type="text" name="buttonId" id="buttonId" size="40" /></td>
		</tr>
		<tr>
			<td align="right">状态：</td>
			<td>
				<input name="status" id="status" size="40" class="easyui-combobox" data-options="
					url : '<t:path />/jsp/noFilter.do?method=queryEnumForSelect&className=StatusEnum',
					panelHeight : 50,
					editable : false,
					required : true,
					missingMessage : '状态必须选择！'
				" />
			</td>
		</tr>
		<tr id="iconNameTr">
			<td align="right">图标名称：</td>
			<td>
				<input name="iconName" id="iconName" size="40" />
			</td>
		</tr>
		<tr>
			<td align="right">排序：</td>
			<td>
				<input name="sortNumber" id="sortNumber" size="40"
					class="easyui-numberbox" data-options="min : 0,max : 1000,required : true"
				 />
			</td>
		</tr>
		<tr style="padding-top:20px">
			<td align="center" colspan="2" id="buttons">
				<a id="addRootMenu" class="easyui-linkbutton" data-options="iconCls:'icon-add'" href="#" onclick="addMenu('<%=MenuTypeEnum.ROOT.toString()%>');">增加根节点</a>
				<a id="addChildMenu" class="easyui-linkbutton" data-options="iconCls:'icon-add'" href="#" onclick="addMenu('<%=MenuTypeEnum.CHILD.toString()%>');">新增子节点</a>
				<a id="addMenuButton" class="easyui-linkbutton" data-options="iconCls:'icon-add'" href="#" onclick="addMenu('<%=MenuTypeEnum.BUTTON.toString()%>');">新增按钮</a>
				<a id="deleteMenu" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" href="#" onclick="delMenu();">删除节点</a>
				<a id="saveMenu" class="easyui-linkbutton" data-options="iconCls:'icon-save'" href="#" onclick="saveMenu();">保存</a>
			</td>
		</tr>
	</table>
</form>
<script>
	$(function() {
		$("#menuEditForm").form("load", {
			menuName : "<t:write name='menuName' />",
			menuUrl : "<t:write name='menuUrl' />",
			status : "<t:write name='status' defaultValue='<%=StatusEnum.A.toString()%>' />",
			iconName : "<t:write name='iconName' />",
			sortNumber : "<t:write name='sortNumber' />",
			extraUrl : "<t:write name='extraUrl' />",
			buttonId : "<t:write name='buttonId' />"
		});
		var menuType = "<t:write name='menuType' />";
		if ("<t:write name='menuId' />" == "0") {
			$("#addRootMenu,#addChildMenu,#addChildMenu2,#addMenuButton,#deleteMenu").hide();
		}
		if (menuType == "<%=MenuTypeEnum.ROOT.toString()%>") {
			$("#buttonIdTr,#menuUrlTr,#extraUrlTr").hide();
			$("#addMenuButton").hide();
		} else if (menuType == "<%=MenuTypeEnum.CHILD.toString()%>") {
			$("#buttonIdTr").hide();
			$("#addChildMenu").hide();
			//$("#iconNameTr").hide();
		} else if (menuType == "<%=MenuTypeEnum.BUTTON.toString()%>") {
			$("#addChildMenu,#addChildMenu2,#addMenuButton,#menuUrlTr").hide();
			$("#iconNameTr").hide();
		} else {
			$("#buttonIdTr,#addChildMenu,#addChildMenu2,#addMenuButton,#deleteMenu,#saveMenu,#extraUrlTr").hide();
			$("#iconNameTr").hide();
		}
	});
	function addMenu(menuType) {
		var treeNode = $("#menuLeft").myTree("getSelected");
		var data = {
			menuId : 0,
			menuType : menuType,
			parentMenuId : menuType == "<%=MenuTypeEnum.ROOT.toString()%>" ? 0 : treeNode.attributes.menuId
		};
		$("#menuRight").panel("refresh", "<t:path />/jsp/sysManage/sysMenu/menuRight.jsp?" + $.param(data, true));
	};
	function delMenu() {
		if ($("#menuId").val() == "") {
			window.top.$.messager.alert("错误", "请选择一个菜单", "error");
			return;
		}
		window.top.$.messager.confirm("确认", "您确定要删除该菜单/按钮吗？", function(flag) {
			if (flag) {
				window.top.$.messager.progress({
					title : "正在执行",
					msg : "正在执行，请稍后...",
					interval : 500,
					text : ""
				});
				$.ajax({
					url : "<t:path />/jsp/menuDelete.do",
					data : {
						menuIds : "<t:write name='menuId' />"
					},
					dataType : "json",
					success : function(data) {
						window.top.$.messager.progress("close");
						if (data.returnCode == "<%=ReturnCodeEnum.CODE_OK.toString()%>") {
							$('#menuLeft').myTree("reload");
							$.messager.show({
								title : "提示",
								msg : "删除菜单/按钮成功！"
							});
							$("#menuEditForm").form("clear");
						} else {
							window.top.$.messager.alert("删除菜单/按钮失败", data.returnInfo, "error");
						}
					}
				});
			}
		});
	};
	function saveMenu() {
		$("#menuEditForm").form("submit", {
			url : "<t:path />/jsp/menuSave.do",
			onSubmit : function() {
				if (!$("#menuEditForm").form("validate")) {
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
				if (data) {
					data = $.parseJSON(data);
					if (data.returnCode == "<%=ReturnCodeEnum.CODE_OK.toString()%>") {
						$("#menuLeft").myTree("reload");
						$.messager.show({
							title : "提示",
							msg : "保存成功！"
						});
						$("#menuEditForm").form("clear");
					} else {
						window.top.$.messager.alert("出错", data.returnInfo, "error");
					}
				} else {
					window.top.$.messager.alert("出错", data.returnInfo, "error");
				}
			}
		});
	};
</script>

