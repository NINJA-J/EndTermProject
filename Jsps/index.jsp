<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>A Page</title>
</head>
<body>
	<h1>Success!!!</h1>
	<%
		out.print( (String)session.getAttribute( "user_now" ) + "<br>" );
		out.print( (String)request.getParameter( "u_name") + "<br>" );
		out.print( (String)request.getParameter( "u_gender") + "<br>" );
		out.print( (String)request.getParameter( "u_birth") + "<br>" );
		out.print( (String)request.getParameter( "u_address") + "<br>" ); 
		out.print( (String)request.getParameter( "u_phone") + "<br>" );
		out.print( (String)request.getParameter( "u_referee") + "<br>" ); 
		out.print( (String)request.getParameter( "u_branch") + "<br>" );
		out.print( (String)request.getParameter( "u_committee") + "<br>" );
		out.print( (String)session.getAttribute( "error" ) + "<br>" );
		out.print( (String)session.getAttribute( "uId" ) + "<br>" );
	%>
</body>
</html>