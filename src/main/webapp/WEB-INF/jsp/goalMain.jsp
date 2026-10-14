<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%@ page import="model.Goal" %>
<%@ page import="java.util.List" %>
<%
User loginUser = (User) session.getAttribute("loginUser");
List<Goal> goalList = (List<Goal>) request.getAttribute("goalList");
String errorMsg = (String) request.getAttribute("errorMsg");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>目標管理</title>
</head>

<body>
<h1>目標管理</h1>
<p>
<%= loginUser.getName() %>さん、ログイン中
</p>
<p>
<a href="Logout">ログアウト</a>
</p>
<h2>目標を登録</h2>
<form action="GoalMain" method="post">
	<input type="text" name="title">
	<input type="submit" value="登録">
</form>
<% if (errorMsg != null) { %>
<p><%= errorMsg %></p>
<% } %>
<h2>目標一覧</h2>
<% for (Goal goal : goalList) { %>
<p>
<!-- 
<%= goal.getTitle() %>
 -->
ID：<%= goal.getId() %><br>
ユーザー：<%= goal.getUserName() %><br>
目標：<a href="TaskMain?goalId=<%= goal.getId() %>"><%= goal.getTitle() %></a><br>
進捗：
<progress value="<%= goal.getProgress() %>" max="100"></progress>
<%= goal.getProgress() %>％
</p>
<% } %>
</body>
</html>