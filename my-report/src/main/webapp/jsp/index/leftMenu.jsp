<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="t" uri="/my-tags" %>
<script>
	$(function() {
		var div = $("<div>", {
			title : "所有报表"
		}).appendTo($("#left"));
		var ul = $("<ul>").appendTo(div);
		ul.myTree({
			animate : true,
			lines : true,
			ajaxParam : {
				url : "<t:path />/jsp/queryUserReportGroup.do",
				cache : false
			},
			allClosed : true,
			idField : "groupId",
			pidField : "parentGroupId",
			textField : "groupName",
			rootPid : 0,
			onClick : function(node) {
				if (!ul.tree("isLeaf", node.target)) {
					ul.tree("toggle", node.target);
				} else {
					addTab({
						menuName : node.text,
						menuId : parseInt(node.id) * -1,
						menuUrl : "/jsp/index/reportInfoList4Index.jsp?reportId=" + node.id
					});
				}
			},
			onBeforeExpand : function(node) {
				var children = ul.myTree("getChildren", node.target);
				if (!children || children.length == 0) {
					ul.tree("options").url = "<t:path/>/jsp/queryReportByReportGroupId.do?groupId=" + node.attributes.groupId;
				}
			}
		});
		$("#left").myAccordion({
			fit : true,
			rootPid : "<t:write name='menuId' />",
			idField : "menuId",
			pidField : "parentMenuId",
			textField : "menuName",
			ajaxParam : {
				cache : false,
				type : "POST",
				dataType : "json",
				url : "<t:path />/jsp/queryUserSessionMenu.do"
			},
			onClickMenu : function(item, parent) {
				addTab(item);
			}
		});
	});
</script>

