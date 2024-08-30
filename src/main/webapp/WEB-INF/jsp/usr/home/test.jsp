<%--
  Created by IntelliJ IDEA.
  User: han-yeongsin
  Date: 2024. 8. 29.
  Time: 오전 11:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <title>WAV 파일 업로드 및 재생</title>
</head>
<body>
<h1>WAV 파일 업로드 및 재생</h1>
<form action="${pageContext.request.contextPath}/upload" method="post" enctype="multipart/form-data">
    <input type="file" name="file" accept=".wav" required/>
    <button type="submit">업로드 및 재생</button>
</form>

<c:if test="${not empty fileUrl}">
    <h2>업로드된 파일:</h2>
    <audio controls>
        <source src="${fileUrl}" type="audio/wav">
        Your browser does not support the audio element.
    </audio>
</c:if>

<c:if test="${not empty message}">
    <p style="color:red;">${message}</p>
</c:if>
</body>
</html>