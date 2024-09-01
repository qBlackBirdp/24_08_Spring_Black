<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>Upload Result</title>
</head>
<body>
<h1>File Upload Result</h1>

<!-- 업로드 결과 메시지를 표시 -->
<c:if test="${not empty message}">
    <p>${message}</p>
</c:if>

<!-- 파일이 업로드된 경우 -->
<c:if test="${not empty fileUrl}">
    <h2>Uploaded File:</h2>
    <audio controls>
        <source src="${fileUrl}" type="audio/wav">
        Your browser does not support the audio element.
    </audio>
    <br><br>
    <a href="${fileUrl}" download>Download Uploaded File</a>
</c:if>

<!-- 오류 메시지가 있을 경우 -->
<c:if test="${not empty error}">
    <p style="color:red;">${error}</p>
</c:if>

<!-- 다른 파일 업로드 링크 -->
<p><a href="${pageContext.request.contextPath}/usr/home/uploadFormTest">Upload another file</a></p>

</body>
</html>
