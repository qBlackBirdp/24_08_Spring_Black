<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Main</title>

    <!-- 번들된 JS 파일을 불러오기 -->
    <script src="http://localhost:8082/bundle.js"></script>

    <script>

    </script>

</head>
<body>
<h1>Welcome to the Main Page!</h1>

<p>로그인된 사용자: <span id="userName">${userName}</span></p>
<p>사용자 이메일: <span id="userEmail">${userEmail}</span></p>

<!-- 로그인 제공자가 구글인지 스포티파이인지 확인 -->
<c:choose>
    <c:when test="${provider == 'google'}">
        <p>Google 사용자입니다.</p>
    </c:when>
    <c:when test="${provider == 'spotify'}">
        <p>Spotify 사용자입니다.</p>
    </c:when>
    <c:otherwise>
        <p>알 수 없는 로그인 제공자입니다.</p>
    </c:otherwise>
</c:choose>

<!-- 로그아웃 버튼 -->
<button class="logout-btn" onclick="socialLogout()">로그아웃</button>
</body>
</html>
