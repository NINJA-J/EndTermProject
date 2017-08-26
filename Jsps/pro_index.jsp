<%--
  Created by IntelliJ IDEA.
  User: XPS 13 9350
  Date: 2017/8/25
  Time: 14:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="zh-CN">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>能力示范文稿管理系统</title>
  <!-- Bootstrap CSS 文件 -->
  <link rel="stylesheet" href="./static/bootstrap/css/bootstrap.min.css">
</head>
<body>

<!-- 头部 -->
<div class="jumbotron">
  <div class="container">
    <div class="row">
      <div class="col-md-11">
        <h2>BJUT</h2>
        <p>能力示范文稿管理系统</p>
      </div>
      <div class="col-md-1">
        <div class="dropdown">
          <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
            写者
            <span class="caret"></span>
          </button>
          <ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
            <li><a href="#">写者</a></li>
            <li><a href="#">管理员</a></li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- 中间内容区局 -->
<div class="container">
  <div class="row">

    <!-- 左侧菜单区域   -->
    <!--8.25 更新此处代码：添加各href中的url链接-->
    <!-- 左侧菜单区域   -->
    <div class="col-md-3">
      <div class="list-group">
        <a href="pro_index.jsp" class="list-group-item active">所有提案</a><!--跳转到提案目录-->
        <a href="pro_per_info.jsp" class="list-group-item">个人提案</a><!--跳转到个人提案-->
        <a href="pro_form.jsp" class="list-group-item">提案编制</a><!--跳转到提案编制-->
        <a href="#" class="list-group-item">个人信息</a><!--@未制作完成！！！-->
      </div>
    </div>

    <!-- 右侧内容区域 -->
    <div class="col-md-9">

      <!-- 成功提示框 -->
      <!--<div class="alert alert-success alert-dismissible" role="alert">-->
      <!--<button type="button" class="close" data-dismiss="alert" aria-label="Close">-->
      <!--<span aria-hidden="true">&times;</span>-->
      <!--</button>-->
      <!--<strong>成功!</strong> 操作成功提示！-->
      <!--</div>-->

      <!--&lt;!&ndash; 失败提示框 &ndash;&gt;-->
      <!--<div class="alert alert-danger alert-dismissible" role="alert">-->
      <!--<button type="button" class="close" data-dismiss="alert" aria-label="Close">-->
      <!--<span aria-hidden="true">&times;</span>-->
      <!--</button>-->
      <!--&lt;!&ndash;<strong>失败!</strong> 操作失败提示！&ndash;&gt;-->
      <!--</div>-->

      <!-- 自定义内容区域 -->
      <div class="panel panel-default">
        <div class="panel-heading">当前所有提案</div>
        <div class="table-responsive">
          <table class="table table-striped table-hover">
            <thead>
            <tr>
              <th>编号</th>
              <th>提案名称</th>
              <th>作者</th>
              <th>截止日期</th>
              <th>状态</th>
              <th>附议数</th>
              <th>反对数</th>
              <th width="120">操作</th>
            </tr>
            </thead>
            <tbody>
            <tr>
              <th scope="row">001</th>
              <td>123</td>
              <td>123</td>
              <td>123</td>
              <td>123</td>
              <td>123</td>
              <td>123</td>
              <td>
                <a href="">详情</a>
                <!--<a href="">修改</a>-->
                <!--<a href="">删除</a>-->
              </td>
            </tr>
            <tr>
              <th scope="row">001</th>
              <td>123</td>
              <td>123</td>
              <td>123</td>
              <td>123</td>
              <td>123</td>
              <td>123</td>
              <td>
                <a href="">详情</a>
                <!--<a href="">修改</a>-->
                <!--<a href="">删除</a>-->
              </td>
            </tr>
            <tr>
              <th scope="row">001</th>
              <td>123</td>
              <td>123</td>
              <td>123</td>
              <td>123</td>
              <td>123</td>
              <td>123</td>
              <td>
                <a href="">详情</a>
                <!--<a href="">修改</a>-->
                <!--<a href="">删除</a>-->
              </td>
            </tr>
            <tr>
              <th scope="row">001</th>
              <td>123</td>
              <td>123</td>
              <td>123</td>
              <td>123</td>
              <td>123</td>
              <td>123</td>
              <td>
                <a href="">详情</a>
                <!--<a href="">修改</a>-->
                <!--<a href="">删除</a>-->
              </td>
            </tr>

            </tbody>
          </table>
        </div>
      </div>

      <!-- 分页  -->
      <div>
        <ul class="pagination pull-right">
          <li>
            <a href="#" aria-label="Previous">
              <span aria-hidden="true">&laquo;</span>
            </a>
          </li>
          <li class="active"><a href="#">1</a></li>
          <li><a href="#">2</a></li>
          <li><a href="#">3</a></li>
          <li><a href="#">4</a></li>
          <li><a href="#">5</a></li>
          <li>
            <a href="#" aria-label="Next">
              <span aria-hidden="true">&raquo;</span>
            </a>
          </li>
        </ul>
      </div>

    </div>
  </div>
</div>

<!-- 尾部 -->
<div class="jumbotron" style="margin:0;">
  <div class="container">
    <span>  @2017 BJUT</span>
  </div>
</div>

<!-- jQuery 文件 -->
<script src="./static/jquery/jquery.min.js"></script>
<!-- Bootstrap JavaScript 文件 -->
<script src="./static/bootstrap/js/bootstrap.min.js"></script>

</body>
</html>
