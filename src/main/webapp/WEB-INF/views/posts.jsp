<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Diary posts</title>
</head>
<body>

<c:url var="addAction" value="/post/add" ></c:url>

<form:form action="${addAction}" commandName="post">
<table>
	<c:if test="${!empty post.title}">
	<tr>
		<td>
			<form:label path="post_id">
				<spring:message text="ID"/>
			</form:label>
		</td>
		<td>
			<form:input path="post_id" readonly="true" size="8"  disabled="true" />
			<form:hidden path="post_id" />
		</td> 
	</tr>
	</c:if>
	<tr>
		<td>
			<form:label path="title">
				<spring:message text="Title"/>
			</form:label>
		</td>
		<td>
			<form:input path="title" />
		</td> 
	</tr>
	<tr>
		<td>
			<form:label path="text">
				<spring:message text="Text"/>
			</form:label>
		</td>
		<td>
			<form:input path="text" />
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<c:if test="${!empty post.title}">
				<input type="submit"
					value="<spring:message text="Edit Post"/>" />
			</c:if>
			<c:if test="${empty post.title}">
				<input type="submit"
					value="<spring:message text="Add Post"/>" />
			</c:if>
		</td>
	</tr>
</table>	
</form:form>
<br>

	<h3>My posts</h3>
	<c:if test="${!empty all_posts}">
		<table class="tg">
		<tr>
			<th width="80">Post ID</th>
			<th width="120">Title</th>
			<th width="120">Text</th>
			<th width="60">Edit</th>
			<th width="60">Delete</th>
		</tr>
		<c:forEach items="${all_posts}" var="post">
			<tr>
				<td>${post.post_id}</td>
				<td>${post.title}</td>
				<td>${post.text}</td>
				<td><a href="<c:url value='/posts/edit/${post.post_id}' />" >Edit</a></td>
				<td><a href="<c:url value='/posts/remove/${post.post_id}' />" >Delete</a></td>
			</tr>
		</c:forEach>
		</table>
	</c:if>
</body>
</html>