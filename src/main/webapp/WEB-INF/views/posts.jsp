<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Diary posts</title>
</head>
<body>
	<table>
		<c:forEach items="${all_posts}" var="post">
			<tr>
				<td><c:out value="${post.title}" /></td>
			</tr>
			<tr>
				<td><c:out value="${post.text}" /></td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>