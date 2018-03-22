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
		<ul id="reportGroupLeft"></ul>
	</div>
	<div region="center">
		<div id="reportGroupRight"></div>
	</div>
<script>
	$(function() {
		$("#reportGroupLeft").myTree({
			animate : true,
			lines : true,
			ajaxParam : {
				url : "<t:path />/jsp/queryAllReportGroup.do?searchType=0",
				cache : false
			},
			idField : "groupId",
			pidField : "parentGroupId",
			textField : "groupName",
			rootPid : 0,
			onClick : function(node) {
				$("#reportGroupRight").panel("refresh", "<t:path />/jsp/reportManage/reportGroup/reportGroupRight.jsp?" + $.param(node.attributes, true));
			}
		});
		$("#reportGroupRight").panel({
			title : "编辑报表组",
			fit : true,
			cache : false,
			href : "<t:path />/jsp/reportManage/reportGroup/reportGroupRight.jsp"
		});
	});
</script>
</body>
</html>

