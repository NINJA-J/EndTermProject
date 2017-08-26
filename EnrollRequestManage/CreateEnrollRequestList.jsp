<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="stdafx.jsp" %>

<!--html 的其他部分  -->

<!--这里是请求的列表生成部分  -->
<%
	User admin = (User)session.getAttribute("user_now");//需要获取当前用户，即根据当前权限查询可批准的人员列表
	ArrayList<EnrollRequest> eList = con.getEnrollRequestFor( admin );
	
	for( EnrollRequest er : eList ){
		out.print("<tr>...............</tr>");
	}
%>

<!--html 的其他部分  -->
