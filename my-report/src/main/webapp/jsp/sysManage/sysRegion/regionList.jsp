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
		<ul id="regionLeft"></ul>
	</div>
	<div region="center">
		<div id="regionRight"></div>
	</div>
<script>
	$(function() {
		$("#regionLeft").myTree({
			animate : true,
			lines : true,
			ajaxParam : {
				url : "<t:path />/jsp/queryAllRegion.do?treeType=myTree",
				cache : false
			},
			idField : "regionId",
			pidField : "parentRegionId",
			textField : "regionName",
			rootPid : 0,
			onClick : function(node) {
				$("#regionRight").panel("refresh", "<t:path />/jsp/sysManage/sysRegion/regionRight.jsp?" + $.param(node.attributes, true));
			}
		});
		$("#regionRight").panel({
			title : "编辑地区",
			fit : true,
			cache : false,
			href : "<t:path />/jsp/sysManage/sysRegion/regionRight.jsp"
		});
	});
</script>
</body>
</html>


