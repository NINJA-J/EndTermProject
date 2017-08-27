<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file = "stdafx.jsp" %>
<%
/* 添加一个用户的个人信息，返回值
 * SQLConnection.USER_EXIST			同名（实名）用户已存在
 * SQLConnection.REFERRER_NO_FOUND	推荐人不存在
 * SQLConnection.DB_OPER_FAILURE	日期格式错误
 * SQLConnection.DB_OPER_FAILURE	数据库操作失败
 * SQLConnection.SUCCESS			成功
 * */
 
 	int error = -1;
	int uId = con.getLoginId( (String)session.getAttribute( "user_now" ) );
	error = con.addUser(
 			uId, 
 			(String)request.getParameter("u_name"),
 			(String)request.getParameter("u_gender"),
 			(String)request.getParameter("u_birth"), 
 			(String)request.getParameter("u_address"), 
 			(String)request.getParameter("u_phone"), 
 			(String)request.getParameter("u_referee"), 
 			(String)request.getParameter("u_branch"), 
 			(String)request.getParameter("u_committee") );
	
	session.setAttribute( "error", String.valueOf( error ) );
	session.setAttribute( "uId", String.valueOf( uId ) );
	
	if( error == SQLConnection.SUCCESS ){
		response.sendRedirect( "Login.jsp" );
	} else {
		response.sendRedirect( "index.jsp" );
	}
%>
