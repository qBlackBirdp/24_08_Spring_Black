<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Main</title>

    <!-- 번들된 JS 파일을 불러오기 -->
    <script src="http://localhost:8082/bundle.js"></script>

    <!-- 자바스크립트를 통해 콘솔에 출력 -->
    <script>
        // document.addEventListener("DOMContentLoaded", function() {
        //     // JSP에서 전달된 사용자 정보를 JavaScript 변수로 가져옴
        //     const userName = document.getElementById('userName').textContent;
        //     const userEmail = document.getElementById('userEmail').textContent;
        //
        //     // 브라우저 콘솔에 사용자 정보 출력
        //     if (userName && userEmail) {
        //         console.log("로그인된 사용자 이름: " + userName);
        //         console.log("로그인된 사용자 이메일: " + userEmail);
        //     } else {
        //         console.log("로그인된 사용자가 없습니다.");
        //     }
        // });
    </script>

    <script>

    </script>

</head>
<body>
<h1>Welcome to the Main Page!</h1>

<p>로그인된 사용자: <span id="userName">${userName}</span></p>
<p>사용자 이메일: <span id="userEmail">${userEmail}</span></p>

<!-- 로그아웃 버튼 -->
<button class="logout-btn" onclick="socialLogout()">로그아웃</button>
</body>
</html>
