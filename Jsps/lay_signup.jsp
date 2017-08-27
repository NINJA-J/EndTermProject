<%--
  Created by IntelliJ IDEA.
  User: XPS 13 9350
  Date: 2017/8/26
  Time: 10:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>signup</title>
</head>
<body>
<%
  //从前端用户注册界面 接受用户想要注册的用户名密码

	String username = new String(request.getParameter("username_sign"));
  	String pwd = new String(request.getParameter("password_sign"));
  
  	
	session.setAttribute("user_now",username);
	pageContext.forward("pro_verify.jsp");
%>


<!--测试用 不用管
//if(username.equals("admin")&&pwd.equals("123")){
//  pageContext.forward("pro_index.jsp");
// }
// else{
//   %><!--<script type="text/javascript" language="javascript">
   //         alert("您的账号正在审核，请耐心等待...");
   // window.document.location.href="pro_login.jsp";

   // </script>-->
  // }-->
</body>
</html>
