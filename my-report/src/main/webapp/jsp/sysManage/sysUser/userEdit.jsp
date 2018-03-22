<%@page import="com.myreport.enums.StatusEnum"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="t" uri="/my-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<form name="userEditForm" id="userEditForm" method="post">
	<input type="hidden" name="userId" id="userId" value="<t:write name='userId' />" />
	<input type="hidden" name="roleId" id="roleId" value="<t:write name='roleId' />" />
	<input type="hidden" name="userRegionHidden" id="userRegionHidden" />
	<table cellpadding="0" cellspacing="0" border="1" width="90%" align="center" style="margin-top:20px">
		<tr>
			<td width="30%" align="right">登录名：</td>
			<td width="70%">
				<input name="loginName" id="loginName" size="30" value="<t:write name='loginName' />" 
					class="easyui-validatebox" data-options="required:true,validType:'checkLoginName',missingMessage:'登录名不能为空'" />
			</td>
		</tr>
		<tr id="passwdTr">
			<td align="right">密码：</td>
			<td><input type="password" name="loginPwd" id="loginPwd" size="30" /></td>
		</tr>
		<tr id="passwd2Tr">
			<td align="right">重复密码：</td>
			<td><input type="password" name="loginPwd2" id="loginPwd2" size="30" /></td>
		</tr>
		<tr>
			<td align="right">用户名称：</td>
			<td><input name="userName" id="userName" value="<t:write name='userName' />" size="30" class="easyui-validatebox" data-options="required:true" /></td>
		</tr>
		<tr>
			<td align="right">手机号码：</td>
			<td><input name="mobile" id="mobile" value="<t:write name='mobile' />" size="30" class="easyui-validatebox" data-options="validType:'checkMobile'" /></td>
		</tr>
		<tr>
			<td align="right">固定号码：</td>
			<td><input name="phoneNumber" id="phoneNumber" value="<t:write name='phoneNumber' />" size="30" class="easyui-validatebox" data-options="validType:'checkPhoneNum'" /></td>
		</tr>
		<tr>
			<td align="right">电子邮箱地址：</td>
			<td><input name="email" id="email" value="<t:write name='email' />" size="30" class="easyui-validatebox" data-options="required:false,validType:'email'" /></td>
		</tr>
		<tr>
			<td align="right">用户地址：</td>
			<td><input name="address" id="address" value="<t:write name='address' />" size="30" /></td>
		</tr>
		<tr>
			<td align="right">所属角色：</td>
			<td>
				<input name="roleIdForSelect" id="roleIdForSelect" size="30" />
			</td>
		</tr>
		<tr>
			<td align="right">所属地区：</td>
			<td>
				<input name="userRegionTree" id="userRegionTree" size="30" />
			</td>
		</tr>
		<tr>
			<td align="right">用户状态：</td>
			<td>
				<input name="status" id="status" size="30" class="easyui-combobox" data-options="
					url : '<t:path />/jsp/noFilter.do',
					queryParams : {
						method : 'queryEnumForSelect',
						className : 'StatusEnum'
					},
					panelHeight : 50,
					editable : false,
					value : '<t:write name='status' defaultValue='<%=StatusEnum.A.toString()%>' />',
					required : true
				" />
			</td>
		</tr>
	</table>
</form>
<script>
	$(function() {
		var roleId = "<t:write name='roleId' />";
		if (roleId == "0") {
			roleId = "";
		}
		$("#userEditForm #roleIdForSelect").combobox({
			url : "<t:path />/jsp/noFilter.do",
			queryParams : {
				method : "queryRoleForSelect"
			},
			textField : "roleName",
			valueField : "roleId",
			editable : false,
			value : roleId
		});
		
		if ("<t:write name='userId' />" == "0") {
			$("#userEditForm #loginPwd").validatebox({
				required : true,
				validType : "length[6,20]",
				missingMessage : "密码不能为空!"
			});
			$("#userEditForm #loginPwd2").validatebox({
				required : true,
				validType : "checkPwd['#userEditForm #loginPwd']"
			});
		} else {
			$("#userEditForm #loginName").attr("readonly", true);
			$("#userEditForm #passwdTr,#passwd2Tr").hide();
		}
		$("#userRegionTree").combotree({
			required : false,
			url : "<t:path />/jsp/queryAllRegion.do",
			queryParams : {
				treeType : "tree"
			},
			multiple : true,
			editable : false,
			animate : true,
			checkbox : true,
			lines : true,
			idField : "regionId",
			pidField : "parentRegionId",
			textField : "regionName",
			rootPid : 86,
			onClick : function(node) {
				if (!$("#userRegionTree").combotree("tree").tree("isLeaf", node.target)) {
					$("#userRegionTree").combotree("tree").tree("toggle", node.target);
				}
			},
			onLoadSuccess : function(node, data) {
				$.ajax({
					url : "<t:path />/jsp/queryUserRegion.do?userId=<t:write name='userId' />",
					dataType : "json",
					type : "POST",
					success : function(json) {
						if (json && json != "") {
							$(json).each(function(i, item) {
								var t = $("#userRegionTree").combotree("tree").tree("find", item.regionId);
								if (t) {
									$("#userRegionTree").combotree("tree").tree("check", t.target);
								}
							});
						}
					}
				});
			}
		});
		$("#userEditForm").form();
	});
</script>

