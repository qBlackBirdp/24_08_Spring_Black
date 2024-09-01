<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>Uploaded Files</title>
</head>
<body>
<h1>Upload your WAV file</h1>

<form action="${pageContext.request.contextPath}/upload" method="post" enctype="multipart/form-data">
    <input type="file" name="file" accept=".wav" required />
    <button type="submit">Upload</button>
</form>

<h2>Uploaded Files:</h2>
<ul>
    <c:forEach var="fileName" items="${files}">
        <li>
            <a href="${pageContext.request.contextPath}/download?fileName=${fileName}">${fileName}</a>
            <br>
            <audio controls>
                <source src="${pageContext.request.contextPath}/download?fileName=${fileName}" type="audio/wav">
                Your browser does not support the audio element.
            </audio>
        </li>
    </c:forEach>
</ul>

</body>
</html>
