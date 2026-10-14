<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.Task" %>
<%@ page import="java.util.List" %>
<%
List<Task> taskList = (List<Task>) request.getAttribute("taskList");

Integer goalId = (Integer) request.getAttribute("goalId");

String errorMsg = (String) request.getAttribute("errorMsg");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>タスクを追加</h2>
<form action="TaskMain" method="post">
	<input type="hidden" name="action" value="add">
	<input type="hidden" name="goalId" value="<%= goalId %>">
	<input type="text" name="title">
	<input type="submit" value="追加">
</form>
<% if (errorMsg != null) { %>
<p><%= errorMsg %></p>
<% } %>
<h2>タスク一覧</h2>
<% for (Task task : taskList) { %>
<p>
ID；<%= task.getId() %><br>
タスク：<%= task.getTitle() %><br>
状態：<%= task.isCompleted() ? "完了" : "未完了" %>
</p>

<% if (!task.isCompleted()) { %>

<form action="TaskMain" method="post">
<input type="hidden" name="action" value="complete">
<input type="hidden" name="goalId" value="<%= goalId %>">
<input type="hidden" name="taskId" value="<%= task.getId() %>">
<input type="submit" value="完了">
</form>

<% } %>

<form action="TaskMain" method="post">
<input type="hidden" name="action" value="edit">
<input type="hidden" name="goalId" value="<%= goalId %>">
<input type="hidden" name="taskId" value="<%= task.getId() %>">
<input type="text" name="title" value="<%= task.getTitle() %>">
<input type="submit" value="編集">
</form>

<form action="TaskMain" method="post">
<input type="hidden" name="action" value="delete">
<input type="hidden" name="goalId" value="<%= goalId %>">
<input type="hidden" name="taskId" value="<%= task.getId() %>">
<input type="submit" value="削除">
</form>

<hr>

<% } %>
</body>
</html>