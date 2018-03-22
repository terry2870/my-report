<%@page import="com.myreport.constants.Constant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="t" uri="/my-tags" %>
<html>
<head>
	<jsp:include page="/jsp/common/includeJquery.jsp" flush="true" />
	<title>修改密码</title>
</head>
<body>
<div id="panel"></div>
<script>
	$(function() {
		$("#panel").dialog({
			width : 400,
			height : 230,
			left : 250,
			top : 50,
			draggable : false,
			title : "修改密码",
			href : "<t:path />/jsp/sysManage/pwdManage/pwdPanel.jsp",
			queryParams : {
				loginName : "<t:write name='<%=Constant.USER_SESSION%>' property='loginName' />"
			},
			cache : false,
			closable : false,
			buttons : [{
				id : "modifyPwdButton",
				text : "保存",
				iconCls : "icon-save",
				handler : function() {
					$("#pwdForm").submit();
				}
			}],
			onLoad : function() {
				$("#pwdForm,#pwdForm input[type='text']").keydown(function(e) {
					if (e.keyCode == 13) {
						$("#pwdForm").submit();
					}
				});
			}
		});
	});
</script>
</body>
</html>

