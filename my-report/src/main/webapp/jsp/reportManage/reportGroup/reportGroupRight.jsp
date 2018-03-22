<%@page import="com.myreport.enums.ReturnCodeEnum"%>
<%@page import="com.myreport.enums.StatusEnum"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="t" uri="/my-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<form name="reportGroupEditForm" id="reportGroupEditForm" method="post">
	<input type="hidden" name="parentGroupId" id="parentGroupId" value="<t:write name='parentGroupId' />" />
	<input type="hidden" name="groupId" id="groupId" value="<t:write name='groupId' />" />
	<table cellpadding="0" cellspacing="0" border="0" width="80%" style="margin-top:10px">
		<tr>
			<td width="30%" align="right">报表组名称：</td>
			<td width="70%">
				<input type="text" name="groupName" id="groupName" size="40" class="easyui-validatebox" data-options="
					required:true,
					validType : 'checkName',
					invalidMessage : '请输入正确的报表组名，报表组名不能输入形如（@#$）等特殊字符'
				" />
			</td>
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
				<a id="addRootReportGroup" class="easyui-linkbutton" data-options="iconCls:'icon-add'" href="#" onclick="addRootReportGroup();">增加根节点</a>
				<a id="addChildReportGroup" class="easyui-linkbutton" data-options="iconCls:'icon-add'" href="#" onclick="addReportGroup();">新增子节点</a>
				<a id="deleteReportGroup" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" href="#" onclick="delReportGroup();">删除节点</a>
				<a id="saveReportGroup" class="easyui-linkbutton" data-options="iconCls:'icon-save'" href="#" onclick="saveReportGroup();">保存</a>
			</td>
		</tr>
	</table>
</form>
<script>
	$(function() {
		$("#reportGroupEditForm").form("load", {
			groupName : "<t:write name='groupName' />",
			status : "<t:write name='status' defaultValue='<%=StatusEnum.A.toString()%>' />",
			sortNumber : "<t:write name='sortNumber' />"
		});
		var groupId = "<t:write name='groupId' />";
		var parentGroupId = "<t:write name='parentGroupId' />";
		if (!groupId && !parentGroupId) {
			$("#addChildReportGroup,#deleteReportGroup,#saveReportGroup").hide();
		} else if (groupId == 0 && parentGroupId == 0) {
			$("#addRootReportGroup,#addChildReportGroup,#deleteReportGroup").hide();
		} else if (groupId == 0 && parentGroupId > 0) {
			$("#addRootReportGroup,#addChildReportGroup,#deleteReportGroup").hide();
		}
	});
	function addRootReportGroup() {
		var data = {
			groupId : 0,
			parentGroupId : 0
		};
		$("#reportGroupRight").panel("refresh", "<t:path />/jsp/reportManage/reportGroup/reportGroupRight.jsp?" + $.param(data, true));
	};
	function addReportGroup() {
		var treeNode = $("#reportGroupLeft").myTree("getSelected");
		var data = {
			groupId : 0,
			parentGroupId : treeNode.attributes.groupId
		};
		$("#reportGroupRight").panel("refresh", "<t:path />/jsp/reportManage/reportGroup/reportGroupRight.jsp?" + $.param(data, true));
	};
	function delReportGroup() {
		if ($("#groupId").val() == "") {
			window.top.$.messager.alert("错误", "请选择一个报表组", "error");
			return;
		}
		window.top.$.messager.confirm("确认", "您确定要删除该报表组吗？", function(flag) {
			if (flag) {
				window.top.$.messager.progress({
					title : "正在执行",
					msg : "正在执行，请稍后...",
					interval : 500,
					text : ""
				});
				$.ajax({
					url : "<t:path />/jsp/deleteReportGroup.do",
					data : {
						groupId : "<t:write name='groupId' />"
					},
					dataType : "json",
					success : function(data) {
						window.top.$.messager.progress("close");
						if (data.returnCode == "<%=ReturnCodeEnum.CODE_OK.toString()%>") {
							$('#reportGroupLeft').myTree("reload");
							$.messager.show({
								title : "提示",
								msg : "删除报表组成功！"
							});
							$("#reportGroupEditForm").form("clear");
						} else {
							window.top.$.messager.alert("删除报表组失败", data.returnInfo, "error");
						}
					}
				});
			}
		});
	};
	function saveReportGroup() {
		$("#reportGroupEditForm").form("submit", {
			url : "<t:path />/jsp/saveReportGroup.do",
			onSubmit : function() {
				if (!$("#reportGroupEditForm").form("validate")) {
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
						$("#reportGroupLeft").myTree("reload");
						$.messager.show({
							title : "提示",
							msg : "保存成功！"
						});
						$("#reportGroupEditForm").form("clear");
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

