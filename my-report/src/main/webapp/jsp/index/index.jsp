<%@page import="com.myreport.utils.SessionUtil"%>
<%@page import="com.myreport.bean.SysUserBean"%>
<%@page import="com.myreport.enums.MenuTypeEnum"%>
<%@page import="com.myreport.constants.Constant"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="t" uri="/my-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<%
	Object list = request.getSession().getAttribute(Constant.USER_MENU_LIST);
%>
<head>
<title>运维统一报表平台</title>
<jsp:include page="/jsp/common/includeJquery.jsp" flush="true" />
<script src="<t:write name='jsPath' />/jquery/easyui/extenPlugins/datagrid-dnd.js"></script>
</head>
<body class="easyui-layout" id="ff"  data-options="fit:true">
	<div region="north" style="height: 73px;border-style: none;overflow: hidden;">
		<div id="top">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="top_table">
				<tr>
					<td width="220" height="69" rowspan="2"><div align="center"><img src="<t:path />/images/logo.gif" width="100" height="60" /></div></td>
					<td height="26" valign="middle" class="top_bg01"><div align="right">你好，<t:write name="<%=Constant.USER_SESSION%>" property="userName" />&nbsp;&nbsp; |&nbsp; <a href="#" id="logout">注销</a></div></td>
				</tr>
				<%-- <tr>
					<td valign="bottom"><table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="43" valign="bottom" class="top_bg02">
								<div class="fd" id="topMenu">
								</div>
							<br /></td>
						</tr>
					</table></td>
				</tr> --%>
			</table>
		</div>
	</div>
	<div region="west" iconCls="icon-reload" split="true" title="导航菜单" style="width: 250px;">
		<div id="left">
			<jsp:include page="/jsp/index/leftMenu.jsp" flush="true">
				<jsp:param name="menuId" value="0" />
			</jsp:include>
		</div>
	</div>
	<div region="center">
		<div id="right">
			<div title="首页">
				<jsp:include page="/jsp/homePage/homePage.jsp" flush="true" />
			</div>
		</div>
	</div>
	<script>
		$(function() {
			$("#logout").click(function() {
				$.messager.confirm('确认','你确定要注销登录吗?',function(f){
					if (!f) {
						return ;
					}
					window.location.href = "<t:path />/jsp/logout.jsp";
				});
			}).linkbutton({
			   plain:true,
			   iconCls:'icon-cancel'
			});
			$("#printBtn").click(function(){
			
			}).linkbutton({
			   plain:true
			});;
			$("#helpBtn").click(function(){
			
			}).linkbutton({
			   plain:true
			});
			<%-- var rootMenu = <%=JSONArray.fromObject(list)%>;
			$(rootMenu).each(function(i, item) {
				if (item.menuType != "<%=MenuTypeEnum.ROOT.toString()%>") {
					return true;
				}
				$("#topMenu").append($("<span>").addClass("main_menu").click(function() {
					$("#topMenu span").attr("class", "main_menu");
					$(this).attr("class", "main_menu_on");
					if (item.menuName == "首页") {
						$("#left").load("<t:path />/jsp/homePage/leftTree.jsp");
					} else {
						$("#left").load("<t:path />/jsp/index/leftMenu.jsp", {
							menuId : item.menuId
						});
					}
					if (item.menuName == "首页") {
						$("#right").tabs("select", "首页");
					}
				}).append($("<img>").attr({
					width : 16,
					height : 16,
					border : 0,
					align : "absmiddle",
					src : "<t:path />" + item.iconName
				})).append(item.menuName)).append($("<div>").addClass("menu_line"));
			});
			$("#topMenu span:first").removeClass().addClass("main_menu_on"); --%>
			$("#right").myTabs({
				fit : true,
				maxSizeAble : true
			});
		});
		function addTab(item) {
			if ($("#right").tabs("exists", item.menuName)) {
				$("#right").tabs("select", item.menuName);
			} else {
				var iframeId = "iframe_" + item.menuId;
				var iframe = $("<iframe>").attr({
					width : "100%",
					height : "100%",
					frameborder : 0,
					id : iframeId
				});
				if (item.menuUrl.indexOf("?") > 0) {
					iframe.attr("src", "<t:path />"+ item.menuUrl +"&menuId="+ item.menuId + "&iframeId=" + iframeId);
				} else {
					iframe.attr("src", "<t:path />"+ item.menuUrl +"?menuId="+ item.menuId + "&iframeId=" + iframeId);
				}
				$("#right").tabs("add", {
					title : item.menuName,
					content : iframe,
					closable : true,
					selected : true,
					cache : true
				});
			}
		}
	</script>
	<jsp:include page="/jsp/common/giveRight.jsp" flush="true" />
</body>
</html>

