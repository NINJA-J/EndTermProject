<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="stdafx.jsp" %>
<%@ page import="java.util.ArrayList" %>

<%
	//获取上个页面存储的请求列表，即将之于页面的单选框一一对应
	ArrayList<EnrollRequest> eList = (ArrayList<EnrollRequest>)session.getAttribute("EnrollRequestList");
	//获取当前登录用户信息
	User admin = (User)session.getAttribute( "user_now" );
	
	session.removeAttribute( "EnrollRequestList" );
	
	for( int i = 0; i < eList.size(); i++ ){
		//这个判断为获取上个页面单选框状态的判断，我假设单选框的名称为switch+编号，这个==true只是示例，换成真正的判断方法
		if( request.getAttribute( "switch" + i ) == "true" ){
			//函数原型（批准人（及当前登录者），所批准请求）
			con.permitEnrollRequest( admin, eList.get( i ) );
		}
	}
%>