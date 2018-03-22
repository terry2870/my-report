<%@page import="com.alibaba.fastjson.JSON"%>
<%@page import="com.myreport.enums.MenuTypeEnum"%>
<%@page import="com.myreport.constants.Constant"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	Object list = request.getSession().getAttribute(Constant.USER_MENU_LIST);
%>
<script>
	$(function() {
		var rootMenu = <%=JSON.toJSONString(list)%>;
		var dataList = [];
		if (rootMenu && rootMenu.length > 0) {
			$(rootMenu).each(function(i, item) {
				if (item.menuType != "<%=MenuTypeEnum.BUTTON.toString()%>") {
					dataList.push(item);
				}
			});
		}
		$("#left").myTree({
			lines : true,
			animate : true,
			dataList : dataList,
			idField : "menuId",
			pidField : "parentMenuId",
			textField : "menuName",
			rootPid : 0,
			onClick : function(node) {
				if ($("#left").myTree("isLeaf", node.target)) {
					addTab(node.attributes);
				} else {
					$("#left").myTree("toggle", node.target);
				}
			}
		});
	});
</script>

