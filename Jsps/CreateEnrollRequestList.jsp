<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 这个include将所有需要的java文件都包含进来， 并创建con数据库连接 -->
<%@ include file="stdafx.jsp" %>

<!--html 的其他部分  -->

<!--这里是请求的列表生成部分  -->
<%
	//需要获取当前用户，即根据当前权限查询可批准的人员列表
	User admin = (User)session.getAttribute("user_now");
	//获取请求列表
	ArrayList<EnrollRequest> eList = con.getEnrollRequestFor( admin );
	//储存列表
	session.setAttribute( "EnrollRequestList", eList );
	//遍历每一项申请并打印入列表
	for( EnrollRequest er : eList ){
		out.print("<tr>...............</tr>");
		er.getUser();//获取发出请求的人员
		er.getIndustryReqId();//获取申请的行业分会ID，-1表示未申请
		er.getCommitteeReqId();//获取申请的委员会ID，-1表示未申请
		er.getSeminarReqId();//获取申请的研究会ID，-1表示未申请
	}
%>

<!--html 的其他部分  -->
