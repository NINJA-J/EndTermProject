<%--
  Created by IntelliJ IDEA.
  User: XPS 13 9350
  Date: 2017/8/26
  Time: 10:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>能力示范文稿管理系统</title>
</head>
<body>
<!--还未添加跳转页面-->
<form name="user_info" action="lay_regist.jsp" method="POST">
  <div>
    <label for="inputName" >Name</label>
    <input type="text" id="inputName" placeholder="Name" name="u_name" >
  </div>
  <div>
    <label for="inputSex" >Gender</label>
    <input type="text" id="inputSex" placeholder="Gender" name="u_gender">
  </div>
  <div>
    <label for="inputBirth" >Birth</label>
    <input type="text" id="inputBirth" placeholder="Birth" name="u_birth">
  </div>
  <div>
    <label for="inputAddress" >Address</label>
    <input type="text" id="inputAddress" placeholder="Address" name="u_address">
  </div>
  <div>
    <label for="inputPhone" >Phone</label>
    <input type="text"  id="inputPhone" placeholder="Phone" name="u_phone">
  </div>
  <div>
    <label for="inputReferee" >referee</label>
    <input type="text"  id="inputReferee" placeholder="referee" name="u_referee">
  </div>
  <div>
    <label for="inputBranch" >Branch</label>
    <select  id="inputBranch" name="u_branch">
      <option>1</option>
      <option>2</option>
      <option>3</option>
      <option>4</option>
      <option>5</option>
    </select>
  </div>
  <div>
    <label for="inputCommittee" >Committee</label>
    <select id="inputCommittee" name="u_committee">
      <option>1</option>
      <option>2</option>
      <option>3</option>
      <option>4</option>
      <option>5</option>
    </select>
  </div>
  <div>
    <input type="submit" value="注册会员">
  </div>
</form>
</body>
</html>
