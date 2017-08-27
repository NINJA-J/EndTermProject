<%@ page language="java" contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="Jonathan.AuditedFile" %>
<%@ page import="Jonathan.Comment" %>
<%@ page import="Jonathan.EnrollRequest" %>
<%@ page import="Jonathan.FileBasic" %>
<%@ page import="Jonathan.Proposal" %>
<%@ page import="Jonathan.SQLConnection" %>
<%@ page import="Jonathan.Standard" %>
<%@ page import="Jonathan.SysStandard" %>
<%@ page import="Jonathan.User" %>

<%@ page import="java.util.ArrayList" %>

<%
	/* if( (User)session.getAttribute( "UserNow" ) == null )
		pageContext.forward( "index.jsp" ); */
	SQLConnection con = new SQLConnection();
	con.connectToDatabase(
			"localhost:3306", 
			"DocManager",
			"root", 
			"" );
%>