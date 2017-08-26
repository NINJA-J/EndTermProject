<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="stdafx.jsp" %>
<%@ page import="java.util.ArrayList" %>

<%
	ArrayList<EnrollRequest> eList = (ArrayList<EnrollRequest>)session.getAttribute("EnrollRequestList");
	User admin = (User)session.getAttribute( "user_now" );
	
	for( int i = 0; i < eList.size(); i++ ){
		if( request.getAttribute( "switch" + i ) == "true" ){
			con.permitEnrollRequest( admin, eList.get( i ) );
		}
	}
%>