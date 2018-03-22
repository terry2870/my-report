<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="t" uri="/my-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>main</title>
	<jsp:include page="/jsp/common/includeJquery.jsp" flush="true" />
	<script src="<t:write name='jsPath' />/myTags/myJqueryPlugins/js/easyui.hp.myTree.js"></script>
</head>
<body class="easyui-layout">
	<div region="west" style="width: 300px;">
		<ul id="menuLeft"></ul>
	</div>
	<div region="center">
		<div id="menuRight"></div>
	</div>
<script>
	$(function() {
		$("#menuLeft").myTree({
			animate : true,
			lines : true,
			ajaxParam : {
				url : "<t:path />/jsp/queryAllMenu.do",
				cache : false
			},
			idField : "menuId",
			pidField : "parentMenuId",
			textField : "menuName",
			rootPid : 0,
			onClick : function(node) {
				$("#menuRight").panel("refresh", "<t:path />/jsp/sysManage/sysMenu/menuRight.jsp?" + $.param(node.attributes, true));
			}
		});
		$("#menuRight").panel({
			title : "编辑菜单",
			fit : true,
			cache : false,
			href : "<t:path />/jsp/sysManage/sysMenu/menuRight.jsp"
		});
	});
</script>
</body>
</html>

